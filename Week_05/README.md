数据表创建
=========
用户表
---------
```sql
CREATE TABLE `t_user` (
  id int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键', //用户ID
  `name` varchar(32) NOT NULL DEFAULT '',
  `email` varchar(32) NOT NULL DEFAULT '',
  `phone` varchar(16) NOT NULL,
  `status` tinyint NOT NULL default 0

  ctime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间', 
  mtime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `phone` (`phone`)
)ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '用户表'
```

## 商品表
```sql
CREATE TABLE `t_goods` (
  id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键', //用户ID
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT '商品名称',
  `price` float(10,2) NOT NULL DEFAULT '0.00' COMMENT '商品单价',
  `status` tinyint NOT NULL default 0, 
  ctime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间', 
  mtime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
)ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '商品表'
```
## 订单表
```sql
CREATE TABLE `t_orders` (
  id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键', 
  `order_id` varchar(32) NOT NULL DEFAULT '' COMMENT '订单号',  //生成算法生成
  `total_price` float(10,2) NOT NULL DEFAULT '0.00',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '下单用户',
  `status` tinyint NOT NULL default 0 COMMENT '订单状态', 
  `order_time` DATETIME NOT NULL,
  `pay_time` DATETIME NOT NULL,
  ctime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间', 
  mtime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`)
)ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '订单表'
```

## 订单详情表
```sql
CREATE TABLE `t_orders_detail` (
  id BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键', //用户ID
  `order_id` varchar(32) NOT NULL DEFAULT '' COMMENT '订单号',
  `good_id` BIGINT(20) UNSIGNED NOT NULL COMMENT '商品ID',
  `price` float(10,2) NOT NULL DEFAULT '0.00' COMMENT '商品单价',
  `nums` int unsigned not null default 0 COMMENT '商品数量',
  `total_price` float(10,2) NOT NULL DEFAULT '0.00' COMMENT '商品总价',
  `status` tinyint NOT NULL default 0 COMMENT '状态', 
  ctime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间', 
  mtime DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE `order_goods` (`order_id`)
)ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT = '订单详情表'
```
