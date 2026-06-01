# Spring Boot 项目通用编码规范

## 1. 项目基础信息
- **Java 版本**: 17 (LTS) 或 21 (LTS)
- **Spring Boot 版本**: 3.2+
- **构建工具**: Maven 或 Gradle

## 2. 核心架构规则
- **分层原则**: 严格遵循 Controller → Service → Repository/Mapper 三层架构。Controller 只负责参数校验、响应封装，**禁止任何业务逻辑**；Service 层处理核心业务；Repository/Mapper 层只做数据访问。
- **依赖注入**: 严格使用**构造器注入**，禁止使用 `@Autowired` 字段注入。这能让依赖关系不可变且显式，提高可测试性[reference:5]。
- **包结构**: 推荐按**功能（Feature）分包**，而不是按技术角色（如 `controller`、`service`）分包[reference:6]。
- **事务管理**: 所有更新操作必须在 Service 层使用 `@Transactional` 注解，并明确指定 `rollbackFor = Exception.class`。
- **日志记录**: 使用 SLF4J 门面 API，通过 `@Slf4j` 注解创建 `log` 对象，**严禁使用 `System.out.println()`**。

## 3. 代码风格与规范
- **命名规范**:
    - Controller: `XxxController`
    - Service 接口: `XxxService`，实现类: `XxxServiceImpl`
    - Repository/Mapper: `XxxRepository` 或 `XxxMapper`
    - DTO: `XxxRequest` / `XxxResponse` 或 `XxxDTO`
- **API 规范**:
    - 严格遵循 **RESTful** 风格。
    - **所有接口必须使用统一的 `Result<T>` 类封装响应**。
- **异常处理**:
    - 使用 `@ControllerAdvice` 实现**全局异常处理**，避免在 Controller 中进行 `try-catch`[reference:7]。
    - 业务异常统一抛出继承自 `RuntimeException` 的自定义异常。

## 4. 测试要求
- 为关键 Service 方法和 Repository 方法编写单元测试。
- 确保新增或修改的代码行覆盖率和分支覆盖率达标。

## 5. 后端接口变更 SOP
- **修改接口后**，确保应用正在运行，然后执行 `mvn springdoc-openapi:generate` 重新生成 `api-docs.yaml`
- 将 `api-docs.yaml` 与代码一起提交到 Git
- 提交信息格式：`feat(api): 变更描述`