package com.shankar.taskmaster.controller;

import com.shankar.taskmaster.entity.Project;
import com.shankar.taskmaster.entity.Task;
import com.shankar.taskmaster.service.ProjectService;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final Tracer tracer;

    public ProjectController(ProjectService projectService, Tracer tracer) {
        this.projectService = projectService;
        this.tracer = tracer;
    }

    @GetMapping
    public List<Project> getAllProjects(){
        Span span = tracer.spanBuilder("getAllProjects").startSpan();
        try {
            return projectService.getAllProjects();
        } finally {
            span.end();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id){
        Span span = tracer.spanBuilder("getProjectById").startSpan();
        try {
            Optional<Project> project = projectService.getProjectById(id);
            if (project.isPresent()) {
                return ResponseEntity.ok(project.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } finally {
            span.end();
        }
    }

    @PostMapping
    public Project createProject(@RequestBody Project project){
        Span span = tracer.spanBuilder("createProject").startSpan();
        try {
            return projectService.saveProject(project);
        } finally {
            span.end();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project projectDetails){
        Span span = tracer.spanBuilder("updateProject").startSpan();
        try {
            Optional<Project> project = projectService.getProjectById(id);
            if (project.isPresent()) {
                Project updatedProject = project.get();
                updatedProject.setName(projectDetails.getName());
                updatedProject.setDescription(projectDetails.getDescription());
                updatedProject.getTasks().clear();
                updatedProject.getTasks().addAll(projectDetails.getTasks());
                return ResponseEntity.ok(projectService.saveProject(updatedProject));
            } else {
                return ResponseEntity.notFound().build();
            }
        } finally {
            span.end();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id){
        Span span = tracer.spanBuilder("deleteProject").startSpan();
        try {
            projectService.deleteProject(id);
            return ResponseEntity.noContent().build();
        } finally {
            span.end();
        }
    }
}
