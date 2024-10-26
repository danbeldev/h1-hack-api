package ru.danbeldev.h1hackapi.features.services.importData;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JsonTaskImportService implements TaskImportService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public void importData(Long projectId, MultipartFile file) {
        File tempFile = File.createTempFile("upload-", ".json");
        file.transferTo(tempFile);

        var data = parsePointsJSON(tempFile);

        tempFile.delete();

        createAndSaveTask(projectId, data);
    }

    @SneakyThrows
    private Map<String, List<String>> parsePointsJSON(File file) {
        // Используем ObjectMapper для разбора JSON
        return objectMapper.readValue(file, HashMap.class);
    }

    public void createAndSaveTask(Long projectId, Map<String, List<String>> data) {
        String taskKey = "h1_hack_project_" + projectId + "_tasks";
        data.forEach((key, value) -> {
            redisTemplate.opsForHash().put(taskKey, key, value);
        });

        String propsKey = "h1_hack_project_" + projectId + "_props";
        redisTemplate.opsForList().rightPushAll(propsKey, data.keySet().toArray());
    }
}