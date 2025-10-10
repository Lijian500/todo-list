<template>
  <div class="task-form">
    <h2>{{ isEditing ? '编辑任务' : '添加新任务' }}</h2>
    <form @submit.prevent="handleSubmit">
      <div class="form-group">
        <label for="title">标题 *</label>
        <input
          id="title"
          v-model="formData.title"
          type="text"
          placeholder="输入任务标题"
          required
        />
      </div>
      
      <div class="form-group">
        <label for="description">描述</label>
        <textarea
          id="description"
          v-model="formData.description"
          placeholder="输入任务描述（可选）"
          rows="4"
        ></textarea>
      </div>

      <div class="form-group checkbox-group">
        <label>
          <input
            type="checkbox"
            v-model="formData.completed"
          />
          标记为已完成
        </label>
      </div>

      <div class="form-actions">
        <button type="submit" class="btn-primary">
          {{ isEditing ? '更新' : '添加' }}
        </button>
        <button type="button" @click="handleReset" class="btn-secondary">
          重置
        </button>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import type { Task, TaskRequest } from '../types/task';

const props = defineProps<{
  task?: Task;
}>();

const emit = defineEmits<{
  submit: [task: TaskRequest];
}>();

const isEditing = ref(false);
const formData = ref<TaskRequest>({
  title: '',
  description: '',
  completed: false,
});

watch(() => props.task, (newTask) => {
  if (newTask) {
    isEditing.value = true;
    formData.value = {
      title: newTask.title,
      description: newTask.description || '',
      completed: newTask.completed,
    };
  }
}, { immediate: true });

const handleSubmit = () => {
  emit('submit', { ...formData.value });
  handleReset();
};

const handleReset = () => {
  formData.value = {
    title: '',
    description: '',
    completed: false,
  };
  isEditing.value = false;
};
</script>

<style scoped>
.task-form {
  background: white;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 24px;
}

.task-form h2 {
  margin: 0 0 20px 0;
  font-size: 20px;
  color: #111827;
}

.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #374151;
  font-size: 14px;
}

.form-group input[type='text'],
.form-group textarea {
  width: 100%;
  padding: 10px 12px;
  border: 1px solid #d1d5db;
  border-radius: 6px;
  font-size: 14px;
  font-family: inherit;
  transition: border-color 0.2s;
}

.form-group input[type='text']:focus,
.form-group textarea:focus {
  outline: none;
  border-color: #3b82f6;
}

.checkbox-group label {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
}

.checkbox-group input[type='checkbox'] {
  width: 18px;
  height: 18px;
  cursor: pointer;
}

.form-actions {
  display: flex;
  gap: 12px;
  margin-top: 20px;
}

.btn-primary,
.btn-secondary {
  padding: 10px 24px;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.2s;
}

.btn-primary {
  background: #3b82f6;
  color: white;
}

.btn-primary:hover {
  background: #2563eb;
}

.btn-secondary {
  background: #e5e7eb;
  color: #374151;
}

.btn-secondary:hover {
  background: #d1d5db;
}
</style>
