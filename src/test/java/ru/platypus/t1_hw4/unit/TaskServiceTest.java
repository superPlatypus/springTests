package ru.platypus.t1_hw4.unit;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.platypus.t1_hw4.dto.TaskRequestDTO;
import ru.platypus.t1_hw4.dto.TaskResponseDTO;
import ru.platypus.t1_hw4.entity.Task;
import ru.platypus.t1_hw4.exception.TaskNotFoundException;
import ru.platypus.t1_hw4.repository.TaskRepository;
import ru.platypus.t1_hw4.service.TaskService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    public void testCreateTask_Success() {
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO("Test Title", "Test Description", "In Progress");

        Task savedTask = new Task(1L, "Test Title", "Test Description", "In Progress");

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        TaskResponseDTO responseDTO = taskService.createTask(taskRequestDTO);

        assertNotNull(responseDTO);
        assertEquals(savedTask.getId(), responseDTO.id());
        assertEquals(savedTask.getTitle(), responseDTO.title());
        assertEquals(savedTask.getDescription(), responseDTO.description());
        assertEquals(savedTask.getStatus(), responseDTO.status());

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    public void testGetTask_Success() {
        Task task = new Task(1L, "Test Title", "Test Description", "In Progress");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskResponseDTO responseDTO = taskService.getTask(1L);

        assertNotNull(responseDTO);
        assertEquals(task.getId(), responseDTO.id());
        assertEquals(task.getTitle(), responseDTO.title());
        assertEquals(task.getDescription(), responseDTO.description());
        assertEquals(task.getStatus(), responseDTO.status());

        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetTask_NotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TaskNotFoundException.class, () -> {
            taskService.getTask(1L);
        });

        assertEquals("Задача с id 1 не найдена", exception.getMessage());

        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    public void testUpdateTask_Success() {
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO("Updated Title", "Updated Description", "Completed");
        Task existingTask = new Task(1L, "Old Title", "Old Description", "In Progress");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        when(taskRepository.save(existingTask)).thenReturn(existingTask);

        TaskResponseDTO responseDTO = taskService.updateTask(1L, taskRequestDTO);

        assertNotNull(responseDTO);

        // Проверка значений полей ответа
        assertEquals(1L, responseDTO.id());
        assertEquals("Updated Title", responseDTO.title());
        assertEquals("Updated Description", responseDTO.description());
        assertEquals("Completed", responseDTO.status());

        // Проверка, что поля существующей задачи были обновлены
        assertEquals("Updated Title", existingTask.getTitle());
        assertEquals("Updated Description", existingTask.getDescription());
        assertEquals("Completed", existingTask.getStatus());

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).save(existingTask);
    }


    @Test
    public void testUpdateTask_NotFound() {
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO("Updated Title", "Updated Description", "Completed");

        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TaskNotFoundException.class, () -> {
            taskService.updateTask(1L, taskRequestDTO);
        });

        assertEquals("Задача с id 1 не найдена", exception.getMessage());

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    public void testDeleteTask_Success() {
        Task existingTask = new Task(1L, "Test Title", "Test Description", "In Progress");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));
        doNothing().when(taskRepository).delete(existingTask);

        taskService.deleteTask(1L);

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, times(1)).delete(existingTask);
    }

    @Test
    public void testDeleteTask_NotFound() {
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(TaskNotFoundException.class, () -> {
            taskService.deleteTask(1L);
        });

        assertEquals("Задача с id 1 не найдена", exception.getMessage());

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository, never()).delete(any(Task.class));
    }








}
