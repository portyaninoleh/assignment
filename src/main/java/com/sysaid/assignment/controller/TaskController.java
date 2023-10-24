package com.sysaid.assignment.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sysaid.assignment.domain.Status;
import com.sysaid.assignment.domain.Task;
import com.sysaid.assignment.dtos.TaskStatusDto;
import com.sysaid.assignment.service.TaskServiceImpl;

/**
 * the controller is a basic structure and save some time on "dirty" work.
 */

@RestController
public class TaskController {


	private final TaskServiceImpl taskService;

	/**
	 * constructor for dependency injection
	 * @param taskService
	 */
	public TaskController(TaskServiceImpl taskService) {
		this.taskService = taskService;
	}

	/**
	 * will return uncompleted tasks for given user
	 * @param user the user which the tasks relevant for
	 * @param type type of the task
	 * @return list uncompleted tasks for the user
	 */
	@GetMapping("/uncompleted-tasks/{user}")
	public ResponseEntity<List<Task>> getUncomplitedTasks(@PathVariable ("user") String user, @RequestParam(name = "type",required = false) String type){
		List<Task> tasksByType = taskService.getNotCompletedTasksByType(type);
		return ResponseEntity.ok(tasksByType);
	}

	/**
	 * example for simple API use
	 * @return random task of the day
	 */
	@GetMapping("/task-of-the-day")
	public  ResponseEntity<Task> getTaskOfTheDay(){
		return ResponseEntity.ok(taskService.getTaskOfTheDay());
	}

	@GetMapping("/tasks-by-status/{status}")
	public  ResponseEntity<List<Task>> getTaskOfTheDay(@PathVariable Status status){
		return ResponseEntity.ok(taskService.getTasksByStatus(status));
	}
	
	@GetMapping("/rated-tasks")
	public  ResponseEntity<List<Task>> getRatedTasks(){
		return ResponseEntity.ok(taskService.getRatedTasks());
	}
	
	@PutMapping("/status")
	public ResponseEntity<Boolean> setTaskStatus(@RequestBody TaskStatusDto taskStatusDto) {
		taskService.setTaskStatus(taskStatusDto);
		return ResponseEntity.ok(true);
	}
	
	
}

