<template>
  <div class="task-list">
    <div class="list-header">
      <h2>任务列表</h2>
      <div class="filter-buttons">
        <button
          @click="setFilter('all')"
          :class="{ active: filter === 'all' }"
        >
          全部 ({{ tasks.length }})
        </button>
        <button
          @click="setFilter('active')"
          :class="{ active: filter === 'active' }"
        >
          进行中 ({{ activeTasks }})
        </button>
        <button
          @click="setFilter('completed')"
          :class="{ active: filter === 'completed' }"
        >
          已完成 ({{ completedTasks }})
        </button>
      </div>
    </div>

    <div v-if="filteredTasks.length === 0" class="empty-state">
      <p>暂无任务</p>
    </div>

    <div v-else class="task-items">
      <TaskItem
        v-for="task in filteredTasks"
        :key="task.id"
        :task="task"
        @toggle="$emit('toggle', $event)"
        @delete="$emit('delete', $event)"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import type { Task } from '../types/task';
import TaskItem from './TaskItem.vue';

const props = defineProps<{
  tasks: Task[];
}>();

defineEmits<{
  toggle: [id: number];
  delete: [id: number];
}>();

type Filter = 'all' | 'active' | 'completed';

const filter = ref<Filter>('all');

const filteredTasks = computed(() => {
  switch (filter.value) {
    case 'active':
      return props.tasks.filter((task) => !task.completed);
    case 'completed':
      return props.tasks.filter((task) => task.completed);
    default:
      return props.tasks;
  }
});

const activeTasks = computed(
  () => props.tasks.filter((task) => !task.completed).length
);

const completedTasks = computed(
  () => props.tasks.filter((task) => task.completed).length
);

const setFilter = (newFilter: Filter) => {
  filter.value = newFilter;
};
</script>

<style scoped>
.task-list {
  background: white;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.list-header {
  margin-bottom: 20px;
}

.list-header h2 {
  margin: 0 0 16px 0;
  font-size: 20px;
  color: #111827;
}

.filter-buttons {
  display: flex;
  gap: 8px;
}

.filter-buttons button {
  padding: 8px 16px;
  background: #f3f4f6;
  color: #4b5563;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
}

.filter-buttons button:hover {
  background: #e5e7eb;
}

.filter-buttons button.active {
  background: #3b82f6;
  color: white;
  border-color: #3b82f6;
}

.empty-state {
  text-align: center;
  padding: 40px 20px;
  color: #9ca3af;
}

.empty-state p {
  margin: 0;
  font-size: 16px;
}

.task-items {
  margin-top: 16px;
}
</style>
