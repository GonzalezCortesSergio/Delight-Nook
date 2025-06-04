-- Insertando una caja con 'id', 'dinero_caja', 'nombre' y 'deleted'
INSERT INTO caja (id, dinero_caja, nombre, deleted)
VALUES (1, 1000.50, 'Caja Principal', false);

-- Insertando otra caja con 'id', 'dinero_caja', 'nombre' y 'deleted'
INSERT INTO caja (id, dinero_caja, nombre, deleted)
VALUES (2, 500.00, 'Caja de Sucursal A', false);

-- Insertando una caja, dejando 'deleted' con el valor predeterminado 'false'
INSERT INTO caja (id, dinero_caja, nombre, deleted)
VALUES (3, 1500.75, 'Caja Central', false);

-- Insertando una caja con valores diferentes
INSERT INTO caja (id, dinero_caja, nombre, deleted)
VALUES (4, 2000.25, 'Caja de Emergencia', false);

-- Insertando otra caja con 'id', 'dinero_caja', 'nombre' y 'deleted'
INSERT INTO caja (id, dinero_caja, nombre, deleted)
VALUES (5, 750.00, 'Caja de Oficina', false);

ALTER SEQUENCE caja_seq RESTART WITH 55;

INSERT INTO categoria (id, nombre, deleted) VALUES (1, 'Tecnología', false);
INSERT INTO categoria (id, nombre, deleted) VALUES (2, 'Accesorios para el hogar', false);
INSERT INTO categoria (id, nombre, deleted) VALUES (3, 'Juguetes', false);
INSERT INTO categoria (id, nombre, deleted) VALUES (4, 'Cuidado personal', false);
INSERT INTO categoria (id, nombre, deleted) VALUES (5, 'Ropa', false);
INSERT INTO categoria (id, nombre, deleted, categoria_padre_id) VALUES (6, 'Electrónica', false, 1);
INSERT INTO categoria (id, nombre, deleted) VALUES (7, 'Papelería', false);
INSERT INTO categoria (id, nombre, deleted) VALUES (8, 'Bolsos y mochilas', false);
INSERT INTO categoria (id, nombre, deleted, categoria_padre_id) VALUES (9, 'Decoración', false, 2);
INSERT INTO categoria (id, nombre, deleted, categoria_padre_id) VALUES (10, 'Vajilla y utensilios de cocina', false, 2);
INSERT INTO categoria (id, nombre, deleted, categoria_padre_id) VALUES (11, 'Muebles pequeños', false, 2);
INSERT INTO categoria (id, nombre, deleted, categoria_padre_id) VALUES (12, 'Higiene y salud', false, 4);
INSERT INTO categoria (id, nombre, deleted, categoria_padre_id) VALUES (13, 'Bebés y niños', false, 3);
INSERT INTO categoria (id, nombre, deleted) VALUES (14, 'Relojes', false);
INSERT INTO categoria (id, nombre, deleted) VALUES (15, 'Accesorios para mascotas', false);
INSERT INTO categoria (id, nombre, deleted, categoria_padre_id) VALUES (16, 'Cuidado facial', false, 4);
INSERT INTO categoria (id, nombre, deleted, categoria_padre_id) VALUES (17, 'Cuidado del cabello', false, 4);
INSERT INTO categoria (id, nombre, deleted) VALUES (18, 'Organizadores', false);
INSERT INTO categoria (id, nombre, deleted) VALUES (19, 'Herramientas', false);
INSERT INTO categoria (id, nombre, deleted) VALUES (20, 'Cajas y almacenamiento', false);
INSERT INTO categoria (id, nombre, deleted, categoria_padre_id) VALUES (21, 'Tecnología portátil', false, 1);
INSERT INTO categoria (id, nombre, deleted, categoria_padre_id) VALUES (22, 'Accesorios para el móvil', false, 6);
INSERT INTO categoria (id, nombre, deleted) VALUES (23, 'Cultura pop y entretenimiento', false);
INSERT INTO categoria (id, nombre, deleted, categoria_padre_id) VALUES (24, 'Cuidado del cuerpo', false, 4);
INSERT INTO categoria (id, nombre, deleted, categoria_padre_id) VALUES (25, 'Baterías y cargadores', false, 22);
INSERT INTO categoria (id, nombre, deleted, categoria_padre_id) VALUES (26, 'Iluminación y lámparas', false, 2);
INSERT INTO categoria (id, nombre, deleted) VALUES (27, 'Artículos de oficina', false);
INSERT INTO categoria (id, nombre, deleted) VALUES (28, 'Accesorios de viaje', false);
INSERT INTO categoria (id, nombre, deleted) VALUES (29, 'Decoración navideña', false);
INSERT INTO categoria (id, nombre, deleted) VALUES (30, 'Productos de belleza', false);

ALTER SEQUENCE categoria_seq RESTART WITH 80;


-- Insertando productos con distintos proveedores
INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (1, 'Auriculares Inalámbricos', 18.99, 'Auriculares Bluetooth, compactos y de buena calidad de sonido', false, 'Sony', 6);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (2, 'Cojín Decorativo', 9.99, 'Cojín suave y cómodo, con diseño moderno', false, 'Zara Home', 9);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (3, 'Juego de Tazas', 14.50, 'Set de 2 tazas de cerámica con diseño divertido y colorido', false, 'H&M Home', 12);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (4, 'Crema Hidratante', 8.50, 'Crema facial hidratante con extracto de té verde', false, 'Nivea', 16);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (5, 'Cargador USB', 7.99, 'Cargador USB tipo C para teléfonos móviles y dispositivos', false, 'Anker', 21);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (6, 'Mochila de Tela', 19.99, 'Mochila ligera y práctica, perfecta para el día a día', false, 'Adidas', 8);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (7, 'Silla de Escritorio', 45.99, 'Silla ergonómica y cómoda para oficina o estudio', false, 'Ikea', 27);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (8, 'Lámpara LED', 12.99, 'Lámpara de escritorio LED con luz regulable', false, 'Philips', 26);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (9, 'Reloj Digital', 22.00, 'Reloj de pulsera con pantalla LED digital, resistente al agua', false, 'Casio', 18);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (10, 'Pelota de Fútbol', 10.99, 'Pelota de fútbol de tamaño oficial, ideal para entrenamientos', false, 'Nike', 5);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (11, 'Batería Externa', 24.99, 'Batería portátil de 10000 mAh para cargar tus dispositivos', false, 'Xiaomi', 21);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (12, 'Set de Baño', 15.99, 'Set completo de baño con gel de ducha, shampoo y esponja', false, 'LOreal', 12);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (13, 'Taza de Cerámica', 6.99, 'Taza con diseño moderno, ideal para café o té', false, 'H&M Home', 9);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (14, 'Estuche para Laptop', 18.99, 'Estuche de neopreno para laptops hasta 15 pulgadas', false, 'Targus', 21);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (15, 'Bolso de Mano', 26.99, 'Bolso de mano elegante con múltiples compartimentos', false, 'Michael Kors', 22);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (16, 'Cinta Métrica', 3.50, 'Cinta métrica de 5 metros, perfecta para medir con precisión', false, 'Stanley', 13);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (17, 'Funda para Teléfono', 6.99, 'Funda de silicona para teléfonos móviles, de diseño flexible', false, 'OtterBox', 22);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (18, 'Aceite Esencial', 7.50, 'Aceite esencial de lavanda para relajación y bienestar', false, 'The Body Shop', 16);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (19, 'Set de Pinceles para Maquillaje', 11.99, 'Set de pinceles de alta calidad para maquillaje', false, 'Real Techniques', 24);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (20, 'Cinta Adhesiva', 2.50, 'Cinta adhesiva de uso múltiple, ideal para oficina y manualidades', false, 'Scotch', 27);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (21, 'Lentes de Sol', 15.99, 'Lentes de sol con protección UV, diseño moderno', false, 'Ray-Ban', 23);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (22, 'Mueble Organizador', 29.99, 'Mueble organizador para la oficina o el hogar', false, 'Ikea', 12);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (23, 'Mascarilla Facial', 5.99, 'Mascarilla facial hidratante para una piel más suave', false, 'Neutrogena', 16);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (24, 'Teclado Mecánico', 35.99, 'Teclado mecánico con retroiluminación RGB, ideal para gamers', false, 'Logitech', 6);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (25, 'Planta Artificial', 7.50, 'Planta artificial de interior, fácil de mantener', false, 'Ikea', 14);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (26, 'Cartera de Cuero', 22.50, 'Cartera de cuero sintético con diseño moderno y elegante', false, 'Fossil', 22);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (27, 'Funda para Tablet', 14.99, 'Funda de neopreno para tablet de 10 pulgadas', false, 'Targus', 21);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (28, 'Lampara de Noche', 12.99, 'Lámpara LED para mesa de noche con ajuste de brillo', false, 'Philips', 26);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (29, 'Pulsera Fitness', 20.00, 'Pulsera fitness para monitorear actividad física y salud', false, 'Fitbit', 5);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (30, 'Tijeras de Precisión', 4.99, 'Tijeras de alta precisión para manualidades y costura', false, 'Fiskars', 13);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (31, 'Tenedor de Cocina', 3.99, 'Tenedor de cocina de acero inoxidable', false, 'Cuisinart', 10);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (32, 'Batería Portátil', 19.99, 'Batería externa de alta capacidad para cargar tus dispositivos', false, 'Anker', 21);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (33, 'Botella de Agua', 7.50, 'Botella de agua reutilizable de acero inoxidable', false, 'Swell', 9);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (34, 'Soporte para Celular', 5.99, 'Soporte ajustable para teléfono móvil y tablet', false, 'Mophie', 22);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (35, 'Almohada Viscoelástica', 18.99, 'Almohada ortopédica para un sueño más reparador', false, 'Tempur', 12);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (36, 'Espejo de Maquillaje', 12.99, 'Espejo con luz LED para aplicación de maquillaje', false, 'SimpleHuman', 24);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (37, 'Linterna LED', 9.99, 'Linterna LED compacta, ideal para viajes y emergencias', false, 'Energizer', 13);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (38, 'Cortina de Ducha', 14.99, 'Cortina de ducha impermeable con diseño moderno', false, 'Ikea', 9);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (39, 'Papel Higiénico', 2.99, 'Papel higiénico suave y resistente', false, 'Charmin', 12);

INSERT INTO producto (id, nombre, precio_unidad, descripcion, deleted, proveedor, categoria_id)
VALUES (40, 'Jarra Térmica', 19.99, 'Jarra térmica para mantener bebidas calientes o frías', false, 'Thermos', 9);

ALTER SEQUENCE producto_seq RESTART WITH 90;
