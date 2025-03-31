package org.andrey.taskmanager.service;

import org.andrey.taskmanager.domain.task.Task;
import org.andrey.taskmanager.domain.task.TaskStatus;
import org.andrey.taskmanager.domain.user.User;
import org.andrey.taskmanager.exception.OperationNotAllowedException;
import org.andrey.taskmanager.exception.TaskNotFoundException;
import org.andrey.taskmanager.repository.TaskRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TaskService taskService;

    private Task task;
    private User author;
    private User performer;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        author = new User();
        author.setId(1L);
        author.setEmail("author@example.com");
        performer = new User();
        performer.setId(2L);
        performer.setEmail("performer@example.com");
        task = new Task();
        task.setId(1L);
        task.setAuthor(author);
        task.setTaskPerformer(performer);
        task.setStatus(TaskStatus.PENDING);
    }

    @Test
    public void findAllTasks_ShouldReturnPagedTasks() {
        List<Task> tasks = Arrays.asList(task);
        Page<Task> taskPage = new PageImpl<>(tasks);
        when(taskRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(taskPage);

        Page<Task> result = taskService.findAllTasks(0, 10, new HashMap<>());

        assertEquals(1, result.getTotalElements());
        assertEquals(task, result.getContent().get(0));
        verify(taskRepository).findAll(any(Specification.class), any(PageRequest.class));
    }

    @Test
    public void findTaskById_ShouldReturnTask_WhenTaskExists() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));

        Task foundTask = taskService.findTaskById(task.getId());

        assertEquals(task, foundTask);
        verify(taskRepository).findById(task.getId());
    }

    @Test
    public void findTaskById_ShouldThrowTaskNotFoundException_WhenTaskDoesNotExist() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> taskService.findTaskById(task.getId()));
    }

    @Test
    public void saveTask_ShouldSaveTaskWithValidAuthorAndPerformer() {
        when(userService.getUserById(author.getId())).thenReturn(author);
        when(userService.getUserById(performer.getId())).thenReturn(performer);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task savedTask = taskService.saveTask(task);

        assertEquals(task, savedTask);
        verify(taskRepository).save(task);
    }

    @Test
    public void deleteTaskById_ShouldCallRepositoryDelete() {
        taskService.deleteTaskById(task.getId());
        verify(taskRepository).deleteById(task.getId());
    }

    @Test
    public void updateTaskStatus_ShouldUpdateStatus_WhenUserIsAuthor() {
        SecurityContext context = mock(SecurityContext.class);
        SecurityContextHolder.setContext(context);
        Authentication authentication = new UsernamePasswordAuthenticationToken(author.getEmail(),
                author.getPassword(),
                Lists.list(new SimpleGrantedAuthority("ROLE_USER")));
        when(context.getAuthentication()).thenReturn(authentication);
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        Task updatedTask = taskService.updateTaskStatus(task.getId(), TaskStatus.IN_PROCESS.getCode());

        assertEquals(TaskStatus.IN_PROCESS.getCode(), updatedTask.getStatus().getCode());
        verify(taskRepository).save(updatedTask);
    }

    @Test
    public void updateTaskStatus_ShouldThrowOperationNotAllowedException_WhenUserIsNotAuthorOrAdmin() {
        SecurityContext context = mock(SecurityContext.class);
        SecurityContextHolder.setContext(context);
        Authentication authentication = new UsernamePasswordAuthenticationToken("someOtherUser@example.com",
                author.getPassword(),
                Lists.list(new SimpleGrantedAuthority("ROLE_USER")));
        when(context.getAuthentication()).thenReturn(authentication);
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        assertThrows(OperationNotAllowedException.class, () -> taskService.updateTaskStatus(task.getId(), TaskStatus.IN_PROCESS.getCode()));
    }

    @Test
    public void updateTaskPerformer_ShouldUpdatePerformer() {
        when(taskRepository.findById(task.getId())).thenReturn(Optional.of(task));
        when(userService.getUserById(performer.getId())).thenReturn(performer);
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        Task updatedTask = taskService.updateTaskPerformer(task.getId(), performer.getId());

        assertEquals(performer, updatedTask.getTaskPerformer());
        verify(taskRepository).save(updatedTask);
    }

    @Test
    public void findTasksByAuthor_ShouldReturnPagedTasks() {
        List<Task> tasks = Arrays.asList(task);
        Page<Task> taskPage = new PageImpl<>(tasks);
        when(taskRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(taskPage);

        Page<Task> result = taskService.findTasksByAuthor(author.getId(), 0, 10, new HashMap<>());

        assertEquals(1, result.getTotalElements());
        assertEquals(task, result.getContent().get(0));
        verify(taskRepository).findAll(any(Specification.class), any(PageRequest.class));
    }

    @Test
    public void findTasksByPerformer_ShouldReturnPagedTasks() {
        List<Task> tasks = Arrays.asList(task);
        Page<Task> taskPage = new PageImpl<>(tasks);
        when(taskRepository.findAll(any(Specification.class), any(PageRequest.class))).thenReturn(taskPage);

        Page<Task> result = taskService.findTasksByPerformer(performer.getId(), 0, 10, new HashMap<>());

        assertEquals(1, result.getTotalElements());
        assertEquals(task, result.getContent().get(0));
        verify(taskRepository).findAll(any(Specification.class), any(PageRequest.class));
    }
}
