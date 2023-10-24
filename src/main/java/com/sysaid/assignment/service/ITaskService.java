package com.sysaid.assignment.service;

import java.util.List;

import com.sysaid.assignment.domain.Status;
import com.sysaid.assignment.domain.Task;
import com.sysaid.assignment.dtos.TaskStatusDto;

public interface ITaskService {
    List<Task> getNotCompletedTasksByType(String type);
    void setTaskStatus(TaskStatusDto taskStatusDto);
    List<Task> getTasksByStatus(Status status);
    Task getTaskOfTheDay();
    List<Task> getRatedTasks();
}
