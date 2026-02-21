# UrbanServe - Urban Services Booking Platform

A console-based Java application for managing and booking urban services. This project uses MongoDB as its database to handle various entities such as customers, service providers, bookings, and services.

## Features

- **Interactive Console Menu**: Easy-to-use command-line interface for managing the entire system.
- **Collection Management**: Perform CRUD (Create, Read, Update, Delete) operations on:
  - Customers
  - Providers
  - Services
  - Bookings
  - Provider Services (mapping providers to their services with experience levels and pricing)
  - Feedback
  - Booking Details
- **Sample Data Initialization**: Option to auto-populate the database with sample data for quick testing.
- **Database Backend**: Powered by MongoDB for flexible and scalable data storage.

## Project Architecture

- **`ServiceBookingApp.java`**: The main entry point of the application containing the console menu and core application logic.
- **`MongoDBConnection.java`**: Handles the connection to the local MongoDB instance.
- **`DataInitializer.java`**: Contains logic to drop existing collections and insert mock data for testing.

## Prerequisites

- **Java JDK 8 or higher**
- **MongoDB**: A running local MongoDB instance on the default port (`localhost:27017`).
- **Dependencies (JAR files)**:
  - MongoDB Java Driver (`bson`, `mongodb-driver-core`, `mongodb-driver-sync` v5.5.1)
  - SLF4J for logging (`slf4j-api`, `slf4j-simple` v2.0.7)

## Database Details

- **Database Name**: `ServiceBookingDB`
- **Collections**:
  - `customers`
  - `providers`
  - `services`
  - `bookings`
  - `provider_services`
  - `feedback`
  - `booking_details`

## Setup and Execution

1. Make sure MongoDB is running locally on port `27017`.
2. Add the required JAR files to your project's build path (or place them in the classpath as specified in `.classpath`).
3. Compile the Java files in the `src/DBMS_PROJECT` directory.
4. Run the `ServiceBookingApp` main class.
5. On startup, you will be prompted to initialize sample data. Choose `yes` to auto-populate the database or `no` to start with an empty database.
6. Use the on-screen menu to navigate and perform operations.
