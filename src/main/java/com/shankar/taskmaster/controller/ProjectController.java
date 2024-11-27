package com.shankar.taskmaster.controller;

import com.shankar.taskmaster.entity.Project;
import com.shankar.taskmaster.entity.Task;
import com.shankar.taskmaster.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public List<Project> getAllProjects(){
        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id){
        Optional<Project> project = projectService.getProjectById(id);
        if(project.isPresent()){
            return ResponseEntity.ok(project.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Project createProject(@RequestBody Project project){
        return projectService.saveProject(project);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project projectDetails){
        Optional<Project> project = projectService.getProjectById(id);
        if(project.isPresent()){
            Project updatedProject = project.get();
            updatedProject.setName(projectDetails.getName());
            updatedProject.setDescription(projectDetails.getDescription());

            //Clear the current tasks to ensure fresh creation
            updatedProject.getTasks().clear();

            for(Task task : projectDetails.getTasks()){
                updatedProject.addTask(task);
            }

            return ResponseEntity.ok(projectService.saveProject(updatedProject));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id){
        projectService.deleteProject(id);
        return  ResponseEntity.noContent().build();
    }
}