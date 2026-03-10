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

## Despliegue en Producción con AWS (App Runner + RDS)

Esta guía te mostrará cómo desplegar la aplicación de forma rápida y sencilla en AWS.

### Paso 1: Subir el código a un repositorio de GitHub

AWS App Runner necesita acceso a tu código para construir y desplegar la aplicación.

1.  Crea un nuevo repositorio en [GitHub](https://github.com/new).
2.  Sube el código de este proyecto a tu repositorio.

### Paso 2: Crear una base de datos con AWS RDS

1.  **Ve a la consola de AWS** y busca el servicio **RDS**.
2.  Haz clic en **"Crear base de datos"**.
3.  **Configuración**:
    *   **Elegir un método de creación**: "Creación estándar".
    *   **Motor**: "MySQL".
    *   **Plantilla**: "Nivel gratuito" (Free Tier) para empezar sin costos.
    *   **Identificador de la instancia**: `facturacion-db`.
    *   **Credenciales**: Define un usuario y una contraseña maestra. **Guárdalos bien, los necesitarás más adelante**.
    *   **Conectividad**:
        *   En "Acceso público", selecciona **"Sí"**. Esto permitirá que App Runner se conecte a la base de datos.
        *   **Importante**: Para un entorno de producción real, se recomienda configurar una VPC y mantener la base de datos privada. Sin embargo, para un despliegue rápido, el acceso público es la opción más sencilla.
4.  Haz clic en **"Crear base de datos"**. La creación tardará unos minutos.
5.  Una vez creada, ve a los detalles de la instancia y **copia el "Endpoint"** (la URL de la base de datos).

### Paso 3: Desplegar la aplicación con AWS App Runner

1.  **Ve a la consola de AWS** y busca el servicio **App Runner**.
2.  Haz clic en **"Crear un servicio de App Runner"**.
3.  **Configuración**:
    *   **Fuente**: "Repositorio de código fuente".
    *   **Conexión**: Conecta tu cuenta de GitHub a AWS.
    *   **Repositorio y rama**: Selecciona el repositorio que creaste en el Paso 1 y la rama principal (`main` o `master`).
    *   **Desencadenador del despliegue**: "Automático".
4.  **Configurar la compilación**:
    *   App Runner detectará que es un proyecto de Java y usará un runtime de Amazon Corretto.
    *   **Comando de compilación**: `mvn clean install -DskipTests`
    *   **Comando de inicio**: `java -jar target/facturacion-0.0.1-SNAPSHOT.jar`
    *   **Puerto**: `8080`.
5.  **Configurar el servicio**:
    *   **Nombre del servicio**: `facturacion-api`.
    *   **Variables de entorno**: Aquí conectarás la aplicación a la base de datos RDS.
        *   `DATABASE_URL`: `jdbc:mysql://<ENDPOINT_DE_TU_BD_RDS>:3306/facturacion` (Reemplaza `<ENDPOINT_DE_TU_BD_RDS>` con el que copiaste).
        *   `DATABASE_USERNAME`: El usuario que configuraste en RDS.
        *   `DATABASE_PASSWORD`: La contraseña que configuraste en RDS.
6.  Revisa la configuración y haz clic en **"Crear y desplegar"**.

App Runner comenzará a construir la imagen y a desplegar tu servicio. Una vez completado, te proporcionará una URL pública donde tu API estará disponible.

## Estructura del Proyecto

*   `src/`: Código fuente de la aplicación Spring Boot.
*   `Dockerfile`: Definición de la imagen de la aplicación.
*   `docker-compose.yml`: Orquestación de los servicios (App + BD).
*   `pom.xml`: Dependencias de Maven.
