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

## Estado

🚧 En construcción — proyecto de portfolio desarrollado de forma incremental.
