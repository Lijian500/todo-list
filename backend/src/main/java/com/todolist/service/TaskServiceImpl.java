package com.todolist.service;

import com.todolist.dto.TaskRequest;
import com.todolist.dto.TaskResponse;
import com.todolist.entity.Task;
import com.todolist.repository.TaskRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 任务服务实现类
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public List<TaskResponse> getAllTasks(Boolean completed) {
        List<Task> tasks;
        if (completed != null) {
            tasks = taskRepository.findByCompletedOrderByCreatedAtDesc(completed);
        } else {
            tasks = taskRepository.findAllByOrderByCreatedAtDesc();
        }

        return tasks.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<TaskResponse> getTaskById(Long id) {
        return taskRepository.findById(id)
                .map(this::convertToResponse);
    }

    @Override
    public TaskResponse createTask(TaskRequest request) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setCompleted(request.getCompleted() != null ? request.getCompleted() : false);

        Task savedTask = taskRepository.save(task);
        return convertToResponse(savedTask);
    }

    @Override
    public Optional<TaskResponse> updateTask(Long id, TaskRequest request) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setTitle(request.getTitle());
                    task.setDescription(request.getDescription());
                    if (request.getCompleted() != null) {
                        task.setCompleted(request.getCompleted());
                    }
                    Task updatedTask = taskRepository.save(task);
                    return convertToResponse(updatedTask);
                });
    }

    @Override
    public boolean deleteTask(Long id) {
        return taskRepository.findById(id)
                .map(task -> {
                    taskRepository.delete(task);
                    return true;
                })
                .orElse(false);
    }

    /**
     * 将Task实体转换为TaskResponse
     *
     * @param task 任务实体
     * @return 任务响应
     */
    private TaskResponse convertToResponse(Task task) {
        TaskResponse response = new TaskResponse();
        BeanUtils.copyProperties(task, response);
        return response;
    }
}
