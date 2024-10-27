package ru.danbeldev.h1hackapi.features.services.importData;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.danbeldev.h1hackapi.features.services.importData.models.Prop;
import ru.danbeldev.h1hackapi.features.services.importData.models.ValueType;

import java.io.*;
import java.util.*;

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
    private Map<Prop, List<String>> parsePointsJSON(File file) {
        var data = new HashMap<Prop, List<String>>();
        List<Map<String, Object>> jsonData = objectMapper.readValue(file, List.class);

        if (jsonData.isEmpty()) {
            throw new IllegalArgumentException("JSON data is empty");
        }

        var properties = new ArrayList<Prop>();

        // Extract properties from the first JSON object
        jsonData.get(0).keySet().forEach(key -> {
            var prop = new Prop();
            prop.setName(key);
            prop.setType(ValueType.NOT_DEFINED);
            properties.add(prop);
            data.put(prop, new ArrayList<>());
        });

        // Populate data
        for (Map<String, Object> jsonObject : jsonData) {
            for (Prop prop : properties) {
                String value = String.valueOf(jsonObject.get(prop.getName()));

                List<String> list = data.get(prop);
                list.add(value);

                if (!prop.getName().isBlank()) {
                    ValueType currentType = prop.getType();
                    ValueType newType = getValueType(value);

                    if (currentType == ValueType.NOT_DEFINED) {
                        prop.setType(newType);
                    } else if (currentType != ValueType.STRING && newType != currentType) {
                        prop.setType(ValueType.STRING);
                    }

                    // Update data
                    data.put(prop, list);
                } else {
                    data.put(prop, list);
                }
            }
        }

        return data;
    }

    public void createAndSaveTask(Long projectId, Map<Prop, List<String>> data) {
        String taskKey = "h1_hack_project_" + projectId + "_type_tasks";
        data.forEach((key, value) -> {
            redisTemplate.opsForHash().put(taskKey, key.getName(), value);
        });

        String propsKey = "h1_hack_project_" + projectId + "_props";
        redisTemplate.opsForList().rightPushAll(propsKey, data.keySet().toArray());
    }

    public ValueType getValueType(String value) {
        try {
            Long.parseLong(value);
            return ValueType.INTEGER;
        } catch (NumberFormatException ignore) {
        }
        try {
            Double.parseDouble(value);
            return ValueType.DOUBLE;
        } catch (NumberFormatException ignore) {
        }
        if (value.matches("^\\d{4}-\\d{2}-\\d{2}(?:\\s\\d{2}:\\d{2}:\\d{2}(?:\\.\\d{1,6})?)?$")) {
            return ValueType.DATE;
        }
        return ValueType.STRING;
    }
}