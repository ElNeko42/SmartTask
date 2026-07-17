# Smart Task Allocator API

API que recibe tareas entrantes y las **asigna automáticamente** al miembro del equipo
con menor carga de trabajo disponible, aplicando reglas estrictas de capacidad.

El núcleo del proyecto es el **dominio**: es él quien decide a quién se asigna cada tarea,
sin consultar la base de datos. La infraestructura (REST, persistencia) solo orquesta.

## Arquitectura

Diseño guiado por el dominio (**DDD**) sobre una arquitectura **hexagonal**
(puertos y adaptadores):

```
com.smarttask.taskallocator
├── domain            → Modelo de dominio puro (sin dependencias de framework)
│   └── model
│       ├── task      → Task, TaskTitle, TaskUrgency
│       └── member    → Member (agregado), capacidad y regla canAccept()
├── application       → Casos de uso
│   ├── port.in       → Puertos de entrada (qué puede pedir el mundo exterior)
│   ├── port.out      → Puertos de salida (qué necesita el dominio del exterior)
│   └── service       → Implementación de los casos de uso
└── infrastructure    → Adaptadores
    └── adapter
        ├── in.web        → Controladores REST + DTOs
        └── out.persistence → Repositorios (in-memory / JPA)
```

### Conceptos de dominio destacados

- **Value Objects**: `TaskTitle` (valida longitud), `TaskUrgency` (enum con lógica de peso).
- **Agregados / Entidades**: `Member` (mantiene su lista de tareas y su límite de capacidad),
  `Task`.
- **Regla de negocio principal**: `Member.canAccept(Task task)` — el agregado decide,
  controlando su propio estado, si puede aceptar una tarea sin exceder su capacidad.

## Stack

| Capa      | Tecnología          |
|-----------|---------------------|
| Backend   | Java 17, Spring Boot 3, Maven |
| Frontend  | React               |

## Cómo ejecutar

### Backend

```bash
cd backend
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

Endpoints principales:

| Método | Ruta            | Descripción                                   |
|--------|-----------------|-----------------------------------------------|
| POST   | `/api/members`  | Da de alta a un miembro (`name`, `maxCapacity`) |
| GET    | `/api/members`  | Lista el equipo con su carga actual           |
| POST   | `/api/tasks`    | Envía una tarea y la asigna automáticamente   |

### Frontend

```bash
cd frontend
npm install
npm run dev
```

La interfaz queda en `http://localhost:5173` y habla con el backend a través de un
proxy de Vite (`/api` → `http://localhost:8080`), así que basta con tener el backend
arrancado.

## Tests

```bash
cd backend
./mvnw test
```

Cubren los value objects, la regla `canAccept` y la invariante de capacidad del
agregado `Member`, la política de asignación y el caso de uso de asignación.

## Estado

✅ Backend (DDD + hexagonal, API REST, tests) y frontend (React + TypeScript)
funcionando de extremo a extremo. Persistencia in-memory; el puerto `MemberRepository`
permite sustituirla por JPA sin tocar dominio ni aplicación.
