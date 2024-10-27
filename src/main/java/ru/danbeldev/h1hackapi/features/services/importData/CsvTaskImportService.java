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
import java.util.regex.PatternSyntaxException;

@Service
@RequiredArgsConstructor
public class CsvTaskImportService implements TaskImportService {

    private final RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper;

    private static final String DATE_REGEX = "^\\d{4}-\\d{2}-\\d{2}(?:\\s\\d{2}:\\d{2}:\\d{2}(?:\\.\\d{1,6})?)?$";

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
    private Map<Prop, List<String>> parsePointsCSV(File file) {
        var data = new HashMap<Prop, List<String>>();
        StringBuilder content = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            var properties = new ArrayList<Prop>();

            br.readLine();

            // Чтение первой строки для заголовков
            if ((line = br.readLine()) != null) {
                String[] headers = line.split(";");
                for (String header : headers) {
                    var prop = new Prop();
                    prop.setName(header.trim());
                    prop.setType(ValueType.NOT_DEFINED);
                    properties.add(prop);
                    data.put(prop, new ArrayList<>());
                }
            }

            var p = 0;
            // Чтение остальных строк
            while ((line = br.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
                p++;
            }

            int current = 0;
            var con = content.toString().split(";");

            for (int i = 0; i < con.length; i++) {
                var pr = properties.get(current);
                System.out.println(pr);
                var c = con[i];
                ArrayList<String> list = (ArrayList<String>) data.get(pr);
                list.add(c);

                if (!pr.getName().isBlank()) {
                    ValueType currentType = pr.getType();
                    ValueType newType = getValueType(pr.getName());

                    if (currentType == ValueType.NOT_DEFINED) {
                        pr.setType(newType);
                    } else if (currentType != ValueType.STRING && newType != currentType) {
                        pr.setType(ValueType.STRING);
                    }

                    // Обновляем данные
                    data.remove(pr);
                    data.put(pr, list);
                } else {
                    data.put(pr, list);
                }

                if (current == properties.size() - 1) {
                    current = 0;
                } else {
                    current += 1;
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
            Double.parseDouble(value);
            return ValueType.DOUBLE;
        } catch (NumberFormatException ignore) {
        }
        try {
            Long.parseLong(value);
            return ValueType.INTEGER;
        } catch (NumberFormatException ignore) {
        }
        try {
            if (DATE_REGEX.matches(value)) return ValueType.DATE;
        } catch (PatternSyntaxException ignore) {
        }
        return ValueType.STRING;
    }
}
