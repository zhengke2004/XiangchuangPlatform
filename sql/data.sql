-- 乡创农游智能体平台演示数据
-- 执行顺序：先执行 init.sql，再执行本文件。
-- 所有账号、联系方式、地址均为演示数据，不对应真实个人。

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE reviews;
TRUNCATE TABLE notifications;
TRUNCATE TABLE favorites;
TRUNCATE TABLE order_items;
TRUNCATE TABLE orders;
TRUNCATE TABLE activity_availability;
TRUNCATE TABLE activities;
TRUNCATE TABLE goods;
TRUNCATE TABLE users;

-- 演示账号统一密码为 password，登录后请立即修改。
INSERT INTO users
    (user_id, username, password, email, phone, avatar, real_name, gender,
     province, city, address, user_type, is_active)
VALUES
    (1, 'demo_user', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
     'demo.user@example.test', '13800000001', '/images/user.png', '演示用户', '保密',
     '示例省', '示例市', '示例地址', '普通用户', TRUE),
    (2, 'demo_farmer', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
     'demo.farmer@example.test', '13800000002', '/images/user.png', '演示农户', '保密',
     '示例省', '示例市', '示例地址', '农户', TRUE),
    (3, 'demo_merchant', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
     'demo.merchant@example.test', '13800000003', '/images/user.png', '演示商家', '保密',
     '示例省', '示例市', '示例地址', '商家', TRUE),
    (4, 'demo_admin', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi',
     'demo.admin@example.test', '13800000000', '/images/user.png', '演示管理员', '保密',
     '示例省', '示例市', '示例地址', '管理员', TRUE);



/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

/*!40000 ALTER TABLE `goods` DISABLE KEYS */;
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (1,'有机高山大米','华东','有机农产品','秋','张大山','浙江安吉','采用传统耕种方式，无农药化肥，口感香糯，米香浓郁',48.50,'5kg/袋',4.5,'/images/products/大米.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-08-29 07:58:05');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (2,'云南冰糖橙','西南','时令生鲜','秋','李秀英','云南玉溪','皮薄多汁，甜度高，现摘现发，保证新鲜',25.80,'3kg/箱',4.3,'/images/products/navel.jpeg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 19:46:40');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (3,'传统竹编果篮','华东','手工艺品','全年','王爷爷','浙江东阳','传统工艺手工编织，天然环保，美观实用',68.00,'直径35cm',4.8,'/images/products/竹编果篮.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 19:46:59');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (4,'太行山野生蜂蜜','华北','加工食品','全年','赵师傅','山西太行山','深山放养，天然百花蜜，无添加，纯正蜜香',88.00,'500g/瓶',4.6,'/images/products/蜂蜜.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 19:47:10');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (5,'潮汕手工牛肉丸','华南','乡村特产','全年','陈师傅','广东潮汕','传统手工捶打，真材实料，口感Q弹，真空包装',45.00,'500g/袋',4.9,'/images/products/牛肉丸.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 19:48:03');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (6,'有机山茶油','华东','有机农产品','秋','周阿姨','江西婺源','冷压初榨，营养丰富，适合中式烹饪',128.00,'1L/瓶',4.4,'/images/products/蜂蜜.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 19:48:54');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (7,'新疆红枣','西北','乡村特产','秋','阿依古丽','新疆和田','自然风干，肉质饱满，香甜可口',35.00,'500g/袋',4.7,'/images/products/红枣.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 20:54:43');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (8,'长白山人参','东北','有机农产品','秋','采参人老李','吉林长白山','林下参，自然生长，补气养生',198.00,'50g/盒',4.5,'/images/products/人参.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 20:56:11');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (9,'手工刺绣香囊','华中','手工艺品','全年','绣娘小芳','湖南湘西','传统刺绣工艺，精美图案，可作香囊或挂饰',45.00,'个',4.8,'/images/products/香囊.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 20:56:37');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (10,'烟台红富士苹果','华北','时令生鲜','秋','王大叔','山东烟台','个大饱满，脆甜多汁，现摘现发',58.00,'5kg/箱',4.6,'/images/products/苹果.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 20:58:02');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (11,'武夷山大红袍','华东','乡村特产','春','茶农老陈','福建武夷山','岩茶之王，香气馥郁，回甘持久',168.00,'250g/盒',4.9,'/images/products/大红袍.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:00:38');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (12,'五常有机大米','东北','有机农产品','秋','稻花香老张','黑龙江五常','颗粒饱满，晶莹剔透，米饭香软',68.00,'5kg/袋',4.8,'/images/products/五常大米.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:00:52');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (13,'云南野生菌礼盒','西南','时令生鲜','夏','采菌人老杨','云南楚雄','多种野生菌搭配，山珍美味',128.00,'1kg/盒',4.7,'/images/products/菌菇.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:05:06');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (14,'手工陶瓷茶杯','华东','手工艺品','全年','陶艺师小李','江西景德镇','手工拉坯，青花釉彩，典雅大方',88.00,'个',4.8,'/images/products/陶瓷茶杯.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:06:42');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (15,'四川腊肉','西南','加工食品','冬','老腊肉张','四川巴中','传统烟熏工艺，肥瘦相间，风味独特',58.00,'500g/袋',4.7,'/images/products/腊肉.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:06:53');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (16,'阳澄湖大闸蟹','华东','时令生鲜','秋','蟹农老王','江苏苏州','膏黄肉嫩，鲜活发货，送礼佳品',188.00,'4只/盒',4.9,'/images/products/大闸蟹.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:10:38');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (17,'手工竹编茶席','华东','手工艺品','全年','竹编传承人','浙江安吉','精细竹编工艺，自然雅致',128.00,'套',4.8,'/images/products/竹编茶席.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:12:17');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (18,'东北黑木耳','东北','乡村特产','秋','木耳大王','黑龙江牡丹江','肉厚朵大，泡发率高，口感爽脆',42.00,'250g/袋',4.6,'/images/products/黑木耳.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:12:29');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (19,'手工编织草帽','华南','手工艺品','夏','草编阿婆','广西北海','传统草编工艺，遮阳透气，文艺范儿',38.00,'顶',4.5,'/images/products/草帽.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:15:48');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (20,'宁夏枸杞','西北','有机农产品','夏','枸杞老农','宁夏中卫','粒大饱满，自然晾晒，养生佳品',48.00,'250g/袋',4.7,'/images/products/枸杞.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:15:57');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (21,'手工蓝印花布','华东','手工艺品','全年','印染师傅','江苏南通','传统蓝印花布工艺，可做桌布、围裙',68.00,'米',4.7,'/images/products/蓝布.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:19:28');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (22,'舟山海鲜大礼包','华东','时令生鲜','冬','渔老大','浙江舟山','多种海鲜组合，新鲜冷冻，年货首选',198.00,'3kg/箱',4.8,'/images/products/海鲜.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:22:47');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (23,'陕西猕猴桃','西北','时令生鲜','秋','周至果农','陕西周至','果肉细腻，酸甜适口，维C之王',42.00,'2.5kg/箱',4.5,'/images/products/kiwi.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 19:45:12');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (24,'手工皮影','西北','手工艺品','全年','皮影艺人','陕西华县','传统皮影制作，可收藏或表演',128.00,'套',4.9,'/images/products/shadow.jpeg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:23:45');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (25,'湖南剁椒','华中','加工食品','全年','辣妹子','湖南长沙','传统配方，鲜辣爽口，下饭神器',18.00,'250g/瓶',4.6,'/images/products/chili.jpeg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:24:35');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (26,'手工剪纸','华北','手工艺品','全年','剪纸艺人','河北蔚县','传统剪纸艺术，图案精美，装裱成品',58.00,'幅',4.8,'/images/products/papercut.jpeg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:24:38');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (27,'云南普洱茶','西南','乡村特产','春','普洱茶农','云南普洱','陈年普洱，醇厚顺滑，越陈越香',158.00,'357g/饼',4.8,'/images/products/puer.jpeg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:25:29');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (28,'手工绣花鞋','华中','手工艺品','全年','巧手阿婆','湖北恩施','手工绣花，千层底，舒适耐穿',88.00,'双',4.7,'/images/products/shoes.jpeg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:26:22');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (29,'赣南脐橙','华中','时令生鲜','冬','橙农老谢','江西赣州','皮薄肉厚，汁多味甜，橙香浓郁',52.00,'5kg/箱',4.8,'/images/products/navel.jpeg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:56:20');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (30,'手工风筝','华北','手工艺品','春','风筝传人','山东潍坊','传统风筝制作，可放飞可观赏',45.00,'个',4.7,'/images/products/风筝.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:31:55');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (31,'安徽臭鳜鱼','华东','加工食品','全年','徽菜师傅','安徽黄山','传统工艺发酵，风味独特，徽菜代表',68.00,'条',4.6,'/images/products/鳜鱼.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:32:58');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (32,'手工木雕摆件','华东','手工艺品','全年','木雕大师','浙江东阳','精工细作，栩栩如生，收藏佳品',198.00,'件',4.9,'/images/products/木雕.jpeg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:34:24');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (33,'海南椰子','华南','时令生鲜','夏','椰农阿叔','海南文昌','新鲜椰子，汁水清甜，果肉嫩滑',32.00,'2个/袋',4.5,'/images/products/椰子.jpeg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:35:18');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (34,'手工竹编灯笼','华东','手工艺品','全年','灯笼师傅','浙江温州','传统竹编灯笼，喜庆吉祥',48.00,'对',4.7,'/images/products/竹编灯笼.jpeg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:35:53');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (35,'内蒙古奶酪','华北','加工食品','全年','草原牧民','内蒙古呼伦贝尔','纯正奶源，传统工艺，营养丰富',38.00,'250g/袋',4.7,'/images/products/奶酪.jpeg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:39:08');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (36,'手工扎染围巾','西南','手工艺品','全年','扎染艺人','云南大理','传统扎染工艺，天然染料，独特纹样',98.00,'条',4.8,'/images/products/tiescarf.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 19:45:12');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (37,'新疆葡萄干','西北','加工食品','秋','葡萄农','新疆吐鲁番','自然晾晒，颗粒饱满，香甜可口',28.00,'500g/袋',4.6,'/images/products/葡萄干.jpeg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:40:24');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (38,'手工银饰','西南','手工艺品','全年','银匠传人','贵州黔东南','传统手工打造，精美纹饰，苗族风格',158.00,'件',4.9,'/images/products/silver.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 19:45:12');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (39,'福建蜜柚','华东','时令生鲜','秋','柚农老林','福建平和','皮薄肉厚，清甜多汁，柚子中的极品',42.00,'2个/箱',4.7,'/images/products/柚子.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:42:14');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (40,'手工石磨豆腐','华东','加工食品','全年','豆腐西施','安徽淮南','传统石磨工艺，豆香浓郁，嫩滑可口',12.00,'500g/盒',4.8,'/images/products/tofu.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:43:18');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (41,'湖北小龙虾','华中','时令生鲜','夏','虾农老李','湖北潜江','鲜活龙虾，肉质饱满，适合各种烹饪',68.00,'1kg/盒',4.7,'/images/products/小龙虾.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:44:23');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (42,'手工砖雕','华北','手工艺品','全年','砖雕艺人','山西平遥','传统砖雕工艺，古建筑装饰艺术品',268.00,'件',4.9,'/images/products/brick.jpeg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:44:32');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (43,'广西芒果','华南','时令生鲜','夏','芒果大王','广西百色','果肉细腻，香甜多汁，芒果中的贵族',48.00,'2.5kg/箱',4.7,'/images/products/mango.jpeg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:45:59');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (44,'手工铜器','西南','手工艺品','全年','铜匠传人','云南丽江','手工打造，实用与艺术兼具',128.00,'件',4.8,'/images/products/copper.jpeg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:46:18');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (45,'贵州茅台镇酱酒','西南','加工食品','全年','酿酒师傅','贵州茅台镇','传统酱香工艺，醇厚绵长，收藏佳品',298.00,'500ml/瓶',4.9,'/images/products/茅台.jpg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:48:19');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (46,'手工刺绣屏风','华东','手工艺品','全年','苏绣大师','江苏苏州','精美苏绣，双面绣工艺，家居装饰极品',888.00,'件',5.0,'/images/products/刺绣屏风.jpeg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:49:15');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (47,'广东荔枝','华南','时令生鲜','夏','荔枝农','广东增城','果肉晶莹，甜如蜜糖，荔枝中的极品',58.00,'2kg/箱',4.8,'/images/products/荔枝.jpeg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:50:03');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (48,'手工年画','华北','手工艺品','冬','年画传人','天津杨柳青','传统年画工艺，吉祥图案，新年必备',38.00,'张',4.7,'/images/products/年画.jpeg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:51:52');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (49,'重庆火锅底料','西南','加工食品','全年','火锅大师','重庆','传统配方，麻辣鲜香，正宗重庆味',25.00,'500g/袋',4.9,'/images/products/火锅底料.jpeg','/images/user.png',0,100,1,'2026-03-18 19:45:12','2026-03-18 21:52:41');
INSERT INTO `goods` (`goods_id`, `goods_name`, `goods_area`, `goods_category`, `goods_time`, `goods_producer`, `goods_p_address`, `goods_introduce`, `goods_price`, `goods_standard`, `goods_stars`, `goods_picture`, `goods_p_picture`, `sales_count`, `stock`, `is_active`, `created_at`, `updated_at`) VALUES (50,'手工唐卡','西南','手工艺品','全年','唐卡画师','西藏拉萨','传统唐卡绘制，佛教艺术珍品',588.00,'幅',5.0,'/images/products/唐卡.jpeg','/images/user.png',2,98,1,'2026-03-18 19:45:12','2026-09-23 15:37:18');
/*!40000 ALTER TABLE `goods` ENABLE KEYS */;

/*!40000 ALTER TABLE `activities` DISABLE KEYS */;
INSERT INTO `activities` (`activity_id`, `activity_name`, `activity_type`, `activity_theme`, `activity_location`, `activity_address`, `activity_duration`, `activity_price`, `activity_description`, `activity_images`, `max_participants`, `current_booked`, `activity_rating`, `review_count`, `is_active`, `created_at`, `updated_at`) VALUES (1,'农耕体验','农耕体验','亲子,农耕','青山农场','浙江安吉','2.5小时',150.00,'体验种植、采摘等农耕活动，了解有机农业','[\"/images/大棚.jpg\"]',20,0,4.8,0,1,'2026-03-18 14:51:14','2026-03-18 14:51:14');
INSERT INTO `activities` (`activity_id`, `activity_name`, `activity_type`, `activity_theme`, `activity_location`, `activity_address`, `activity_duration`, `activity_price`, `activity_description`, `activity_images`, `max_participants`, `current_booked`, `activity_rating`, `review_count`, `is_active`, `created_at`, `updated_at`) VALUES (2,'果园采摘','果园采摘','亲子,美食','阳光果园','江苏苏州','2小时',120.00,'时令水果采摘，适合家庭亲子活动','[\"/images/村落.jpg\"]',15,0,4.6,0,1,'2026-03-18 14:51:14','2026-03-18 14:51:14');
INSERT INTO `activities` (`activity_id`, `activity_name`, `activity_type`, `activity_theme`, `activity_location`, `activity_address`, `activity_duration`, `activity_price`, `activity_description`, `activity_images`, `max_participants`, `current_booked`, `activity_rating`, `review_count`, `is_active`, `created_at`, `updated_at`) VALUES (3,'乡村美食制作','乡村美食','美食,文化','农家小院','浙江杭州','2小时',180.00,'学习制作传统农家菜，品尝地道乡村美食','[\"/images/丰收节.webp\"]',12,0,4.9,0,1,'2026-03-18 14:51:14','2026-03-18 14:51:14');
INSERT INTO `activities` (`activity_id`, `activity_name`, `activity_type`, `activity_theme`, `activity_location`, `activity_address`, `activity_duration`, `activity_price`, `activity_description`, `activity_images`, `max_participants`, `current_booked`, `activity_rating`, `review_count`, `is_active`, `created_at`, `updated_at`) VALUES (4,'田园骑行','田园骑行','生态,亲子','田园风光带','浙江湖州','2小时',80.00,'沿着乡间小路骑行，欣赏田园风光','[\"/images/蔬菜种植.webp\"]',10,0,4.5,0,1,'2026-03-18 14:51:14','2026-03-18 14:51:14');
INSERT INTO `activities` (`activity_id`, `activity_name`, `activity_type`, `activity_theme`, `activity_location`, `activity_address`, `activity_duration`, `activity_price`, `activity_description`, `activity_images`, `max_participants`, `current_booked`, `activity_rating`, `review_count`, `is_active`, `created_at`, `updated_at`) VALUES (5,'手工艺体验','手工艺体验','文化,亲子','手艺工坊','浙江东阳','3小时',200.00,'学习竹编、陶艺等传统手工艺','[\"/images/民宿.jpg\"]',8,0,4.7,0,1,'2026-03-18 14:51:14','2026-03-18 14:51:14');
/*!40000 ALTER TABLE `activities` ENABLE KEYS */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- 生产者姓名统一替换为匿名标识，避免公开可能涉及个人的姓名。
UPDATE goods SET goods_producer = '示范生产者';

-- 活动可用日期使用相对日期，保证项目导入后仍有可预约场次。
INSERT INTO activity_availability
    (availability_id, activity_id, available_date, available_spots, price_adjustment, is_available)
VALUES
    (1, 1, DATE_ADD(CURDATE(), INTERVAL 7 DAY), 10, 0.00, TRUE),
    (2, 1, DATE_ADD(CURDATE(), INTERVAL 8 DAY), 15, 0.00, TRUE),
    (3, 1, DATE_ADD(CURDATE(), INTERVAL 9 DAY), 20, 0.00, TRUE),
    (4, 2, DATE_ADD(CURDATE(), INTERVAL 7 DAY), 8, 0.00, TRUE),
    (5, 2, DATE_ADD(CURDATE(), INTERVAL 8 DAY), 12, 0.00, TRUE),
    (6, 3, DATE_ADD(CURDATE(), INTERVAL 7 DAY), 6, 0.00, TRUE);

-- 订单数据仅用于演示订单列表和详情页面。
INSERT INTO orders
    (order_id, order_no, user_id, order_type, total_amount, payment_method,
     payment_status, order_status, contact_name, contact_phone, contact_address,
     travel_date, participants, special_requests, created_at, updated_at)
VALUES
    (1, 'DEMO202601010001', 1, '商品', 1176.00, NULL, '待付款', '待处理',
     '演示用户', '13800000001', '演示地址，非真实地址', NULL, 1, NULL,
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'DEMO202601010002', 1, '旅游', 300.00, NULL, '待付款', '待处理',
     '演示用户', '13800000001', NULL, DATE_ADD(CURDATE(), INTERVAL 14 DAY), 2, '无',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO order_items
    (item_id, order_id, item_type, item_id_ref, item_name, quantity, price, subtotal)
VALUES
    (1, 1, '商品', 50, '手工唐卡', 2, 588.00, 1176.00),
    (2, 2, '活动', 1, '农耕体验', 2, 150.00, 300.00);

INSERT INTO notifications
    (notification_id, user_id, title, content, type, is_read, created_at)
VALUES
    (1, 1, '欢迎使用演示平台', '这是一条演示通知，不包含真实用户信息。', '系统', FALSE, CURRENT_TIMESTAMP),
    (2, 1, '演示订单已创建', '你可以在订单中心查看演示订单状态。', '订单', FALSE, CURRENT_TIMESTAMP);

ALTER TABLE users AUTO_INCREMENT = 5;
ALTER TABLE goods AUTO_INCREMENT = 51;
ALTER TABLE activities AUTO_INCREMENT = 6;
ALTER TABLE activity_availability AUTO_INCREMENT = 7;
ALTER TABLE orders AUTO_INCREMENT = 3;
ALTER TABLE order_items AUTO_INCREMENT = 3;
ALTER TABLE favorites AUTO_INCREMENT = 1;
ALTER TABLE notifications AUTO_INCREMENT = 3;
ALTER TABLE reviews AUTO_INCREMENT = 1;

SET FOREIGN_KEY_CHECKS = 1;


