# Northwind — Aplicación Web Full Stack

**Universidad de las Fuerzas Armadas ESPE**  
**Autor:** Romel Yugcha  
**Asignatura:** Aplicaciones Web

---

## Descripción

Aplicación web de gestión de pedidos basada en la base de datos clásica **Northwind**. Permite administrar productos, registrar nuevas órdenes y consultar el historial de pedidos. La arquitectura separa completamente el backend (API REST) del frontend (SPA).

---

## Tecnologías

### Backend
| Tecnología | Versión |
|---|---|
| Java | 25 |
| Spring Boot | 3.4.5 |
| Spring Data JPA | — |
| Spring Security | — |
| PostgreSQL Driver | — |
| Lombok | — |
| Maven | — |

### Frontend
| Tecnología | Versión |
|---|---|
| Vue 3 | ^3.4.29 |
| Vite | ^5.3.1 |
| Vue Router | ^4.3.3 |
| Pinia | ^2.1.7 |
| Axios | ^1.7.2 |
| Tailwind CSS | ^3.4.4 |

### Base de datos
- PostgreSQL — base de datos Northwind

---

## Estructura del proyecto

```
├── BACKEND/                        # API REST Spring Boot
│   └── src/main/java/com/northwind/backend/
│       ├── config/                 # SecurityConfig, DataInitializer
│       ├── controller/             # AuthController, ProductController,
│       │                           # OrderController, CatalogController
│       ├── dto/                    # Request y Response DTOs
│       ├── entity/                 # Entidades JPA
│       ├── repository/             # Repositorios Spring Data
│       └── service/                # Lógica de negocio
│
├── FRONTEND-VUE/                   # SPA Vue 3
│   └── src/
│       ├── api/                    # Configuración de Axios
│       ├── components/             # NavBar
│       ├── router/                 # Rutas protegidas
│       ├── stores/                 # Pinia: auth, cart
│       └── views/                  # Vistas de la aplicación
│
└── database/                       # Scripts SQL de inicialización
```

---

## Configuración de la base de datos

1. Crear la base de datos `northwind` en PostgreSQL e importar el dump original de Northwind.

2. Ejecutar los scripts en orden desde la carpeta `database/`:

```sql
-- 01: Crear tabla de usuarios de la app
\i database/01_create_app_users.sql

-- 02: Agregar columna app_user_id a orders
\i database/02_alter_orders.sql

-- 03: Insertar usuarios de prueba (contraseñas BCrypt)
\i database/03_seed_app_users.sql

-- 04: Crear secuencia para orders.order_id
\i database/04_orders_sequence.sql

-- 05: Crear secuencia para products.product_id
\i database/05_products_sequence.sql
```

---

## Configuración del backend

Editar `BACKEND/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/northwind
spring.datasource.username=postgres
spring.datasource.password=admin
server.port=8080
```

---

## Ejecución

### Backend

```bash
cd BACKEND
./mvnw spring-boot:run
```

La API queda disponible en `http://localhost:8080`.

### Frontend

```bash
cd FRONTEND-VUE
npm install
npm run dev
```

La aplicación queda disponible en `http://localhost:5173`.

---

## Endpoints de la API

### Autenticación
| Método | Ruta | Descripción |
|---|---|---|
| POST | `/api/auth/login` | Iniciar sesión |

### Productos
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/products?page=0&size=10` | Listar productos paginados |
| GET | `/api/products/{id}` | Obtener producto por ID |
| POST | `/api/products` | Crear producto |
| PUT | `/api/products/{id}` | Actualizar producto |
| DELETE | `/api/products/{id}` | Deshabilitar producto |

### Órdenes
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/orders` | Listar todas las órdenes |
| GET | `/api/orders/{id}` | Detalle de una orden |
| POST | `/api/orders` | Crear nueva orden |

### Catálogo
| Método | Ruta | Descripción |
|---|---|---|
| GET | `/api/categories` | Listar categorías |
| GET | `/api/suppliers` | Listar proveedores |
| GET | `/api/customers` | Listar clientes |

---

## Rutas del frontend

| Ruta | Vista | Protegida |
|---|---|---|
| `/login` | Inicio de sesión | No |
| `/products` | Tabla de productos con paginación | Sí |
| `/products/new` | Formulario nuevo producto | Sí |
| `/products/:id/edit` | Formulario editar producto | Sí |
| `/orders` | Listado de órdenes | Sí |
| `/orders/new` | Nueva orden con carrito | Sí |
| `/orders/:id` | Detalle de orden | Sí |

---

## Usuarios de prueba

| Usuario | Contraseña |
|---|---|
| `admin` | `admin123` |

> Las contraseñas se almacenan con hash **BCrypt**.

---

## Funcionalidades principales

- Autenticación con sesión en memoria (Pinia + localStorage)
- CRUD de productos con validación de formularios
- Paginación de 10 en 10 en la tabla de productos
- Creación de órdenes con carrito de compras
- Visualización del detalle de cada orden
- Protección de rutas en el frontend
- CORS configurado para desarrollo local
