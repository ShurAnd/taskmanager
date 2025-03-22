package org.andrey.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.andrey.taskmanager.domain.task.Task;
import org.andrey.taskmanager.domain.task.TaskComment;
import org.andrey.taskmanager.service.TaskCommentService;
import org.andrey.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Контроллер для обработки запросов по API задач
 */
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskCommentService taskCommentService;
    private final ObjectMapper objectMapper;

    @Autowired
    public TaskController(TaskService taskService,
                          TaskCommentService taskCommentService,
                          ObjectMapper objectMapper){
        this.taskService = taskService;
        this.taskCommentService = taskCommentService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResponseEntity<String> findAllTasks(@RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                               @RequestParam(value = "limit", defaultValue = "10") Integer limit) throws Exception{
        List<Task> result = taskService.findAllTasks(offset, limit);
        return ResponseEntity.ok()
        .body(objectMapper.writeValueAsString(result));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> findTaskById(@PathVariable Long id) throws Exception{
        Task result = taskService.findTaskById(id);
        return ResponseEntity.ok()
                .body(objectMapper.writeValueAsString(result));
    }

    @PostMapping
    public ResponseEntity<String> createTask(@RequestBody String taskJson) throws Exception{
        Task task = objectMapper.readValue(taskJson, Task.class);
        task = taskService.createTask(task);
        return ResponseEntity.created(null)
                .body(objectMapper.writeValueAsString(task));
    }

    @PutMapping
    public ResponseEntity<String> updateTask(@RequestBody String taskJson) throws Exception{
        Task task = objectMapper.readValue(taskJson, Task.class);
        task = taskService.updateTask(task);
        return ResponseEntity.ok()
                .body(objectMapper.writeValueAsString(task));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTaskById(Long id) throws Exception{
        taskService.deleteTaskById(id);
        return ResponseEntity.noContent()
                .build();
    }

    @PatchMapping("/{taskId}/{statusCode}")
    public ResponseEntity<String> updateTaskStatus(@PathVariable("taskId") Long taskId,
                                                   @PathVariable("statusCode") Integer statusCode) throws Exception{
        Task result = taskService.updateTaskStatus(taskId, statusCode);
        return ResponseEntity.ok()
                .body(objectMapper.writeValueAsString(result));
    }

    @PostMapping("/comment/{taskId}")
    public ResponseEntity<String> postTaskComment(@RequestBody String commentJson) throws Exception{
        TaskComment comment = objectMapper.readValue(commentJson, TaskComment.class);
        comment = taskCommentService.createComment(comment);

        return ResponseEntity.created(null)
                .body(objectMapper.writeValueAsString(comment));
    }

    @GetMapping("/comment/{taskId}")
    public ResponseEntity<String> getCommentsByTask(@PathVariable Long id,
                                                   @RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                                   @RequestParam(value = "limit", defaultValue = "10") Integer limit) throws Exception{
        List<TaskComment> result = taskCommentService.findAllCommentsForTask(id, offset, limit);

        return ResponseEntity.ok(objectMapper.writeValueAsString(result));
    }

    @GetMapping("/author/{id}")
    public ResponseEntity<String> getTasksByAuthor(@PathVariable Long id,
                                                   @RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                                   @RequestParam(value = "limit", defaultValue = "10") Integer limit) throws Exception{
        List<Task> result = taskService.findTasksByAuthor(id, offset, limit);

        return ResponseEntity.ok(objectMapper.writeValueAsString(result));
    }

    @GetMapping("/performer/{id}")
    public ResponseEntity<String> getTasksByPerformer(@PathVariable Long id,
                                                   @RequestParam(value = "offset", defaultValue = "0") Integer offset,
                                                   @RequestParam(value = "limit", defaultValue = "10") Integer limit) throws Exception{
        List<Task> result = taskService.findTasksByPerformer(id, offset, limit);

        return ResponseEntity.ok(objectMapper.writeValueAsString(result));
    }
}
