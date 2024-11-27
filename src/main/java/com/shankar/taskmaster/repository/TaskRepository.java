package com.shankar.taskmaster.repository;

import com.shankar.taskmaster.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
