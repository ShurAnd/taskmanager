package org.andrey.taskmanager.domain.task;

import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;

@Getter
public enum TaskFilters {

    TITLE_FILTER("title", "title") {
        @Override
        public Specification<Task> getSpecification(String value) {
            return TaskSpecification.title(value);
        }
    },
    STATUS_FILTER("status", "status") {
        @Override
        public Specification<Task> getSpecification(String value) {
            return TaskSpecification.status(value);
        }
    },
    PRIORITY_FILTER("priority", "priority") {
        @Override
        public Specification<Task> getSpecification(String value) {
            return TaskSpecification.priority(value);
        }
    };

    private String filterName = "";
    private String tableField = "";

    TaskFilters(String filterName, String tableField) {
        this.filterName = filterName;
        this.tableField = tableField;
    }

    public static TaskFilters fromFilterName(String filterName) {
        return Arrays.stream(values())
                .filter(f -> f.getFilterName().equals(filterName))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Неизвестный фильтр для задач"));
    }

    public abstract Specification<Task> getSpecification(String value);
}
