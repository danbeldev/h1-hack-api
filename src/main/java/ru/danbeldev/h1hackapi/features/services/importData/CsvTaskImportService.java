package ru.danbeldev.h1hackapi.features.services.importData;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CsvTaskImportService implements TaskImportService {

    private final RedisTemplate<String, Object> redisTemplate;

    @SneakyThrows
    @Override
    public void importData(Long projectId, MultipartFile file) {
        File tempFile = File.createTempFile("upload-", ".csv");
        file.transferTo(tempFile);

        var data = parsePointsCSV(tempFile);

        tempFile.delete();

        createAndSaveTask(projectId, data);
    }

    @SneakyThrows
    private Map<String, List<String>> parsePointsCSV(File file) {
        var data = new HashMap<String, List<String>>();

        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            var properties = new ArrayList<String>();

            br.readLine();

            // Чтение первой строки для заголовков
            if ((line = br.readLine()) != null) {
                String[] headers = line.split(";");
                for (String header : headers) {
                    properties.add(header.trim());
                    data.put(header.trim(), new ArrayList<>());
                }
            }

            // Чтение остальных строк
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                for (int i = 0; i < values.length; i++) {
                    var name = properties.get(i);
                    var list = (ArrayList<String>) data.get(name);
                    list.add(values[i].trim());
                    data.put(name, list);
                }
            }
        }

        return data;
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
