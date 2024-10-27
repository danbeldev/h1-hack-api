package ru.danbeldev.h1hackapi.features.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import ru.danbeldev.h1hackapi.features.entities.ProjectEntity;
import ru.danbeldev.h1hackapi.features.repositories.ProjectRepository;
import ru.danbeldev.h1hackapi.features.services.importData.models.Prop;
import ru.danbeldev.h1hackapi.features.services.importData.models.ValueType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public List<ProjectEntity> getAll() {
        return projectRepository.findAll();
    }

    public List<Prop> getAllProperties(Long id) {
        var redisData = redisTemplate.opsForList().range("h1_hack_project_" + id + "_props", 0, -1).stream().map(Object::toString).toList();

        List<Prop> props = new ArrayList<>();
        for (String item : redisData) {
            String name = item.replaceAll(".*name=(.*?),.*", "$1").trim();
            props.add(new Prop(name, ValueType.valueOf(extractTypeValue(item))));
        }
        return props;
    }

    public static String extractTypeValue(String input) {
        // Регулярное выражение для поиска значения type
        String regex = "type=([A-Za-z]+)";
        Pattern pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(1); // Возвращаем значение, найденное в первой группе
        }
        return null; // Если значение не найдено
    }

    public Map<String, List<String>> getTasks(@PathVariable Long projectId) {
        String taskKey = "h1_hack_project_" + projectId + "_type_tasks";
        Map<Object, Object> redisData = redisTemplate.opsForHash().entries(taskKey);

        Map<String, List<String>> result = new HashMap<>();

        redisData.forEach((key, value) -> {
            if (value instanceof List<?>) {
                result.put((String) key, (List<String>) value);
            }
        });

        return result;
    }

    @Transactional
    public ProjectEntity create(String name) {
        var project = new ProjectEntity();
        project.setName(name);
        return projectRepository.save(project);
    }

    @Transactional(readOnly = true)
    public ProjectEntity getById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
