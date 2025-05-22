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
(UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174000'), 'Bankinter'),
(UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174001'), 'ING'),
(UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174002'), 'Santander'),
(UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174003'), 'Openbank'),
(UUID_TO_BIN('123e4567-e89b-12d3-a456-426614174004'), 'Evo Banco');
-- Insert data into departamento table
INSERT INTO departamento (id, nombre) VALUES
(UUID_TO_BIN('321e4567-e89b-12d3-a456-426614174005'), 'Recursos Humanos'),
(UUID_TO_BIN('456e7890-a12b-34cd-56ef-123456789abc'), 'Finanzas'),
(UUID_TO_BIN('789abcde-1234-5678-abcd-ef0123456789'), 'Logística'),
(UUID_TO_BIN('9876abcd-4321-1abc-def0-1234567890ab'), 'Atención al Cliente'),
(UUID_TO_BIN('01234567-89ab-cdef-0123-456789abcdef'), 'Ventas');

-- Insert data into especialidad table
INSERT INTO especialidad (id, nombre) VALUES
(UUID_TO_BIN('11112222-3333-4444-5555-666677778888'), 'Resolutivo'),
(UUID_TO_BIN('99998888-7777-6666-5555-444433332222'), 'Analítico'),
(UUID_TO_BIN('aaaa1111-bbbb-2222-cccc-3333dddd4444'), 'Empático'),
(UUID_TO_BIN('12344321-4321-1234-5678-876543210000'), 'Resistente al estrés'),
(UUID_TO_BIN('abcdabcd-abcd-abcd-abcd-abcdabcdabcd'), 'Organizado'),
(UUID_TO_BIN('deadbeef-dead-beef-dead-beefdeadbeef'), 'Multitarea');

INSERT INTO pais (id, nombre, prefijo_telefonico) VALUES
(UUID_TO_BIN('60f7a1cb-3f5e-41d0-9c91-0182e9fcd98a'), 'Alemania', '+49'),
(UUID_TO_BIN('3c96f6e8-9c45-4379-8617-ea2b44f15693'), 'Italia', '+39'),
(UUID_TO_BIN('8f7dc59d-2a76-47fb-a4b6-3f99a6e5bb71'), 'Portugal', '+351'),
(UUID_TO_BIN('e1c7bde1-3e57-4abf-896f-2c75dbf8a998'), 'Países Bajos', '+31'),
(UUID_TO_BIN('7b9a6f62-1ec2-4d3b-a7e2-dfc78d1a5df2'), 'Suiza', '+41'),
(UUID_TO_BIN('23d2d2a1-b95f-4c3e-a51b-e5d3f624ac5b'), 'Austria', '+43'),
(UUID_TO_BIN('ee58e4ad-8e4f-4077-8f85-bf0b79a290cd'), 'Suecia', '+46');

INSERT INTO tipo_tarjeta (id, nombre) VALUES
(UUID_TO_BIN('87df48ac-947f-4e3a-9444-ea9d97cd75d1'), 'Maestro'),
(UUID_TO_BIN('4be90ea2-8a3c-4b7c-a0a6-3384d1932f9c'), 'Discover'),
(UUID_TO_BIN('bfa0174d-1667-4053-9a3f-5d2a97dbd109'), 'Diners Club'),
(UUID_TO_BIN('c8ed3a6a-9d8e-48e6-b15f-8d2f9f885a40'), 'JCB'),
(UUID_TO_BIN('113b5e41-e2e5-42a3-b54c-162901ed2bce'), 'UnionPay');

INSERT INTO tipo_via (id, tipo) VALUES
(UUID_TO_BIN('b3a7c8a5-bfd9-4c6c-b456-9d3a5f7fc1cd'), 'Camino'),
(UUID_TO_BIN('c1f98f2e-8ea6-4b91-bba7-8d4765ae1f2e'), 'Plaza'),
(UUID_TO_BIN('d213fab4-e3e4-4d6f-8a2b-3e4b7289b9fa'), 'Carretera'),
(UUID_TO_BIN('e5f6d7c1-723b-4b7d-92a7-afe2eab67590'), 'Travesía'),
(UUID_TO_BIN('a9f5c6b8-95c3-44e7-b62e-d1d5b0d71cb6'), 'Ronda');

