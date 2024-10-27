package ru.danbeldev.h1hackapi.features.services.importData.update;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.danbeldev.h1hackapi.features.entities.ProjectEntity;
import ru.danbeldev.h1hackapi.features.entities.TaskUpdate;
import ru.danbeldev.h1hackapi.features.repositories.TaskUpdateRepository;
import ru.danbeldev.h1hackapi.features.services.ProjectService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CsvTaskUpdateImportService implements TaskUpdateImportService {

    private final ProjectService projectService;

    private final TaskUpdateRepository taskUpdateRepository;

    @Override
    @SneakyThrows
    public void importData(Long projectId, MultipartFile file) {
        File tempFile = File.createTempFile("upload-", ".csv");
        file.transferTo(tempFile);

        var project = projectService.getById(projectId);

        var tasks = parsePointsCSV(tempFile, project);

        taskUpdateRepository.saveAll(tasks);
    }

    @SneakyThrows
    private List<TaskUpdate> parsePointsCSV(File file, ProjectEntity project) {
        var tasks = new ArrayList<TaskUpdate>();

        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;

            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                var entity = new TaskUpdate();
                entity.setPropertyName(values[2]);
                entity.setOldValue(values[3]);
                entity.setNewValue(values[4]);
                entity.setData(stringToInstant(values[1]));
                entity.setProject(project);
                tasks.add(entity);
            }
        }

        return tasks;
    }

    public static Instant stringToInstant(String dateString) {
        // Определяем формат даты и времени
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

        // Парсим строку в LocalDateTime
        var localDateTime = LocalDateTime.parse(dateString, formatter);

        // Преобразуем LocalDateTime в Instant (по умолчанию по текущему часовому поясу)
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant();
    }
}
