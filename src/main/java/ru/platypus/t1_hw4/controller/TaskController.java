package ru.platypus.t1_hw4.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.platypus.t1_hw4.dto.TaskRequestDTO;
import ru.platypus.t1_hw4.dto.TaskResponseDTO;
import ru.platypus.t1_hw4.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody TaskRequestDTO taskRequestDTO) {
        TaskResponseDTO taskResponseDTO = taskService.createTask(taskRequestDTO);
        return ResponseEntity.ok(taskResponseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> getTask(@PathVariable Long id) {
        TaskResponseDTO taskResponseDTO = taskService.getTask(id);
        return ResponseEntity.ok(taskResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateTask(@PathVariable Long id, @RequestBody TaskRequestDTO taskRequestDTO) {
        TaskResponseDTO taskResponseDTO = taskService.updateTask(id, taskRequestDTO);
        return ResponseEntity.ok(taskResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getAllTasks() {
        List<TaskResponseDTO> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }
}
