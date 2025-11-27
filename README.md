# Microshop Platform

<div align="center">

![Java](https://img.shields.io/badge/Java-17+-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen)
![React](https://img.shields.io/badge/React-18.2.0-blue)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)
![License](https://img.shields.io/badge/License-Apache%202.0-blue)

**Plataforma e-commerce moderna con arquitectura de microservicios**

[Características](#-características) • [Arquitectura](#-arquitectura) • [Tecnologías](#-tecnologías) • [Instalación](#-instalación) • [Uso](#-uso)

</div>

---

## 📋 Tabla de Contenidos

- [Descripción](#-descripción)
- [Características](#-características)
- [Arquitectura](#-arquitectura)
- [Tecnologías](#-tecnologías)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación](#-instalación)
- [Uso](#-uso)
- [Decisiones de Diseño](#-decisiones-de-diseño)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [API Endpoints](#-api-endpoints)
- [Próximas Mejoras](#-próximas-mejoras)
- [Contribución](#-contribución)
- [Licencia](#-licencia)

---

## 🎯 Descripción

**Microshop Platform** es una plataforma e-commerce completa desarrollada con arquitectura de microservicios. El proyecto demuestra las mejores prácticas de desarrollo empresarial, incluyendo separación de responsabilidades, escalabilidad horizontal, y comunicación entre servicios mediante service discovery.

La plataforma está diseñada para ser un **portafolio profesional** que muestra competencias en:

- Arquitectura de microservicios
- Spring Boot y Spring Cloud
- React y desarrollo frontend moderno
- Integración de sistemas
- DevOps y CI/CD

---

## ✨ Características

### Backend

- ✅ **Arquitectura de Microservicios** con Spring Boot 3.x
- ✅ **Service Discovery** con Netflix Eureka
- ✅ **API Gateway** con Spring Cloud Gateway
- ✅ **Autenticación JWT** con Spring Security
- ✅ **Base de Datos PostgreSQL** con migraciones Flyway
- ✅ **Documentación API** con Swagger/OpenAPI
- ✅ **Validaciones** con Bean Validation
- ✅ **Manejo de Excepciones** centralizado

### Frontend

- ✅ **React 18** con Hooks y Context API
- ✅ **React Router** para navegación
- ✅ **Tailwind CSS** para diseño moderno y responsive
- ✅ **Autenticación JWT** integrada
- ✅ **Panel de Administración** para gestión de productos
- ✅ **Diseño Responsive** para móviles y desktop

### DevOps

- ✅ **Docker Compose** para desarrollo local
- ✅ **CI/CD** con GitHub Actions
- ✅ **Migraciones Automáticas** con Flyway

---

## 🏗️ Arquitectura

### Diagrama de Arquitectura

```
┌─────────────────────────────────────────────────────────────────┐
│                         Cliente Web                             │
│                      (React Frontend)                            │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             │ HTTP/HTTPS
                             ▼
┌─────────────────────────────────────────────────────────────────┐
│                    API Gateway (8080)                            │
│              Spring Cloud Gateway                                │
│  • Enrutamiento centralizado                                    │
│  • Balanceo de carga                                             │
│  • Punto único de entrada                                       │
└───────┬───────────────┬───────────────┬─────────────────────────┘
        │               │               │
        │               │               │
        ▼               ▼               ▼
   ┌────────┐    ┌─────────┐    ┌─────────┐
   │ Users  │    │Products │    │ Orders  │
   │ (8081)  │    │ (8082)  │    │ (8083)  │
   │         │    │         │    │         │
   │ • CRUD  │    │ • CRUD  │    │ • CRUD  │
   │ • Auth  │    │ • Search│    │ • Track │
   └────┬────┘    └────┬────┘    └────┬────┘
        │               │               │
        └───────────────┴───────────────┘
                        │
                        ▼
        ┌───────────────────────────────┐
        │    Eureka Server (8761)        │
        │    Service Discovery           │
        │  • Registro de servicios       │
        │  • Health checks               │
        └───────────────────────────────┘
                        │
                        ▼
        ┌───────────────────────────────┐
        │    PostgreSQL (5432)            │
        │    Base de Datos               │
        │  • Users DB                     │
        │  • Products DB                 │
        │  • Orders DB                   │
        └───────────────────────────────┘
```

### Componentes Principales

#### 1. **Eureka Server** (Puerto 8761)

Servidor de descubrimiento de servicios que mantiene un registro centralizado de todos los microservicios. Permite que los servicios se descubran dinámicamente sin necesidad de conocer URLs hardcodeadas.

**Características:**

- Auto-registro de servicios
- Health checks automáticos
- Dashboard web para monitoreo

#### 2. **API Gateway** (Puerto 8080)

Punto de entrada único para todas las peticiones del cliente. Enruta las solicitudes a los microservicios apropiados usando service discovery.

**Rutas configuradas:**

- `/api/users/**` → `user-service`
- `/api/products/**` → `product-service`
- `/api/orders/**` → `order-service`

#### 3. **User Service** (Puerto 8081)

Gestiona usuarios y autenticación.

**Funcionalidades:**

- CRUD de usuarios
- Autenticación JWT
- Autorización basada en roles (ROLE_USER, ROLE_ADMIN)
- Hash de contraseñas con BCrypt

#### 4. **Product Service** (Puerto 8082)

Gestiona el catálogo de productos.

**Funcionalidades:**

- CRUD de productos
- Búsqueda por categoría
- Gestión de inventario
- Documentación Swagger/OpenAPI

#### 5. **Order Service** (Puerto 8083)

Gestiona pedidos (preparado para implementación completa).

---

## 🛠️ Tecnologías

### Backend

| Tecnología               | Versión  | Uso                          |
| ------------------------ | -------- | ---------------------------- |
| **Java**                 | 17+      | Lenguaje principal           |
| **Spring Boot**          | 3.2.0    | Framework principal          |
| **Spring Cloud**         | 2023.0.0 | Microservicios               |
| **Spring Security**      | 3.2.0    | Autenticación y autorización |
| **Spring Data JPA**      | 3.2.0    | Persistencia                 |
| **Spring Cloud Gateway** | 2023.0.0 | API Gateway                  |
| **Netflix Eureka**       | 2023.0.0 | Service Discovery            |
| **PostgreSQL**           | 15       | Base de datos                |
| **Flyway**               | -        | Migraciones de BD            |
| **JWT (jjwt)**           | 0.12.3   | Tokens de autenticación      |
| **Swagger/OpenAPI**      | 2.3.0    | Documentación API            |
| **Maven**                | 3.6+     | Gestión de dependencias      |

### Frontend

| Tecnología       | Versión | Uso          |
| ---------------- | ------- | ------------ |
| **React**        | 18.2.0  | Framework UI |
| **React Router** | 6.20.0  | Enrutamiento |
| **Tailwind CSS** | 3.3.6   | Estilos      |
| **Axios**        | 1.6.2   | Cliente HTTP |
| **Vite**         | 5.0.0   | Build tool   |

### DevOps

| Herramienta        | Uso                |
| ------------------ | ------------------ |
| **Docker**         | Contenedores       |
| **Docker Compose** | Orquestación local |
| **GitHub Actions** | CI/CD              |

---

## 📦 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- **JDK 17** o superior
- **Maven 3.6+**
- **Node.js 18+** y **npm**
- **Docker** y **Docker Compose**
- **Git**

---

## 🚀 Instalación

### 1. Clonar el Repositorio

```bash
git clone <repository-url>
cd microshop-platform
```

### 2. Levantar PostgreSQL con Docker

```bash
docker-compose up -d
```

Esto iniciará PostgreSQL en el puerto 5432 con las siguientes credenciales:

- **Usuario**: `microshop`
- **Contraseña**: `microshop123`
- **Base de datos**: `microshopdb`

Verifica que el contenedor esté corriendo:

```bash
docker ps
```

### 3. Compilar el Backend

```bash
mvn clean install
```

Esto compilará todos los microservicios y ejecutará las migraciones Flyway automáticamente.

### 4. Iniciar los Servicios Backend

**Importante:** Los servicios deben iniciarse en el siguiente orden:

#### Paso 1: Eureka Server

```bash
cd backend/eureka-server
mvn spring-boot:run
```

Verifica que esté corriendo: http://localhost:8761

#### Paso 2: User Service

```bash
cd backend/user-service
mvn spring-boot:run
```

#### Paso 3: Product Service

```bash
cd backend/product-service
mvn spring-boot:run
```

#### Paso 4: API Gateway

```bash
cd backend/api-gateway
mvn spring-boot:run
```

### 5. Iniciar el Frontend

```bash
cd frontend
npm install
npm run dev
```

El frontend estará disponible en: http://localhost:3000

---

## 💻 Uso

### Verificar Servicios en Eureka

Una vez iniciados todos los servicios, accede al dashboard de Eureka:

- **URL**: http://localhost:8761
- Deberías ver registrados: `user-service`, `product-service` y `api-gateway`

### Probar Autenticación

#### 1. Registrar un Usuario

```bash
POST http://localhost:8080/api/users
Content-Type: application/json

{
  "name": "Juan Pérez",
  "email": "juan@example.com",
  "password": "password123",
  "role": "CUSTOMER"
}
```

O desde el frontend: http://localhost:3000/register

#### 2. Iniciar Sesión

```bash
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "juan@example.com",
  "password": "password123"
}
```

**Respuesta:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "user": {
    "id": 1,
    "name": "Juan Pérez",
    "email": "juan@example.com",
    "role": "CUSTOMER",
    "createdAt": "2025-11-27T22:00:00"
  }
}
```

O desde el frontend: http://localhost:3000/login

#### 3. Usar el Token

```bash
GET http://localhost:8080/api/users
Authorization: Bearer <tu-token-jwt>
```

### Probar CRUD de Productos

#### 1. Listar Productos (Público)

```bash
GET http://localhost:8080/api/products
```

O desde el frontend: http://localhost:3000/products

#### 2. Crear Producto (Requiere Autenticación)

```bash
POST http://localhost:8080/api/products
Authorization: Bearer <tu-token-jwt>
Content-Type: application/json

{
  "name": "Laptop Dell XPS 15",
  "description": "Laptop de alto rendimiento con procesador Intel i7",
  "price": 1299.99,
  "stock": 10,
  "category": "Electrónica"
}
```

O desde el frontend (como ADMIN): http://localhost:3000/admin/products

#### 3. Buscar por Categoría

```bash
GET http://localhost:8080/api/products/search?category=Electrónica
```

#### 4. Obtener Producto por ID

```bash
GET http://localhost:8080/api/products/1
```

#### 5. Actualizar Producto (Requiere Autenticación)

```bash
PUT http://localhost:8080/api/products/1
Authorization: Bearer <tu-token-jwt>
Content-Type: application/json

{
  "name": "Laptop Dell XPS 15 (Actualizada)",
  "description": "Descripción actualizada",
  "price": 1199.99,
  "stock": 8,
  "category": "Electrónica"
}
```

#### 6. Eliminar Producto (Requiere Autenticación)

```bash
DELETE http://localhost:8080/api/products/1
Authorization: Bearer <tu-token-jwt>
```

### Documentación Swagger

Accede a la documentación interactiva del Product Service:

- **URL**: http://localhost:8082/swagger-ui.html

---

## 🎨 Decisiones de Diseño

### ¿Por qué Microservicios?

1. **Escalabilidad Independiente**: Cada servicio puede escalarse según su demanda
2. **Tecnologías Heterogéneas**: Permite usar diferentes tecnologías por servicio
3. **Despliegue Independiente**: Cambios en un servicio no afectan a otros
4. **Fallas Aisladas**: Un fallo en un servicio no derriba toda la aplicación
5. **Equipos Autónomos**: Diferentes equipos pueden trabajar en servicios distintos

### Patrones Arquitectónicos Implementados

#### 1. **Service Discovery Pattern**

- **Implementación**: Netflix Eureka
- **Razón**: Permite descubrimiento dinámico de servicios sin hardcodear URLs
- **Beneficio**: Facilita el escalado horizontal y la alta disponibilidad

#### 2. **API Gateway Pattern**

- **Implementación**: Spring Cloud Gateway
- **Razón**: Punto único de entrada que centraliza cross-cutting concerns
- **Beneficio**: Autenticación, logging, rate limiting en un solo lugar

#### 3. **Database per Service**

- **Implementación**: Cada servicio tiene su propia base de datos (actualmente compartida, preparado para separación)
- **Razón**: Independencia de datos y evolución independiente del esquema
- **Beneficio**: Un servicio no puede acceder directamente a datos de otro

#### 4. **Circuit Breaker Pattern** (Preparado)

- **Razón**: Prevenir fallos en cascada cuando un servicio está caído
- **Estado**: Preparado para implementar con Resilience4j

#### 5. **CQRS Pattern** (Preparado)

- **Razón**: Separar operaciones de lectura y escritura para optimizar rendimiento
- **Estado**: Preparado para implementar

### Arquitectura por Capas

Cada microservicio sigue una arquitectura limpia por capas:

```
Controller Layer    → Maneja peticiones HTTP, validación de entrada
Service Layer       → Contiene lógica de negocio
Repository Layer    → Abstracción del acceso a datos
Model Layer         → Entidades del dominio (JPA)
DTO Layer           → Objetos de transferencia de datos
```

**Beneficios:**

- Separación clara de responsabilidades
- Fácil de testear
- Mantenibilidad mejorada
- Reutilización de código

### Seguridad

- **JWT Tokens**: Stateless authentication
- **BCrypt**: Hash seguro de contraseñas
- **Role-Based Access Control (RBAC)**: Control de acceso por roles
- **Validaciones**: Bean Validation en backend y frontend

---

## 📁 Estructura del Proyecto

```
microshop-platform/
├── .github/
│   └── workflows/          # CI/CD con GitHub Actions
│       ├── backend-ci.yml
│       └── frontend-ci.yml
├── backend/               # Microservicios Backend
│   ├── eureka-server/     # Service Discovery
│   │   ├── pom.xml
│   │   └── src/main/
│   │       ├── java/com/microshop/eureka/
│   │       └── resources/application.yml
│   ├── api-gateway/       # API Gateway
│   │   ├── pom.xml
│   │   └── src/main/
│   │       ├── java/com/microshop/gateway/
│   │       └── resources/application.yml
│   ├── user-service/      # Servicio de Usuarios
│   │   ├── pom.xml
│   │   └── src/main/
│   │       ├── java/com/microshop/user/
│   │       │   ├── controller/    # REST Controllers
│   │       │   ├── service/       # Lógica de negocio
│   │       │   ├── repository/   # JPA Repositories
│   │       │   ├── model/         # Entidades
│   │       │   ├── dto/           # DTOs
│   │       │   ├── security/      # Spring Security
│   │       │   ├── config/         # Configuraciones
│   │       │   ├── exception/      # Excepciones
│   │       │   └── util/           # Utilidades
│   │       └── resources/
│   │           ├── application.yml
│   │           └── db/migration/  # Scripts Flyway
│   ├── product-service/   # Servicio de Productos
│   │   ├── pom.xml
│   │   └── src/main/
│   │       ├── java/com/microshop/product/
│   │       │   ├── controller/
│   │       │   ├── service/
│   │       │   ├── repository/
│   │       │   ├── model/
│   │       │   ├── dto/
│   │       │   ├── config/
│   │       │   ├── exception/
│   │       │   └── util/
│   │       └── resources/
│   │           ├── application.yml
│   │           └── db/migration/
│   └── order-service/     # Servicio de Pedidos
│       └── ...
├── frontend/              # Aplicación React
│   ├── src/
│   │   ├── components/    # Componentes React
│   │   ├── pages/         # Páginas
│   │   ├── services/      # Servicios API
│   │   ├── contexts/      # Context API
│   │   └── ...
│   ├── package.json
│   └── vite.config.js
├── docker-compose.yml     # Configuración Docker
├── pom.xml                # POM padre Maven
└── README.md              # Este archivo
```

---

## 🔌 API Endpoints

### Autenticación

| Método | Endpoint          | Descripción       | Autenticación |
| ------ | ----------------- | ----------------- | ------------- |
| POST   | `/api/auth/login` | Iniciar sesión    | No            |
| POST   | `/api/users`      | Registrar usuario | No            |

### Usuarios

| Método | Endpoint          | Descripción        | Autenticación        |
| ------ | ----------------- | ------------------ | -------------------- |
| GET    | `/api/users`      | Listar usuarios    | Sí (ROLE_USER/ADMIN) |
| GET    | `/api/users/{id}` | Obtener usuario    | Sí (ROLE_USER/ADMIN) |
| POST   | `/api/users`      | Crear usuario      | Sí (ROLE_USER/ADMIN) |
| PUT    | `/api/users/{id}` | Actualizar usuario | Sí (ROLE_USER/ADMIN) |
| DELETE | `/api/users/{id}` | Eliminar usuario   | Sí (ROLE_USER/ADMIN) |

### Productos

| Método | Endpoint                            | Descripción          | Autenticación |
| ------ | ----------------------------------- | -------------------- | ------------- |
| GET    | `/api/products`                     | Listar productos     | No            |
| GET    | `/api/products/{id}`                | Obtener producto     | No            |
| GET    | `/api/products/search?category=...` | Buscar por categoría | No            |
| POST   | `/api/products`                     | Crear producto       | Sí            |
| PUT    | `/api/products/{id}`                | Actualizar producto  | Sí            |
| DELETE | `/api/products/{id}`                | Eliminar producto    | Sí            |

**Nota:** Los endpoints de creación, actualización y eliminación de productos requieren autenticación JWT.

---

## 🔄 Próximas Mejoras

### Backend

- [ ] Implementar Order Service completo
- [ ] Comunicación entre servicios con OpenFeign
- [ ] Circuit Breaker con Resilience4j
- [ ] Distributed Tracing con Zipkin/Jaeger
- [ ] Logging centralizado (ELK Stack)
- [ ] Métricas con Prometheus y Grafana
- [ ] Separación de bases de datos por servicio
- [ ] Cache con Redis
- [ ] Mensajería asíncrona (RabbitMQ/Kafka)

### Frontend

- [ ] Tests unitarios con React Testing Library
- [ ] Optimización de imágenes
- [ ] PWA capabilities
- [ ] Internacionalización (i18n)
- [ ] Dark mode
- [ ] Carrito de compras
- [ ] Checkout y pagos

### DevOps

- [ ] Dockerización de servicios
- [ ] Kubernetes deployment manifests
- [ ] Helm charts
- [ ] Monitoring con Grafana
- [ ] Alertas con Prometheus

---

## 🤝 Contribución

Este es un proyecto de portafolio profesional. Las contribuciones son bienvenidas.

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

---

## 📄 Licencia

Este proyecto está bajo la Licencia Apache 2.0. Ver el archivo `LICENSE` para más detalles.

---

## 👤 Autor

**Microshop Platform Team**

- Proyecto desarrollado como portafolio profesional
- Demostrando competencias en arquitectura de microservicios, Spring Boot y React

---

## 📞 Soporte

Para preguntas o sugerencias, abre un issue en el repositorio.

---

<div align="center">

**Desarrollado con ❤️ usando Spring Boot, Spring Cloud y React**

⭐ Si te gusta este proyecto, dale una estrella!

</div>
