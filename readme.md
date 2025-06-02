# InfinityTech

A InfinityTech é uma API RESTful desenvolvida com React/Typescript e Java/Spring Boot. Seu objetivo é monitorar falhas
na API ExtrAI Dados da Di2win, fornecendo relatórios detalhados sobre erros e desempenho.

## Ficha Técnica

- **Base URL:** https://api.infinitytech.com
- **Formato de Dados:** JSON para requisições e respostas.
- **Autenticação:** _A definir_
- **Códigos de status:**
  - 200 OK: Requisição atendida com sucesso.
  - 201 Created: Recurso criado com sucesso.
  - 400 Bad Request: Erro na requisição (por exemplo, parâmetros incorretos).
  - 404 Not Found: Arquivo ou recurso não localizado.
  - 500 Internal Server Error: Erro interno do servidor.

## Features

- Dashboard com gráficos e indicadores
- Tabela de erros detalhada
- Filtros por data e pesquisa
- Download de relatórios
- Notificações de erros em tempo real

## Tecnologias

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

## Pré-requisitos

### Frontend

- Instale o [Node.js](https://nodejs.org/pt) (versão 18 ou superior)
- Instale o pnpm: `npm install -g pnpm`

### Backend

- Instale o [Java 17 ou superior](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
- Instale o [Maven](https://maven.apache.org/)
- Instale o [PostgreSQL](https://www.postgresql.org/download/)
- Recomenda-se iniciar o projeto backend pelo [Spring Initializr](https://start.spring.io/)

## Guia de Instalação

1. Clone o repositório: https://github.com/zcriticz/infinitytech.git

2. Abra o projeto no seu editor de código favorito (recomenda-se
   o [IntelliJ IDEA](https://www.jetbrains.com/idea/download/))

### Frontend

3. No terminal, acesse a pasta `frontend` e execute:

   ```bash
   pnpm install
   pnpm run dev

### Backend

1. Para o backend, abra o terminal na pasta do projeto e execute:

```bash
mvn spring-boot:run
```

Para mais detalhes, consulte a documentação do Spring Boot. Vocẽ pode encontrá-lo no arquivo `pom.xml` do projeto.

2. Certifique-se de que o banco de dados PostgreSQL esteja rodando e configurado corretamente.

## Autores

- [Vitor Martins](https://github.com/VitorMarins) - Desenvolvedor Frontend
- [Vinicius Gabriel](https://github.com/viniciusgss) - Desenvolvedor Frontend
- [Luciano Tomaz](https://github.com/HadryTz) - Desenvolvedor Frontend
- [Cristian Santos](https://github.com/zcriticz) - Desenvolvedor Backend
- [Gabriela Pires](https://github.com/Gabipsn11) - Desenvolvedora Backend
- [Pedro Vitor](https://github.com/PV-Lopes) - Banco de Dados
- [Rayra Lima](https://github.com/Rayralima) - Banco de Dados

## Licença

Este projeto é licenciado sob a Licença MIT - consulte o arquivo [LICENSE](LICENSE) para mais detalhes.

