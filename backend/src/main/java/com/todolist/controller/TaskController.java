package com.todolist.controller;
import com.todolist.dto.TaskRequest;
import com.todolist.dto.TaskResponse;
import com.todolist.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 任务控制器，处理任务的CRUD操作
 */
@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    @Autowired
    private TaskService taskService;
    /**
     * 获取所有任务，支持按完成状态过滤
     *
     * @param completed 任务完成状态（可选）
     * @return {@link ResponseEntity<List<TaskResponse>>}
     */
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks(
            @RequestParam(required = false) Boolean completed) {
        List<TaskResponse> response = taskService.getAllTasks(completed);
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
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
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
        TaskResponse response = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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
        return taskService.updateTask(id, request)
                .map(ResponseEntity::ok)
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
        boolean deleted = taskService.deleteTask(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
