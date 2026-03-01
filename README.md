# Facturación API

Proyecto de sistema de facturación desarrollado con Spring Boot y Dockerizado para facilitar su despliegue.

## Tecnologías

*   Java 21
*   Spring Boot
*   MySQL 8.0
*   Docker & Docker Compose
*   Maven
*   JWT (JSON Web Tokens) para seguridad

## Requisitos

*   Docker y Docker Compose instalados.
*   (Opcional) Java 21 y Maven si se desea ejecutar sin contenedores.

## Instalación y Ejecución (Docker)

La forma más sencilla de ejecutar el proyecto es utilizando Docker Compose.

1.  **Navegar a la carpeta del proyecto**.

2.  **Construir y levantar los contenedores**:

    ```bash
    docker-compose up --build
    ```

    Este comando compilará la aplicación (asegúrate de que el Dockerfile incluya la etapa de build o que hayas generado el .jar previamente si el Dockerfile lo requiere), creará la imagen de Docker y levantará tanto la base de datos MySQL como la aplicación Spring Boot.

3.  **Acceder a la aplicación**:
    Una vez que los contenedores estén corriendo, la API estará disponible en:
    `http://localhost:8080`

## Configuración

El archivo `docker-compose.yml` ya contiene la configuración necesaria para conectar la aplicación con la base de datos.

*   **Base de datos**: MySQL
*   **Puerto de la App**: 8080
*   **Puerto de la BD (Host)**: 3307 (Mapeado al 3306 del contenedor para evitar conflictos con instalaciones locales)
*   **Credenciales de BD**:
    *   Usuario: `user`
    *   Contraseña: `password`
    *   Base de datos: `facturacion`
    *   Root password: `rootpassword`

## Funcionamiento de la API

La aplicación expone una API REST para la gestión de facturas y clientes, protegida mediante autenticación JWT.

### Autenticación (`/auth`)

*   **Registro**: `POST /auth/register` - Permite registrar nuevos usuarios.
*   **Login**: `POST /auth/login` - Autentica al usuario y devuelve un token JWT necesario para acceder a los otros endpoints.

### Clientes (`/clients`)

Endpoints protegidos (requieren rol `ADMIN` según configuración):

*   `GET /clients`: Listar todos los clientes.
*   `GET /clients/{id}`: Obtener un cliente por ID.
*   `POST /clients`: Crear un nuevo cliente.
*   `PUT /clients/{id}`: Actualizar un cliente existente.
*   `DELETE /clients/{id}`: Eliminar un cliente.

### Facturas (`/invoices`)

Endpoints para la gestión de facturas:

*   `GET /invoices`: Listar todas las facturas.
*   `GET /invoices/{id}`: Obtener una factura por ID.
*   `POST /invoices`: Crear una nueva factura (recibe `InvoiceDTO`).
*   `PUT /invoices/{id}`: Actualizar una factura.
*   `DELETE /invoices/{id}`: Eliminar una factura.

## Estructura del Proyecto

*   `src/`: Código fuente de la aplicación Spring Boot.
*   `Dockerfile`: Definición de la imagen de la aplicación.
*   `docker-compose.yml`: Orquestación de los servicios (App + BD).
*   `pom.xml`: Dependencias de Maven.
