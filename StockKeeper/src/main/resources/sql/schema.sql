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



ALTER TABLE brands ADD COLUMN is_deleted BOOLEAN NOT NULL DEFAULT false;


ALTER TABLE brands
ADD created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
ADD updated_at TIMESTAMP DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
ADD updated_by BIGINT(20) DEFAULT NULL;

ALTER TABLE brands ADD created_by BIGINT(20) DEFAULT NULL;



ALTER TABLE categories
ADD created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
ADD updated_at TIMESTAMP DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
ADD created_by INT(11) DEFAULT NULL,
ADD updated_by INT(11) DEFAULT NULL;
ALTER TABLE categories ADD COLUMN is_deleted BOOLEAN NOT NULL DEFAULT false;


ALTER TABLE subcategories
ADD created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
ADD created_by BIGINT,
ADD updated_by BIGINT,
ADD is_deleted TINYINT(1) DEFAULT 0;


ALTER TABLE products
ADD created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
ADD created_by BIGINT,
ADD updated_by BIGINT,
ADD is_deleted TINYINT(1) DEFAULT 0;


ALTER TABLE products ADD COLUMN   upc VARCHAR(50) NOT NULL;

ALTER TABLE products ADD COLUMN quantity_in_box INT(11) NOT NULL DEFAULT 0 AFTER quantity;
ALTER TABLE products ADD COLUMN is_available TINYINT(1) NOT NULL DEFAULT 1 AFTER updated_by;


CREATE TABLE prices (
  id INT(11) NOT NULL AUTO_INCREMENT,
  product_id INT(11) NOT NULL,
  price_type VARCHAR(50) NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  created_by BIGINT(20) DEFAULT NULL,
  updated_by BIGINT(20) DEFAULT NULL,
  is_deleted TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  FOREIGN KEY (product_id) REFERENCES products(id)
);


ALTER TABLE prices ADD currency_code VARCHAR(10) NOT NULL DEFAULT 'USD' AFTER price;

CREATE TABLE vendors (
  id INT(11) NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  address TEXT,
  phone_number VARCHAR(20),
  email VARCHAR(255),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  created_by BIGINT(20) DEFAULT NULL,
  updated_by BIGINT(20) DEFAULT NULL,
  is_deleted TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (id)
);

INSERT INTO vendors (name, address, phone_number, email) 
VALUES ('Vendor A', '123 Main Street', '555-1234', 'vendorA@example.com'),
       ('Vendor B', '456 Oak Avenue', '555-5678', 'vendorB@example.com'),
       ('Vendor C', '789 Elm Boulevard', '555-9012', 'vendorC@example.com');
       
       
       
             
 CREATE TABLE purchase_orders (
  id INT(11) NOT NULL AUTO_INCREMENT,
  vendor_id INT(11) NOT NULL,
  order_date DATE NOT NULL,
  total_amount DECIMAL(10,2) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  created_by BIGINT(20) DEFAULT NULL,
  updated_by BIGINT(20) DEFAULT NULL,
  is_deleted TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  FOREIGN KEY (vendor_id) REFERENCES vendors(id)
);

ALTER TABLE purchase_orders ADD currency_code VARCHAR(3) NOT NULL DEFAULT 'USD';



      INSERT INTO purchase_orders (vendor_id, order_date, total_amount, created_at, updated_at, created_by, updated_by, is_deleted, currency_code)
VALUES (1, '2022-01-01', 1000.00, NOW(), NOW(), 1, 1, 0, 'USD'),
       (2, '2022-02-15', 2500.00, NOW(), NOW(), 1, 1, 0, 'EUR'),
       (3, '2022-03-20', 5000.00, NOW(), NOW(), 2, 1, 0, 'USD'),
       (2, '2022-04-10', 1500.00, NOW(), NOW(), 3, 1, 0, 'GBP'),
       (1, '2022-05-05', 800.00, NOW(), NOW(), 2, 1, 0, 'USD');
       
       
 

CREATE TABLE purchase_order_items (
  id INT(11) NOT NULL AUTO_INCREMENT,
  purchase_order_id INT(11) NOT NULL,
  product_id INT(11) NOT NULL,
  purchase_price DECIMAL(10,2) NOT NULL,
  quantity INT(11) NOT NULL,
  currency_code VARCHAR(3) NOT NULL DEFAULT 'USD',
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  created_by BIGINT(20) DEFAULT NULL,
  updated_by BIGINT(20) DEFAULT NULL,
  is_deleted TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  FOREIGN KEY (purchase_order_id) REFERENCES purchase_orders(id),
  FOREIGN KEY (product_id) REFERENCES products(id)
);



INSERT INTO purchase_order_items (purchase_order_id, product_id, purchase_price, quantity, currency_code)
VALUES (1, 4, 10.50, 5, 'USD'),
       (1, 4, 15.00, 10, 'USD');
       
  
       
 ALTER TABLE vendors MODIFY COLUMN is_deleted TINYINT(1) DEFAULT 0;
 
 ALTER TABLE brands
ADD COLUMN manufacturer_id INT NULL;


CREATE TABLE manufacturer (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  address VARCHAR(255),
  description VARCHAR(255),
  created_by BIGINT,
  updated_by BIGINT,
  is_deleted BOOLEAN DEFAULT false,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


ALTER TABLE products ADD manufacturer_new INT;

ALTER TABLE products DROP COLUMN manufacturer;

ALTER TABLE products
ADD COLUMN barcode VARCHAR(255);


CREATE TABLE `customers` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(255) NOT NULL,
  `last_name` varchar(255) NOT NULL,
  `newsletter` tinyint(1) DEFAULT '0',
  `tax_file` varchar(255) DEFAULT NULL,
  `website` varchar(255) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `tax_id_number` varchar(255) DEFAULT NULL,
  `company` varchar(255) DEFAULT NULL,
  `address_line1` varchar(255) DEFAULT NULL,
  `address_line2` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `state_province` varchar(255) DEFAULT NULL,
  `zip_postal_code` varchar(20) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `business_name` varchar(255) DEFAULT NULL,
  `business_setup` varchar(255) DEFAULT NULL,
  `business_organization` varchar(255) DEFAULT NULL,
  `years_in_business` int DEFAULT NULL,
  `locations` int DEFAULT NULL,
  `business_type` varchar(255) DEFAULT NULL,
  `sales_channel` varchar(255) DEFAULT NULL,
  `how_did_you_hear` varchar(255) DEFAULT NULL,
  `monthly_inventory_budget` decimal(10,2) DEFAULT NULL,
  `created_by` bigint DEFAULT NULL,
  `updated_by` bigint DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `approval_status` char(1) DEFAULT 'P',
  PRIMARY KEY (`id`)
) ;

------Cart & Order 


CREATE TABLE `cart` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
)


CREATE TABLE `cart_items` (
  `id` int NOT NULL AUTO_INCREMENT,
  `cart_id` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
)

CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int DEFAULT NULL,
  `total_price` decimal(10,2) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) DEFAULT '0',
  `order_status` varchar(255) NOT NULL DEFAULT 'PENDING',
  `shipping_address` varchar(2000) DEFAULT NULL,
  PRIMARY KEY (`id`)
)

CREATE TABLE `order_items` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int DEFAULT NULL,
  `product_id` int DEFAULT NULL,
  `quantity` int DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
)

CREATE TABLE `payment` (
  `id` int NOT NULL AUTO_INCREMENT,
  `order_id` int DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `payment_method` varchar(100) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
)


INSERT INTO roles (name) VALUES ('ROLE_CUSTOMER');

ALTER TABLE products ADD COLUMN flavor VARCHAR(255);




CREATE TABLE returns (
    return_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT,
    return_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20),
    reason VARCHAR(255),
    comments VARCHAR(255),
    credit_amount DECIMAL(10, 2),
    credit_date TIMESTAMP,
    created_by VARCHAR(100),
    updated_by VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


CREATE TABLE sales_person (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(255) ,
    last_name VARCHAR(255) ,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(20),
    department VARCHAR(255),
    position VARCHAR(255),
    hire_date DATE,
    territory VARCHAR(255),
    sales_quota DECIMAL(10, 2),
    sales_ytd DECIMAL(10, 2),
    is_active TINYINT(1) ,
    last_login TIMESTAMP
);

CREATE TABLE `customer_sales_person` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `customer_id` bigint NOT NULL,
  `sales_person_id` bigint NOT NULL,
  `assigned_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
)
ALTER TABLE order_items
ADD COLUMN product_name VARCHAR(255),
ADD COLUMN image_url VARCHAR(255);
ALTER TABLE returns
ADD COLUMN correlation_id VARCHAR(255);

ALTER TABLE credits
ADD COLUMN credit_description VARCHAR(255);

ALTER TABLE sales_person
ADD COLUMN user_id BIGINT;
