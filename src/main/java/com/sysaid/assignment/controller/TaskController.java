package com.sysaid.assignment.controller;

import com.sysaid.assignment.domain.Task;
import com.sysaid.assignment.service.TaskServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
	public ResponseEntity<Task[]> getUncomplitedTasks(@PathVariable ("user") String user, @RequestParam(name = "type",required = false) String type){
		throw  new NotImplementedException();
	}

	/**
	 * example for simple API use
	 * @return random task of the day
	 */
	@GetMapping("/task-of-the-day")
	public  ResponseEntity<Task> getTaskOfTheDay(){
		//example of service use
		return taskService.getRandomTask();
	}


}

