package com.sysaid.assignment.repository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.sysaid.assignment.domain.Status;
import com.sysaid.assignment.domain.Task;

@Repository
public class TaskRepositoryImpl implements TaskRepository {
	/** 
	 * Not sure this class is needed as far as we do not have neither SQL nor NoSQL DB. 
	 * But still we have to work with some data and that is the reason I decided to 
	 * implement DDD pattern, so the logic of accessing to the data should be separated
	 * from the business logic.
	 * 
	 * Also it is a question were should we implement the caching logic. Normally 
	 * it should be implemented on the service layer. But in our case it should 
	 * be discussed because we are simulating some data store and thus I think
	 * it is acceptable to place it here.
	 */
	private final static Map<String, List<Task>> TASKS = Map.of();
    private final String RANDOM_TASK_PATTERN = "%s/activity";
    private final String TASK_BY_TYPE_PATTERN = "%s/activity?type=%s";
    
	@Value("${external.boredapi.baseURL}")
    private String baseUrl;

	/**
	 * This implementation suppose to get random task ONLY from already cached
	 * tasks. So if we have only one cached task, we will get it each time. 
	 * Not sure it is correct behavior but the requirements say that we have to avoid
	 * any extra requests to the https://www.boredapi.com/ service. So I wouldn't
	 * say that it is some bug or misunderstanding, it is just a place for enhancement.
	 * 
	 * Also probably we need to check for duplicates during downloading the new tasks.
	 * But it should be discussed to because it with the current simulating of some
	 * data store it will add a lot of complexity as we will have to run across all
	 * the cached tasks and compare it with the new one.
	 */
	@Override
	public Task getRandomTask(String userEmail) {
		initTasks(userEmail);
		if (TASKS.get(userEmail).isEmpty()) {
			String endpointUrl = String.format(RANDOM_TASK_PATTERN, baseUrl);

	        RestTemplate template = new RestTemplate();
	        ResponseEntity<Task> responseEntity = template.getForEntity(endpointUrl, Task.class);
			TASKS.get(userEmail).add(responseEntity.getBody());
		}
		Random random = new Random();
		return TASKS.get(userEmail).get(random.ints(0, TASKS.get(userEmail).size()).findFirst().getAsInt());
	}

	@Override
	public List<Task> getTasksByStatus(String userEmail, Status status) {
		return Optional.ofNullable(TASKS.get(userEmail)).map(
				tasks -> tasks.stream().filter(task -> task.getStatus() == status).collect(Collectors.toList())).get();
	}

	@Override
	public List<Task> getTasksByStatusCounted(String userEmail, Status status, int limit) {
		return getTasksByStatus(userEmail, status).subList(0, limit);
	}

	@Override
	public List<Task> getTasksByTypeCounted(String userEmail, String type, int limit) {
		initTasks(userEmail);
		List<Task> usersTasks = TASKS.get(userEmail).stream()
				.filter(task -> task.getType().equals(type))
				.filter(task -> task.getStatus() != Status.COMPLETED)
				.collect(Collectors.toList());
		if (usersTasks.size() >= limit) {
			return usersTasks.subList(0, limit);
		}
		String endpointUrl = String.format(TASK_BY_TYPE_PATTERN, baseUrl, type);
        RestTemplate template = new RestTemplate();
		IntStream.range(0, limit - usersTasks.size()).boxed().forEach(i -> {
	        ResponseEntity<Task> responseEntity = template.getForEntity(endpointUrl, Task.class);
			TASKS.get(userEmail).add(responseEntity.getBody());
		});
		return TASKS.get(userEmail);
	}

	/**
	 * It is a question what is better: initialize this map during creation of this bean
	 * or call this method to initialize this map during each request? From my perspective 
	 * I don't like any of these approaches because in the first one we are hardcoding 
	 * all possible users in this layer that is not acceptable as for me. Another approach
	 * is not much better but still it allows us to keep some flexibility. 
	 * 
	 * How it could be done better? We could implement some decorator using reflection to
	 * call this method before calling some another, but I'm not sure it is needed here. 
	 * 
	 */
	private void initTasks(String userEmail) {
		if (TASKS.get(userEmail) == null) {
			TASKS.put(userEmail, List.of());
		}
	}

	@Override
	public void setStatusToTask(String userEmail, String key, Status status) {
		initTasks(userEmail);
		getTaskByKey(userEmail, key)
		// TODO would be nice to throw some exception in case the element is not present
		.ifPresent(task -> task.setStatus(status)); 
	}

	private Optional<Task> getTaskByKey(String userEmail, String key) {
		initTasks(userEmail);
		return TASKS.get(userEmail).stream()
		.filter(task -> task.getKey().equals(key))
		.findFirst();
	} 
	
	@Override
	public void incrementRate(String userEmail, String key, int incRate) {
		getTaskByKey(userEmail, key)
		// TODO would be nice to throw some exception in case the element is not present
		.ifPresent(task -> task.setRate(task.getRate() + incRate));
	}

	@Override
	public List<Task> getTasksByRateHighness(String userEmail, int rateHighness) {
		initTasks(userEmail);
		TASKS.get(userEmail).sort(Comparator.comparing(Task::getRate).reversed());
		List<Task> distinctList = io.vavr.collection.List.ofAll(TASKS.get(userEmail))
				.distinctBy(Task::getRate)
				.toJavaList();
		if (distinctList.size() < rateHighness) {
			// There is nothing specified in requirements regarding such case so 
			// I'm just returning empty list. 
			return List.of();
		}
		return TASKS.get(userEmail).stream()
				.filter(task -> task.getRate().equals(distinctList.get(rateHighness).getRate()))
				.collect(Collectors.toUnmodifiableList());
	}
	
}
