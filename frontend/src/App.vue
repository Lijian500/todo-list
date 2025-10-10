<script setup lang="ts">
import { ref, onMounted } from 'vue';
import TaskForm from './components/TaskForm.vue';
import TaskList from './components/TaskList.vue';
import { taskService } from './services/taskService';
import type { Task, TaskRequest } from './types/task';

const tasks = ref<Task[]>([]);
const loading = ref(false);
const error = ref<string | null>(null);

const loadTasks = async () => {
  try {
    loading.value = true;
    error.value = null;
    tasks.value = await taskService.getAllTasks();
  } catch (err) {
    error.value = '加载任务失败。请确保后端服务正在运行。';
    console.error('Error loading tasks:', err);
  } finally {
    loading.value = false;
  }
};

const handleSubmit = async (taskData: TaskRequest) => {
  try {
    error.value = null;
    await taskService.createTask(taskData);
    await loadTasks();
  } catch (err) {
    error.value = '创建任务失败';
    console.error('Error creating task:', err);
  }
};

const handleToggle = async (id: number) => {
  try {
    error.value = null;
    const task = tasks.value.find((t) => t.id === id);
    if (task) {
      await taskService.updateTask(id, {
        title: task.title,
        description: task.description,
        completed: !task.completed,
      });
      await loadTasks();
    }
  } catch (err) {
    error.value = '更新任务失败';
    console.error('Error updating task:', err);
  }
};

const handleDelete = async (id: number) => {
  if (confirm('确定要删除这个任务吗？')) {
    try {
      error.value = null;
      await taskService.deleteTask(id);
      await loadTasks();
    } catch (err) {
      error.value = '删除任务失败';
      console.error('Error deleting task:', err);
    }
  }
};

onMounted(() => {
  loadTasks();
});
</script>

<template>
  <div class="app-container">
    <header class="app-header">
      <h1>任务管理系统</h1>
      <p>使用 Vue 3 + TypeScript + Spring Boot 3</p>
    </header>

    <main class="app-main">
      <div v-if="error" class="error-message">
        {{ error }}
      </div>

      <div v-if="loading" class="loading">
        加载中...
      </div>

      <div v-else class="content">
        <TaskForm @submit="handleSubmit" />
        <TaskList
          :tasks="tasks"
          @toggle="handleToggle"
          @delete="handleDelete"
        />
      </div>
    </main>
  </div>
</template>

<style scoped>
.app-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.app-header {
  text-align: center;
  color: white;
  margin-bottom: 32px;
  padding: 20px 0;
}

.app-header h1 {
  margin: 0 0 8px 0;
  font-size: 36px;
  font-weight: 700;
}

.app-header p {
  margin: 0;
  font-size: 16px;
  opacity: 0.9;
}

.app-main {
  max-width: 800px;
  margin: 0 auto;
}

.error-message {
  background: #fef2f2;
  color: #dc2626;
  padding: 12px 16px;
  border-radius: 8px;
  margin-bottom: 16px;
  border: 1px solid #fecaca;
}

.loading {
  text-align: center;
  color: white;
  font-size: 18px;
  padding: 40px;
}

.content {
  animation: fadeIn 0.3s ease-in;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
