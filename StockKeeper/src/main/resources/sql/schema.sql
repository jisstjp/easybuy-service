--USER

CREATE TABLE users (
  id INT NOT NULL AUTO_INCREMENT,
  username VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  first_name VARCHAR(255),
  last_name VARCHAR(255),
  PRIMARY KEY (id)
);


CREATE TABLE roles (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE (name)
);
INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');



CREATE TABLE user_roles (
  user_id INT NOT NULL,
  role_id INT NOT NULL,
  PRIMARY KEY (user_id, role_id),
  CONSTRAINT fk_user_roles_users FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_user_roles_roles FOREIGN KEY (role_id) REFERENCES roles(id)
);


INSERT INTO user_roles (user_id, role_id)
VALUES (1, 1);

---Product  mangemnet----- 

CREATE TABLE brands (
  id INT(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  logo_url VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);


INSERT INTO brands (name, description, logo_url) VALUES
('Microsoft', 'A technology company from the US that develops software, hardware, and other technology products', 'https://example.com/images/microsoft-logo.png'),
('Samsung', 'A technology company from Korea', 'https://example.com/images/samsung-logo.png');





CREATE TABLE categories (
  id INT(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  PRIMARY KEY (id)
);

INSERT INTO categories (name, description) VALUES
('Electronics', 'A category for electronic devices and accessories'),
('Clothing', 'A category for clothes, shoes, and other fashion accessories');




CREATE TABLE subcategories (
  id INT(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  category_id INT(11) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (category_id) REFERENCES categories(id)
);

INSERT INTO subcategories (name, description, category_id) VALUES
('Smartphones', 'A subcategory for smartphones', 1),
('Laptops', 'A subcategory for laptops', 1),
('Headphones', 'A subcategory for headphones', 1),
('Men''s Clothing', 'A subcategory for men''s clothing and accessories', 2),
('Women''s Clothing', 'A subcategory for women''s clothing and accessories', 2);




CREATE TABLE products (
  id INT(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  brand_id INT(11) NOT NULL,
  category_id INT(11) NOT NULL,
  subcategory_id INT(11) NOT NULL,
  sku VARCHAR(50) NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  quantity INT(11) NOT NULL,
  weight DECIMAL(10,2) NOT NULL,
  manufacturer VARCHAR(255) NOT NULL,
  length DECIMAL(10,2) NOT NULL,
  width DECIMAL(10,2) NOT NULL,
  height DECIMAL(10,2) NOT NULL,
  image_url VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (brand_id) REFERENCES brands(id),
  FOREIGN KEY (category_id) REFERENCES categories(id),
  FOREIGN KEY (subcategory_id) REFERENCES subcategories(id)
);

INSERT INTO products (name, description, brand_id, category_id, subcategory_id, sku, price, quantity, weight, manufacturer, length, width, height, image_url) VALUES
('Samsung S21', 'A premium Android phone with 5G support and 120Hz refresh rate', 4, 1, 1, 'SAMS21', 799.99, 50, 0.18, 'Samsung', 6.24, 2.90, 0.31, 'https://example.com/images/samsung-galaxy-s21.jpg');

