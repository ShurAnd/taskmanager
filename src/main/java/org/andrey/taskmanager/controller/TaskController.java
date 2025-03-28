package org.andrey.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.andrey.taskmanager.domain.task.Task;
import org.andrey.taskmanager.domain.task.TaskComment;
import org.andrey.taskmanager.service.TaskCommentService;
import org.andrey.taskmanager.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Контроллер для обработки запросов по API задач
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final TaskCommentService taskCommentService;
    private final ObjectMapper objectMapper;

    @Autowired
    public TaskController(TaskService taskService,
                          TaskCommentService taskCommentService,
                          ObjectMapper objectMapper) {
        this.taskService = taskService;
        this.taskCommentService = taskCommentService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<String> findAllTasks(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                               @RequestParam(value = "size", defaultValue = "10") Integer size,
                                               @RequestParam Map<String, String> filters) throws Exception {
        Page<Task> result = taskService.findAllTasks(page, size, filters);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(objectMapper.writeValueAsString(result));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<String> findTaskById(@PathVariable Long id) throws Exception {
        Task result = taskService.findTaskById(id);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(objectMapper.writeValueAsString(result));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> createTask(@RequestBody Task task) throws Exception {
        task = taskService.saveTask(task);
        return ResponseEntity.created(null)
                .header("Content-Type", "application/json")
                .body(objectMapper.writeValueAsString(task));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> updateTask(@RequestBody @Valid Task task) throws Exception {
        task = taskService.saveTask(task);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(objectMapper.writeValueAsString(task));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> deleteTaskById(@PathVariable Long id) throws Exception {
        taskService.deleteTaskById(id);
        return ResponseEntity.noContent()
                .build();
    }

    @PatchMapping("/{taskId}/{statusCode}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<String> updateTaskStatus(@PathVariable("taskId") Long taskId,
                                                   @PathVariable("statusCode") Integer statusCode) throws Exception {
        Task result = taskService.updateTaskStatus(taskId, statusCode);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(objectMapper.writeValueAsString(result));
    }

    @PatchMapping("/{taskId}/performer/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<String> updateTaskPerformer(@PathVariable("taskId") Long taskId,
                                                      @PathVariable("userId") Long userId) throws Exception {
        Task result = taskService.updateTaskPerformer(taskId, userId);
        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(objectMapper.writeValueAsString(result));
    }

    @PostMapping("/comment/{taskId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<String> postTaskComment(@PathVariable Long taskId,
                                                  @RequestBody String comment) throws Exception {
        TaskComment taskComment = taskCommentService.createComment(comment, taskId);

        return ResponseEntity.created(null)
                .header("Content-Type", "application/json")
                .body(objectMapper.writeValueAsString(comment));
    }

    @GetMapping("/comment/{taskId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<String> getCommentsByTask(@PathVariable Long taskId,
                                                    @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(value = "size", defaultValue = "10") Integer size) throws Exception {
        Page<TaskComment> result = taskCommentService.findAllCommentsForTask(taskId, page, size);

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(objectMapper.writeValueAsString(result));
    }

    @GetMapping("/author/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<String> getTasksByAuthor(@PathVariable Long id,
                                                   @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                   @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                   @RequestParam Map<String, String> filters) throws Exception {
        Page<Task> result = taskService.findTasksByAuthor(id, page, size, filters);

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(objectMapper.writeValueAsString(result));
    }

    @GetMapping("/performer/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<String> getTasksByPerformer(@PathVariable Long id,
                                                      @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                      @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                      @RequestParam Map<String, String> filters) throws Exception {
        Page<Task> result = taskService.findTasksByPerformer(id, page, size, filters);

        return ResponseEntity.ok()
                .header("Content-Type", "application/json")
                .body(objectMapper.writeValueAsString(result));
    }
}
