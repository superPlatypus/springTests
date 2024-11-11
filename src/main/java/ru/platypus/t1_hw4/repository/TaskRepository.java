package ru.platypus.t1_hw4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.platypus.t1_hw4.entity.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
}