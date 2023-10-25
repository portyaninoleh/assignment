package com.sysaid.assignment.repository;

import java.util.List;

import com.sysaid.assignment.domain.Status;
import com.sysaid.assignment.domain.Task;

public interface TaskRepository {
	Task getRandomTask(String userEmail);
	List<Task> getTasksByStatus(String userEmail, Status status);
	List<Task> getTasksByStatusCounted(String userEmail, Status status, int limit);
	List<Task> getTasksByTypeCounted(String userEmail, String type, int limit);
	void setStatusToTask(String userEmail, String key, Status status);
	void incrementRate(String userEmail, String key, int incRate);
	List<Task> getTasksByRateHighness(String userEmail, int rateHighness);
}
