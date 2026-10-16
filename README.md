# 乡创农游智能体平台 - 重构项目文档

## 项目概述

本项目是**乡创农游智能体平台**的重构版本，原项目基于 Node.js + Express + EJS 模板引擎开发，现已重构为**前后端分离架构**：

- **前端**：Vue 3 + Vite + Element Plus + Pinia + Vue Router
- **后端**：Spring Boot 3 + MyBatis-Plus + JWT + MySQL

重构保留了原项目的全部核心功能，包括：用户认证、农品市集、旅游定制、资讯指南等模块。

---

## 技术栈

### 后端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 17+ | 编程语言（已在 JDK 21 下验证） |
| Spring Boot | 3.2.5 | 后端框架 |
| MyBatis-Plus | 3.5.5 | ORM框架 |
| MySQL | 8.0+ | 数据库 |
| JWT (jjwt) | 0.12.5 | 身份认证 |
| Lombok | 1.18.32 | 代码简化 |
| Spring Validation | - | 参数校验 |

### 前端技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.4.21 | 前端框架 |
| Vite | 5.2.0 | 构建工具 |
| Element Plus | 2.6.1 | UI组件库 |
| Pinia | 2.1.7 | 状态管理 |
| Vue Router | 4.3.0 | 路由管理 |
| Axios | 1.6.8 | HTTP客户端 |
| Sass | 1.72.0 | CSS预处理器 |

---

## 项目结构

```
XiangchuangPlatform(java)/
├── sql/                       # SQL初始化脚本
│   ├── init.sql               # 数据库和表结构
│   └── data.sql               # 脱敏后的演示数据
├── backend/                   # Spring Boot后端
│   ├── pom.xml                # Maven依赖配置
│   └── src/main/
│       ├── java/com/xiangchuang/
│       │   ├── XiangchuangPlatformApplication.java  # 启动类
│       │   ├── config/        # 配置类
│       │   │   ├── CorsConfig.java         # 跨域配置
│       │   │   ├── JwtConfig.java          # JWT配置
│       │   │   └── MybatisPlusConfig.java  # MyBatis-Plus配置
│       │   ├── controller/    # REST控制器
│       │   │   ├── AuthController.java     # 认证接口
│       │   │   ├── ProductController.java  # 商品接口
│       │   │   └── ActivityController.java # 活动接口
│       │   ├── service/       # 服务层
│       │   │   ├── AuthService.java
│       │   │   ├── ProductService.java
│       │   │   ├── ActivityService.java
│       │   │   └── impl/      # 服务实现类
│       │   ├── mapper/        # 数据访问层（MyBatis-Plus）
│       │   │   ├── UserMapper.java
│       │   │   ├── GoodsMapper.java
│       │   │   ├── ActivityMapper.java
│       │   │   └── ReviewMapper.java
│       │   ├── entity/        # 实体类（与数据库表映射）
│       │   │   ├── User.java
│       │   │   ├── Goods.java
│       │   │   ├── Activity.java
│       │   │   ├── Review.java
│       │   │   ├── Order.java
│       │   │   ├── Favorite.java
│       │   │   └── Notification.java
│       │   ├── dto/           # 数据传输对象
│       │   │   ├── request/   # 请求DTO
│       │   │   └── response/  # 响应DTO
│       │   └── util/          # 工具类
│       │       ├── JwtUtil.java     # JWT工具
│       │       └── PasswordUtil.java # 密码加密
│       └── resources/
│           ├── application.yml       # 本地配置（已忽略，不提交）
│           └── application.yml.example  # 配置模板
└── frontend/                   # Vue3前端
    ├── index.html             # HTML入口
    ├── package.json           # npm依赖
    ├── vite.config.js         # Vite配置
    ├── public/                # 静态资源
    │   └── images/            # 产品图片和其他图片资源
    └── src/
        ├── main.js            # 应用入口
        ├── App.vue            # 根组件
        ├── style.css          # 全局样式
        ├── router/            # 路由配置
        │   └── index.js
        ├── stores/            # Pinia状态管理
        │   └── auth.js        # 用户认证状态
        ├── api/               # API接口封装
        │   └── index.js
        ├── components/        # 公共组件
        │   ├── Header.vue     # 顶部导航
        │   └── Footer.vue     # 底部信息
        └── views/             # 页面视图
            ├── Home.vue       # 首页
            ├── Market.vue     # 农品市集
            ├── Travel.vue     # 旅游定制
            ├── Guide.vue      # 资讯指南
            ├── Account.vue    # 个人账户
            ├── Login.vue      # 登录页面
            ├── Register.vue   # 注册页面
            ├── ProductDetail.vue  # 商品详情
            ├── ActivityDetail.vue # 活动详情
            ├── Orders.vue     # 订单列表
            ├── Favorites.vue  # 收藏列表
            ├── Cart.vue       # 购物车
            └── NotFound.vue   # 404页面
```

---

## 数据库设计

### 核心数据表

| 表名 | 说明 |
|------|------|
| `users` | 用户表 |
| `goods` | 商品表 |
| `activities` | 活动表 |
| `reviews` | 评价表 |
| `orders` | 订单表 |
| `order_items` | 订单项表 |
| `favorites` | 收藏表 |
| `notifications` | 通知表 |
| `activity_availability` | 活动可用日期表 |

### 数据库连接配置

仓库不保存真实数据库密码或 JWT 密钥。复制配置模板后，在本地
`backend/src/main/resources/application.yml` 中填写数据库账号密码，
或通过 `DB_URL`、`DB_USERNAME`、`DB_PASSWORD`、`JWT_SECRET` 环境变量覆盖：

```bash
cp backend/src/main/resources/application.yml.example backend/src/main/resources/application.yml
```

---

## API接口说明

### 认证模块 (`/api/auth`)

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/auth/login` | 用户登录 |
| POST | `/api/auth/register` | 用户注册 |
| POST | `/api/auth/logout` | 用户登出 |
| GET | `/api/auth/check` | 检查登录状态 |
| GET | `/api/auth/me` | 获取当前用户信息 |

### 商品模块 (`/api/products`)

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/products` | 获取商品列表（支持分页、筛选、排序） |
| GET | `/api/products/recommendations` | 获取推荐商品 |
| GET | `/api/products/hot` | 获取热门商品 |
| GET | `/api/products/search` | 搜索商品 |
| GET | `/api/products/{id}` | 获取商品详情 |
| GET | `/api/products/{id}/reviews` | 获取商品评价 |
| POST | `/api/products/{id}/reviews` | 添加商品评价 |
| GET | `/api/products/filters/all` | 获取筛选选项 |

### 活动模块 (`/api/activities`)

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/activities` | 获取活动列表 |
| GET | `/api/activities/theme/{theme}` | 按主题获取活动 |
| POST | `/api/activities/calculate` | 计算活动费用 |
| GET | `/api/activities/recommendations` | 获取推荐活动 |
| GET | `/api/activities/{id}` | 获取活动详情 |
| GET | `/api/activities/{id}/availability` | 检查活动日期可用性 |
| GET | `/api/activities/{id}/available-dates` | 获取活动可用日期 |
| GET | `/api/activities/{id}/reviews` | 获取活动评价 |
| POST | `/api/activities/{id}/reviews` | 添加活动评价 |

---

## 启动方式

### 环境要求

- JDK 17+
- MySQL 8.0+
- Node.js 18+

### 后端启动

```bash
# 进入后端目录
cd backend

# 首次运行：复制配置模板并填写本地数据库密码
cp src/main/resources/application.yml.example src/main/resources/application.yml

# 使用Maven启动
mvn spring-boot:run

# 或打包后运行
mvn clean package
java -jar target/xiangchuang-platform-1.0.0.jar
```

服务地址：http://localhost:8080

### 前端启动

```bash
# 进入前端目录
cd frontend

# 安装依赖（首次运行）
npm install

# 启动开发服务器
npm run dev

# 构建生产版本
npm run build
```

服务地址：http://localhost:5173

### 前端代理配置

前端通过 Vite 代理将 `/api` 请求转发到后端：

```javascript
// vite.config.js
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true
    }
  }
}
```

---

## 核心功能

### 1. 用户认证系统
- 手机号+密码登录
- 用户注册
- JWT令牌认证
- 登录状态检查

### 2. 农品市集
- 商品列表展示
- 商品搜索与筛选（分类、产地、时令、价格区间）
- 商品详情查看
- 商品评价管理

### 3. 旅游定制
- 活动列表展示
- 活动选择与预订
- 费用智能计算（活动费+服务费+交通费）
- 参与人数选择

### 4. 个人中心
- 用户信息管理
- 订单管理
- 收藏管理
- 购物车管理

### 5. 资讯指南
- 资讯分类浏览
- 热门文章推荐
- 搜索功能

---

## 关键特性

- **前后端分离**：前端Vue3 + 后端Spring Boot
- **JWT认证**：无状态身份认证机制
- **RESTful API**：规范的接口设计
- **统一响应格式**：`ApiResponse<T>` 封装
- **分页支持**：MyBatis-Plus分页插件
- **跨域处理**：CORS配置
- **密码加密**：BCrypt加密存储

---

## 开发注意事项

1. 先执行 `sql/init.sql`，再执行 `sql/data.sql`
2. 后端启动前需确保数据库连接和 JWT 密钥配置正常
3. 前端开发时后端服务需同时运行，否则 API 调用会失败
4. 演示账号密码仅用于本地运行，部署到公网前必须修改

---

## 已知问题与待完善项

### 后端
- ✅ **缺少日志配置**：生产环境建议配置日志文件输出

### 前端
- ✅ **Mock数据**：以下页面使用硬编码mock数据，尚未对接真实API：
  - `Orders.vue` - 订单列表
  - `Favorites.vue` - 收藏列表
  - `Cart.vue` - 购物车
  - `Guide.vue` - 资讯指南
- ✅ **图片路径**：商品图片存储在 `public/images/products/`，需确保后端API返回的图片路径与此一致
- ✅ **dist目录**：已包含构建产物，建议在提交前删除

### 数据库
- ✅ **SQL脚本**：`sql/init.sql` 包含表结构，`sql/data.sql` 包含脱敏演示数据
- ✅ **表结构兼容**：确保 MySQL 8.0+ 版本兼容性

### 安全
- ✅ **敏感配置**：数据库密码和 JWT 密钥使用环境变量覆盖，本地 `application.yml` 已加入 `.gitignore`
- ✅ **公开数据**：SQL 演示数据和联系方式均为合成数据，不包含真实用户隐私

---

## 数据库初始化

在启动后端前，依次执行表结构和脱敏数据脚本：

```bash
# 登录 MySQL
mysql -u root -p

# 执行表结构
source sql/init.sql;

# 执行脱敏演示数据
source sql/data.sql;
```

### 演示账号

所有演示账号的初始密码均为 `password`：

| 身份 | 手机号 |
|------|--------|
| 普通用户 | `13800000001` |
| 农户 | `13800000002` |
| 商家 | `13800000003` |
| 管理员 | `13800000000` |

这些账号仅用于本地演示，不对应任何真实用户。
