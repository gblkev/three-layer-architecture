INSERT INTO color (id, hexa_code)
VALUES ('color_id_1', '#000000');
INSERT INTO color (id, hexa_code)
VALUES ('color_id_2', '#000001');

INSERT INTO driver (id, first_name, last_name)
VALUES ('driver_id_1', 'Forrest', 'Gump');
INSERT INTO driver (id, first_name, last_name)
VALUES ('driver_id_2', 'Tom', 'Hanks');

INSERT INTO car (id, color_id, driver_id)
VALUES ('car_id_1', 'color_id_1', 'driver_id_1');
INSERT INTO car (id, color_id, driver_id)
VALUES ('car_id_2', 'color_id_2', 'driver_id_2');