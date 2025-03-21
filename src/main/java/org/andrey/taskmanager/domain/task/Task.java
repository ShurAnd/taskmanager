package org.andrey.taskmanager.domain.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.andrey.taskmanager.domain.user.User;

/**
 * Класс для описания задачи
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
     private Long id = 0L;
     private String title = "";
     private String description = "";
     private TaskStatus status = TaskStatus.PENDING;
     private TaskPriority priority =TaskPriority.LOW;
     private User author = new User();
     private User taskPerformer = new User();
}
