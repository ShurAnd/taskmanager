package org.andrey.taskmanager.domain.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.andrey.taskmanager.domain.user.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для описания задачи
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
//     Идентификатор задачи
     private Long id = -1L;
//     Заголовок задачи
     private String title = "";
//     Описание задачи
     private String description = "";
//     Статус задачи
     private TaskStatus status = TaskStatus.PENDING;
//     Приоритет задачи
     private TaskPriority priority = TaskPriority.LOW;
//     Автор создания задачи
     private User author = new User();
//     Исполнитель задачи
     private User taskPerformer = new User();
}
