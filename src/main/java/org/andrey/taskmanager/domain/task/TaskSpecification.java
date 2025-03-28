package org.andrey.taskmanager.domain.task;

import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.andrey.taskmanager.domain.user.User;
import org.springframework.data.jpa.domain.Specification;

@Data
@AllArgsConstructor
public class TaskSpecification implements Specification<Task> {

    private TaskSearchCriteria criteria;

    @Override
    public Predicate toPredicate
            (Root<Task> root, CriteriaQuery<?> query, CriteriaBuilder builder) {

        if (criteria.getOperation().equalsIgnoreCase(">")) {
            return builder.greaterThanOrEqualTo(
                    root.<String>get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase("<")) {
            return builder.lessThanOrEqualTo(
                    root.<String>get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(
                        root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }

    public static Specification<Task> title(String title) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
        };
    }

    public static Specification<Task> author(Long id) {
        return (root, query, criteriaBuilder) -> {
            Join<Task, User> taskAuthor = root.join("author");
            return criteriaBuilder.equal(taskAuthor.get("id"), id);
        };
    }

    public static Specification<Task> performer(Long id) {
        return (root, query, criteriaBuilder) -> {
            Join<Task, User> taskAuthor = root.join("taskPerformer");
            return criteriaBuilder.equal(taskAuthor.get("id"), id);
        };
    }

    public static Specification<Task> status(String status) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("status"), TaskStatus.fromValue(status));
        };
    }

    public static Specification<Task> priority(String priority) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get("status"), TaskPriority.fromValue(priority));
        };
    }
}
