-- 乡创农游智能体平台数据库结构
-- 仅包含建库和建表语句；演示数据请执行 data.sql。

CREATE DATABASE IF NOT EXISTS xiangchuang_platform
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE xiangchuang_platform;

SET NAMES utf8mb4;

CREATE TABLE IF NOT EXISTS users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(20) NOT NULL UNIQUE,
    avatar VARCHAR(255) DEFAULT '/images/user.png',
    real_name VARCHAR(50),
    gender ENUM('男', '女', '保密') DEFAULT '保密',
    birth_date DATE,
    province VARCHAR(50),
    city VARCHAR(50),
    address TEXT,
    user_type ENUM('普通用户', '农户', '商家', '管理员') DEFAULT '普通用户',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS goods (
    goods_id INT PRIMARY KEY AUTO_INCREMENT,
    goods_name VARCHAR(100) NOT NULL,
    goods_area VARCHAR(50) NOT NULL,
    goods_category VARCHAR(50) NOT NULL,
    goods_time VARCHAR(20) NOT NULL,
    goods_producer VARCHAR(50) NOT NULL,
    goods_p_address VARCHAR(100) NOT NULL,
    goods_introduce TEXT,
    goods_price DECIMAL(10,2) NOT NULL,
    goods_standard VARCHAR(50) NOT NULL,
    goods_stars DECIMAL(2,1) DEFAULT 4.0,
    goods_picture VARCHAR(500) NOT NULL,
    goods_p_picture VARCHAR(500) NOT NULL,
    sales_count INT DEFAULT 0,
    stock INT DEFAULT 100,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_category (goods_category),
    INDEX idx_area (goods_area),
    INDEX idx_time (goods_time),
    INDEX idx_price (goods_price),
    FULLTEXT idx_search (goods_name, goods_introduce)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS activities (
    activity_id INT PRIMARY KEY AUTO_INCREMENT,
    activity_name VARCHAR(100) NOT NULL,
    activity_type VARCHAR(50) NOT NULL,
    activity_theme VARCHAR(100) NOT NULL,
    activity_location VARCHAR(100) NOT NULL,
    activity_address VARCHAR(200) NOT NULL,
    activity_duration VARCHAR(50) NOT NULL,
    activity_price DECIMAL(10,2) NOT NULL,
    activity_description TEXT,
    activity_images TEXT,
    max_participants INT DEFAULT 20,
    current_booked INT DEFAULT 0,
    activity_rating DECIMAL(2,1) DEFAULT 4.5,
    review_count INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_type (activity_type),
    INDEX idx_theme (activity_theme),
    INDEX idx_price (activity_price)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS activity_availability (
    availability_id INT PRIMARY KEY AUTO_INCREMENT,
    activity_id INT NOT NULL,
    available_date DATE NOT NULL,
    available_spots INT NOT NULL,
    price_adjustment DECIMAL(10,2) DEFAULT 0,
    is_available BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_availability_activity
        FOREIGN KEY (activity_id) REFERENCES activities(activity_id) ON DELETE CASCADE,
    UNIQUE KEY unique_activity_date (activity_id, available_date),
    INDEX idx_date (available_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(32) NOT NULL UNIQUE,
    user_id INT NOT NULL,
    order_type ENUM('商品', '旅游') NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(50),
    payment_status ENUM('待付款', '已付款', '已退款', '支付失败') DEFAULT '待付款',
    order_status ENUM('待处理', '进行中', '已完成', '已取消') DEFAULT '待处理',
    contact_name VARCHAR(50),
    contact_phone VARCHAR(20),
    contact_address TEXT,
    travel_date DATE,
    participants INT DEFAULT 1,
    special_requests TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_orders_user
        FOREIGN KEY (user_id) REFERENCES users(user_id),
    INDEX idx_user_created (user_id, created_at),
    INDEX idx_status (order_status),
    INDEX idx_order_no (order_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS order_items (
    item_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    item_type ENUM('商品', '活动') NOT NULL,
    item_id_ref INT NOT NULL,
    item_name VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_order_items_order
        FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    INDEX idx_order (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS favorites (
    favorite_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    item_type ENUM('商品', '活动') NOT NULL,
    item_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_favorites_user
        FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    UNIQUE KEY unique_favorite (user_id, item_type, item_id),
    INDEX idx_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS notifications (
    notification_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    title VARCHAR(200) NOT NULL,
    content TEXT NOT NULL,
    type ENUM('系统', '订单', '活动', '商品', '其他') DEFAULT '系统',
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notifications_user
        FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_user_read (user_id, is_read)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS reviews (
    review_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    item_type ENUM('商品', '活动') NOT NULL,
    item_id INT NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    content TEXT,
    images TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_reviews_user
        FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_item (item_type, item_id),
    INDEX idx_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
