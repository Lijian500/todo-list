<template>
  <div class="task-item" :class="{ completed: task.completed }">
    <div class="task-content">
      <div class="task-checkbox">
        <input
          type="checkbox"
          :checked="task.completed"
          @change="$emit('toggle', task.id!)"
        />
      </div>
      <div class="task-details">
        <h3>{{ task.title }}</h3>
        <p v-if="task.description" class="task-description">
          {{ task.description }}
        </p>
        <div class="task-meta">
          <span class="task-date">
            创建于: {{ formatDate(task.createdAt!) }}
          </span>
        </div>
      </div>
    </div>
    <div class="task-actions">
      <button @click="$emit('delete', task.id!)" class="btn-delete">
        删除
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { Task } from '../types/task';

defineProps<{
  task: Task;
}>();

defineEmits<{
  toggle: [id: number];
  delete: [id: number];
}>();

const formatDate = (dateStr: string): string => {
  const date = new Date(dateStr);
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
  });
};
</script>

<style scoped>
.task-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  background: white;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  margin-bottom: 12px;
  transition: all 0.3s ease;
}

.task-item:hover {
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.task-item.completed {
  opacity: 0.7;
  background: #f9fafb;
}

.task-content {
  display: flex;
  flex: 1;
  gap: 12px;
}

.task-checkbox input[type='checkbox'] {
  width: 20px;
  height: 20px;
  cursor: pointer;
  margin-top: 4px;
}

.task-details {
  flex: 1;
}

.task-details h3 {
  margin: 0 0 8px 0;
  font-size: 16px;
  font-weight: 600;
  color: #111827;
}

.task-item.completed h3 {
  text-decoration: line-through;
  color: #6b7280;
}

.task-description {
  margin: 0 0 8px 0;
  font-size: 14px;
  color: #6b7280;
  line-height: 1.5;
}

.task-meta {
  font-size: 12px;
  color: #9ca3af;
}

.task-actions {
  display: flex;
  gap: 8px;
}

.btn-delete {
  padding: 6px 16px;
  background: #ef4444;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: background 0.2s;
}

.btn-delete:hover {
  background: #dc2626;
}
</style>
