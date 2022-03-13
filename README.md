# Ticket Booking
Demo repo for setting up baseline.
## Code Structure
```
.
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── darkhorse
    │   │           └── ticketbooking
    │   │               ├── TicketBookingApplication.java 
    │   │               └── order
    │   │                   ├── constants       --Contants
    │   │                   ├── controller      --Api definition
    │   │                   ├── exception       --Exceptions
    │   │                   ├── gateway         --3rd party clients
    │   │                   ├── messagequeue    --Message Queue
    │   │                   ├── model           --Domain model
    │   │                   ├── repository      --DB persistant components
    │   │                   └── service         --Business processs
    │   └── resources
    │       ├── application.yml
    │       ├── db
    │       │   └── migration                   --Flyway scripts
    │       ├── static
    │       └── templates
    └── test
        ├── java
        │   └── com
        │       └── darkhorse
        │           └── ticketbooking
        │               ├── base
        │               │   └── BaseContainerTest.java  --Unit test base
        │               ├── common
        │               │   └── JSONUtils.java  
        │               └── order
        │                   ├── controller
        │                   ├── gateway
        │                   ├── messagequeue
        │                   ├── repository
        │                   └── service
        └── resources
            ├── application-test.yml
            └── db
                └── init.sql            --Init DB scripts for testcontainer

```
## Prerequisite
* Java11
* gradle
* Colima: Provide docker env for local DB and testcontainers
* DB: sqlserver
## Build
```shell
./gradlew build
```