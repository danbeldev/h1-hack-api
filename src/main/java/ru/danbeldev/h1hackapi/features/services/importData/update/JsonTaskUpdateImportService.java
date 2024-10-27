package ru.danbeldev.h1hackapi.features.services.importData.update;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.danbeldev.h1hackapi.features.entities.ProjectEntity;
import ru.danbeldev.h1hackapi.features.entities.TaskUpdate;
import ru.danbeldev.h1hackapi.features.repositories.TaskUpdateRepository;
import ru.danbeldev.h1hackapi.features.services.ProjectService;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JsonTaskUpdateImportService implements TaskUpdateImportService {

    private final ProjectService projectService;
    private final TaskUpdateRepository taskUpdateRepository;
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public void importData(Long projectId, MultipartFile file) {
        // Сохранение временного файла
        File tempFile = File.createTempFile("upload-", ".json");
        file.transferTo(tempFile);

        // Получаем проект
        var project = projectService.getById(projectId);

        // Парсим JSON и сохраняем задачи
        var tasks = parsePointsJSON(tempFile, project);
        taskUpdateRepository.saveAll(tasks);

        // Удаляем временный файл после использования
        tempFile.delete();
    }

    @SneakyThrows
    private List<TaskUpdate> parsePointsJSON(File file, ProjectEntity project) {
        // Чтение файла JSON и преобразование в список TaskUpdate
        List<TaskUpdate> tasks = objectMapper.readValue(file, new TypeReference<>() {});

        // Установка проекта для каждой задачи
        for (TaskUpdate task : tasks) {
            task.setProject(project);
        }

        return tasks;
    }
}