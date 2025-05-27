[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/pcuellarfer/practica_integradora)


# 🚀 Práctica Integradora - Sistema de Gestión Empresarial

## 📋 Descripción General

Esta aplicación es un **sistema integral de gestión empresarial** desarrollado como práctica integradora del Grupo F. El sistema permite la administración completa de empleados, nóminas, colaboraciones y catálogo de productos a través de una arquitectura de microservicios dockerizada.

## 🏗️ Arquitectura y Tecnologías

### Contenedores Docker
La aplicación utiliza una arquitectura distribuida con los siguientes servicios:

- **🐬 MySQL Database**: Base de datos principal en puerto 3306
- **🍃 Spring Boot Apps**: Aplicaciones de administración y empleados en Tomcat (puerto 8080)
- **🌐 Apache HTTP Server**: Servidor web frontend en puerto 80

### Stack Tecnológico

**Backend:**
- Spring Boot 3.4.4 con Java 17/23 
- Spring Security para autenticación 
- Spring Data JPA con MySQL 
- Thymeleaf para vistas 

**Frontend:**
- HTML5, CSS3, JavaScript
- Bootstrap 5.3.0
- Font Awesome 6.5.0

**Base de Datos:**
- MySQL 8 con esquema completo de gestión empresarial 

## 📋 Prerequisitos

- 🐳 **Docker** y **Docker Compose** instalados
- 🔌 Puertos 80, 3306 y 8080 disponibles
- 🌐 Conexión a internet para descargar las imágenes

## 📥 Clonación del Repositorio

```bash
# Clonar el repositorio
git clone https://github.com/davidsmh23/prueba_integradora.git

# Navegar al directorio del proyecto
cd prueba_integradora
```

## 🚀 Ejecución de la Aplicación

### 1️⃣ Levantar todos los contenedores

```bash
# Ejecutar desde el directorio raíz del proyecto
docker-compose up -d
```

### 2️⃣ Verificar que los servicios estén corriendo

```bash
# Ver el estado de los contenedores
docker-compose ps

# Ver logs en tiempo real
docker-compose logs -f
```

### 3️⃣ Esperar la inicialización completa

El sistema incluye un healthcheck para MySQL que garantiza que la base de datos esté lista antes de iniciar las aplicaciones Spring Boot.

## 🌐 Acceso a la Aplicación

### Desde la Red Local (Servidor)
- **Frontend Apache**: `http://localhost:80`
- **App Administración**: `http://localhost:8080`
- **Base de Datos**: `localhost:3306`

### Desde Cliente Externo (a través del Router)
- **Frontend**: `http://[IP_DEL_ROUTER]:80`
- **Aplicaciones**: `http://[IP_DEL_ROUTER]:8080`

> 💡 **Nota**: La aplicación está diseñada para ejecutarse en un servidor detrás de un router. Los usuarios se conectan desde sus máquinas y realizan solicitudes al router, que redirige el tráfico a los servicios correspondientes.

## 📁 Estructura del Proyecto

```
prueba_integradora/
├── 🏢 administracion-app/     # Aplicación de administración Spring Boot
├── 🐱 tomcat/                 # Configuración y contenido del servidor tomcat
├── 🌐 apache/                 # Configuración y contenido del servidor web
├── 🗄️ sql/                    # Scripts de inicialización de BD
└── 🐳 docker-compose.yaml     # Orquestación de contenedores
```

## ✨ Características Principales

### 👥 Gestión de Empleados
- Registro completo de datos personales y laborales
- Gestión de especialidades y departamentos
- Sistema de jerarquías y colaboraciones

### 💰 Sistema de Nóminas
- Cálculo automático de nóminas con devengos y deducciones 
- Gestión de datos económicos y bancarios

### 🔐 Seguridad y Autenticación
- Sistema de usuarios con recuperación de contraseñas 
- Configuración de email para notificaciones
- Control de sesiones y bloqueos

### 📦 Gestión de Catálogo
- Sistema de productos con categorías y colores
- Diferentes tipos de productos (libros, muebles, ropa)

## 🛠️ Comandos Útiles

```bash
# Parar los servicios
docker-compose down

# Rebuild y restart
docker-compose down && docker-compose up -d --build

# Ver logs de un servicio específico
docker-compose logs [nombre_servicio]

# Acceder al contenedor MySQL
docker-compose exec mysql mysql -u root -padmin integradora_compose
```

## 🔧 Configuración de Red

La aplicación utiliza una red Docker personalizada llamada `red_integradora` que permite la comunicación entre todos los contenedores. Las aplicaciones Spring Boot se conectan a MySQL usando el hostname `mysql` definido en el compose.

---

⚡ **¡Listo para usar!** Una vez ejecutados estos pasos, tendrás un sistema completo de gestión empresarial funcionando en tu infraestructura.
