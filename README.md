# Ticket Booking
Demo repo for setting up baseline.
## Package Structure Overview
```
├── README.md
├── build.gradle
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── darkhorse
    │   │           └── ticketbooking
    │   │               ├── TicketBookingApplication.java
    │   │               └── order
    │   │                   └── constants   --Contants
    │   │                       └── Message.java
    │   │                   ├── controller  --Api definitions
    │   │                   │   ├── OrderController.java
    │   │                   │   └── dto     --Controller layer models
    │   │                   ├── exception   --Exceptions
    │   │                   │   ├── NoAvailableSeatException.java
    │   │                   │   ├── OrderException.java
    │   │                   │   └── SeatBookServiceUnavailableException.java
    │   │                   ├── gateway     --3rd party clients
    │   │                   │   ├── FlightReportClient.java
    │   │                   │   ├── FlightReportGateway.java
    │   │                   │   ├── SeatBookingClient.java
    │   │                   │   ├── SeatBookingGateway.java
    │   │                   │   └── dto     --Gateway layer models
    │   │                   ├── messagequeue    --Message queues
    │   │                   │   ├── FlightReportQueue.java
    │   │                   │   ├── QueueConfiguration.java
    │   │                   │   └── dto     --MessageQueue layer models
    │   │                   ├── model       --Core models
    │   │                   │   ├── Order.java
    │   │                   │   ├── OrderStatus.java
    │   │                   │   └── Passenger.java
    │   │                   ├── repository  --Repositories
    │   │                   │   ├── OrderJpaRepository.java
    │   │                   │   ├── OrderRepository.java
    │   │                   │   ├── PassengerJpaRepository.java
    │   │                   │   └── po      --Persistent models
    │   │                   └── service     --Core processes
    │   │                       └── OrderService.java
    │   └── resources
    │       ├── application.yml
    │       └── db
    │           └── migration
    │               ├── V20220312200202__create-orders-table.sql
    │               └── V20220312200222__create-passengers-table.sql
    └── test
        ├── java
        │   └── com
        │       └── darkhorse
        │           └── ticketbooking
        │               ├── base
        │               │   └── BaseContainerTest.java  --Base unit test class
        │               ├── common
        │               │   └── JSONUtils.java
        │               └── order
        │                   ├── controller
        │                   │   └── OrderControllerTest.java
        │                   ├── gateway
        │                   │   ├── FlightReportGatewayTest.java
        │                   │   └── SeatBookingGatewayTest.java
        │                   ├── messagequeue
        │                   │   └── FlightReportQueueTest.java
        │                   ├── repository
        │                   │   └── OrderRepositoryTest.java
        │                   └── service
        │                       └── OrderServiceTest.java
        └── resources
            ├── application-test.yml
            └── db
                └── init.sql        --Init DB script for testcontainer

```
## Prerequisite
* Java11
* gradle
* Colima
  * Provide docker env for local DB and testcontainers. SqlServer is used as DB.
  * Link: https://github.com/abiosoft/colima
  * FAQ: 如遇安装问题见公司邮件"舍弃docker desktop切换colima踩坑日记"
## Main technical dependencies
* SpringBoot
* Feign
* RabbitMQ
* Testing
  * Test Container --Faking Database
  * Wiremock --Faking 3rd party clients
  * Mockito --Stubbing dependencies in UT
## Build
```shell
./gradlew build
```
## Run
* use postman mock server to fake flight report and seat booking service
* start up colima
* create sqlserver container and rabbitmq