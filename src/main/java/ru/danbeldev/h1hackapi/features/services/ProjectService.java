package ru.danbeldev.h1hackapi.features.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import ru.danbeldev.h1hackapi.features.entities.ProjectEntity;
import ru.danbeldev.h1hackapi.features.repositories.ProjectRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final RedisTemplate<String, Object> redisTemplate;

    @Transactional(readOnly = true)
    public List<ProjectEntity> getAll() {
        return projectRepository.findAll();
    }

    public List<String> getAllProperties(Long id) {
        return redisTemplate.opsForList().range("h1_hack_project_" + id + "_props", 0, -1).stream().map(Object::toString).toList();
//        return (List<String>) redisTemplate.opsForValue().get("h1_hack_project_" + id + "_props");
    }

    public Map<String, List<String>> getTasks(@PathVariable Long projectId) {
        String taskKey = "h1_hack_project_" + projectId + "_tasks";
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
