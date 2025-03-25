package org.andrey.taskmanager.domain.task;

import jakarta.persistence.*;
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
@Entity(name = "comments")
public class TaskComment {
//    Идентификатор комментария
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = 1L;
//    идентификатор задачи к которой оставлен комментарий
    private Long taskId = -1L;
//    Текст комментария
    private String comment = "";
//    Автор комментария
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private User author = new User();
}
