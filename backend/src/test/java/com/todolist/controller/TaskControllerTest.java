package com.todolist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todolist.entity.Task;
import com.todolist.repository.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Task buildTask(Long id, String title, String description, boolean completed) {
        Task t = new Task();
        t.setId(id);
        t.setTitle(title);
        t.setDescription(description);
        t.setCompleted(completed);
        return t;
    }

    @Test
    @DisplayName("GET /api/tasks - 获取所有任务 & 按完成状态过滤")
    void getAllTasks() throws Exception {
        List<Task> all = Arrays.asList(
                buildTask(2L, "t2", "d2", false),
                buildTask(1L, "t1", "d1", true)
        );
        when(taskRepository.findAllByOrderByCreatedAtDesc()).thenReturn(all);

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].title").value("t2"))
                .andExpect(jsonPath("$[0].description").value("d2"))
                .andExpect(jsonPath("$[0].completed").value(false));

        List<Task> completedTrue = List.of(buildTask(3L, "t3", "d3", true));
        when(taskRepository.findByCompletedOrderByCreatedAtDesc(true)).thenReturn(completedTrue);

        mockMvc.perform(get("/api/tasks").param("completed", "true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(3))
                .andExpect(jsonPath("$[0].completed").value(true));
    }

    @Test
    @DisplayName("GET /api/tasks/{id} - 根据ID获取任务")
    void getTaskById() throws Exception {
        Task task = buildTask(10L, "title", "desc", false);
        when(taskRepository.findById(10L)).thenReturn(Optional.of(task));

        mockMvc.perform(get("/api/tasks/{id}", 10))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.description").value("desc"))
                .andExpect(jsonPath("$.completed").value(false));

        when(taskRepository.findById(404L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/tasks/{id}", 404))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/tasks - 创建任务")
    void createTask() throws Exception {
        // 请求体
        var request = new java.util.HashMap<String, Object>();
        request.put("title", "new title");
        request.put("description", "new desc");
        request.put("completed", true);

        // save 返回持久化后的实体
        Task saved = buildTask(100L, "new title", "new desc", true);
        when(taskRepository.save(ArgumentMatchers.any(Task.class))).thenReturn(saved);

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.title").value("new title"))
                .andExpect(jsonPath("$.description").value("new desc"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    @DisplayName("PUT /api/tasks/{id} - 更新任务")
    void updateTask() throws Exception {
        Task existing = buildTask(7L, "old", "old desc", false);
        when(taskRepository.findById(7L)).thenReturn(Optional.of(existing));

        Task updated = buildTask(7L, "new", "new desc", true);
        when(taskRepository.save(ArgumentMatchers.any(Task.class))).thenReturn(updated);

        var request = new java.util.HashMap<String, Object>();
        request.put("title", "new");
        request.put("description", "new desc");
        request.put("completed", true);

        mockMvc.perform(put("/api/tasks/{id}", 7)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(7))
                .andExpect(jsonPath("$.title").value("new"))
                .andExpect(jsonPath("$.description").value("new desc"))
                .andExpect(jsonPath("$.completed").value(true));

        when(taskRepository.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/tasks/{id}", 999)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/tasks/{id} - 删除任务")
    void deleteTask() throws Exception {
        Task existing = buildTask(5L, "to del", "desc", false);
        when(taskRepository.findById(5L)).thenReturn(Optional.of(existing));

        mockMvc.perform(delete("/api/tasks/{id}", 5))
                .andExpect(status().isNoContent());

        verify(taskRepository, times(1)).delete(existing);

        when(taskRepository.findById(888L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/tasks/{id}", 888))
                .andExpect(status().isNotFound());
    }
}