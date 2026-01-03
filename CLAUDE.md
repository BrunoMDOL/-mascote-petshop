# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Run the application (development)
mvn spring-boot:run

# Compile the project
mvn compile

# Package as JAR
mvn package

# Clean and package
mvn clean package

# Run the packaged JAR
java -jar target/mascote-petshop-1.0.0.jar
```

## Project Overview

Mascote Pet Shop - Sistema de catálogo e pedidos para pet shop. Aplicação web Java usando Spring Boot 3.2 com JDK 21.

**Stack:** Spring Boot 3.2, Thymeleaf, Spring Data JPA, H2 Database (persistido em arquivo), Bootstrap 5, JavaScript vanilla.

## Architecture

```
src/main/java/com/mascotepetshop/
├── MascotePetShopApplication.java    # Entry point
├── config/
│   ├── WebConfig.java                # Static resources & uploads config
│   └── DataLoader.java               # Seed data (CommandLineRunner)
├── controller/
│   ├── CatalogoController.java       # Public catalog pages
│   ├── AdminController.java          # Admin CRUD operations
│   └── ApiController.java            # REST endpoints
├── model/
│   ├── Categoria.java                # Category entity
│   └── Produto.java                  # Product entity
├── repository/
│   ├── CategoriaRepository.java
│   └── ProdutoRepository.java
└── service/
    ├── CategoriaService.java
    ├── ProdutoService.java
    └── FileStorageService.java       # Image upload handling
```

## Key URLs

- `/` - Public catalog
- `/admin` - Admin panel (login: admin / mascote123)
- `/h2-console` - Database console (dev only)

## Data Persistence

H2 database persists to `./data/mascotepetshop.mv.db`. Uploaded images are stored in `./uploads/` directory and served at `/uploads/*`.

## Business Configuration

Pet shop info (name, address, WhatsApp, hours) is configured in `application.properties` with `petshop.*` prefix.
