# NextStep

## Descripción del proyecto

**NextStep** es un proyecto de fin de grado en ingeniería informática diseñado para apoyar a los jóvenes en su transición hacia la independencia financiera. Proporciona una plataforma completa para gestionar gastos, planificar pagos, simular escenarios financieros y generar informes detallados, todo en una única aplicación.

El objetivo principal es facilitar la educación financiera y proporcionar herramientas prácticas para la gestión económica personal.

---

## Funcionalidades principales

1. **Tablero inicial (`InicioView`)**:
   - Resumen general de la situación financiera del usuario.
   - Incluye gráficos interactivos y accesos rápidos.

2. **Control de pagos (`PagosView`)**:
   - Gestión de pagos recurrentes.
   - Creación, edición y eliminación de pagos.

3. **Control de gastos (`GastosView`)**:
   - Registro de gastos y organización por categorías personalizadas.

4. **Generación de informes (`InformePdfService`)**:
   - Informes en PDF configurables por periodos (semanal, mensual, trimestral).

5. **Simulación financiera (`SimulacionView`)**:
   - Herramienta para evaluar la situación financiera futura del usuario.

6. **Notificaciones (`InAppNotifView` y `EmailNotifService`)**:
   - Recordatorios de pagos y metas financieras, con notificaciones in-app y por correo.

7. **Panel administrativo (`AdminView`)**:
   - Herramientas para que administradores gestionen usuarios y sus datos.

8. **Perfil y de notificaciones (`PerfilView`)**:
   - Configuración y personalización de perfil y notificaciones.

---

## Tecnologías utilizadas

- **Backend:** Java con Spring Boot (JWT para autenticación).
- **Frontend:** Vaadin Flow (Java basado en componentes).
- **Base de datos:** MySQL (gestionada con Docker Compose).
- **Contenerización:** Docker y Docker Compose.
- **Diseño:** Lucidchart (modelado UML) y Figma (interfaces).

---

## Cómo instalar y ejecutar el proyecto

El proyecto está completamente contenerizado, por lo que no se requieren configuraciones manuales. Solo necesitas instalar **Docker** y **Docker Compose**.

### **1. Requisitos previos**
1. **Instalar Docker y Docker Compose**:
   - [Guía de instalación de Docker](https://docs.docker.com/get-docker/)
   - [Guía de instalación de Docker Compose](https://docs.docker.com/compose/install/)

2. **Clonar el repositorio**:
   ```bash
   git clone git@github.com:UFV-INGINF/proyecto-fin-de-grado-2024-YBaamondeUFV.git
   ```

---

### **2. Ejecutar el proyecto**

1. **Iniciar todos los servicios:**
   Desde el directorio raíz del proyecto, ejecuta:
   ```bash
   docker-compose up --build
   ```

2. **Acceso a los servicios:**
   - **Frontend (interfaz de usuario):** [http://localhost:3000](http://localhost:3000)
   - **Backend (API REST):** [http://localhost:8080](http://localhost:8080)
   - **Base de datos MySQL:** Disponible en `localhost:3306`.

3. **Parar los servicios:**
   Para detener todos los contenedores:
   ```bash
   docker-compose down
   ```

---

## Estructura del proyecto

```
nextstep/
├── backend/           # Código fuente del backend (Spring Boot)
│   ├── Dockerfile     # Imagen Docker del backend
├── frontend/          # Código fuente del frontend (Vaadin)
│   ├── Dockerfile     # Imagen Docker del frontend
├── mysql/             # Archivos de inicialización para MySQL
├── docker-compose.yml # Configuración de Docker Compose
├── README.md          # Documentación del proyecto
└── .env               # Variables de entorno
```

---

## Notas adicionales

1. **Datos iniciales en la base de datos:**
   - Puedes agregar datos iniciales en el archivo `mysql/init.sql`. Este archivo se cargará automáticamente al iniciar el contenedor de MySQL.

2. **Configuraciones sensibles:**
   - Variables como contraseñas y claves están gestionadas en el archivo `.env`.

---

## Contacto

- **Autor:** Yago Baamonde Soengas
- **Correo:** yagobaamonde@gmail.com
- **GitHub:** https://github.com/UFV-INGINF/proyecto-fin-de-grado-2024-YBaamondeUFV
