create table if not exists banco
(
    id     binary(16)   not null
        primary key,
    nombre varchar(255) null
);

create table if not exists categoria
(
    id     binary(16)   not null
        primary key,
    nombre varchar(255) null
);

create table if not exists departamento
(
    id     binary(16)   not null
        primary key,
    nombre varchar(255) null
);

create table if not exists especialidad
(
    id     binary(16)   not null
        primary key,
    nombre varchar(255) null
);

create table if not exists genero
(
    id        binary(16)   not null
        primary key,
    identidad varchar(255) null
);

create table if not exists pais
(
    id                 binary(16)   not null
        primary key,
    nombre             varchar(255) null,
    prefijo_telefonico varchar(255) null
);

create table if not exists producto
(
    es_perecedero     bit          not null,
    fecha_fabricacion date         null,
    precio            double       not null,
    tipo_producto     tinyint      null,
    unidades          int          not null,
    id                binary(16)   not null
        primary key,
    descripcion       varchar(255) null,
    marca             varchar(255) null
);

create table if not exists libro
(
    alto           double       null,
    ancho          double       null,
    numero_paginas int          null,
    profundidad    double       null,
    segunda_mano   bit          null,
    id             binary(16)   not null
        primary key,
    autor          varchar(255) null,
    editorial      varchar(255) null,
    tapa           varchar(255) null,
    titulo         varchar(255) null,
    constraint FKi1tww7xnqtr9t96rqjyt014y2
        foreign key (id) references producto (id)
);

create table if not exists mueble
(
    alto        double       null,
    ancho       double       null,
    profundidad double       null,
    id          binary(16)   not null
        primary key,
    estilo      varchar(255) null,
    tipo_madera varchar(255) null,
    constraint FKatvsga8926qb1uh91grhllx2x
        foreign key (id) references producto (id)
);

create table if not exists producto_categorias
(
    categorias_id binary(16) not null,
    productos_id  binary(16) not null,
    constraint FK9mgtryxmdsmvwxc6ty4un2tvr
        foreign key (productos_id) references producto (id),
    constraint FKbwu008ctojlkgrtlkskhe625
        foreign key (categorias_id) references categoria (id)
);

create table if not exists producto_colores
(
    colores     tinyint    null,
    producto_id binary(16) not null,
    constraint FKgwgpdggf6y4lig0frrmpwdwpc
        foreign key (producto_id) references producto (id)
);

create table if not exists proveedor
(
    id     binary(16)   not null
        primary key,
    nombre varchar(255) null
);

create table if not exists ropa
(
    id       binary(16)   not null
        primary key,
    genero   varchar(255) null,
    material varchar(255) null,
    talla    varchar(255) null,
    constraint FKpg941i8mrujc3d24jxmq8j3bo
        foreign key (id) references producto (id)
);

create table if not exists tipo_documento
(
    id   binary(16)   not null
        primary key,
    tipo varchar(255) null
);

create table if not exists tipo_tarjeta
(
    id     binary(16)   not null
        primary key,
    nombre varchar(255) null
);

create table if not exists tipo_via
(
    id   binary(16)   not null
        primary key,
    tipo varchar(255) null
);

create table if not exists token_recuperacion
(
    id    bigint       not null
        primary key,
    email varchar(255) null,
    token varchar(255) null
);

create table if not exists usuario
(
    contador_inicios        int            not null,
    estado_bloqueado        bit            not null,
    bloqueado_hasta         datetime(6)    null,
    pk_usuario              binary(16)     not null
        primary key,
    contrasena              varchar(255)   null,
    contrasena_recuperacion varchar(255)   null,
    email                   varchar(255)   null,
    motivo_bloqueo          varchar(255)   null,
    nombre                  varchar(255)   null,
    archivo                 varbinary(255) null
);

create table if not exists empleado
(
    codigo_postal      int          null,
    edad               int          null,
    fecha_contratacion date         null,
    fecha_nacimiento   date         null,
    numero_direccion   int          null,
    telefono           int          null,
    fecha_cese         datetime(6)  null,
    departamento_id    binary(16)   null,
    genero_id          binary(16)   null,
    jefe_id            binary(16)   null,
    pais_id            binary(16)   null,
    pk_empleado        binary(16)   not null
        primary key,
    tipo_documento_id  binary(16)   null,
    tipo_via           binary(16)   null,
    usuario_id         binary(16)   null,
    apellido           varchar(255) null,
    comentarios        varchar(255) null,
    documento          varchar(255) null,
    foto_url           varchar(255) null,
    localidad          varchar(255) null,
    nombre             varchar(255) null,
    nombre_direccion   varchar(255) null,
    planta             varchar(255) null,
    portal             varchar(255) null,
    prefijo_telefono   varchar(255) null,
    puerta             varchar(255) null,
    region             varchar(255) null,
    constraint UK6ff36el6hfqwrtnvk0y9jd6sh
        unique (usuario_id),
    constraint FK1vh081x7velnqbx2u80u81jh4
        foreign key (jefe_id) references empleado (pk_empleado),
    constraint FKb5jqkb078n71303323tckun9y
        foreign key (genero_id) references genero (id),
    constraint FKcvqmeghkabb4tt6472pabt2a4
        foreign key (usuario_id) references usuario (pk_usuario),
    constraint FKftbecmo27nxjkpgmcnh9243ng
        foreign key (tipo_documento_id) references tipo_documento (id),
    constraint FKhdjjhohpyjsfta5g6p8b8e00i
        foreign key (departamento_id) references departamento (id),
    constraint FKktu7bn17ear2q61by9m456874
        foreign key (pais_id) references pais (id)
);

create table if not exists colaboracion
(
    empleado_1_id binary(16) null,
    empleado_2_id binary(16) null,
    id            binary(16) not null
        primary key,
    constraint FKdsogsutyv7eswh9ihybovj1p0
        foreign key (empleado_2_id) references empleado (pk_empleado),
    constraint FKj304d9mmff8dfl9e8f6cc4xdo
        foreign key (empleado_1_id) references empleado (pk_empleado)
);

create table if not exists datos_economicos
(
    comision      decimal(10, 2) null,
    salario       decimal(10, 2) null,
    banco         binary(16)     null,
    empleado_id   binary(16)     not null
        primary key,
    tipo_tarjeta  binary(16)     null,
    ano_caducidad varchar(255)   null,
    cvc           varchar(255)   null,
    mes_caducidad varchar(255)   null,
    num_cuenta    varchar(255)   null,
    num_tarjeta   varchar(255)   null,
    constraint FK6mdwroy7p8xfi46lxuilg6sje
        foreign key (empleado_id) references empleado (pk_empleado)
);

create table if not exists empleado_especialidad
(
    empleado_id     binary(16) not null,
    especialidad_id binary(16) not null,
    primary key (empleado_id, especialidad_id),
    constraint FK87rcoykx9x0w2hquf5ykdyr1o
        foreign key (especialidad_id) references especialidad (id),
    constraint FKhdis49h29lb4ps5jwp1qbct5w
        foreign key (empleado_id) references empleado (pk_empleado)
);

create table if not exists etiqueta
(
    id      binary(16)   not null
        primary key,
    jefe_id binary(16)   null,
    texto   varchar(255) null,
    constraint UKarjd5gw7phkhbfcqmg9puaa45
        unique (jefe_id, texto),
    constraint FK8egwwbypq1y1unagvm15jdypu
        foreign key (jefe_id) references empleado (pk_empleado)
);

create table if not exists etiqueta_empleado
(
    empleado_id binary(16) not null,
    etiqueta_id binary(16) not null,
    constraint FK4wk1ld3mn6g4u2844b4ne9fpy
        foreign key (empleado_id) references empleado (pk_empleado),
    constraint FK752uj72q0b657ulbqg5da7mdn
        foreign key (etiqueta_id) references etiqueta (id)
);

create table if not exists mensaje
(
    fecha_envio     datetime(6)  null,
    colaboracion_id binary(16)   null,
    pk_mensaje      binary(16)   not null
        primary key,
    contenido       varchar(255) null,
    constraint FKb6gv0hajmekmsvo4ncafx7wd0
        foreign key (colaboracion_id) references colaboracion (id)
);

create table if not exists nomina
(
    bruto_acumulado           decimal(38, 2) null,
    cantidad_percibida        decimal(38, 2) null,
    deducciones               decimal(38, 2) null,
    devengos                  decimal(38, 2) null,
    empleado_codigo_postal    int            null,
    empleado_numero_direccion int            null,
    empresa_codigo_postal     int            null,
    empresa_numero_direccion  int            null,
    fecha_alta_emp            date           null,
    fecha_fin                 date           null,
    fecha_inicio              date           null,
    retenciones               decimal(38, 2) null,
    salario_neto              decimal(38, 2) null,
    empleado_id               binary(16)     not null,
    empleado_tipo_via         binary(16)     null,
    empresa_tipo_via          binary(16)     null,
    pk_nomina                 binary(16)     not null
        primary key,
    cif                       varchar(255)   null,
    departamento              varchar(255)   null,
    documento_empleado        varchar(255)   null,
    empleado_localidad        varchar(255)   null,
    empleado_nombre_direccion varchar(255)   null,
    empleado_planta           varchar(255)   null,
    empleado_portal           varchar(255)   null,
    empleado_puerta           varchar(255)   null,
    empleado_region           varchar(255)   null,
    empresa_localidad         varchar(255)   null,
    empresa_nombre_direccion  varchar(255)   null,
    empresa_planta            varchar(255)   null,
    empresa_portal            varchar(255)   null,
    empresa_puerta            varchar(255)   null,
    empresa_region            varchar(255)   null,
    nombre_emp                varchar(255)   null,
    nombre_empresa            varchar(255)   null,
    num_seguridad_social      varchar(255)   null,
    perfil_profesional        varchar(255)   null,
    constraint FKm8493swspthispthv1lqbup06
        foreign key (empleado_id) references empleado (pk_empleado)
);

create table if not exists linea_nomina
(
    cantidad        decimal(38, 2) null,
    porcentaje      decimal(38, 2) null,
    nomina_id       binary(16)     null,
    pk_linea_nomina binary(16)     not null
        primary key,
    concepto        varchar(255)   null,
    constraint FK5h4scqcp21ddd1jjnsuwm5op0
        foreign key (nomina_id) references nomina (pk_nomina)
);

create table if not exists periodo_colaboracion
(
    fecha_fin       date       null,
    fecha_inicio    date       null,
    colaboracion_id binary(16) null,
    id              binary(16) not null
        primary key,
    constraint UKsi3ajyxsk1iyvsg4yaqcook6j
        unique (colaboracion_id),
    constraint FKc5fpfyu4bto7c210652tnqkqn
        foreign key (colaboracion_id) references colaboracion (id)
);

create table if not exists solicitud_colaboracion
(
    fecha_solicitud           datetime(6) null,
    empleado_receptor_id      binary(16)  null,
    empleado_solicitante_id   binary(16)  null,
    pk_solicitud_colaboracion binary(16)  not null
        primary key,
    constraint FKdr80dam69tdnv9ju19fxqdm0k
        foreign key (empleado_receptor_id) references empleado (pk_empleado),
    constraint FKh0lykvw9k57lxmdq0qqpyx8js
        foreign key (empleado_solicitante_id) references empleado (pk_empleado)
);

create table if not exists usuario_contador_navegador
(
    contador     int          null,
    usuario_id   binary(16)   not null,
    navegador_id varchar(255) not null,
    primary key (usuario_id, navegador_id),
    constraint FKawot9ou6k7h1gljxd973yoxee
        foreign key (usuario_id) references usuario (pk_usuario)
);

-- Insert data into banco table
INSERT INTO banco (id, nombre) VALUES
(UUID_TO_BIN('f47ac10b-58cc-4372-a567-0e02b2c3d479'), 'BBVA'),
(UUID_TO_BIN('550e8400-e29b-41d4-a716-446655440000'), 'Caixa'),
(UUID_TO_BIN('6ba7b810-9dad-11d1-80b4-00c04fd430c8'), 'Sabadell');

-- Insert data into categoria table (assuming no data provided in the Java code)
-- INSERT INTO categoria (id, nombre) VALUES (UUID_TO_BIN('...'), '...');

-- Insert data into departamento table
INSERT INTO departamento (id, nombre) VALUES
(UUID_TO_BIN('a1b2c3d4-e5f6-4789-90ab-cdef01234567'), 'Informatica'),
(UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174000'), 'Universidad'),
(UUID_TO_BIN('abcdef90-1234-5678-9abc-def012345678'), 'Marketing'),
(UUID_TO_BIN('fedcba09-8765-4321-bcde-f0123456789a'), 'Departamento');

-- Insert data into especialidad table
INSERT INTO especialidad (id, nombre) VALUES
(UUID_TO_BIN('98765432-10fe-dcba-9876-543210fedcba'), 'Creativo'),
(UUID_TO_BIN('23456789-abcd-ef01-2345-6789abcdef01'), 'Trabajo en equipo'),
(UUID_TO_BIN('bcde0123-4567-89ab-cdef-0123456789ab'), 'Rapido');

INSERT INTO genero (id, identidad) VALUES
(UUID_TO_BIN('dcd80d61-5e1c-4f60-b4d2-92d3c37f6d41'), 'Masculino'),
(UUID_TO_BIN('1a7f8be7-9f94-4692-a0b2-f0e730f045fc'), 'Femenino'),
(UUID_TO_BIN('8b5a67dc-e6f6-4ff2-9098-5c89d04123b6'), 'Otro');

INSERT INTO especialidad (id, nombre) VALUES
(UUID_TO_BIN('5c3ab843-d693-4e7d-8507-f518b7c2aa26'), 'Programación'),
(UUID_TO_BIN('c4fc7dd0-8b48-4c86-ae74-9d1e7fa279fb'), 'Contabilidad');

INSERT INTO pais (id, nombre, prefijo_telefonico) VALUES
(UUID_TO_BIN('a2f4b930-68b6-4b59-9a9f-06f00b0b52ef'), 'España', '+34'),
(UUID_TO_BIN('f0e3f6d5-4934-4b4b-b60c-7c90c132a0e2'), 'México', '+52');

INSERT INTO tipo_documento (id, tipo) VALUES
(UUID_TO_BIN('95c2d693-4713-4bc2-ae02-329b74118f33'), 'DNI'),
(UUID_TO_BIN('40e3bb04-c305-4a7e-9a5a-4c1c05b447d7'), 'Pasaporte');

INSERT INTO tipo_tarjeta (id, nombre) VALUES
(UUID_TO_BIN('14df4e87-38a4-4de1-8d3e-8d86b4cbff1d'), 'Débito'),
(UUID_TO_BIN('635228ad-5148-4d75-9499-79a62eb5b0c5'), 'Crédito');

INSERT INTO tipo_via (id, tipo) VALUES
(UUID_TO_BIN('6e77b6e5-ef45-4051-853c-707b7b5888c1'), 'Calle'),
(UUID_TO_BIN('23b2cc29-5d52-4421-90d4-65d30cc9d51b'), 'Avenida');

-- Insertar el usuario
INSERT INTO usuario (
    pk_usuario, contador_inicios, estado_bloqueado, bloqueado_hasta,
    contrasena, contrasena_recuperacion, email, motivo_bloqueo,
    nombre, archivo
) VALUES (
    UUID_TO_BIN('f6e02a3d-7d58-4f9b-b56d-6cbf3a35c178'), 0, 0, NULL,
    '$2a$10$Q9kD4Mmf6gBMhTvAxWCEzu24ylfig1Z3dJGO5TzN61OIzOWy6YS8m',
    NULL, 'davidsmh23@gmail.com', NULL,
    'Juan', NULL
);

-- Insertar el empleado
INSERT INTO integradora_compose.empleado (
    pk_empleado,
    usuario_id,
    nombre,
    apellido,
    foto_url,
    genero_id,
    departamento_id,
    fecha_nacimiento,
    pais_id,
    tipo_documento_id,
    fecha_contratacion,
    tipo_via
    -- Puedes añadir más columnas si tienes valores, ejemplo: codigo_postal, telefono, etc.
) VALUES (
    UUID_TO_BIN('bc35e776-801f-40dc-b3ff-95e12b9c3d41'), -- UUID empleado
    UUID_TO_BIN('f6e02a3d-7d58-4f9b-b56d-6cbf3a35c178'), -- UUID usuario
    'Juan',
    'Lopez',
    '/uploads/empleados/d164fcfb-5047-4213-be21-d575d75462bd.gif',
    UUID_TO_BIN('dcd80d61-5e1c-4f60-b4d2-92d3c37f6d41'), -- género masculino (ejemplo)
    UUID_TO_BIN('a1b2c3d4-e5f6-4789-90ab-cdef01234567'),
    '1990-01-01',
    UUID_TO_BIN('a2f4b930-68b6-4b59-9a9f-06f00b0b52ef'), -- país España (ejemplo)
    UUID_TO_BIN('95c2d693-4713-4bc2-ae02-329b74118f33'), -- tipo documento DNI (ejemplo)
    '1985-07-15',
    UUID_TO_BIN('3c53f94b-d518-4f94-9689-6f7c7a4e3145') -- tipo vía (ejemplo)
);

