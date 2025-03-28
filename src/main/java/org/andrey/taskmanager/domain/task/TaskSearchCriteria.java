package org.andrey.taskmanager.domain.task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskSearchCriteria {
    private String key;
    private String operation;
    private Object value;
}
