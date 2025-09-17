-- Eliminación de tablas existentes
drop table clientes cascade constraints;
drop table empleados cascade constraints;
drop table proveedor cascade constraints;
drop table marca cascade constraints;
drop table categorias_productos cascade constraints;
drop table sucursal cascade constraints;
drop table productos cascade constraints;
drop table ventas cascade constraints;
drop table detalles_ventas cascade constraints;
drop table citas cascade constraints;
drop table optometristas cascade constraints;
drop table historial_medico_visual cascade constraints;
drop table recetasopticas cascade constraints;
drop table laboratorios cascade constraints;
drop table devoluciones cascade constraints;
drop table stock_sucursales cascade constraints;
drop table detalle_pedidos_proveedor cascade constraints;
drop table movimientos_inventario cascade constraints;
drop table tarjetas_regalo cascade constraints;
drop table promociones cascade constraints;
drop table pedidos_personalizados cascade constraints;
drop table tratamientos_lentes cascade constraints;
drop table pedidos_proveedor cascade constraints;
drop table pedidos_laboratorio cascade constraints;

-- Tabla Clientes
create table clientes (
   rut          varchar2(20) primary key,
   nombre       varchar2(255),
   apellido     varchar2(255),
   correo       varchar2(255),
   direccion    varchar2(255),
   numero       number,
   id_promocion number
);

-- Inserciones para Clientes
insert into clientes (rut,nombre,apellido,correo,direccion,numero,id_promocion
) values ( '12345678-9','Fernando','Pessoa','fernan.pessoa@email.com','Av. Principal 123',912345678,1 );
insert into clientes (rut,nombre,apellido,correo,direccion,numero,id_promocion
) values ( '19555666-7','Soren','Kierkegaard','soren.kierkegaard@email.com','temor y temblor 2565',974561106,3 );
insert into clientes (
   rut,
   nombre,
   apellido,
   correo,
   direccion,
   numero,
   id_promocion
) values ( '23456789-0',
           'Rene',
           'Descartes',
           'rene.descartes@email.com',
           'Calle Secundaria 456',
           987654321,
           2 );
insert into clientes (
   rut,
   nombre,
   apellido,
   correo,
   direccion,
   numero,
   id_promocion
) values ( '34567890-1',
           'Simone',
           'De Beauvoir',
           'simone.beauvoir@email.com',
           'Av. Central 789',
           956789123,
           null );
insert into clientes (
   rut,
   nombre,
   apellido,
   correo,
   direccion,
   numero,
   id_promocion
) values ( '45678901-2',
           'Virginia',
           'Woolf',
           'virginia.woolf@email.com',
           'Pasaje Norte 101',
           945612378,
           3 );
insert into clientes (
   rut,
   nombre,
   apellido,
   correo,
   direccion,
   numero,
   id_promocion
) values ( '56789012-3',
           'Gilles',
           'Deleuzes',
           'gilles.deleuzes@email.com',
           'Av. Sur 202',
           978945612,
           null );
insert into clientes (
   rut,
   nombre,
   apellido,
   correo,
   direccion,
   numero,
   id_promocion
) values ( '67890123-4',
           'Charles',
           'Baudelaire',
           'michel.focault@email.com',
           'Calle Este 303',
           932165498,
           1 );
insert into clientes (
   rut,
   nombre,
   apellido,
   correo,
   direccion,
   numero,
   id_promocion
) values ( '78901234-5',
           'Martin',
           'Heidegger',
           'martin.heidegger@email.com',
           'Av. Oeste 404',
           964897321,
           null );
insert into clientes (
   rut,
   nombre,
   apellido,
   correo,
   direccion,
   numero,
   id_promocion
) values ( '89012345-6',
           'Pablo',
           'De Rokha',
           'pablo.rokha@email.com',
           'Boulevard 505',
           915478963,
           2 );
insert into clientes (
   rut,
   nombre,
   apellido,
   correo,
   direccion,
   numero,
   id_promocion
) values ( '90123456-7',
           'Friedrich',
           'Nietzsche',
           'federico.nietzsche@email.com',
           'Pasaje Sur 606',
           989632145,
           null );
insert into clientes (
   rut,
   nombre,
   apellido,
   correo,
   direccion,
   numero,
   id_promocion
) values ( '01234567-8',
           'William',
           'Borroughs',
           'willi.borroughs@email.com',
           'Av. Norte 707',
           937518264,
           3 );

select * from clientes;
-- Tabla Empleados
create table empleados (
   rut      varchar2(255) primary key,
   nombre   varchar2(255),
   puesto   varchar2(255),
   sucursal varchar2(255)
);

-- Inserciones para Empleados
insert into empleados (
   rut,
   nombre,
   puesto,
   sucursal
) values ( '11222333-4',
           'Dionisio',
           'Vendedor',
           'Sucursal Centro' );
insert into empleados (
   rut,
   nombre,
   puesto,
   sucursal
) values ( '22333444-5',
           'Emily',
           'Optometrista',
           'Sucursal Norte' );
insert into empleados (
   rut,
   nombre,
   puesto,
   sucursal
) values ( '33444555-6',
           'Alan',
           'Gerente',
           'Sucursal Centro' );
insert into empleados (
   rut,
   nombre,
   puesto,
   sucursal
) values ( '44555666-7',
           'Yukio',
           'Vendedor',
           'Sucursal Sur' );
insert into empleados (
   rut,
   nombre,
   puesto,
   sucursal
) values ( '55666777-8',
           'Alejandra',
           'Optometrista',
           'Sucursal Centro' );
insert into empleados (
   rut,
   nombre,
   puesto,
   sucursal
) values ( '66777888-9',
           'Lou Reed',
           'Vendedor',
           'Sucursal Norte' );
insert into empleados (
   rut,
   nombre,
   puesto,
   sucursal
) values ( '77888999-0',
           'Alan Turing',
           'Técnico',
           'Sucursal Sur' );
insert into empleados (
   rut,
   nombre,
   puesto,
   sucursal
) values ( '88999000-1',
           'Clementino',
           'Vendedor',
           'Sucursal Centro' );
insert into empleados (
   rut,
   nombre,
   puesto,
   sucursal
) values ( '99000111-2',
           'Oscar',
           'Optometrista',
           'Sucursal Norte' );
insert into empleados (
   rut,
   nombre,
   puesto,
   sucursal
) values ( '00111222-3',
           'Elena',
           'Vendedor',
           'Sucursal Sur' );

select * from empleados;


-- Tabla proveedor
create table proveedor (
   id_proveedor integer
      generated always as identity
   primary key,
   nombre       varchar2(255)
);

-- Inserciones para proveedor
insert into proveedor ( nombre ) values ( 'Optical Solutions' );
insert into proveedor ( nombre ) values ( 'VisionTech' );
insert into proveedor ( nombre ) values ( 'LensMakers' );
insert into proveedor ( nombre ) values ( 'FrameDesign' );
insert into proveedor ( nombre ) values ( 'ClearView' );
insert into proveedor ( nombre ) values ( 'OptiCare' );
insert into proveedor ( nombre ) values ( 'EyeStyle' );
insert into proveedor ( nombre ) values ( 'LensWorld' );
insert into proveedor ( nombre ) values ( 'VisionPlus' );
insert into proveedor ( nombre ) values ( 'OptiFrame' );

select * from proveedor; 

-- Tabla Marca
create table marca (
   id_marca     integer
      generated always as identity
   primary key,
   nombre_marca varchar2(255)
);

-- Inserciones para Marca
insert into marca ( nombre_marca ) values ( 'Ray-Ban' );
insert into marca ( nombre_marca ) values ( 'Oakley' );
insert into marca ( nombre_marca ) values ( 'Persol' );
insert into marca ( nombre_marca ) values ( 'Carrera' );
insert into marca ( nombre_marca ) values ( 'Prada' );
insert into marca ( nombre_marca ) values ( 'Gucci' );
insert into marca ( nombre_marca ) values ( 'Armani' );
insert into marca ( nombre_marca ) values ( 'Dior' );
insert into marca ( nombre_marca ) values ( 'Versace' );
insert into marca ( nombre_marca ) values ( 'Tom Ford' );

select * from marca;

-- Tabla Categorias_productos
create table categorias_productos (
   id     integer
      generated always as identity
   primary key,
   nombre varchar2(255)
);

-- Inserciones para Categorias_productos
insert into categorias_productos ( nombre ) values ( 'Gafas de Sol' );
insert into categorias_productos ( nombre ) values ( 'Gafas de Lectura' );
insert into categorias_productos ( nombre ) values ( 'Lentes de Contacto' );
insert into categorias_productos ( nombre ) values ( 'Monturas' );
insert into categorias_productos ( nombre ) values ( 'Lentes Progresivos' );
insert into categorias_productos ( nombre ) values ( 'Lentes Bifocales' );
insert into categorias_productos ( nombre ) values ( 'Accesorios' );
insert into categorias_productos ( nombre ) values ( 'Estuches' );
insert into categorias_productos ( nombre ) values ( 'Líquidos Limpiadores' );
insert into categorias_productos ( nombre ) values ( 'Lentes Fotocromáticos' );

-- Tabla Sucursal
create table sucursal (
   id              integer
      generated always as identity
   primary key,
   nombre_sucursal varchar2(255)
);

-- Inserciones para Sucursal
insert into sucursal ( nombre_sucursal ) values ( 'Sucursal Centro' );
insert into sucursal ( nombre_sucursal ) values ( 'Sucursal Norte' );
insert into sucursal ( nombre_sucursal ) values ( 'Sucursal Sur' );
insert into sucursal ( nombre_sucursal ) values ( 'Sucursal Este' );
insert into sucursal ( nombre_sucursal ) values ( 'Sucursal Oeste' );
insert into sucursal ( nombre_sucursal ) values ( 'Sucursal Mall Plaza' );
insert into sucursal ( nombre_sucursal ) values ( 'Sucursal Costanera' );
insert into sucursal ( nombre_sucursal ) values ( 'Sucursal Alto Las Condes' );
insert into sucursal ( nombre_sucursal ) values ( 'Sucursal Parque Arauco' );
insert into sucursal ( nombre_sucursal ) values ( 'Sucursal La Dehesa' );

-- Tabla Productos
create table productos (
   id               integer
      generated always as identity
   primary key,
   nombre           varchar2(255),
   precio           float,
   stock_disponible integer,
   id_marca         integer,
   id_categoria     integer,
   id_proveedor     integer
);

-- Inserciones para Productos
insert into productos (
   nombre,
   precio,
   stock_disponible,
   id_marca,
   id_categoria,
   id_proveedor
) values ( 'Ray-Ban Aviator',
           150.99,
           25,
           1,
           1,
           1 );
insert into productos (
   nombre,
   precio,
   stock_disponible,
   id_marca,
   id_categoria,
   id_proveedor
) values ( 'Oakley Flak Jacket',
           180.50,
           15,
           2,
           1,
           2 );
insert into productos (
   nombre,
   precio,
   stock_disponible,
   id_marca,
   id_categoria,
   id_proveedor
) values ( 'Persol PO714',
           210.75,
           10,
           3,
           1,
           3 );
insert into productos (
   nombre,
   precio,
   stock_disponible,
   id_marca,
   id_categoria,
   id_proveedor
) values ( 'Carrera Champion',
           135.25,
           20,
           4,
           1,
           4 );
insert into productos (
   nombre,
   precio,
   stock_disponible,
   id_marca,
   id_categoria,
   id_proveedor
) values ( 'Prada Linea Rossa',
           295.99,
           8,
           5,
           1,
           5 );
insert into productos (
   nombre,
   precio,
   stock_disponible,
   id_marca,
   id_categoria,
   id_proveedor
) values ( 'Gucci GG0063O',
           275.50,
           12,
           6,
           1,
           6 );
insert into productos (
   nombre,
   precio,
   stock_disponible,
   id_marca,
   id_categoria,
   id_proveedor
) values ( 'Armani EA4079',
           165.75,
           18,
           7,
           1,
           7 );
insert into productos (
   nombre,
   precio,
   stock_disponible,
   id_marca,
   id_categoria,
   id_proveedor
) values ( 'Dior DIORSTELLAIRE',
           320.00,
           6,
           8,
           1,
           8 );
insert into productos (
   nombre,
   precio,
   stock_disponible,
   id_marca,
   id_categoria,
   id_proveedor
) values ( 'Versace VE4285',
           245.99,
           9,
           9,
           1,
           9 );
insert into productos (
   nombre,
   precio,
   stock_disponible,
   id_marca,
   id_categoria,
   id_proveedor
) values ( 'Tom Ford FT0394',
           305.25,
           7,
           10,
           1,
           10 );

-- Tabla Ventas
create table ventas (
   id           integer
      generated always as identity
   primary key,
   fecha        date,
   total_venta  float,
   rut_cliente  varchar2(255),
   rut_empleado varchar2(255),
   id_sucursal  integer
);

-- Inserciones para Ventas
insert into ventas (
   fecha,
   total_venta,
   rut_cliente,
   rut_empleado,
   id_sucursal
) values ( to_date('2023-05-15','YYYY-MM-DD'),
           150.99,
           '12345678-9',
           '11222333-4',
           1 );
insert into ventas (
   fecha,
   total_venta,
   rut_cliente,
   rut_empleado,
   id_sucursal
) values ( to_date('2023-05-16','YYYY-MM-DD'),
           180.50,
           '23456789-0',
           '22333444-5',
           2 );
insert into ventas (
   fecha,
   total_venta,
   rut_cliente,
   rut_empleado,
   id_sucursal
) values ( to_date('2023-05-17','YYYY-MM-DD'),
           210.75,
           '34567890-1',
           '33444555-6',
           1 );
insert into ventas (
   fecha,
   total_venta,
   rut_cliente,
   rut_empleado,
   id_sucursal
) values ( to_date('2023-05-18','YYYY-MM-DD'),
           135.25,
           '45678901-2',
           '44555666-7',
           3 );
insert into ventas (
   fecha,
   total_venta,
   rut_cliente,
   rut_empleado,
   id_sucursal
) values ( to_date('2023-05-19','YYYY-MM-DD'),
           295.99,
           '56789012-3',
           '55666777-8',
           1 );
insert into ventas (
   fecha,
   total_venta,
   rut_cliente,
   rut_empleado,
   id_sucursal
) values ( to_date('2023-05-20','YYYY-MM-DD'),
           275.50,
           '67890123-4',
           '66777888-9',
           2 );
insert into ventas (
   fecha,
   total_venta,
   rut_cliente,
   rut_empleado,
   id_sucursal
) values ( to_date('2023-05-21','YYYY-MM-DD'),
           165.75,
           '78901234-5',
           '77888999-0',
           3 );
insert into ventas (
   fecha,
   total_venta,
   rut_cliente,
   rut_empleado,
   id_sucursal
) values ( to_date('2023-05-22','YYYY-MM-DD'),
           320.00,
           '89012345-6',
           '88999000-1',
           1 );
insert into ventas (
   fecha,
   total_venta,
   rut_cliente,
   rut_empleado,
   id_sucursal
) values ( to_date('2023-05-23','YYYY-MM-DD'),
           245.99,
           '90123456-7',
           '99000111-2',
           2 );
insert into ventas (
   fecha,
   total_venta,
   rut_cliente,
   rut_empleado,
   id_sucursal
) values ( to_date('2023-05-24','YYYY-MM-DD'),
           305.25,
           '01234567-8',
           '00111222-3',
           3 );

-- Tabla Detalles_ventas
create table detalles_ventas (
   id          integer
      generated always as identity
   primary key,
   id_venta    integer,
   id_producto integer
);

-- Inserciones para Detalles_ventas
insert into detalles_ventas (
   id_venta,
   id_producto
) values ( 1,
           1 );
insert into detalles_ventas (
   id_venta,
   id_producto
) values ( 2,
           2 );
insert into detalles_ventas (
   id_venta,
   id_producto
) values ( 3,
           3 );
insert into detalles_ventas (
   id_venta,
   id_producto
) values ( 4,
           4 );
insert into detalles_ventas (
   id_venta,
   id_producto
) values ( 5,
           5 );
insert into detalles_ventas (
   id_venta,
   id_producto
) values ( 6,
           6 );
insert into detalles_ventas (
   id_venta,
   id_producto
) values ( 7,
           7 );
insert into detalles_ventas (
   id_venta,
   id_producto
) values ( 8,
           8 );
insert into detalles_ventas (
   id_venta,
   id_producto
) values ( 9,
           9 );
insert into detalles_ventas (
   id_venta,
   id_producto
) values ( 10,
           10 );

-- Tabla Citas
create table citas (
   id              integer
      generated always as identity
   primary key,
   fecha           date,
   rut_cliente     varchar2(255),
   id_optometrista integer
);

-- Inserciones para Citas
insert into citas (
   fecha,
   rut_cliente,
   id_optometrista
) values ( to_date('2023-06-01','YYYY-MM-DD'),
           '12345678-9',
           1 );
insert into citas (
   fecha,
   rut_cliente,
   id_optometrista
) values ( to_date('2023-06-02','YYYY-MM-DD'),
           '23456789-0',
           2 );
insert into citas (
   fecha,
   rut_cliente,
   id_optometrista
) values ( to_date('2023-06-03','YYYY-MM-DD'),
           '34567890-1',
           3 );
insert into citas (
   fecha,
   rut_cliente,
   id_optometrista
) values ( to_date('2023-06-04','YYYY-MM-DD'),
           '45678901-2',
           1 );
insert into citas (
   fecha,
   rut_cliente,
   id_optometrista
) values ( to_date('2023-06-05','YYYY-MM-DD'),
           '56789012-3',
           2 );
insert into citas (
   fecha,
   rut_cliente,
   id_optometrista
) values ( to_date('2023-06-06','YYYY-MM-DD'),
           '67890123-4',
           3 );
insert into citas (
   fecha,
   rut_cliente,
   id_optometrista
) values ( to_date('2023-06-07','YYYY-MM-DD'),
           '78901234-5',
           1 );
insert into citas (
   fecha,
   rut_cliente,
   id_optometrista
) values ( to_date('2023-06-08','YYYY-MM-DD'),
           '89012345-6',
           2 );
insert into citas (
   fecha,
   rut_cliente,
   id_optometrista
) values ( to_date('2023-06-09','YYYY-MM-DD'),
           '90123456-7',
           3 );
insert into citas (
   fecha,
   rut_cliente,
   id_optometrista
) values ( to_date('2023-06-10','YYYY-MM-DD'),
           '01234567-8',
           1 );

-- Tabla Optometristas
create table optometristas (
   id     integer
      generated always as identity
   primary key,
   nombre varchar2(255)
);

-- Inserciones para Optometristas
insert into optometristas ( nombre ) values ( 'Dr. Sigmund Freud' );
insert into optometristas ( nombre ) values ( 'Dra. Emily Dickinson' );
insert into optometristas ( nombre ) values ( 'Dr. Jacques Lacan' );
insert into optometristas ( nombre ) values ( 'Dra. Anais Nin' );
insert into optometristas ( nombre ) values ( 'Dr. Felix Guattari' );
insert into optometristas ( nombre ) values ( 'Dra. Ada Lovelace' );
insert into optometristas ( nombre ) values ( 'Dr. Jean Jacques Rousseau' );
insert into optometristas ( nombre ) values ( 'Dra. Lou Andreas-Salome' );
insert into optometristas ( nombre ) values ( 'Dr. Ruben Dario' );
insert into optometristas ( nombre ) values ( 'Dra. Alejandra Pizarnik' );

-- Tabla Historial_medico_visual
create table historial_medico_visual (
   id              integer
      generated always as identity
   primary key,
   fecha_examen    date,
   observaciones   varchar2(255),
   rut_cliente     varchar2(255),
   id_optometrista integer
);

-- Inserciones para Historial_medico_visual
insert into historial_medico_visual (
   fecha_examen,
   observaciones,
   rut_cliente,
   id_optometrista
) values ( to_date('2023-01-15','YYYY-MM-DD'),
           'Miopía leve, necesita lentes para lectura',
           '12345678-9',
           1 );
insert into historial_medico_visual (
   fecha_examen,
   observaciones,
   rut_cliente,
   id_optometrista
) values ( to_date('2023-02-10','YYYY-MM-DD'),
           'Astigmatismo moderado',
           '23456789-0',
           2 );
insert into historial_medico_visual (
   fecha_examen,
   observaciones,
   rut_cliente,
   id_optometrista
) values ( to_date('2023-03-05','YYYY-MM-DD'),
           'Presbicia, necesita lentes progresivos',
           '34567890-1',
           3 );
insert into historial_medico_visual (
   fecha_examen,
   observaciones,
   rut_cliente,
   id_optometrista
) values ( to_date('2023-04-20','YYYY-MM-DD'),
           'Miopía y astigmatismo',
           '45678901-2',
           1 );
insert into historial_medico_visual (
   fecha_examen,
   observaciones,
   rut_cliente,
   id_optometrista
) values ( to_date('2023-05-12','YYYY-MM-DD'),
           'Hipermetropía leve',
           '56789012-3',
           2 );
insert into historial_medico_visual (
   fecha_examen,
   observaciones,
   rut_cliente,
   id_optometrista
) values ( to_date('2023-06-08','YYYY-MM-DD'),
           'Catarata incipiente',
           '67890123-4',
           3 );
insert into historial_medico_visual (
   fecha_examen,
   observaciones,
   rut_cliente,
   id_optometrista
) values ( to_date('2023-07-25','YYYY-MM-DD'),
           'Miopía alta',
           '78901234-5',
           1 );
insert into historial_medico_visual (
   fecha_examen,
   observaciones,
   rut_cliente,
   id_optometrista
) values ( to_date('2023-08-30','YYYY-MM-DD'),
           'Astigmatismo e hipermetropía',
           '89012345-6',
           2 );
insert into historial_medico_visual (
   fecha_examen,
   observaciones,
   rut_cliente,
   id_optometrista
) values ( to_date('2023-09-14','YYYY-MM-DD'),
           'Presbicia moderada',
           '90123456-7',
           3 );
insert into historial_medico_visual (
   fecha_examen,
   observaciones,
   rut_cliente,
   id_optometrista
) values ( to_date('2023-10-05','YYYY-MM-DD'),
           'Miopía moderada y astigmatismo',
           '01234567-8',
           1 );

-- Tabla RecetasOpticas
create table recetasopticas (
   id           integer
      generated always as identity
   primary key,
   id_historial integer
);

-- Inserciones para RecetasOpticas
insert into recetasopticas ( id_historial ) values ( 1 );
insert into recetasopticas ( id_historial ) values ( 2 );
insert into recetasopticas ( id_historial ) values ( 3 );
insert into recetasopticas ( id_historial ) values ( 4 );
insert into recetasopticas ( id_historial ) values ( 5 );
insert into recetasopticas ( id_historial ) values ( 6 );
insert into recetasopticas ( id_historial ) values ( 7 );
insert into recetasopticas ( id_historial ) values ( 8 );
insert into recetasopticas ( id_historial ) values ( 9 );
insert into recetasopticas ( id_historial ) values ( 10 );

-- Tabla Laboratorios
create table laboratorios (
   id         integer
      generated always as identity
   primary key,
   nombre_lab varchar2(255)
);

-- Inserciones para Laboratorios
insert into laboratorios ( nombre_lab ) values ( 'LabVision Central' );
insert into laboratorios ( nombre_lab ) values ( 'OptiLab Norte' );
insert into laboratorios ( nombre_lab ) values ( 'CrystalLens Sur' );
insert into laboratorios ( nombre_lab ) values ( 'PrecisionLabs' );
insert into laboratorios ( nombre_lab ) values ( 'ClearView Laboratories' );
insert into laboratorios ( nombre_lab ) values ( 'Advanced Optical Tech' );
insert into laboratorios ( nombre_lab ) values ( 'QualityLens Lab' );
insert into laboratorios ( nombre_lab ) values ( 'VisionCraft Labs' );
insert into laboratorios ( nombre_lab ) values ( 'Premium Optics Lab' );
insert into laboratorios ( nombre_lab ) values ( 'Elite Vision Laboratories' );

-- Tabla Devoluciones
create table devoluciones (
   id               integer
      generated always as identity
   primary key,
   fecha_devolucion date,
   motivo           varchar2(255),
   id_venta         integer,
   rut_cliente      varchar2(255)
);

-- Inserciones para Devoluciones
insert into devoluciones (
   fecha_devolucion,
   motivo,
   id_venta,
   rut_cliente
) values ( to_date('2023-05-20','YYYY-MM-DD'),
           'Talla incorrecta',
           1,
           '12345678-9' );
insert into devoluciones (
   fecha_devolucion,
   motivo,
   id_venta,
   rut_cliente
) values ( to_date('2023-05-21','YYYY-MM-DD'),
           'No le gustó el color',
           2,
           '23456789-0' );
insert into devoluciones (
   fecha_devolucion,
   motivo,
   id_venta,
   rut_cliente
) values ( to_date('2023-05-25','YYYY-MM-DD'),
           'Defecto de fabricación',
           3,
           '34567890-1' );
insert into devoluciones (
   fecha_devolucion,
   motivo,
   id_venta,
   rut_cliente
) values ( to_date('2023-05-28','YYYY-MM-DD'),
           'Montura incómoda',
           4,
           '45678901-2' );
insert into devoluciones (
   fecha_devolucion,
   motivo,
   id_venta,
   rut_cliente
) values ( to_date('2023-06-01','YYYY-MM-DD'),
           'Cambio de modelo',
           5,
           '56789012-3' );
insert into devoluciones (
   fecha_devolucion,
   motivo,
   id_venta,
   rut_cliente
) values ( to_date('2023-06-05','YYYY-MM-DD'),
           'No cumplió expectativas',
           6,
           '67890123-4' );
insert into devoluciones (
   fecha_devolucion,
   motivo,
   id_venta,
   rut_cliente
) values ( to_date('2023-06-10','YYYY-MM-DD'),
           'Problema de graduación',
           7,
           '78901234-5' );
insert into devoluciones (
   fecha_devolucion,
   motivo,
   id_venta,
   rut_cliente
) values ( to_date('2023-06-15','YYYY-MM-DD'),
           'Ajuste incómodo',
           8,
           '89012345-6' );
insert into devoluciones (
   fecha_devolucion,
   motivo,
   id_venta,
   rut_cliente
) values ( to_date('2023-06-20','YYYY-MM-DD'),
           'Color diferente al esperado',
           9,
           '90123456-7' );
insert into devoluciones (
   fecha_devolucion,
   motivo,
   id_venta,
   rut_cliente
) values ( to_date('2023-06-25','YYYY-MM-DD'),
           'Daño durante el transporte',
           10,
           '01234567-8' );

-- Tabla Stock_sucursales
create table stock_sucursales (
   id          integer
      generated always as identity
   primary key,
   id_producto integer,
   id_sucursal integer,
   cantidad    integer
);

-- Inserciones para Stock_sucursales
insert into stock_sucursales (
   id_producto,
   id_sucursal,
   cantidad
) values ( 1,
           1,
           10 );
insert into stock_sucursales (
   id_producto,
   id_sucursal,
   cantidad
) values ( 2,
           1,
           5 );
insert into stock_sucursales (
   id_producto,
   id_sucursal,
   cantidad
) values ( 3,
           2,
           8 );
insert into stock_sucursales (
   id_producto,
   id_sucursal,
   cantidad
) values ( 4,
           2,
           12 );
insert into stock_sucursales (
   id_producto,
   id_sucursal,
   cantidad
) values ( 5,
           3,
           6 );
insert into stock_sucursales (
   id_producto,
   id_sucursal,
   cantidad
) values ( 6,
           3,
           9 );
insert into stock_sucursales (
   id_producto,
   id_sucursal,
   cantidad
) values ( 7,
           1,
           7 );
insert into stock_sucursales (
   id_producto,
   id_sucursal,
   cantidad
) values ( 8,
           2,
           4 );
insert into stock_sucursales (
   id_producto,
   id_sucursal,
   cantidad
) values ( 9,
           3,
           11 );
insert into stock_sucursales (
   id_producto,
   id_sucursal,
   cantidad
) values ( 10,
           1,
           3 );

-- Tabla Detalle_pedidos_proveedor
create table detalle_pedidos_proveedor (
   id                  integer
      generated always as identity
   primary key,
   id_pedido_proveedor integer,
   id_producto         integer,
   cantidad            integer
);

-- Inserciones para Detalle_pedidos_proveedor
insert into detalle_pedidos_proveedor (
   id_pedido_proveedor,
   id_producto,
   cantidad
) values ( 1,
           1,
           20 );
insert into detalle_pedidos_proveedor (
   id_pedido_proveedor,
   id_producto,
   cantidad
) values ( 1,
           2,
           15 );
insert into detalle_pedidos_proveedor (
   id_pedido_proveedor,
   id_producto,
   cantidad
) values ( 2,
           3,
           10 );
insert into detalle_pedidos_proveedor (
   id_pedido_proveedor,
   id_producto,
   cantidad
) values ( 2,
           4,
           25 );
insert into detalle_pedidos_proveedor (
   id_pedido_proveedor,
   id_producto,
   cantidad
) values ( 3,
           5,
           12 );
insert into detalle_pedidos_proveedor (
   id_pedido_proveedor,
   id_producto,
   cantidad
) values ( 3,
           6,
           18 );
insert into detalle_pedidos_proveedor (
   id_pedido_proveedor,
   id_producto,
   cantidad
) values ( 4,
           7,
           8 );
insert into detalle_pedidos_proveedor (
   id_pedido_proveedor,
   id_producto,
   cantidad
) values ( 4,
           8,
           6 );
insert into detalle_pedidos_proveedor (
   id_pedido_proveedor,
   id_producto,
   cantidad
) values ( 5,
           9,
           14 );
insert into detalle_pedidos_proveedor (
   id_pedido_proveedor,
   id_producto,
   cantidad
) values ( 5,
           10,
           9 );

-- Tabla Movimientos_inventario
create table movimientos_inventario (
   id               integer
      generated always as identity
   primary key,
   fecha_movimiento date,
   id_producto      integer,
   id_sucursal      integer,
   rut_empleado     varchar2(255)
);

-- Inserciones para Movimientos_inventario
insert into movimientos_inventario (
   fecha_movimiento,
   id_producto,
   id_sucursal,
   rut_empleado
) values ( to_date('2023-05-01','YYYY-MM-DD'),
           1,
           1,
           '11222333-4' );
insert into movimientos_inventario (
   fecha_movimiento,
   id_producto,
   id_sucursal,
   rut_empleado
) values ( to_date('2023-05-02','YYYY-MM-DD'),
           2,
           1,
           '22333444-5' );
insert into movimientos_inventario (
   fecha_movimiento,
   id_producto,
   id_sucursal,
   rut_empleado
) values ( to_date('2023-05-03','YYYY-MM-DD'),
           3,
           2,
           '33444555-6' );
insert into movimientos_inventario (
   fecha_movimiento,
   id_producto,
   id_sucursal,
   rut_empleado
) values ( to_date('2023-05-04','YYYY-MM-DD'),
           4,
           2,
           '44555666-7' );
insert into movimientos_inventario (
   fecha_movimiento,
   id_producto,
   id_sucursal,
   rut_empleado
) values ( to_date('2023-05-05','YYYY-MM-DD'),
           5,
           3,
           '55666777-8' );
insert into movimientos_inventario (
   fecha_movimiento,
   id_producto,
   id_sucursal,
   rut_empleado
) values ( to_date('2023-05-06','YYYY-MM-DD'),
           6,
           3,
           '66777888-9' );
insert into movimientos_inventario (
   fecha_movimiento,
   id_producto,
   id_sucursal,
   rut_empleado
) values ( to_date('2023-05-07','YYYY-MM-DD'),
           7,
           1,
           '77888999-0' );
insert into movimientos_inventario (
   fecha_movimiento,
   id_producto,
   id_sucursal,
   rut_empleado
) values ( to_date('2023-05-08','YYYY-MM-DD'),
           8,
           2,
           '88999000-1' );
insert into movimientos_inventario (
   fecha_movimiento,
   id_producto,
   id_sucursal,
   rut_empleado
) values ( to_date('2023-05-09','YYYY-MM-DD'),
           9,
           3,
           '99000111-2' );
insert into movimientos_inventario (
   fecha_movimiento,
   id_producto,
   id_sucursal,
   rut_empleado
) values ( to_date('2023-05-10','YYYY-MM-DD'),
           10,
           1,
           '00111222-3' );

-- Tabla Tarjetas_regalo
create table tarjetas_regalo (
   id            integer
      generated always as identity
   primary key,
   codigo        varchar2(255),
   valor_inicial float,
   rut_cliente   varchar2(255)
);

-- Inserciones para Tarjetas_regalo
insert into tarjetas_regalo (
   codigo,
   valor_inicial,
   rut_cliente
) values ( 'GIFT123456',
           50.00,
           '12345678-9' );
insert into tarjetas_regalo (
   codigo,
   valor_inicial,
   rut_cliente
) values ( 'GIFT234567',
           75.00,
           '23456789-0' );
insert into tarjetas_regalo (
   codigo,
   valor_inicial,
   rut_cliente
) values ( 'GIFT345678',
           100.00,
           '34567890-1' );
insert into tarjetas_regalo (
   codigo,
   valor_inicial,
   rut_cliente
) values ( 'GIFT456789',
           25.00,
           '45678901-2' );
insert into tarjetas_regalo (
   codigo,
   valor_inicial,
   rut_cliente
) values ( 'GIFT567890',
           150.00,
           '56789012-3' );
insert into tarjetas_regalo (
   codigo,
   valor_inicial,
   rut_cliente
) values ( 'GIFT678901',
           200.00,
           '67890123-4' );
insert into tarjetas_regalo (
   codigo,
   valor_inicial,
   rut_cliente
) values ( 'GIFT789012',
           50.00,
           '78901234-5' );
insert into tarjetas_regalo (
   codigo,
   valor_inicial,
   rut_cliente
) values ( 'GIFT890123',
           75.00,
           '89012345-6' );
insert into tarjetas_regalo (
   codigo,
   valor_inicial,
   rut_cliente
) values ( 'GIFT901234',
           100.00,
           '90123456-7' );
insert into tarjetas_regalo (
   codigo,
   valor_inicial,
   rut_cliente
) values ( 'GIFT012345',
           125.00,
           '01234567-8' );

-- Tabla Promociones
create table promociones (
   id               integer
      generated always as identity
   primary key,
   nombre_promocion varchar2(255),
   fecha_inicio     date,
   fecha_fin        date
);

-- Inserciones para Promociones
insert into promociones (
   nombre_promocion,
   fecha_inicio,
   fecha_fin
) values ( 'Verano 2023',
           to_date('2023-01-01','YYYY-MM-DD'),
           to_date('2023-03-31','YYYY-MM-DD') );
insert into promociones (
   nombre_promocion,
   fecha_inicio,
   fecha_fin
) values ( 'Otoño 2023',
           to_date('2023-04-01','YYYY-MM-DD'),
           to_date('2023-06-30','YYYY-MM-DD') );
insert into promociones (
   nombre_promocion,
   fecha_inicio,
   fecha_fin
) values ( 'Invierno 2023',
           to_date('2023-07-01','YYYY-MM-DD'),
           to_date('2023-09-30','YYYY-MM-DD') );
insert into promociones (
   nombre_promocion,
   fecha_inicio,
   fecha_fin
) values ( 'Primavera 2023',
           to_date('2023-10-01','YYYY-MM-DD'),
           to_date('2023-12-31','YYYY-MM-DD') );
insert into promociones (
   nombre_promocion,
   fecha_inicio,
   fecha_fin
) values ( 'Black Friday',
           to_date('2023-11-24','YYYY-MM-DD'),
           to_date('2023-11-26','YYYY-MM-DD') );
insert into promociones (
   nombre_promocion,
   fecha_inicio,
   fecha_fin
) values ( 'Cyber Monday',
           to_date('2023-11-27','YYYY-MM-DD'),
           to_date('2023-11-27','YYYY-MM-DD') );
insert into promociones (
   nombre_promocion,
   fecha_inicio,
   fecha_fin
) values ( 'Navidad 2023',
           to_date('2023-12-15','YYYY-MM-DD'),
           to_date('2023-12-31','YYYY-MM-DD') );
insert into promociones (
   nombre_promocion,
   fecha_inicio,
   fecha_fin
) values ( 'Año Nuevo 2024',
           to_date('2024-01-01','YYYY-MM-DD'),
           to_date('2024-01-15','YYYY-MM-DD') );
insert into promociones (
   nombre_promocion,
   fecha_inicio,
   fecha_fin
) values ( 'Día del Padre',
           to_date('2023-06-18','YYYY-MM-DD'),
           to_date('2023-06-18','YYYY-MM-DD') );
insert into promociones (
   nombre_promocion,
   fecha_inicio,
   fecha_fin
) values ( 'Día de la Madre',
           to_date('2023-05-14','YYYY-MM-DD'),
           to_date('2023-05-14','YYYY-MM-DD') );

-- Tabla Pedidos_personalizados
create table pedidos_personalizados (
   id             integer
      generated always as identity
   primary key,
   fecha_creacion date,
   id_laboratorio integer,
   id_venta       integer,
   rut_cliente    varchar2(255),
   rut_empleado   varchar2(255)
);

-- Inserciones para Pedidos_personalizados
insert into pedidos_personalizados (
   fecha_creacion,
   id_laboratorio,
   id_venta,
   rut_cliente,
   rut_empleado
) values ( to_date('2023-05-16','YYYY-MM-DD'),
           1,
           1,
           '12345678-9',
           '11222333-4' );
insert into pedidos_personalizados (
   fecha_creacion,
   id_laboratorio,
   id_venta,
   rut_cliente,
   rut_empleado
) values ( to_date('2023-05-17','YYYY-MM-DD'),
           2,
           2,
           '23456789-0',
           '22333444-5' );
insert into pedidos_personalizados (
   fecha_creacion,
   id_laboratorio,
   id_venta,
   rut_cliente,
   rut_empleado
) values ( to_date('2023-05-18','YYYY-MM-DD'),
           3,
           3,
           '34567890-1',
           '33444555-6' );
insert into pedidos_personalizados (
   fecha_creacion,
   id_laboratorio,
   id_venta,
   rut_cliente,
   rut_empleado
) values ( to_date('2023-05-19','YYYY-MM-DD'),
           4,
           4,
           '45678901-2',
           '44555666-7' );
insert into pedidos_personalizados (
   fecha_creacion,
   id_laboratorio,
   id_venta,
   rut_cliente,
   rut_empleado
) values ( to_date('2023-05-20','YYYY-MM-DD'),
           5,
           5,
           '56789012-3',
           '55666777-8' );
insert into pedidos_personalizados (
   fecha_creacion,
   id_laboratorio,
   id_venta,
   rut_cliente,
   rut_empleado
) values ( to_date('2023-05-21','YYYY-MM-DD'),
           6,
           6,
           '67890123-4',
           '66777888-9' );
insert into pedidos_personalizados (
   fecha_creacion,
   id_laboratorio,
   id_venta,
   rut_cliente,
   rut_empleado
) values ( to_date('2023-05-22','YYYY-MM-DD'),
           7,
           7,
           '78901234-5',
           '77888999-0' );
insert into pedidos_personalizados (
   fecha_creacion,
   id_laboratorio,
   id_venta,
   rut_cliente,
   rut_empleado
) values ( to_date('2023-05-23','YYYY-MM-DD'),
           8,
           8,
           '89012345-6',
           '88999000-1' );
insert into pedidos_personalizados (
   fecha_creacion,
   id_laboratorio,
   id_venta,
   rut_cliente,
   rut_empleado
) values ( to_date('2023-05-24','YYYY-MM-DD'),
           9,
           9,
           '90123456-7',
           '99000111-2' );
insert into pedidos_personalizados (
   fecha_creacion,
   id_laboratorio,
   id_venta,
   rut_cliente,
   rut_empleado
) values ( to_date('2023-05-25','YYYY-MM-DD'),
           10,
           10,
           '01234567-8',
           '00111222-3' );

-- Tabla Tratamientos_lentes
create table tratamientos_lentes (
   id                 integer
      generated always as identity
   primary key,
   nombre_tratamiento varchar2(255)
);

-- Inserciones para Tratamientos_lentes
insert into tratamientos_lentes ( nombre_tratamiento ) values ( 'Antirreflejo' );
insert into tratamientos_lentes ( nombre_tratamiento ) values ( 'Filtro UV' );
insert into tratamientos_lentes ( nombre_tratamiento ) values ( 'Fotocromático' );
insert into tratamientos_lentes ( nombre_tratamiento ) values ( 'Anti-rayas' );
insert into tratamientos_lentes ( nombre_tratamiento ) values ( 'Anti-empañamiento' );
insert into tratamientos_lentes ( nombre_tratamiento ) values ( 'Blue Light Filter' );
insert into tratamientos_lentes ( nombre_tratamiento ) values ( 'Polarizado' );
insert into tratamientos_lentes ( nombre_tratamiento ) values ( 'Espejeado' );
insert into tratamientos_lentes ( nombre_tratamiento ) values ( 'Hiperhydrofóbico' );
insert into tratamientos_lentes ( nombre_tratamiento ) values ( 'Oleofóbico' );

-- Tabla Pedidos_proveedor
create table pedidos_proveedor (
   id           integer
      generated always as identity
   primary key,
   fecha_pedido date,
   id_proveedor integer,
   rut_empleado varchar2(255)
);

-- Inserciones para Pedidos_proveedor
insert into pedidos_proveedor (
   fecha_pedido,
   id_proveedor,
   rut_empleado
) values ( to_date('2023-04-01','YYYY-MM-DD'),
           1,
           '11222333-4' );
insert into pedidos_proveedor (
   fecha_pedido,
   id_proveedor,
   rut_empleado
) values ( to_date('2023-04-05','YYYY-MM-DD'),
           2,
           '22333444-5' );
insert into pedidos_proveedor (
   fecha_pedido,
   id_proveedor,
   rut_empleado
) values ( to_date('2023-04-10','YYYY-MM-DD'),
           3,
           '33444555-6' );
insert into pedidos_proveedor (
   fecha_pedido,
   id_proveedor,
   rut_empleado
) values ( to_date('2023-04-15','YYYY-MM-DD'),
           4,
           '44555666-7' );
insert into pedidos_proveedor (
   fecha_pedido,
   id_proveedor,
   rut_empleado
) values ( to_date('2023-04-20','YYYY-MM-DD'),
           5,
           '55666777-8' );
insert into pedidos_proveedor (
   fecha_pedido,
   id_proveedor,
   rut_empleado
) values ( to_date('2023-04-25','YYYY-MM-DD'),
           6,
           '66777888-9' );
insert into pedidos_proveedor (
   fecha_pedido,
   id_proveedor,
   rut_empleado
) values ( to_date('2023-05-01','YYYY-MM-DD'),
           7,
           '77888999-0' );
insert into pedidos_proveedor (
   fecha_pedido,
   id_proveedor,
   rut_empleado
) values ( to_date('2023-05-05','YYYY-MM-DD'),
           8,
           '88999000-1' );
insert into pedidos_proveedor (
   fecha_pedido,
   id_proveedor,
   rut_empleado
) values ( to_date('2023-05-10','YYYY-MM-DD'),
           9,
           '99000111-2' );
insert into pedidos_proveedor (
   fecha_pedido,
   id_proveedor,
   rut_empleado
) values ( to_date('2023-05-15','YYYY-MM-DD'),
           10,
           '00111222-3' );

-- Tabla Pedidos_laboratorio
create table pedidos_laboratorio (
   id             integer
      generated always as identity
   primary key,
   fecha_pedido   date,
   id_laboratorio integer,
   id_venta       integer
);

-- Inserciones para Pedidos_laboratorio
insert into pedidos_laboratorio (
   fecha_pedido,
   id_laboratorio,
   id_venta
) values ( to_date('2023-05-17','YYYY-MM-DD'),
           1,
           1 );
insert into pedidos_laboratorio (
   fecha_pedido,
   id_laboratorio,
   id_venta
) values ( to_date('2023-05-18','YYYY-MM-DD'),
           2,
           2 );
insert into pedidos_laboratorio (
   fecha_pedido,
   id_laboratorio,
   id_venta
) values ( to_date('2023-05-19','YYYY-MM-DD'),
           3,
           3 );
insert into pedidos_laboratorio (
   fecha_pedido,
   id_laboratorio,
   id_venta
) values ( to_date('2023-05-20','YYYY-MM-DD'),
           4,
           4 );
insert into pedidos_laboratorio (
   fecha_pedido,
   id_laboratorio,
   id_venta
) values ( to_date('2023-05-21','YYYY-MM-DD'),
           5,
           5 );
insert into pedidos_laboratorio (
   fecha_pedido,
   id_laboratorio,
   id_venta
) values ( to_date('2023-05-22','YYYY-MM-DD'),
           6,
           6 );
insert into pedidos_laboratorio (
   fecha_pedido,
   id_laboratorio,
   id_venta
) values ( to_date('2023-05-23','YYYY-MM-DD'),
           7,
           7 );
insert into pedidos_laboratorio (
   fecha_pedido,
   id_laboratorio,
   id_venta
) values ( to_date('2023-05-24','YYYY-MM-DD'),
           8,
           8 );
insert into pedidos_laboratorio (
   fecha_pedido,
   id_laboratorio,
   id_venta
) values ( to_date('2023-05-25','YYYY-MM-DD'),
           9,
           9 );
insert into pedidos_laboratorio (
   fecha_pedido,
   id_laboratorio,
   id_venta
) values ( to_date('2023-05-26','YYYY-MM-DD'),
           10,
           10 );

-- Constraints (restricciones de integridad referencial)
alter table ventas
   add constraint fk_ventas_cliente
      foreign key ( rut_cliente )
         references clientes ( rut )
            on delete set null;
alter table ventas
   add constraint fk_ventas_empleado
      foreign key ( rut_empleado )
         references empleados ( rut )
            on delete set null;
alter table ventas
   add constraint fk_ventas_sucursal
      foreign key ( id_sucursal )
         references sucursal ( id )
            on delete set null;

alter table detalles_ventas
   add constraint fk_detalles_ventas_venta
      foreign key ( id_venta )
         references ventas ( id )
            on delete cascade;
alter table detalles_ventas
   add constraint fk_detalles_ventas_producto
      foreign key ( id_producto )
         references productos ( id )
            on delete cascade;

alter table stock_sucursales
   add constraint fk_stock_producto
      foreign key ( id_producto )
         references productos ( id )
            on delete cascade;
alter table stock_sucursales
   add constraint fk_stock_sucursal
      foreign key ( id_sucursal )
         references sucursal ( id )
            on delete cascade;

alter table citas
   add constraint fk_citas_cliente
      foreign key ( rut_cliente )
         references clientes ( rut )
            on delete set null;
alter table citas
   add constraint fk_citas_optometrista
      foreign key ( id_optometrista )
         references optometristas ( id )
            on delete set null;

alter table recetasopticas
   add constraint fk_recetas_historial
      foreign key ( id_historial )
         references historial_medico_visual ( id )
            on delete cascade;

alter table devoluciones
   add constraint fk_devoluciones_cliente
      foreign key ( rut_cliente )
         references clientes ( rut )
            on delete set null;
alter table devoluciones
   add constraint fk_devoluciones_venta
      foreign key ( id_venta )
         references ventas ( id )
            on delete set null;

alter table productos
   add constraint fk_productos_proveedor
      foreign key ( id_proveedor )
         references proveedor ( id_proveedor )
            on delete set null;
alter table productos
   add constraint fk_productos_marca
      foreign key ( id_marca )
         references marca ( id_marca )
            on delete set null;
alter table productos
   add constraint fk_productos_categoria
      foreign key ( id_categoria )
         references categorias_productos ( id )
            on delete set null;

alter table pedidos_proveedor
   add constraint fk_pedidos_proveedor_proveedor
      foreign key ( id_proveedor )
         references proveedor ( id_proveedor )
            on delete set null;
alter table pedidos_proveedor
   add constraint fk_pedidos_proveedor_empleado
      foreign key ( rut_empleado )
         references empleados ( rut )
            on delete set null;

alter table detalle_pedidos_proveedor
   add constraint fk_detalle_pedidos_proveedor_pedido
      foreign key ( id_pedido_proveedor )
         references pedidos_proveedor ( id )
            on delete cascade;
alter table detalle_pedidos_proveedor
   add constraint fk_detalle_pedidos_proveedor_producto
      foreign key ( id_producto )
         references productos ( id )
            on delete cascade;

alter table pedidos_personalizados
   add constraint fk_pedidos_personalizados_cliente
      foreign key ( rut_cliente )
         references clientes ( rut )
            on delete set null;
alter table pedidos_personalizados
   add constraint fk_pedidos_personalizados_empleado
      foreign key ( rut_empleado )
         references empleados ( rut )
            on delete set null;
alter table pedidos_personalizados
   add constraint fk_pedidos_personalizados_laboratorio
      foreign key ( id_laboratorio )
         references laboratorios ( id )
            on delete set null;
alter table pedidos_personalizados
   add constraint fk_pedidos_personalizados_venta
      foreign key ( id_venta )
         references ventas ( id )
            on delete set null;

alter table movimientos_inventario
   add constraint fk_movimientos_inventario_producto
      foreign key ( id_producto )
         references productos ( id )
            on delete cascade;
alter table movimientos_inventario
   add constraint fk_movimientos_inventario_sucursal
      foreign key ( id_sucursal )
         references sucursal ( id )
            on delete cascade;
alter table movimientos_inventario
   add constraint fk_movimientos_inventario_empleado
      foreign key ( rut_empleado )
         references empleados ( rut )
            on delete set null;

alter table clientes
   add constraint fk_clientes_promocion
      foreign key ( id_promocion )
         references promociones ( id )
            on delete set null;

alter table tarjetas_regalo
   add constraint fk_tarjetas_cliente
      foreign key ( rut_cliente )
         references clientes ( rut )
            on delete set null;

alter table historial_medico_visual
   add constraint fk_historial_cliente
      foreign key ( rut_cliente )
         references clientes ( rut )
            on delete set null;
alter table historial_medico_visual
   add constraint fk_historial_optometrista
      foreign key ( id_optometrista )
         references optometristas ( id )
            on delete set null;

alter table pedidos_laboratorio
   add constraint fk_pedidos_laboratorio_lab
      foreign key ( id_laboratorio )
         references laboratorios ( id )
            on delete set null;
alter table pedidos_laboratorio
   add constraint fk_pedidos_laboratorio_venta
      foreign key ( id_venta )
         references ventas ( id )
            on delete set null;





--Tipos de datos compuestos: VARRAY 

DECLARE 
   TYPE MarcasArray IS VARRAY(5) OF VARCHAR2(100);
   v_marcas MarcasArray := MarcasArray();
BEGIN    
   v_marcas.EXTEND(3);      
   v_marcas(1) := 'Ray-Ban'; 
   v_marcas(2) := 'Oakley';
   v_marcas(3) := 'Gucci';


   FOR i IN 1 .. v_marcas.COUNT LOOP
      DBMS_OUTPUT.PUT_LINE('('|| i||') ' || 'MARCA '  || ': ' || v_marcas(i));
   END LOOP;
END;
/
-- crear tipos VARRAY para manejar multiples valores 
CREATE OR REPLACE TYPE tipo_producto AS VARRAY(3) OF VARCHAR2(20);
/
CREATE OR REPLACE TYPE telefono_adicional AS VARRAY(2) OF VARCHAR2(20);
/
--Actualizar tabla clientes para usar VARRAY
ALTER TABLE clientes ADD (
   telefono telefono_adicional,
   tipo tipo_producto
);
select * from clientes
--======================================================
-- 1) PROCEDIMIENTO ALMACENADOS
--======================================================

-- PROCEDIMIENTOS ALMACENADOS: BUSCAR PRODUCTOS POR CATEGORÍA
CREATE OR REPLACE PROCEDURE buscar_productos_por_categoria (
   p_categoria IN VARCHAR2,
   p_productos OUT SYS_REFCURSOR
) IS 
   v_count NUMBER := 0;
BEGIN 
   -- Verificar si existen productos en la categoría
   SELECT COUNT(*)
   INTO v_count
   FROM productos p
   JOIN categorias_productos cp ON p.id_categoria = cp.id 
   JOIN marca m ON p.id_marca = m.id_marca
   WHERE UPPER(cp.nombre) = UPPER(p_categoria)
   AND p.stock_disponible > 0;
   
   -- Abrir cursor con productos de la óptica
   OPEN p_productos FOR 
      SELECT p.id, 
             p.nombre, 
             p.stock_disponible, 
             p.precio, 
             m.nombre_marca,
             cp.nombre as categoria
      FROM productos p
      JOIN categorias_productos cp ON p.id_categoria = cp.id 
      JOIN marca m ON p.id_marca = m.id_marca
      WHERE UPPER(cp.nombre) = UPPER(p_categoria)
      AND p.stock_disponible > 0
      ORDER BY p.precio DESC, p.nombre;
   
   -- Mensaje informativo
   IF v_count = 0 THEN
      DBMS_OUTPUT.PUT_LINE('No se encontraron productos disponibles para: ' || p_categoria);
   ELSE
      DBMS_OUTPUT.PUT_LINE('Se encontraron ' || v_count || ' productos disponibles en: ' || p_categoria);
   END IF;

EXCEPTION 
   WHEN OTHERS THEN 
       DBMS_OUTPUT.PUT_LINE('Error al buscar productos: ' || SQLERRM);
       OPEN p_productos FOR SELECT NULL id, NULL nombre, NULL stock_disponible, NULL precio, NULL nombre_marca, NULL categoria FROM dual WHERE 1=0;
END;
/

-- uso con categorías 
DECLARE
   v_cursor SYS_REFCURSOR;
   v_id NUMBER;
   v_nombre VARCHAR2(255);
   v_stock NUMBER;
   v_precio NUMBER;
   v_marca VARCHAR2(255);
   v_categoria VARCHAR2(255);
BEGIN
   DBMS_OUTPUT.PUT_LINE('=== BÚSQUEDA DE GAFAS DE SOL ===');
   
   -- Buscar gafas de sol
   buscar_productos_por_categoria('Gafas de Sol', v_cursor);
   
   -- Mostrar resultados
   LOOP
      FETCH v_cursor INTO v_id, v_nombre, v_stock, v_precio, v_marca, v_categoria;
      EXIT WHEN v_cursor%NOTFOUND;
      
      DBMS_OUTPUT.PUT_LINE('▶ ' || v_marca || ' ' || v_nombre);
      DBMS_OUTPUT.PUT_LINE('  Precio: $' || v_precio || ' | Stock: ' || v_stock || ' unidades');
      DBMS_OUTPUT.PUT_LINE('');
   END LOOP;
   
   CLOSE v_cursor;
   
   DBMS_OUTPUT.PUT_LINE('=== FIN DE BÚSQUEDA ===');
END;
/




-- Procedimiento: asignar promocion a cliente 
CREATE OR REPLACE PROCEDURE asignar_promocion_cliente ( 
   p_rut_cliente IN VARCHAR2,
   p_id_promocion IN INTEGER
) IS
   v_count_cliente INTEGER; 
   v_count_promocion INTEGER;
BEGIN 
   -- verificar que el cliente existe
   SELECT COUNT(*)
   INTO v_count_cliente
   FROM clientes 
   WHERE rut = p_rut_cliente;

   IF v_count_cliente = 0 THEN 
      DBMS_OUTPUT.PUT_LINE('El cliente con rut' || p_rut_cliente || 'no existe..');
   END IF;

   -- verificar que la promocion existe
   SELECT COUNT(*)
   INTO v_count_promocion
   FROM promociones 
   WHERE id = p_id_promocion;

   IF v_count_promocion = 0 THEN 
      DBMS_OUTPUT.PUT_LINE('La promocion con ID ' || p_id_promocion || 'no existe..');
   END IF;

   UPDATE clientes 
      SET id_promocion = p_id_promocion 
   WHERE rut = p_rut_cliente;
   --SQL%ROWCOUNT devuelve el numero de filas afectadas por la ultima sentencia 
   IF SQL%ROWCOUNT = 0 THEN
      DBMS_OUTPUT.PUT_LINE('No se pudo actualizar el cliente'); 
   END IF;
END;
/
   
--PROCEDIMIENTOS ALMACENADOS: GENERAR UN INFORME POR SUCURSAL
CREATE OR REPLACE PROCEDURE generar_informe_ventas_sucursal (
   p_id_sucursal IN INTEGER,
   p_fecha_inicio IN DATE,
   p_fecha_fin IN DATE,
   p_informe OUT SYS_REFCURSOR
) IS 
BEGIN 
   -- VALIDAR FECHA
   IF p_fecha_inicio > p_fecha_fin THEN 
      DBMS_OUTPUT.PUT_LINE('La fecha de inicio no puede ser mayor a la fecha de fin...');
   END IF;

   OPEN p_informe FOR 
      SELECT s.nombre_sucursal AS sucursal,
         COUNT(DISTINCT v.id) AS tota_ventas,
         SUM(v.total_venta) AS monto_total,
         AVG(v.total_venta) AS venta_promedio,
         MIN(v.fecha) AS primera_venta,
         MAX(v.fecha) AS ultima_venta
      FROM ventas v 
      JOIN sucursal s ON v.id_sucursal = s.id
      WHERE v.id_sucursal = p_id_sucursal
      AND v.fecha BETWEEN p_fecha_inicio AND p_fecha_fin
   GROUP BY s.nombre_sucursal;
EXCEPTION 
   WHEN OTHERS THEN 
      DBMS_OUTPUT.PUT_LINE('Error al generar informe...' || SQLERRM);
END;
/


--========================================================
-- 2) TRIGGERS 
--========================================================

--TRIGGERS: Verificar stock antes de venta 
create or replace trigger tr_verificar_stock_venta
before insert on detalles_ventas
for each row
declare
   v_stock integer;
   v_producto_nombre VARCHAR2(255);
begin
   select stock_disponible, nombre
     into v_stock, v_producto_nombre
     from productos
    where id = :new.id_producto;
    
   if v_stock <= 0 then
      raise_application_error(-20002, 'El producto' || v_producto_nombre || 'no tiene stock disponible. Stock disponible' || v_stock );
   end if;
-- Reducir el stock automaticamente
   UPDATE productos
      SET stock_disponible = stock_disponible - 1
   WHERE id = :NEW.id_producto;

EXCEPTION
   WHEN NO_DATA_FOUND THEN 
      raise_application_error(-20008, 'El producto con ID ' || :NEW.id_producto || 'no existe');
   WHEN OTHERS THEN
      raise_application_error(-20009, 'Error en verificacion de stock.' || SQLERRM);
END;
/



--TRIGGERS: DEVOLVER UN PRODUCTO DESPUÉS DE UNA DEVOLUCIÓN
create or replace trigger tr_devolver_stock_producto
after insert on devoluciones
for each row
declare
   v_id_producto INTEGER;
   v_cantidad_productos INTEGER;
begin
   -- Obtener el ID del producto de la venta original
   select COUNT(DISTINCT id_producto)
     into v_cantidad_productos
     from detalles_ventas
    where id_venta = :new.id_venta;
   
   
   --si hay multiples productos, necesitamos manejarlo diferente
   IF v_cantidad_productos > 1 THEN
      -- para mas simple, devolvemos stock de todos los productos de la venta
      FOR rec  IN (SELECT id_producto FROM detalles_ventas WHERE id_venta = :NEW.id_venta)
       LOOP 
         UPDATE productos
         SET stock_disponible = stock_disponible + 1
         WHERE id = rec.id_producto;
      END LOOP;
   ELSE 
      SELECT id_producto
      INTO v_id_producto 
      FROM detalles_ventas 
      WHERE id_venta = :NEW.id_venta 
      AND ROWNUM = 1;

      UPDATE productos
      SET stock_disponible = stock_disponible + 1
      WHERE id = v_id_producto;
   END IF;

   DBMS_OUTPUT.PUT_LINE('Stock devuelto para la venta ID: ' || :NEW.id_venta);

EXCEPTION 
   WHEN NO_DATA_FOUND THEN 
      DBMS_OUTPUT.PUT_LINE('No se encontraron productos para la venta ID: ' || :NEW.id_venta);
   WHEN OTHERS THEN 
      DBMS_OUTPUT.PUT_LINE('Error al devolver stock: ' || SQLERRM);

END;
/


--RECORDS-MANEJO DE INFORMACIÓN DE UN PROVEEDOR
begin
   for r_devolucion in (select * from devoluciones where fecha_devolucion = sysdate) loop
      dbms_output.put_line('Se registró una devolución para la venta ID: ' || r_devolucion.id_venta);
   end loop;
end;
/

--RECORD-ALCTUALIZACIÓN DE LA SUCURSAL DE UN EMPLEADO
declare
   v_empleado empleados%rowtype;
   v_sucursal_nueva VARCHAR2(255) := 'Sucursal Centro';

begin
   select *
     into v_empleado
     from empleados
    where rut = '77888999-0';

   v_empleado.sucursal := 'Sucursal Parral';


   update empleados
      set sucursal = v_empleado.sucursal
    where rut = v_empleado.rut;

   commit;
end;
/


--BLOQUE anonimo con cursor implicito
DECLARE
   v_total_clientes NUMBER := 0;
   v_sin_examen NUMBER := 0;
BEGIN
   SELECT COUNT(*) INTO v_total_clientes FROM clientes;
   
   DBMS_OUTPUT.PUT_LINE('=== Clientes sin Examen Visual ===');
   DBMS_OUTPUT.PUT_LINE('Total de clientes: ' || v_total_clientes);
   DBMS_OUTPUT.PUT_LINE('');

   -- Cursor implícito para revisar clientes sin historial médico
   FOR r_cliente IN (
      SELECT c.rut, c.nombre, c.apellido, c.correo 
      FROM clientes c
      WHERE NOT EXISTS (
         SELECT 1 FROM historial_medico_visual h 
         WHERE h.rut_cliente = c.rut
      )
      ORDER BY c.apellido, c.nombre
   ) LOOP
   
      BEGIN
         v_sin_examen := v_sin_examen + 1;
         
         DBMS_OUTPUT.PUT_LINE(v_sin_examen || '. ' || r_cliente.nombre || ' ' || 
                            r_cliente.apellido || ' - RUT: ' || r_cliente.rut);
         DBMS_OUTPUT.PUT_LINE('   Email: ' || NVL(r_cliente.correo, 'Sin email'));
         DBMS_OUTPUT.PUT_LINE('');
         
      EXCEPTION
         WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE('Error procesando cliente: ' || r_cliente.rut);
      END;
   END LOOP;

   DBMS_OUTPUT.PUT_LINE('=== RESUMEN ===');
   DBMS_OUTPUT.PUT_LINE('Clientes sin examen: ' || v_sin_examen);
   DBMS_OUTPUT.PUT_LINE('Clientes con examen: ' || (v_total_clientes - v_sin_examen));
   
   IF v_sin_examen > 0 THEN
      DBMS_OUTPUT.PUT_LINE('Acción: Contactar para agendar primera consulta');
   ELSE
      DBMS_OUTPUT.PUT_LINE('Todos los clientes tienen examen registrado');
   END IF;

END;
/




-- Bloque anonimo con cursor explicito 
DECLARE 
   v_tiene_ventas NUMBER;
   v_total_clientes NUMBER := 0;
   v_clientes_con_ventas NUMBER := 0;
BEGIN 
   SELECT COUNT(*) INTO v_total_clientes FROM clientes;

   DBMS_OUTPUT.PUT_LINE('=======Reporte de ventas por clientes=======');
   DBMS_OUTPUT.PUT_LINE('Total de clientes: ' || v_total_clientes);
   DBMS_OUTPUT.PUT_LINE('');

   -- Iterar sobre los clientes con sus ventas usando LEFT JOIN
   FOR r_cliente IN (
        SELECT c.rut, c.nombre, c.apellido, COUNT(v.id) as num_ventas
        FROM clientes c
        LEFT JOIN ventas v ON c.rut = v.rut_cliente
        GROUP BY c.rut, c.nombre, c.apellido
        ORDER BY c.apellido, c.nombre
   ) 
   LOOP 
      BEGIN 
         v_tiene_ventas := r_cliente.num_ventas;

         IF v_tiene_ventas > 0 THEN 
            v_clientes_con_ventas := v_clientes_con_ventas + 1;
            DBMS_OUTPUT.PUT_LINE('¡BIEN! ' || r_cliente.nombre || ' ' || r_cliente.apellido
                                 ||' ' || r_cliente.rut || ' tiene: ' || v_tiene_ventas || ' Venta(s)' );
         ELSE 
            DBMS_OUTPUT.PUT_LINE('MAL! ' || r_cliente.nombre || ' ' || r_cliente.apellido
                                 ||' ' || r_cliente.rut || ' Aun sin ventas' );
         END IF;
      EXCEPTION
         WHEN OTHERS THEN 
            DBMS_OUTPUT.PUT_LINE('Error: Cliente ' || r_cliente.rut || ' - ' || SQLERRM);
      END;
   END LOOP;

   DBMS_OUTPUT.PUT_LINE('');
   DBMS_OUTPUT.PUT_LINE('==RESUMEN==');   
   DBMS_OUTPUT.PUT_LINE('Clientes con ventas: ' || v_clientes_con_ventas);      
   DBMS_OUTPUT.PUT_LINE('Clientes sin ventas: ' || (v_total_clientes - v_clientes_con_ventas));      
EXCEPTION   
   WHEN OTHERS THEN 
      DBMS_OUTPUT.PUT_LINE('Error General: ' || SQLERRM);
END;
/


-- 4. Records
-- record: Actualizacion de sucursal de empleado 
DECLARE 
   -- Definir un record simple
   TYPE t_empleado IS RECORD (
      rut VARCHAR2(20),
      nombre VARCHAR2(100),
      sucursal VARCHAR2(255)
   );
   
   v_empleado t_empleado;
   v_nueva_sucursal VARCHAR2(255) := 'Sucursal Valparaíso';
BEGIN 
   -- Obtener datos del empleado
   SELECT rut, nombre, sucursal 
   INTO v_empleado.rut, v_empleado.nombre, v_empleado.sucursal
   FROM empleados
   WHERE rut = '77888999-0';

   DBMS_OUTPUT.PUT_LINE('Empleado: ' || v_empleado.nombre);
   DBMS_OUTPUT.PUT_LINE('Sucursal actual: ' || v_empleado.sucursal);   

   -- Verificar si ya está en la sucursal destino
   IF v_empleado.sucursal = v_nueva_sucursal THEN
      DBMS_OUTPUT.PUT_LINE('El empleado ya está en ' || v_nueva_sucursal);
      RETURN;
   END IF;

   -- Actualizar sucursal
   UPDATE empleados
      SET sucursal = v_nueva_sucursal
   WHERE rut = v_empleado.rut;

   COMMIT;
   DBMS_OUTPUT.PUT_LINE('Transferido de "' || v_empleado.sucursal || '" a "' || v_nueva_sucursal || '"');

EXCEPTION
   WHEN NO_DATA_FOUND THEN 
      DBMS_OUTPUT.PUT_LINE('ERROR: Empleado no encontrado');
   WHEN OTHERS THEN 
      ROLLBACK;
      DBMS_OUTPUT.PUT_LINE('ERROR: ' || SQLERRM);
END;
/
select * from clientes 