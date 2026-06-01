# 高考志愿填报小程序 - 后端项目初始化

## 目标
使用 Spring Boot 3.2+ (Java 17) 初始化高考志愿填报小程序后端项目，包名 `com.gaokao.advisor`，遵循 CLAUDE.md 规范。

## 阶段

| 阶段 | 状态 | 描述 |
|------|------|------|
| 1-6 | done | 项目框架、基础设施、构建验证 |
| 7. 用户登录注册 | done | Auth 模块: JWT + 手机验证码 + 微信登录 |
| 8. 院校查询 | done | 分页搜索 + 标签筛选 + 182 所学校预置 |
| 9. 专业查询 | done | 分页 + 模糊搜索 + 241 个专业预置 |
| 10. AI 助手 | done | ZhipuAI GLM-4.5 对接 |

## 决策记录
- **Spring Boot 版本**: 3.2.5
- **包结构**: Feature-based 分包
- **ORM**: Spring Data JPA
- **依赖注入**: 构造器注入
- **响应封装**: 统一 `Result<T>`
- **AI 实现**: RestTemplate 直连 ZhipuAI v4 API（OpenAI 兼容格式）
- **AI 模型**: glm-4.5

## 项目结构
```
com.gaokao.advisor
├── ai                   ← 新增 AI 助手模块
├── auth
├── common
├── user
├── school
├── major
├── recommendation
└── application
```
