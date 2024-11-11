package ru.platypus.t1_hw4.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.platypus.t1_hw4.dto.TaskRequestDTO;
import ru.platypus.t1_hw4.dto.TaskResponseDTO;
import ru.platypus.t1_hw4.entity.Task;
import ru.platypus.t1_hw4.exception.TaskNotFoundException;
import ru.platypus.t1_hw4.repository.TaskRepository;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {


    private final TaskRepository taskRepository;

    public final Function<Task, TaskResponseDTO> toResponseDTO = (Task task) -> new TaskResponseDTO(task.getId(), task.getTitle(),task.getDescription(), task.getStatus());
    public final Function<TaskRequestDTO, Task> toEntity = (TaskRequestDTO TaskRequestDTO) -> new Task(TaskRequestDTO.title(), TaskRequestDTO.description(), TaskRequestDTO.status());



    public TaskResponseDTO createTask(TaskRequestDTO taskRequestDTO) {
        Task task = toEntity.apply(taskRequestDTO);
        Task savedTask = taskRepository.save(task);
        return toResponseDTO.apply(savedTask);
    }

    public TaskResponseDTO getTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Задача с id " + id + " не найдена"));
        return toResponseDTO.apply(task);
    }

    public TaskResponseDTO updateTask(Long id, TaskRequestDTO taskRequestDTO) {
        Task existingTask = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Задача с id " + id + " не найдена"));
        existingTask.setTitle(taskRequestDTO.title());
        existingTask.setDescription(taskRequestDTO.description());
        existingTask.setStatus(taskRequestDTO.status());
        Task updatedTask = taskRepository.save(existingTask);
        return toResponseDTO.apply(updatedTask);
    }

    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Задача с id " + id + " не найдена"));
        taskRepository.delete(task);
    }

    public List<TaskResponseDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream()
                .map(toResponseDTO)
                .collect(Collectors.toList());
    }
}
