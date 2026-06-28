# 🎟️ Event Service

[![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk&logoColor=white)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen?logo=spring&logoColor=white)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-✓-336791?logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Build](https://img.shields.io/badge/Build-Maven-C71A36?logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![Status](https://img.shields.io/badge/Status-Active%20Development-blue)]()

> The configuration backbone of the **IPL Ticket Booking Platform** — manages venues, matches, seating categories, automatic seat generation, and event publishing for every match before a single ticket goes on sale.

---

## Table of Contents

- [Overview](#overview)
- [System Architecture](#system-architecture)
- [Responsibilities & Boundaries](#responsibilities--boundaries)
- [Core Features](#core-features)
- [Domain Model](#domain-model)
- [Event Lifecycle](#event-lifecycle)
- [Design Decisions](#design-decisions)
- [Business Rules](#business-rules)
- [Technology Stack](#technology-stack)
- [Project Structure](#project-structure)
- [API Reference](#api-reference)
- [Getting Started](#getting-started)
- [Testing](#testing)
- [Roadmap](#roadmap)
- [Platform Context](#platform-context)
- [Author](#author)

---

## Overview

The **Event Service** is the administrative core of the IPL Ticket Booking Platform. Before a single ticket can be sold, someone has to define *what* is being sold, *where*, and *how the seats are laid out* — that's this service's job.

Rather than exposing raw ticket inventory, the Event Service prepares the complete event infrastructure: venues, matches, pricing tiers, and the physical seat map. Only once that configuration is validated and complete does an event become eligible for downstream booking — at which point the **Inventory Service** takes over responsibility for live seat availability.

**In one sentence:** Event Service answers *"is this match fully configured and ready to sell?"* — everything after that "yes" belongs to other services.

---

## System Architecture

```text
                           IPL Ticket Booking Platform

                                     Admin
                                       │
                                       ▼
                               Event Service
                                       │
             ┌─────────────────────────┼────────────────────────┐
             ▼                         ▼                        ▼
         Venue Module            Event Module         Seat Category Module
                                                             │
                                                             ▼
                                                    Seat Generation Engine
                                                             │
                                                             ▼
                                                      Published Event
                                                             │
                                                             ▼
                                                     Inventory Service
```

The service sits upstream of everything else in the booking pipeline. Nothing downstream — inventory, booking, payments — can act on an event that hasn't cleared the Event Service's validation gate.

---

## Responsibilities & Boundaries

Keeping a hard line between "what this service owns" and "what it explicitly does not" is what keeps the platform's services loosely coupled.

### ✅ Owned by Event Service

| Domain | Description |
|---|---|
| Venue Management | CRUD operations on stadiums and their capacity metadata |
| Event Management | Match lifecycle from `DRAFT` to `PUBLISHED` |
| Seat Category Configuration | Pricing tiers mapped to row ranges |
| Automatic Seat Generation | Bulk, deterministic seat creation from category configs |
| Event Publishing | Validation gate before an event becomes sellable |
| Business Validation | Capacity checks, duplicate prevention, state-machine enforcement |
| Pagination & Exception Handling | Consistent API ergonomics across all endpoints |

### ❌ Explicitly Out of Scope

| Domain | Owned By |
|---|---|
| Seat Availability | Inventory Service |
| Booking / Reservation | Booking Service |
| Payments | Payment Service |
| Notifications | Notification Service |
| Authentication / Authorization | Auth Service |

This boundary is enforced deliberately — see [Why No Seat Status?](#why-no-seat-status) for the reasoning.

---

## Core Features

### 🏟️ Venue Management

Full CRUD over cricket stadiums, each storing:

- Name, Address, City, State, Country
- Maximum Capacity
- Description

Supports paginated retrieval for catalog-style admin views.

### 🏏 Event Management

Create and manage IPL matches tied to a specific venue. Each event tracks:

- Linked Venue
- Match Title & Description
- Match Date
- Booking Window
- Status (`DRAFT` → `PUBLISHED`)

### 💺 Seat Category Management

Each event defines one or more pricing tiers, each mapped to a contiguous row range:

| Category | Price | Rows |
|---|---:|---|
| VIP | ₹18,000 | A–C |
| Premium | ₹10,000 | D–H |
| Regular | ₹3,500 | J–N |

Every category specifies: **Ticket Price**, **Starting Row**, **Ending Row**, and **Seats Per Row**.

### ⚙️ Automatic Seat Generation

Manually creating thousands of seat rows doesn't scale. Instead, administrators describe the layout and the engine generates every individual seat.

**Input configuration:**

```text
Category:       VIP
Rows:           A–C
Seats Per Row:  25
```

**Generated output:**

```text
VIP-A-001
VIP-A-002
VIP-A-003
...
VIP-C-025
```

Generation runs as a single batch-persistence operation rather than row-by-row inserts, keeping large stadium configurations performant.

### 🚀 Event Publishing

Publishing is the gate between "configured" and "sellable." Before an event can move from `DRAFT` to `PUBLISHED`, the service validates that:

- All seat categories are configured
- Seats have been generated for every category
- Total generated seats respect venue capacity

Only after these checks pass does the event become visible to downstream services — guaranteeing the Inventory Service never has to defend against half-configured events.

---

## Domain Model

```text
Venue
   │
   ▼
Event
   │
   ├──────────────┐
   ▼              ▼
SeatCategory     Seat
```

A `Venue` hosts many `Event`s. Each `Event` owns its own `SeatCategory` definitions and the `Seat`s generated from them — seat categories and seats are scoped per event, not shared globally, since the same physical stand can be priced differently match to match.

---

## Event Lifecycle

```text
Create Venue
      │
      ▼
Create Event            (status: DRAFT)
      │
      ▼
Configure Seat Categories
      │
      ▼
Generate Seats
      │
      ▼
Publish Event            (status: PUBLISHED)
      │
      ▼
Handed off to Inventory Service
```

An event cannot skip stages — publishing is rejected unless every prior stage has been completed and validated.

---

## Design Decisions

### Why Seat Categories Instead of Raw Seats?

Storing thousands of individually-created seat records is both tedious for admins and error-prone. Seat categories let an administrator describe a stand declaratively — row range, seats per row, price — and the system derives every individual seat from that description. One configuration mistake is also far easier to spot and fix than thousands of stray rows.

### Why Is Seat Generation a Separate Operation?

Seat generation happens once per event and is administrative in nature — fundamentally different from seat *retrieval*, which is a frequent, read-heavy operation. Separating the two:

- Keeps write-heavy batch logic isolated from read paths
- Follows the Single Responsibility Principle at the service layer
- Makes it easy to reason about (and test) generation independently of querying

### Why Gate Everything Behind Publishing?

Publishing acts as a validation checkpoint, not just a status flag. It exists specifically to prevent:

- Incomplete event configuration reaching ticket sales
- Events with no seats actually generated
- Seat counts that silently exceed venue capacity
- Accidental premature sale of a match still being configured

### Why No Seat *Status* Field?

A `Seat` record in this service represents a **physical stadium seat** — it exists or it doesn't. Whether that seat is *available*, *held*, or *booked* is state that belongs to the Inventory Service. Mixing both into one model would create two sources of truth for the same concept and force every consumer to reconcile them. Keeping them separate means Event Service can be the system of record for "what physically exists," while Inventory Service owns "what's currently available."

---

## Business Rules

The service enforces the following invariants at the application layer:

- ✅ Booking window must be valid (start precedes end, both in the future at creation time)
- ✅ Duplicate seat categories within the same event are rejected
- ✅ Row ranges within a category must be valid and non-overlapping with sibling categories
- ✅ Seat generation can only be triggered once per event
- ✅ Total seats generated must not exceed venue maximum capacity
- ✅ An event can only be published from the `DRAFT` state
- ✅ Publishing requires seat generation to have already completed successfully
- ✅ All list endpoints support pagination
- ✅ All failures surface through a centralized, consistent exception-handling layer

---

## Technology Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.x |
| Persistence | Spring Data JPA, Hibernate |
| Database | PostgreSQL |
| Build Tool | Maven |
| Boilerplate Reduction | Lombok |

---

## Project Structure

```text
src
└── main
    ├── controller        # REST endpoints
    ├── service
    │      └── impl       # Business logic implementation
    ├── repository         # Spring Data JPA repositories
    ├── mapper             # Entity ↔ DTO conversion
    ├── dto
    │      ├── request     # Inbound API contracts
    │      └── response    # Outbound API contracts
    ├── entity             # JPA entities
    ├── config             # Spring configuration
    └── common
           ├── dto         # Shared DTOs (pagination, error responses)
           ├── entity      # Shared base entities (auditing fields, etc.)
           ├── exception   # Custom exceptions & global handler
           └── util        # Shared utilities
```

---

## API Reference

### Venue APIs

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/v1/venues` | Create a new venue |
| `GET` | `/api/v1/venues` | List venues (paginated) |
| `GET` | `/api/v1/venues/{id}` | Get a venue by ID |
| `PUT` | `/api/v1/venues/{id}` | Update a venue |
| `DELETE` | `/api/v1/venues/{id}` | Delete a venue |

### Event APIs

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/v1/events` | Create a new event (status: `DRAFT`) |
| `GET` | `/api/v1/events` | List events (paginated) |
| `GET` | `/api/v1/events/{id}` | Get an event by ID |
| `PUT` | `/api/v1/events/{id}` | Update an event |
| `DELETE` | `/api/v1/events/{id}` | Delete an event |
| `POST` | `/api/v1/events/{id}/publish` | Validate and publish an event |

### Seat Category APIs

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/v1/events/{eventId}/seat-categories` | Add a seat category to an event |
| `GET` | `/api/v1/events/{eventId}/seat-categories` | List seat categories for an event |
| `GET` | `/api/v1/events/{eventId}/seat-categories/{seatCategoryId}` | Get a specific seat category |
| `PUT` | `/api/v1/events/{eventId}/seat-categories/{seatCategoryId}` | Update a seat category |
| `DELETE` | `/api/v1/events/{eventId}/seat-categories/{seatCategoryId}` | Delete a seat category |

### Seat APIs

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/v1/events/{eventId}/generate-seats` | Trigger batch seat generation for an event |
| `GET` | `/api/v1/events/{eventId}/seats` | List generated seats for an event (paginated) |

---

## Getting Started

### Prerequisites

- Java 21+
- Maven 3.9+
- PostgreSQL 14+ (running locally or accessible via connection string)

### Run Locally

```bash
# Clone the repository
git clone <repository-url>
cd event-service

# Configure database connection in application.yml / application.properties
#   spring.datasource.url=jdbc:postgresql://localhost:5432/event_service_db
#   spring.datasource.username=<your-username>
#   spring.datasource.password=<your-password>

# Build
mvn clean install

# Run
mvn spring-boot:run
```

The service will start on the configured port (default `8080`, or as overridden in `application.yml`).

> **Note:** If this service registers with Eureka as part of the broader platform, ensure the Service Registry is running first so the Event Service can be discovered by other components.

---

## Testing

The service has been end-to-end validated via Postman across the following scenarios:

- Venue CRUD operations
- Event CRUD operations
- Seat Category CRUD operations
- Seat Generation (including idempotency — rejecting a second generation attempt)
- Event Publishing (including rejection paths for incomplete configuration)
- Pagination across all list endpoints
- Input validation and business-rule enforcement
- Global exception handling and error response shape

---

## Roadmap

Planned enhancements, roughly in order of priority:

- [ ] Stadium sections (North / South / East / West) for finer-grained seating geography
- [ ] Dynamic pricing support
- [ ] Multi-day event support
- [ ] Visual seat maps
- [ ] Event image/media attachments
- [ ] Search and filtering across venues and events
- [ ] Soft deletes (currently hard deletes)
- [ ] Swagger / OpenAPI documentation

---

## Platform Context

The Event Service is the **first building block** of the IPL Ticket Booking Platform — a distributed system designed to handle high-concurrency, flash-sale-style ticket booking at scale.

**Companion services (planned/in progress):**

| Service | Responsibility |
|---|---|
| 🔒 Inventory Service | Real-time seat availability via Redis-based locking |
| 🎫 Booking Service | Reservation and order orchestration |
| 💳 Payment Service | Payment processing and settlement |
| 👤 Auth Service | Authentication & authorization (Java 21, Spring Security, JWT, OAuth2) |
| 🔔 Notification Service | Booking confirmations, reminders, alerts |
| 🌐 API Gateway | Single entry point and routing across services |
| ⚙️ Config Server | Centralized configuration management |
| 📦 Service Registry | Service discovery (Eureka) |
| 📊 Monitoring & Observability | Metrics, tracing, and health visibility across the platform |

Together, these services form a scalable, production-oriented architecture built to handle real-world flash-sale booking scenarios — the kind of demand spike an IPL final ticket window actually creates.

---

## Contributors

Built as part of the **Anti-Crash IPL Ticket Flash Sale Engine** project.

- **Prasanth Kumar Bollinedi**
- **Kella Lakshmi Sri Harsha**