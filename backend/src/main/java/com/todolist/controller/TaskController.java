package com.todolist.controller;

import com.todolist.dto.TaskRequest;
import com.todolist.dto.TaskResponse;
import com.todolist.entity.Task;
import com.todolist.repository.TaskRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 任务控制器,处理任务的CRUD操作(由于功能较简单，service层省略，直接在controller中操作repository)
 */
@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    @Autowired
    private TaskRepository taskRepository;

    /**
     * 获取所有任务，支持按完成状态过滤
     *
     * @param completed 任务完成状态（可选）
     * @return {@link ResponseEntity<List<TaskResponse>>}
     */
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks(
            @RequestParam(required = false) Boolean completed) {
        List<Task> tasks;
        if (completed != null) {
            tasks = taskRepository.findByCompletedOrderByCreatedAtDesc(completed);
        } else {
            tasks = taskRepository.findAllByOrderByCreatedAtDesc();
        }

        List<TaskResponse> response = tasks.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * 根据ID获取任务
     *
     * @param id 任务ID
     * @return {@link ResponseEntity<TaskResponse>}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        return taskRepository.findById(id)
                .map(task -> ResponseEntity.ok(convertToResponse(task)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 创建新任务
     *
     * @param request 任务请求体
     * @return {@link ResponseEntity<TaskResponse>}
     */
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(request.getCompleted() != null ? request.getCompleted() : false);

        Task savedTask = taskRepository.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponse(savedTask));
    }

    /**
     * 更新任务
     *
     * @param id      任务ID
     * @param request 任务请求体
     * @return {@link ResponseEntity<TaskResponse>}
     */
    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setTitle(request.getTitle());
                    task.setDescription(request.getDescription());
                    if (request.getCompleted() != null) {
                        task.setCompleted(request.getCompleted());
                    }
                    Task updatedTask = taskRepository.save(task);
                    return ResponseEntity.ok(convertToResponse(updatedTask));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 删除任务
     *
     * @param id 任务ID
     * @return {@link ResponseEntity<Void>}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        return taskRepository.findById(id)
                .map(task -> {
                    taskRepository.delete(task);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private TaskResponse convertToResponse(Task task) {
        TaskResponse response = new TaskResponse();
        BeanUtils.copyProperties(task, response);
        return response;
    }
}
