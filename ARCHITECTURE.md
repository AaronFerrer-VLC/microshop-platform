# Arquitectura de Microshop Platform

## Visión General

Microshop Platform es una aplicación e-commerce construida con arquitectura de microservicios, utilizando Spring Boot 3.x y Spring Cloud. La arquitectura está diseñada para ser escalable, mantenible y seguir las mejores prácticas de desarrollo.

## Diagrama de Arquitectura

```
┌─────────────────────────────────────────────────────────────┐
│                        Cliente                               │
└────────────────────────┬────────────────────────────────────┘
                         │
                         ▼
┌─────────────────────────────────────────────────────────────┐
│                    API Gateway (8080)                        │
│              Spring Cloud Gateway                            │
└────────┬───────────────┬───────────────┬────────────────────┘
         │               │               │
         ▼               ▼               ▼
    ┌────────┐      ┌─────────┐    ┌─────────┐
    │ Users  │      │Products │    │ Orders  │
    │ (8081) │      │ (8082)  │    │ (8083)  │
    └────────┘      └─────────┘    └─────────┘
         │               │               │
         └───────────────┴───────────────┘
                         │
                         ▼
              ┌──────────────────────┐
              │   Eureka Server      │
              │      (8761)          │
              │  Service Discovery   │
              └──────────────────────┘
```

## Componentes de la Arquitectura

### 1. Eureka Server

**Propósito**: Servidor de descubrimiento de servicios (Service Discovery)

**Puerto**: 8761

**Responsabilidades**:
- Mantener un registro de todos los microservicios disponibles
- Permitir que los servicios se registren y descubran entre sí
- Proporcionar información de salud de los servicios

**Tecnologías**:
- Spring Cloud Netflix Eureka Server

### 2. API Gateway

**Propósito**: Punto de entrada único para todas las peticiones del cliente

**Puerto**: 8080

**Responsabilidades**:
- Enrutar peticiones a los microservicios apropiados
- Balanceo de carga entre instancias de servicios
- Punto centralizado para cross-cutting concerns (autenticación, logging, etc.)

**Rutas Configuradas**:
- `/api/users/**` → `user-service`
- `/api/products/**` → `product-service`
- `/api/orders/**` → `order-service`

**Tecnologías**:
- Spring Cloud Gateway
- Netflix Eureka Client (para descubrimiento dinámico)

### 3. User Service

**Propósito**: Gestión de usuarios del sistema

**Puerto**: 8082

**Responsabilidades**:
- CRUD de usuarios
- Autenticación y autorización (a implementar)
- Gestión de perfiles de usuario

**Estructura de Capas**:
```
user-service/
├── controller/    # REST Controllers
├── service/       # Lógica de negocio
├── repository/    # Acceso a datos (JPA)
├── model/         # Entidades del dominio
└── dto/           # Data Transfer Objects
```

**Tecnologías**:
- Spring Boot Web
- Spring Data JPA
- H2 Database (temporal, para desarrollo)

### 4. Product Service

**Propósito**: Gestión del catálogo de productos

**Puerto**: 8082

**Responsabilidades**:
- CRUD de productos
- Gestión de inventario
- Búsqueda y filtrado de productos
- Gestión de categorías

**Estructura de Capas**:
```
product-service/
├── controller/    # REST Controllers
├── service/       # Lógica de negocio
├── repository/    # Acceso a datos (JPA)
├── model/         # Entidades del dominio
└── dto/           # Data Transfer Objects
```

**Tecnologías**:
- Spring Boot Web
- Spring Data JPA
- H2 Database (temporal, para desarrollo)

### 5. Order Service

**Propósito**: Gestión de pedidos

**Puerto**: 8083

**Responsabilidades**:
- Creación de pedidos
- Consulta y seguimiento de pedidos
- Gestión del estado de pedidos
- Integración con otros servicios (usuarios, productos)

**Estructura de Capas**:
```
order-service/
├── controller/    # REST Controllers
├── service/       # Lógica de negocio
├── repository/    # Acceso a datos (JPA)
├── model/         # Entidades del dominio
└── dto/           # Data Transfer Objects
```

**Tecnologías**:
- Spring Boot Web
- Spring Data JPA
- H2 Database (temporal, para desarrollo)

## Patrones Arquitectónicos Implementados

### 1. Arquitectura por Capas

Cada microservicio sigue una arquitectura limpia por capas:

- **Controller Layer**: Maneja las peticiones HTTP, validación de entrada, serialización/deserialización
- **Service Layer**: Contiene toda la lógica de negocio
- **Repository Layer**: Abstracción del acceso a datos
- **Model Layer**: Entidades del dominio (JPA Entities)
- **DTO Layer**: Objetos de transferencia de datos para comunicación con clientes

### 2. Service Discovery

Utilizando Eureka para descubrimiento dinámico de servicios:
- Los servicios se registran automáticamente al iniciar
- El API Gateway descubre servicios dinámicamente
- No es necesario conocer las URLs exactas de los servicios

### 3. API Gateway Pattern

- Punto único de entrada
- Enrutamiento basado en rutas
- Balanceo de carga automático
- Preparado para añadir autenticación, rate limiting, etc.

## Comunicación entre Servicios

### Actual (Preparado)
- Los servicios están configurados para registrarse en Eureka
- El API Gateway puede descubrir servicios dinámicamente

### Próximos Pasos
- Implementar comunicación síncrona usando Spring Cloud OpenFeign
- Implementar comunicación asíncrona usando mensajería (RabbitMQ/Kafka)
- Implementar circuit breakers para resiliencia

## Base de Datos

### Estado Actual
- Cada servicio tiene su propia base de datos H2 en memoria
- Ideal para desarrollo y pruebas rápidas

### Próximos Pasos
- Migrar a bases de datos persistentes (PostgreSQL/MySQL)
- Implementar Database per Service pattern
- Configurar conexiones de base de datos por servicio

## Seguridad (A Implementar)

- Spring Security + JWT para autenticación
- OAuth2 para autorización
- Validación de tokens en el API Gateway
- Roles y permisos por servicio

## Observabilidad (A Implementar)

- Spring Boot Actuator (ya configurado)
- Logging centralizado (ELK Stack)
- Distributed Tracing (Zipkin/Jaeger)
- Métricas (Prometheus + Grafana)

## Escalabilidad

La arquitectura está diseñada para escalar horizontalmente:
- Cada servicio puede ejecutarse en múltiples instancias
- Eureka gestiona el registro de múltiples instancias
- El API Gateway balancea carga automáticamente

## Orden de Inicio Recomendado

1. **Eureka Server** (debe iniciarse primero)
2. **User Service**
3. **Product Service**
4. **Order Service**
5. **API Gateway** (debe iniciarse último para tener todos los servicios disponibles)

## Configuración de Puertos

| Servicio | Puerto | URL Base |
|----------|--------|----------|
| Eureka Server | 8761 | http://localhost:8761 |
| API Gateway | 8080 | http://localhost:8080 |
| User Service | 8081 | http://localhost:8081 |
| Product Service | 8082 | http://localhost:8082 |
| Order Service | 8083 | http://localhost:8083 |

## Convenciones de Nomenclatura

- **Paquetes**: `com.microshop.{service-name}`
- **Clases de aplicación**: `{Service}Application.java`
- **Controllers**: `{Entity}Controller.java`
- **Services**: `{Entity}Service.java`
- **Repositories**: `{Entity}Repository.java`
- **Models**: `{Entity}.java`
- **DTOs**: `{Entity}DTO.java` o `{Entity}RequestDTO.java` / `{Entity}ResponseDTO.java`

## Próximas Mejoras

1. **Comunicación entre Servicios**
   - OpenFeign para comunicación síncrona
   - RabbitMQ/Kafka para comunicación asíncrona

2. **Resiliencia**
   - Circuit Breaker (Resilience4j)
   - Retry mechanisms
   - Timeout handling

3. **Persistencia**
   - Migración a PostgreSQL/MySQL
   - Configuración de múltiples bases de datos

4. **Seguridad**
   - JWT Authentication
   - OAuth2 Authorization
   - API Key management

5. **Testing**
   - Unit tests
   - Integration tests
   - Contract tests (Spring Cloud Contract)

6. **DevOps**
   - Docker containers
   - Docker Compose para desarrollo local
   - Kubernetes deployment manifests

