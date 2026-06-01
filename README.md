# Gaokao Advisor

高考志愿填报助手后端服务，为微信小程序和 Web 端提供院校查询、专业检索、智能推荐、AI 咨询等 API 支持。

## 技术栈

| 技术 | 用途 |
|---|---|
| Spring Boot 3.2.5 | 应用框架 |
| Java 17 | 运行时 |
| MySQL 8 | 数据库 |
| Redis | 验证码缓存 |
| JPA / Hibernate | ORM |
| JWT (jjwt 0.12.5) | 无状态认证 |
| SpringDoc OpenAPI 2.6.0 | API 文档 |
| Zhipu AI (GLM-4.5) | AI 咨询 |
| Maven | 构建 |

## 快速开始

### 环境

JDK 17+, Maven 3.8+, MySQL 8+, Redis 6+

### 配置

```bash
# 创建数据库
mysql -u root -p < sql/init.sql

# 环境变量（或修改 application-dev.yml）
export JWT_SECRET=your-jwt-secret
export WECHAT_APP_ID=your-app-id
export WECHAT_APP_SECRET=your-app-secret
export ZHIPU_API_KEY=your-api-key
```

确保 `application.yml` 中的数据库和 Redis 连接配置正确。

### 运行

```bash
mvn spring-boot:run
# 或打包后运行
mvn clean package -DskipTests && java -jar target/gaokao-advisor-0.0.1-SNAPSHOT.jar
```

服务默认监听 `http://localhost:8080`。

### API 文档

启动后访问：

- Swagger UI：[`http://localhost:8080/swagger-ui/index.html`](http://localhost:8080/swagger-ui/index.html)
- OpenAPI：[`http://localhost:8080/v3/api-docs.yaml`](http://localhost:8080/v3/api-docs.yaml)

接口变更后重新生成文档：

```bash
mvn springdoc-openapi:generate   # 确保应用运行中
```

生成的 `api-docs.yaml` 需与代码一同提交，格式：`feat(api): 变更描述`。

## 项目结构

```
src/main/java/com/gaokao/advisor/
├── auth/              # 认证（微信/手机号登录、JWT）
├── common/            # 基础设施（全局异常、统一响应、安全过滤）
├── user/              # 用户管理
├── school/            # 院校管理
├── major/             # 专业管理
├── recommendation/    # 智能推荐
├── application/       # 志愿管理
├── favorite/          # 院校收藏
└── ai/                # AI 助手（智谱 GLM-4.5）
```

## API 概览

所有接口统一返回 `Result<T>`：

```json
{ "code": 200, "message": "success", "data": {}, "timestamp": 1717200000000 }
```

### 认证

| 方法 | 路径 | 说明 |
|---|---|---|
| POST | `/api/auth/sms` | 发送短信验证码 |
| POST | `/api/auth/register` | 手机号注册 |
| POST | `/api/auth/login/phone` | 手机号登录 |
| POST | `/api/auth/login/wechat` | 微信登录 |
| POST | `/api/auth/login/wechat-phone` | 微信手机号一键登录 |

### 院校

| 方法 | 路径 | 说明 |
|---|---|---|
| GET | `/api/schools` | 分页搜索（关键词/省份/标签） |
| GET | `/api/schools/{id}` | 院校详情 |
| GET | `/api/schools/by-major/{majorId}` | 按专业查院校 |
| POST | `/api/schools` | 新增院校 |

### 专业

| 方法 | 路径 | 说明 |
|---|---|---|
| GET | `/api/majors` | 分页搜索（关键词/学科门类） |
| GET | `/api/majors/{id}` | 专业详情 |
| POST | `/api/majors` | 新增专业 |

### 收藏

| 方法 | 路径 | 说明 |
|---|---|---|
| POST | `/api/favorites/schools/{schoolId}` | 收藏 |
| DELETE | `/api/favorites/schools/{schoolId}` | 取消收藏 |
| GET | `/api/favorites/schools` | 收藏列表 |
| GET | `/api/favorites/schools/check/{schoolId}` | 检查状态 |

### 志愿

| 方法 | 路径 | 说明 |
|---|---|---|
| POST | `/api/applications` | 添加志愿 |
| GET | `/api/applications/user/{userId}` | 用户志愿列表 |
| PUT | `/api/applications/{id}/status` | 更新状态 |

### 推荐

| 方法 | 路径 | 说明 |
|---|---|---|
| POST | `/api/recommendations` | 生成推荐（冲刺/稳妥/保底） |
| GET | `/api/recommendations/history/{userId}` | 推荐历史 |

### AI

| 方法 | 路径 | 说明 |
|---|---|---|
| POST | `/api/ai/chat` | AI 对话（可携带成绩信息） |

### 用户

| 方法 | 路径 | 说明 |
|---|---|---|
| GET | `/api/users/{id}` | 用户信息 |
| PUT | `/api/users/{id}/scores` | 更新成绩 |
| POST | `/api/users/register` | 用户名密码注册 |

## 认证

除 `/api/auth/**` 和 Swagger 路径外，接口需携带 JWT：

```
Authorization: Bearer <token>
```

Token 有效期 24 小时。

## 数据库

| 表 | 说明 |
|---|---|
| `gaokao_user` | 用户 |
| `gaokao_school` | 院校 |
| `gaokao_school_tag` | 院校标签 |
| `gaokao_major` | 专业 |
| `gaokao_recommendation` | 推荐记录 |
| `gaokao_application` | 志愿 |
| `gaokao_school_favorite` | 收藏 |

## 相关项目

| 项目 | 地址 |
|---|---|
| Web 前端 | [lvmou-123/gaokao_school_project-web](https://github.com/lvmou-123/gaokao_school_project-web) |
| 微信小程序端 | [lvmou-123/gaokao_school_project-app](https://github.com/lvmou-123/gaokao_school_project-app) |
