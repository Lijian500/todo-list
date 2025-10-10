import axios from 'axios';
import type { Task, TaskRequest } from '../types/task';

const API_BASE_URL = 'http://localhost:8080/api';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const taskService = {
  getAllTasks: async (completed?: boolean): Promise<Task[]> => {
    const params = completed !== undefined ? { completed } : {};
    const response = await apiClient.get<Task[]>('/tasks', { params });
    return response.data;
  },

  getTaskById: async (id: number): Promise<Task> => {
    const response = await apiClient.get<Task>(`/tasks/${id}`);
    return response.data;
  },

  createTask: async (task: TaskRequest): Promise<Task> => {
    const response = await apiClient.post<Task>('/tasks', task);
    return response.data;
  },

  updateTask: async (id: number, task: TaskRequest): Promise<Task> => {
    const response = await apiClient.put<Task>(`/tasks/${id}`, task);
    return response.data;
  },

  deleteTask: async (id: number): Promise<void> => {
    await apiClient.delete(`/tasks/${id}`);
  },
};
