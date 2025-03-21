package org.andrey.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.andrey.taskmanager.domain.task.Task;
import org.andrey.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final ObjectMapper objectMapper;

    @Autowired
    public TaskController(TaskService taskService,
                          ObjectMapper objectMapper){
        this.taskService = taskService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<String> getAllTasks() throws Exception{
        List<Task> result = taskService.getAllTasks();
        return ResponseEntity.ok().body(objectMapper.writeValueAsString(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getTaskById(@PathVariable Long id) throws Exception{
        Task result = taskService.getTaskById(id);
        return ResponseEntity.ok().body(objectMapper.writeValueAsString(result));
    }

    @PostMapping
    public ResponseEntity<String> saveTask(Task task) throws Exception{
        Task result = taskService.createTask(task);
        return ResponseEntity.created(null).body(objectMapper.writeValueAsString(result));
    }

    @PutMapping
    public ResponseEntity<String> updateTask(Task task) throws Exception{
        Task result = taskService.updateTask(task);
        return ResponseEntity.ok().body(objectMapper.writeValueAsString(result));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTaskById(Long id) throws Exception{
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
