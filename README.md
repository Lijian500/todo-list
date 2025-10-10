# Todo List - 任务管理系统

一个简单而完整的任务管理系统，使用现代技术栈构建。

## 技术栈

### 前端
- **Vue 3** - 渐进式 JavaScript 框架
- **TypeScript** - JavaScript 的超集，提供类型安全
- **Vite** - 下一代前端构建工具
- **Axios** - HTTP 客户端

### 后端
- **Spring Boot 3** - Java 企业级应用框架
- **Spring Data JPA** - 数据持久层
- **Lombok** - 简化 Java 代码

### 数据库
- **MySQL 8** - 关系型数据库

## 功能特性

- ✅ 创建任务
- ✅ 查看任务列表
- ✅ 标记任务为完成/未完成
- ✅ 删除任务
- ✅ 按状态筛选任务（全部/进行中/已完成）
- ✅ 响应式设计

## 项目结构

```
todo-list/
├── backend/               # Spring Boot 后端
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/todolist/
│   │   │   │       ├── controller/    # REST API 控制器
│   │   │   │       ├── entity/        # JPA 实体
│   │   │   │       ├── repository/    # 数据访问层
│   │   │   │       └── dto/          # 数据传输对象
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── pom.xml
├── frontend/              # Vue 3 前端
│   ├── src/
│   │   ├── components/    # Vue 组件
│   │   ├── services/      # API 服务
│   │   ├── types/         # TypeScript 类型定义
│   │   ├── App.vue
│   │   └── main.ts
│   ├── package.json
│   └── vite.config.ts
└── docker-compose.yml     # Docker 配置
```

## 快速开始

### 前置要求

- Java 17 或更高版本
- Node.js 16 或更高版本
- Maven 3.6 或更高版本
- Docker 和 Docker Compose（用于运行 MySQL）

### 1. 启动数据库

使用 Docker Compose 启动 MySQL：

```bash
docker-compose up -d
```

这将启动一个 MySQL 8 容器，配置如下：
- 端口: 3306
- 数据库名: todolist
- 用户名: root
- 密码: root123

### 2. 启动后端

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

后端将在 `http://localhost:8080` 运行。

### 3. 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端将在 `http://localhost:5173` 运行。

### 4. 访问应用

打开浏览器访问 `http://localhost:5173`

## API 端点

### 任务管理

| 方法 | 端点 | 描述 |
|------|------|------|
| GET | `/api/tasks` | 获取所有任务 |
| GET | `/api/tasks?completed=true` | 获取已完成的任务 |
| GET | `/api/tasks?completed=false` | 获取未完成的任务 |
| GET | `/api/tasks/{id}` | 获取单个任务 |
| POST | `/api/tasks` | 创建新任务 |
| PUT | `/api/tasks/{id}` | 更新任务 |
| DELETE | `/api/tasks/{id}` | 删除任务 |

### 请求示例

**创建任务:**
```json
POST /api/tasks
{
  "title": "完成项目文档",
  "description": "编写项目的 README 文档",
  "completed": false
}
```

**更新任务:**
```json
PUT /api/tasks/1
{
  "title": "完成项目文档",
  "description": "编写项目的 README 文档",
  "completed": true
}
```

## 开发

### 后端开发

后端使用 Spring Boot 3 和 Java 17。主要组件：

- **Controller**: 处理 HTTP 请求
- **Service**: 业务逻辑（目前简化，直接在 Controller 中）
- **Repository**: 数据访问层
- **Entity**: JPA 实体类

### 前端开发

前端使用 Vue 3 组合式 API 和 TypeScript。主要组件：

- **TaskForm.vue**: 任务表单组件
- **TaskList.vue**: 任务列表组件
- **TaskItem.vue**: 单个任务项组件

### 构建生产版本

**后端:**
```bash
cd backend
mvn clean package
java -jar target/todo-list-backend-1.0.0.jar
```

**前端:**
```bash
cd frontend
npm run build
```

构建输出在 `frontend/dist` 目录。

## 故障排除

### 数据库连接失败

确保 MySQL 容器正在运行：
```bash
docker-compose ps
```

如果未运行，启动它：
```bash
docker-compose up -d
```

### 后端启动失败

检查 Java 版本（需要 Java 17+）：
```bash
java -version
```

### 前端无法连接后端

确保后端正在运行在 `http://localhost:8080`，并检查浏览器控制台的错误信息。

## License

MIT
