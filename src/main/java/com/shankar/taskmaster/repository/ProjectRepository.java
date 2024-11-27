package com.shankar.taskmaster.repository;

import com.shankar.taskmaster.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
