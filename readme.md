# InfinityTech

InfinityTech is a RESTful API developed with React/TypeScript and Java/Spring Boot. Its goal is to monitor failures in
the Di2win ExtrAI Dados API, providing detailed reports on errors and performance.

## API Reference

- **Base URL:** https://api.infinitytech.com
- **Data Format:** JSON for requests and responses.
- **Authentication:** _To be defined_
- **Status Codes:**
  - 200 OK: Request successfully processed.
  - 201 Created: Resource successfully created.
  - 400 Bad Request: Error in the request (e.g., incorrect parameters).
  - 404 Not Found: File or resource not found.
  - 500 Internal Server Error: Internal server error.

## Features

- Dashboard with charts and indicators
- Detailed error table
- Date filters and search
- Report downloads
- Real-time error notifications

## Technologies

### Frontend

[![React](https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB)](https://reactjs.org/)

[![TypeScript](https://img.shields.io/badge/TypeScript-007ACC?style=for-the-badge&logo=typescript&logoColor=white)](https://www.typescriptlang.org/)

[![Ant Design](https://img.shields.io/badge/Ant%20Design-0170FE?style=for-the-badge&logo=antdesign&logoColor=white)](https://ant.design/)

[![PNPM](https://img.shields.io/badge/pnpm-22272E?style=for-the-badge&logo=pnpm&logoColor=F69220)](https://pnpm.io/)

### Backend

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)

[![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/)

[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)

[![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)

## Prerequisites

### Frontend

- Install [Node.js](https://nodejs.org/en) (version 18 or higher)
- Install pnpm: `npm install -g pnpm`

### Backend

- Install [Java 17 or higher](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- Install [Maven](https://maven.apache.org/)
- Install [PostgreSQL](https://www.postgresql.org/download/)
- It is recommended to start the backend project using [Spring Initializr](https://start.spring.io/)

## Installation Guide

- Clone the repository: https://github.com/zcriticz/infinitytech.git

- Open the project in your favorite code editor (we
  recommend [IntelliJ IDEA](https://www.jetbrains.com/idea/download/))

### Frontend

- In the terminal, go to the `frontend` folder and run:

   ```bash
   pnpm install
   pnpm run dev

### Backend

For the backend, open the terminal in the project folder and run:

`mvn spring-boot:run`

- For more details, check the Spring Boot documentation. You can find it in the project's pom.xml file.

- Make sure PostgreSQL is running and properly configured.
  Contribution Guide
  We welcome contributions! To contribute, please follow these steps:

## Contribution Guide

We welcome contributions! To contribute, please follow these steps:

1. Fork this repository.
2. Create a new branch for your feature or bugfix: git checkout -b feature/your-feature
3. Make your changes and commit them with clear messages.
4. Push your branch: git push origin feature/your-feature
5. Open a Pull Request describing your changes.

Please ensure your code follows the project's style and includes tests when applicable.

## Authors

- Vitor Martins - Frontend Developer
- Vinicius Gabriel - Frontend Developer
- Luciano Tomaz - Frontend Developer
- Cristian Santos - Backend Developer
- Gabriela Pires - Backend Developer
- Pedro Vitor - Database
- Rayra Lima - Database

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.