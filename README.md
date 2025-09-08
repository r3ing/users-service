# User Microservice (Spring Boot 2.5.14, Java 11, Gradle 7.4)

Servicio para **creación** y **consulta** de usuarios.

## Requisitos
- **Java 11**.
- **Spring Boot 2.5.14** + **Gradle 7.4**.
- **Persistencia H2** con Spring Data JPA.
- **JWT** para `token` y rotación en `/login`.
- **Validaciones** con regex para email y password.

## Cómo compilar y ejecutar

> Necesitas Java 11 y Gradle 7.4 instalados localmente (o usa el wrapper de tu entorno).

```bash
# Compilar + tests + reporte de cobertura
gradle clean build

# Ejecutar
gradle bootRun
```

La app levanta en `http://localhost:8080`.

### Variables
Configura (opcional) por entorno:
- `JWT_SECRET` (default: `very-secret-key-for-dev-only`)
- `JWT_EXPIRATION_MILLIS` (default: `3600000`)

## Endpoints

### 1) `POST /api/v1/users/sign-up`
**Request (JSON)**
```json
{
  "name": "Julio Gonzalez",
  "email": "julio@test.com",
  "password": "a2asfGfdfdf4",
  "phones": [
    {"number": 87650009, "citycode": 7, "contrycode": "25"}
  ]
}
```

**Response 200**
```json
{
  "id": "uuid",
  "created": "2025-09-06T16:43:00.000Z",
  "lastLogin": "2025-09-06T16:43:00.000Z",
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "isActive": true,
  "name": "Julio Gonzalez",
  "email": "julio@test.com",
  "password": "a2asfGfdfdf4",
  "phones": [
    {"number": 87650009, "citycode": 7, "contrycode": "25"}
  ]
}
```

**Errores JSON**
```json
{
  "error": [{
    "timestamp": "2025-09-06T16:43:00.000Z",
    "codigo": 400,
    "detail": "email: Email format is invalid"
  }]
}
```
- `409` cuando el email ya existe.

### 2) `GET /api/v1/users/login`
Debes enviar el **token** previo en `Authorization: Bearer <token>`.

**Response 200** (token rotado)
```json
{
  "id": "uuid",
  "created": "Nov 16, 2021 12:51:43 PM",
  "lastLogin": "Nov 16, 2021 12:51:43 PM",
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "isActive": true,
  "name": "Julio Gonzalez",
  "email": "julio@testssw.cl",
  "password": "********",
  "phones": [
    {"number": 87650009, "citycode": 7, "contrycode": "25"}
  ]
}
```

## Curl rápido
```bash
curl -s -X POST http://localhost:8080/api/v1/users/sign-up   -H 'Content-Type: application/json'   -d '{"name":"Julio","email":"julio@test.com","password":"abCdef12","phones":[{"number":12345678,"citycode":2,"contrycode":"56"}]}'

# Usar el token devuelto arriba
curl -s -X GET http://localhost:8080/api/v1/usersl/ogin -H "Authorization: Bearer <TOKEN>"
```


## Diagramas (UML)
En la carpeta `diagrams/` (PlantUML).

- **Component Diagram**: `component.puml`
- **Sequence Diagram**: `sequence-signup-login.puml`

Puedes renderizar en https://www.plantuml.com/plantuml/

