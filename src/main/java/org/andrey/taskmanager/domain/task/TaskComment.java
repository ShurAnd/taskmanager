package org.andrey.taskmanager.domain.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.andrey.taskmanager.domain.user.User;

/**
 * Класс описывающий комментарий к задаче
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskComment {
//    Идентификатор комментария
    private Long id = 1L;
//    идентификатор задачи к которой оставлен комментарий
    private Long taskId = -1L;
//    Текст комментария
    private String comment = "";
//    Автор комментария
    private User author = new User();
}
