package com.sysaid.assignment.service;

import java.util.List;
import java.util.Random;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sysaid.assignment.domain.Status;
import com.sysaid.assignment.domain.Task;
import com.sysaid.assignment.dtos.TaskStatusDto;
import com.sysaid.assignment.repository.TaskRepository;

@Service
public class TaskServiceImpl implements  ITaskService{

	// It could be taken from user request but as far as it is mentioned in the requirements
	// I think it can be hardcoded here
	private final static Integer USER_TASKS_SHOW_LIMIT = 10;
	private final static Integer RATE_INCREMENT_FOR_WISH_LIST = 1;
	private final static Integer RATE_INCREMENT_FOR_COMPLETE = 2;
	private final static Integer RATE_LEVEL_ONE = 0;
	private final static Integer RATE_LEVEL_TWO = 1;
	private final static Integer RATE_LEVEL_THREE = 2;
	private final static Integer RATE_LEVEL_FOUR = 3;
	private final static Integer RATE_LEVEL_FIVE = 4;
	private final static Integer NO_RATE_LEVEL = -1;
	
	private TaskRepository taskRepository;
	
    public TaskServiceImpl(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

    private String getUserEmail() {
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	return authentication.getName();
    }
    
	@Override
	public List<Task> getNotCompletedTasksByType(String type) {
		return taskRepository.getTasksByTypeCounted(getUserEmail(), type, USER_TASKS_SHOW_LIMIT);
	}

	@Override
	public void setTaskStatus(TaskStatusDto taskStatusDto) {
		taskRepository.setStatusToTask(getUserEmail(), taskStatusDto.getKey(), taskStatusDto.getStatus());
		if (taskStatusDto.getStatus() == Status.WHISH_LIST) {
			taskRepository.incrementRate(getUserEmail(), taskStatusDto.getKey(), RATE_INCREMENT_FOR_WISH_LIST);
		}
		if (taskStatusDto.getStatus() == Status.COMPLETED) {
			taskRepository.incrementRate(getUserEmail(), taskStatusDto.getKey(), RATE_INCREMENT_FOR_COMPLETE);
		}
	}

	/**
	 * This service is responsible for getting wishlisted and completed tasks
	 */
	@Override
	public List<Task> getTasksByStatus(Status status) {
		return taskRepository.getTasksByStatus(getUserEmail(), status);
	}

	@Override
	public Task getTaskOfTheDay() {
		return taskRepository.getRandomTask(getUserEmail());
	}

	@Override
	public List<Task> getRatedTasks() {
		Integer rateHighness = getRateHighness();
		// It is not completely understandable if I should return here all rated tasks
		// of the corresponding level, or only one of them. That's why I'm returning all
		// but in case when I shouldn't return the rated tasks, I should return only 
		// one task (as it is mentioned in requirements). Probably it is some typo, it sounds 
		// strange for me, but still I'll keep supporting of requirements.
		if (rateHighness.equals(NO_RATE_LEVEL)) {
			return List.of(taskRepository.getRandomTask(getUserEmail()));
		}
		return taskRepository.getTasksByRateHighness(getUserEmail(), rateHighness);
	}
    
	private Integer getRateHighness() {
		Random random = new Random();
		int randRateHighness = random.ints(1, 101).findFirst().getAsInt();
		if (randRateHighness <= 20) {
			return RATE_LEVEL_ONE;
		}
		if (randRateHighness > 20 && randRateHighness <= 40) {
			return RATE_LEVEL_TWO;
		}
		if (randRateHighness > 40 && randRateHighness <= 50) {
			return RATE_LEVEL_THREE;
		}
		if (randRateHighness > 50 && randRateHighness <= 55) {
			return RATE_LEVEL_FOUR;
		}
		if (randRateHighness > 55 && randRateHighness <= 60) {
			return RATE_LEVEL_FIVE;
		}
		return NO_RATE_LEVEL;
	}
}
