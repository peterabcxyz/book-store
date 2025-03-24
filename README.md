# Online Book Store - Backend

## Overview

This is the backend of an online book store application built using Spring Boot and Java. The application allows users to browse books, add them to a shopping cart, proceed to checkout, and view their purchase history. The backend provides the necessary endpoints for book management, user interactions, and order processing.

Note: The application runs on port 8080 by default. If this port is occupied, update server.port in src/main/resources/application.properties to an available port (e.g., server.port=8081).

## Features

- **Book Inventory:** A catalog of books with attributes such as title, genre, ISBN code, author, and year of publication.
- **Search Functionality:** Users can search for books by title, author, year of publication, or genre.
- **Shopping Cart:** Users can add books to a shopping cart and view the contents of the cart.
- **Checkout Process:** Provides simulation of checkout with options like Web, USSD, and Transfer payments.
- **Purchase History:** Users can view their past purchases.

## Technologies Used

- **Spring Boot** (latest version)
- **Java** (version 17)
- **H2 Database** (for development purposes, for production, consider using a more robust database)
- **Maven** (build and dependency management via Maven Wrapper)
- **JUnit & Mockito** (unit testing)
- **Lombok** (to reduce boilerplate code)
- **MapStruct** (for mapping DTOs to entities)
- **Springdoc OpenAPI** (for generating API documentation)

## Project Structure

- **/src/main/java**: Contains the Java code for the application.
    - **/com/bookstore**: Root package.
    - **/com/bookstore/controller**: REST API controllers.
    - **/com/bookstore/model**: Entity classes representing books, orders, and users.
    - **/com/bookstore/repository**: JPA repositories for database interaction.
    - **/com/bookstore/service**: Service layer for business logic.
    - **/com/bookstore/dto**: Data transfer objects (DTOs) for API responses and requests.
    - **/com/bookstore/config**: Configuration classes.
    - **/com/bookstore/exception**: Exception handling classes.
    - **/com/bookstore/mapper**: MapStruct mappers.

- **/src/main/resources**: Configuration files and static resources.
    - **/application.properties**: Configuration for the application, including database settings (H2 in-memory database for development).
    - **/schema.sql**: (Optional) SQL initialization script for database schema.

- **/src/test/java**: Unit and integration tests for the application.

## Database Configuration

The application uses an H2 in-memory database for development. For production, you should configure a more robust database such as PostgreSQL, MySQL, or any other relational database.

The following database configurations are used in the `application.properties`:

```properties
# H2 Database Configuration
spring.datasource.url=jdbc:h2:mem:bookstore;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# Enable H2 Console (only for development)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

## High level Design
#### 1. Component Interaction Diagram
Below is a simplified representation of the component interaction diagram:

```properties
+-----------------------------------------------------+
|                Book Store Application               |
+-----------------------------------------------------+
|                                                     |
|   +----------------+    +----------------+          |
|   |   Client       |    |   Swagger UI   |          |
|   | (Browser/App)  |    | (API Docs)     |          |
|   +----------------+    +----------------+          |
|          |                       |                  |
|          | HTTP Requests         |                  |
|          v                       v                  |
|   +-----------------------------+                   |
|   |    REST Controllers         |                   |
|   | (Book, Cart, Purchase)      |                   |
|   +-----------------------------+                   |
|          |                                          |
|          v                                          |
|   +-----------------------------+                   |
|   |    Service Layer            |                   |
|   | (BookService, CartService,  |                   |
|   |  PurchaseService, Payment)  |                   |
|   +-----------------------------+                   |
|          |                                          |
|          v                                          |
|   +-----------------------------+                   |
|   |    Repositories (JPA)       |                   |
|   | (Book, Cart, Purchase)      |                   |
|   +-----------------------------+                   |
|          |                                          |
|          v                                          |
|   +-----------------------------+                   |
|   |    Database (H2)            |                   |
|   | (Books, Carts, Purchases)   |                   |
|   +-----------------------------+                   |
|                                                     |
+-----------------------------------------------------+
```

#### 2. Domain Model Relationship Diagram
Below is a simplified representation of the domain relationships:
```properties
+----------+       +----------+       +----------+
|   Book   |<--+---| CartItem |---+-->|   Cart   |
+----------+   |   +----------+   |   +----------+
| id       |   |   | id       |   |   | id       |
| title    |   |   | book     |   |   | userId   |
| genre    |   |   | quantity |   |   | items    |
| isbn     |   |   | cart     |   |   +----------+
| author   |   |   +----------+   |
| pubYear  |   |                  |
| price    |   |   +----------+   |
| qtyStock |   +-->|PurchaseItm|   |
+----------+       +----------+   |   +----------+
| id       |   +-->| Purchase |
| book     |       +----------+
| quantity |       | id       |
+----------+       | userId   |
| purchDate|
| items    |
| payMethod|
+----------+

```

## Setup Instructions:
1. Clone the repository:
```properties
git clone https://github.com/peterabcxyz/book-store.git
cd book-store
```

2. Install dependencies:
```properties
./mvnw clean install
```
On Windows, use:
```properties
mvnw.cmd clean install
```

## Running the Application:
Here are the clear instructions to run the application:

### 1. Build the Project:
Ensure you’re in the project root directory (book-store).
Run the following command to compile the code and resolve dependencies:
```properties
./mvnw clean install
```
On Windows, use:
```properties
mvnw.cmd clean install
```

### 2. Start the Application:
Use Maven to launch the Spring Boot application:
```properties
./mvnw spring-boot:run
```
On Windows, use:
```properties
mvnw.cmd clean install
```

Alternatively, run the JAR file directly after building:
```properties
java -jar target/book-store-0.0.1-SNAPSHOT.jar
```

### 3. Verify the Application is Running:
Open a browser or use curl to check if the application is running:
```properties
curl http://localhost:8080/api/inventories/search
```
Alternatively, explore and test the endpoints directly from the browser via Swagger UI
```properties
URL: http://localhost:8080/swagger-ui.html
```

### 4. Access the H2 Console (Optional):
For development, explore the in-memory H2 database:
URL: http://localhost:8080/h2-console
- **DBC URL: jdbc:h2:mem:bookstore**
- **Username: sa**
- **Password: (leave blank)**

### 5. Stop the Application:
Press Control + C (not Ctrl + C, as macOS uses the Control key) in the terminal window where the application is running.