package com.todolist.service;

import com.todolist.dto.TaskRequest;
import com.todolist.dto.TaskResponse;

import java.util.List;
import java.util.Optional;

/**
 * 任务服务接口
 */
public interface TaskService {
    
    /**
     * 获取所有任务，支持按完成状态过滤
     *
     * @param completed 任务完成状态（可选）
     * @return 任务响应列表
     */
    List<TaskResponse> getAllTasks(Boolean completed);
    
    /**
     * 根据ID获取任务
     *
     * @param id 任务ID
     * @return 任务响应（如果存在）
     */
    Optional<TaskResponse> getTaskById(Long id);
    
    /**
     * 创建新任务
     *
     * @param request 任务请求体
     * @return 创建的任务响应
     */
    TaskResponse createTask(TaskRequest request);
    
    /**
     * 更新任务
     *
     * @param id 任务ID
     * @param request 任务请求体
     * @return 更新后的任务响应（如果存在）
     */
    Optional<TaskResponse> updateTask(Long id, TaskRequest request);
    
    /**
     * 删除任务
     *
     * @param id 任务ID
     * @return 是否删除成功
     */
    boolean deleteTask(Long id);
}
