# SafetyNet Alerts

SafetyNet Alerts is a Spring Boot-based application designed to provide critical information related to fire stations, emergency alerts, and community data. The application exposes several RESTful APIs that allow users to query information about people, fire stations, and community emails based on various parameters.

## Table of Contents

- [Features](#features)
- [Architecture](#architecture)
- [Technologies Used](#technologies-used)
- [Installation](#installation)
- [Running the Application](#running-the-application)
- [API Endpoints](#api-endpoints)
  - [Get People by Fire Station](#get-people-by-fire-station)
  - [Child Alert](#child-alert)
  - [Phone Alert](#phone-alert)
  - [Fire Details by Address](#fire-details-by-address)
  - [Flood Alerts](#flood-alerts)
  - [Person Information](#person-information)
  - [Community Emails](#community-emails)
- [Testing](#testing)
- [Logging](#logging)
- [Contributing](#contributing)
- [License](#license)

## Features

- **Fire Station Management**: Retrieve information about people serviced by specific fire stations.
- **Child Alerts**: Identify and list children residing at a particular address.
- **Emergency Contact Information**: Access phone numbers of individuals within a fire station's jurisdiction.
- **Detailed Fire Information**: Obtain comprehensive details about residents at a specific address, including medical information.
- **Flood Alerts**: Group households by fire station jurisdiction for flood preparedness.
- **Personal Information Lookup**: Fetch detailed personal information based on first and last names.
- **Community Email Collection**: Gather email addresses of all residents within a specified city.
- **Robust Logging**: Every request and response is logged for monitoring and debugging purposes.
- **Unit Testing**: Comprehensive unit tests ensure the reliability and correctness of all functionalities.
- **MVC Architecture**: Follows the Model-View-Controller design pattern adhering to SOLID principles for maintainable and scalable code.

## Architecture

SafetyNet Alerts is structured following the Model-View-Controller (MVC) design pattern:

- **Model**: Represents the data structures (`Person`, `FireStation`, etc.) and interacts with the database through repositories.
- **View**: Exposes RESTful endpoints to the clients.
- **Controller**: Handles incoming HTTP requests, delegates processing to services, and returns appropriate responses.
- **Service**: Contains the business logic, processing data from repositories and preparing responses.

## Technologies Used

- **Java 17**
- **Spring Boot**
- **Spring Web**
- **Spring Data JPA**
- **H2 Database** (or any preferred relational database)
- **Maven**
- **JUnit & Mockito** for testing
- **SLF4J & Logback** for logging

## Installation

1. **Clone the Repository**

   ```bash
   git clone https://github.com/Fall2024-NSCC-ECampus/final-project-alerts-notification-system-w0140158.git
   cd final-project-alerts-notification-system-w0140158
