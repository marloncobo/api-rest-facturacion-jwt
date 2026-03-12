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
*   Cuenta de Google Cloud Platform (GCP).
*   Google Cloud CLI instalado y configurado.

## Instalación y Ejecución Local (Docker)

La forma más sencilla de ejecutar el proyecto localmente es utilizando Docker Compose.

1.  **Navegar a la carpeta del proyecto**.

2.  **Construir y levantar los contenedores**:

    ```bash
    docker-compose up --build
    ```

    Este comando compilará la aplicación (el Dockerfile incluye una etapa de build con Maven), creará la imagen de Docker y levantará tanto la base de datos MySQL como la aplicación Spring Boot.

3.  **Acceder a la aplicación**:
    Una vez que los contenedores estén corriendo, la API estará disponible en:
    `http://localhost:8080`

## Despliegue en Google Cloud Platform (Cloud Run)

Este proyecto está configurado para ser desplegado en **Google Cloud Run**, un servicio serverless que ejecuta contenedores.

### Pasos para Desplegar

1.  **Habilitar servicios en GCP**:
    Asegúrate de tener habilitadas las APIs de *Cloud Run* y *Artifact Registry* en tu proyecto de GCP.

2.  **Configurar Google Cloud CLI**:
    ```bash
    gcloud auth login
    gcloud config set project [ID_DE_TU_PROYECTO]
    ```

3.  **Crear un repositorio en Artifact Registry** (si no existe):
    ```bash
    gcloud artifacts repositories create facturacion-repo --repository-format=docker \
    --location=us-central1 --description="Repositorio para API Facturación"
    ```

4.  **Construir y subir la imagen a GCP**:
    Desde la raíz del proyecto, ejecuta el siguiente comando para construir la imagen usando Cloud Build y subirla al registro:
    ```bash
    gcloud builds submit --tag us-central1-docker.pkg.dev/[ID_DE_TU_PROYECTO]/facturacion-repo/facturacion-app:v1
    ```
    *(Reemplaza `[ID_DE_TU_PROYECTO]` con el ID real de tu proyecto)*.

5.  **Desplegar en Cloud Run**:
    Ejecuta el siguiente comando para desplegar la imagen como un servicio. Necesitarás configurar las variables de entorno para la base de datos de producción (por ejemplo, una instancia de Cloud SQL o una base de datos externa).

    ```bash
    gcloud run deploy facturacion-service \
      --image us-central1-docker.pkg.dev/[ID_DE_TU_PROYECTO]/facturacion-repo/facturacion-app:v1 \
      --platform managed \
      --region us-central1 \
      --allow-unauthenticated \
      --set-env-vars DATABASE_URL="jdbc:mysql://[HOST_BD]:3306/[NOMBRE_BD]",DATABASE_USERNAME="[USUARIO]",DATABASE_PASSWORD="[PASSWORD]"
    ```
    *   **Importante**: Reemplaza las variables de entorno (`DATABASE_URL`, `DATABASE_USERNAME`, `DATABASE_PASSWORD`) con los datos de tu base de datos de producción.
    *   `--allow-unauthenticated`: Hace que el servicio sea público. Si quieres restringirlo, quita esta bandera.

6.  **Verificar despliegue**:
    Al finalizar, la consola te mostrará la URL de tu servicio (ej. `https://facturacion-service-xpMQ...run.app`).

## Configuración

El archivo `docker-compose.yml` ya contiene la configuración necesaria para conectar la aplicación con la base de datos localmente.

*   **Base de datos**: MySQL
*   **Puerto de la App**: 8080
*   **Puerto de la BD (Host)**: 3307 (Mapeado al 3306 del contenedor para evitar conflictos con instalaciones locales)
*   **Credenciales de BD (Local)**:
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
*   `Dockerfile`: Definición de la imagen de la aplicación (Multi-stage build).
*   `docker-compose.yml`: Orquestación de los servicios para desarrollo local.
*   `pom.xml`: Dependencias de Maven.
