package ru.platypus.t1_hw4.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.platypus.t1_hw4.dto.TaskRequestDTO;
import ru.platypus.t1_hw4.entity.Task;
import ru.platypus.t1_hw4.repository.TaskRepository;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ExtendWith(SpringExtension.class)
public class TaskControllerTest extends AbstractIntegrationTest{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    public void testCreateTask() throws Exception {
        TaskRequestDTO taskRequestDTO = new TaskRequestDTO("Integration Test Task", "Integration Description", "In Progress");

        mockMvc.perform(post("/tasks")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(taskRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.title").value("Integration Test Task"))
                .andExpect(jsonPath("$.description").value("Integration Description"))
                .andExpect(jsonPath("$.status").value("In Progress"));
    }

    @Test
    public void testGetTaskById() throws Exception {
        Task task = new Task();
        task.setTitle("Sample Task");
        task.setDescription("Sample Description");
        task.setStatus("Completed");
        task = taskRepository.save(task);

        mockMvc.perform(get("/tasks/" + task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.title").value("Sample Task"))
                .andExpect(jsonPath("$.description").value("Sample Description"))
                .andExpect(jsonPath("$.status").value("Completed"));
    }

    @Test
    public void testUpdateTask() throws Exception {
        Task task = new Task();
        task.setTitle("Old Title");
        task.setDescription("Old Description");
        task.setStatus("In Progress");
        task = taskRepository.save(task);

        TaskRequestDTO taskRequestDTO = new TaskRequestDTO("Updated Title", "Updated Description", "Completed");

        mockMvc.perform(put("/tasks/" + task.getId())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(taskRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId()))
                .andExpect(jsonPath("$.title").value("Updated Title"))
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.status").value("Completed"));
    }

    @Test
    public void testDeleteTask() throws Exception {
        Task task = new Task();
        task.setTitle("Task to be deleted");
        task.setDescription("Description");
        task.setStatus("In Progress");
        task = taskRepository.save(task);

        mockMvc.perform(delete("/tasks/" + task.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/tasks/" + task.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllTasks() throws Exception {
        Task task1 = new Task();
        task1.setTitle("Task 1");
        task1.setDescription("Description 1");
        task1.setStatus("In Progress");

        Task task2 = new Task();
        task2.setTitle("Task 2");
        task2.setDescription("Description 2");
        task2.setStatus("Completed");

        taskRepository.save(task1);
        taskRepository.save(task2);

        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].title").value("Task 2"));
    }

    @Test
    public void testGetTask_NotFound() throws Exception {
        mockMvc.perform(get("/tasks/999"))
                .andExpect(status().isNotFound());

    }






}
