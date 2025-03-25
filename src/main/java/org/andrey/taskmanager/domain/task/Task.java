package org.andrey.taskmanager.domain.task;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
@Entity(name = "tasks")
public class Task {
//     Идентификатор задачи
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id = -1L;
//     Заголовок задачи
     @NotBlank(message = "Нельзя оставлять заголовок задачи пустым")
     private String title = "";
//     Описание задачи
     @NotBlank(message = "Нельзя оставлять описание задачи пустым")
     private String description = "";
//     Статус задачи
     @NotNull(message = "Нельзя не указывать статус задачи")
     private TaskStatus status = TaskStatus.PENDING;
//     Приоритет задачи
     @NotNull(message = "Нельзя не указывать приоритет задачи")
     private TaskPriority priority = TaskPriority.LOW;
//     Автор создания задачи
     @NotNull(message = "Нельзя оставлять данные об авторе задачи пустыми")
     @OneToOne(cascade = CascadeType.PERSIST)
     @JoinColumn(name = "author_id", referencedColumnName = "id")
     private User author = new User();
//     Исполнитель задачи
     @OneToOne(cascade = CascadeType.PERSIST)
     @JoinColumn(name = "performer_id", referencedColumnName = "id")
     private User taskPerformer = new User();
}
