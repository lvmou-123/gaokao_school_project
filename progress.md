# Progress Log

## 2026-05-29 Session 1
- 项目初始化 & 基础框架 & 统一响应 & 全局异常

## 2026-05-29 Session 2
- Task 1: 用户登录注册 (Auth + JWT + 短信/微信)
- Task 2: 院校查询 (标签 + 分页 + 182 所学校)
- Task 3: 专业查询 (模糊搜索 + 分页 + 241 个专业)

## 2026-05-29 Session 2 — AI 助手 (Task 4)
- 创建 ai 模块: controller/service/dto/config
- AiProperties: ZhipuAI API 配置 + 高考志愿 system prompt
- AiServiceImpl: 通过 RestTemplate 调用 ZhipuAI v4 API
- ChatRequest: 支持 message + 可选的 score/rankNum/province 上下文
- ChatResponse: 返回 reply + model
- API: `POST /api/ai/chat` — 发送问题，获取 AI 建议
- 验证: `mvn compile` + `mvn test` 通过
