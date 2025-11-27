# GitHub Actions CI/CD Workflows

Este directorio contiene los workflows de CI/CD para el proyecto Microshop Platform.

## Workflows Disponibles

### 1. Backend CI (`backend-ci.yml`)

Workflow para compilar y testear los microservicios Java/Spring Boot.

**Triggers:**
- Push a ramas `main` o `develop`
- Pull requests a ramas `main` o `develop`
- Solo se ejecuta cuando hay cambios en archivos Java, pom.xml o application.yml

**Jobs:**
- `build-and-test`: Compila y ejecuta tests
- `validate-project-structure`: Valida la estructura del proyecto Maven

**Pasos principales:**
1. Checkout del código
2. Configuración de JDK 17
3. Ejecución de tests (`mvn test`)
4. Compilación y empaquetado (`mvn package`)
5. Subida de artefactos (JARs)

### 2. Frontend CI (`frontend-ci.yml`)

Workflow para compilar y testear la aplicación React.

**Triggers:**
- Push a ramas `main` o `develop`
- Pull requests a ramas `main` o `develop`
- Solo se ejecuta cuando hay cambios en el directorio `frontend/`

**Jobs:**
- `build-and-test`: Compila y ejecuta tests
- `validate-project-structure`: Valida la estructura del proyecto

**Pasos principales:**
1. Checkout del código
2. Configuración de Node.js (versiones 18.x y 20.x)
3. Instalación de dependencias (`npm ci`)
4. Ejecución de linter (`npm run lint`)
5. Ejecución de tests (si existen)
6. Build del proyecto (`npm run build`)
7. Subida de artefactos (dist/)

## Configuración

### Requisitos Previos

1. **Backend:**
   - Proyecto Maven configurado correctamente
   - Tests unitarios en `src/test/java`

2. **Frontend:**
   - `package.json` con scripts configurados
   - `package-lock.json` presente (para npm ci)

### Variables de Entorno (Opcional)

Si necesitas variables de entorno para los tests, puedes agregarlas en los workflows:

```yaml
env:
  SPRING_PROFILES_ACTIVE: test
  DATABASE_URL: ${{ secrets.DATABASE_URL }}
```

### Secrets de GitHub

Para usar secrets en los workflows:

1. Ve a Settings → Secrets and variables → Actions
2. Agrega los secrets necesarios
3. Úsalos en el workflow: `${{ secrets.SECRET_NAME }}`

## Verificación Local

Antes de hacer push, puedes verificar que los comandos funcionan localmente:

### Backend:
```bash
mvn clean test
mvn clean package -DskipTests
```

### Frontend:
```bash
cd frontend
npm ci
npm run lint
npm run build
```

## Artefactos

Los workflows generan artefactos que se pueden descargar:

- **Backend:** JARs compilados de cada microservicio
- **Frontend:** Directorio `dist/` con la build de producción

Los artefactos se mantienen por 7 días.

## Mejoras Futuras

- [ ] Agregar deployment automático
- [ ] Integración con Docker para crear imágenes
- [ ] Notificaciones en Slack/Email
- [ ] SonarQube para análisis de código
- [ ] Tests de integración con bases de datos

