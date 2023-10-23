package com.sysaid.assignment.service;

import com.sysaid.assignment.domain.Task;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TaskServiceImpl implements  ITaskService{

    @Value("${external.boredapi.baseURL}")
    private String baseUrl;

    public ResponseEntity<Task> getRandomTask() {
        String endpointUrl = String.format("%s/activity", baseUrl);

        RestTemplate template = new RestTemplate();
        ResponseEntity<Task> responseEntity = template.getForEntity(endpointUrl, Task.class);

        return responseEntity;
    }
}
