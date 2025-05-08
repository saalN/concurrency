# Script Runner

This project allows you to execute scripts in parallel using Java threads. It provides an API to trigger script execution, monitor results, and manage executions. It uses Spring Boot for the backend and connects to a MySQL database to store execution details.

## Features

- Trigger script execution through a RESTful API.
- View execution results and statuses.
- Delete execution records.
- Parallel script execution using Java threads.

## Technology Stack

- **Backend**: Spring Boot
- **Database**: MySQL
- **Concurrency**: Java Threads via ExecutorService
- **Docker**: Dockerize the application and MySQL for ease of deployment.

## Prerequisites

Before running the project, ensure you have the following tools installed:

- **Docker**: To build and run containers.
- **Docker Compose**: To manage multi-container Docker applications.
- **JDK 17**: If you're building and running the project locally.

## How to Run the Project

### 1. Clone the repository

First, clone this repository to your local machine:

git clone https://github.com/saalN/concurrency.git
cd concurrency 

### 2. Setup Docker Environment
The project comes with a docker-compose.yml file that will set up both the Spring Boot application and a MySQL database. The docker-compose.yml file configures the containers and their interactions.

Make sure your docker-compose.yml file is set up as follows:
version: "3.8"

services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: test
    ports:
      - "3306:3306"
    networks:
      - backend
    volumes:
      - mysql-data:/var/lib/mysql

  app:
    image: your-image-name:latest
    build: .
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/test
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: root
    ports:
      - "8080:8080"
    depends_on:
      - mysql
    networks:
      - backend

networks:
  backend:
    driver: bridge

volumes:
  mysql-data:
    driver: local

### 3. Start the application with Docker Compose
Now that everything is set up, you can start the application and MySQL database using Docker Compose:

docker-compose up --build

This command will:

- Build the Docker images (if not already built).

- Start the MySQL container with the database configured.

- Start your Spring Boot application.

The application will be available at http://localhost:8080/swagger-ui.html and http://localhost:8080

### 5. Interact with the API
Once the application is up and running, you can interact with the API:

- **Trigger script execution**: POST /api/scripts/trigger/{scriptName}

- **Get execution results**: GET /api/scripts/results

- **Get script details by ID**: GET /api/scripts/{id}

- **Delete script by ID**: DELETE /api/scripts/{id}

### 6. Stop the Docker Containers
To stop the application and MySQL containers, use the following command:

docker-compose down

This will stop and remove the containers but keep the volumes intact for the MySQL database.


### Database Schema

The application uses a MySQL database with a table `execution_detail` to store the script execution details. The schema includes the following columns:

- **id**: Unique identifier for each execution.
- **script_name**: Name of the script that was executed.
- **status**: Status of the execution (RUNNING, COMPLETED, FAILED).
- **result**: The output or result of the script execution.
- **start_time**: Timestamp when the script started executing.
- **end_time**: Timestamp when the script finished executing.
