# Findings

## 已有项目状态
- 旧项目已存在，groupId: `com.heima`，artifactId: `gaokao_school_project`
- Spring Boot parent 版本为 `4.0.6`（需确认是否为有效版本）
- 旧包结构: `com.heima.gaokao_school_project`
- 存在 `application.properties`（需转为 application.yml）
- 仅有一个空的 Application 主类和测试类
- 依赖不完整：缺少 JPA、lombok、validation 等核心依赖
- CLAUDE.md 要求 feature-based 分包、构造器注入、统一响应、全局异常处理
