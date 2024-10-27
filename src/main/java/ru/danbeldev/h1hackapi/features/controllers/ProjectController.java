package ru.danbeldev.h1hackapi.features.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.danbeldev.h1hackapi.features.dto.ProjectEntityDto;
import ru.danbeldev.h1hackapi.features.dto.ReportEntityDto;
import ru.danbeldev.h1hackapi.features.mappers.ProjectEntityMapper;
import ru.danbeldev.h1hackapi.features.mappers.ReportEntityMapper;
import ru.danbeldev.h1hackapi.features.services.ProjectService;
import ru.danbeldev.h1hackapi.features.services.ReportService;
import ru.danbeldev.h1hackapi.features.services.importData.CsvTaskImportService;
import ru.danbeldev.h1hackapi.features.services.importData.JsonTaskImportService;
import ru.danbeldev.h1hackapi.features.services.importData.models.Prop;
import ru.danbeldev.h1hackapi.features.services.importData.models.ValueType;
import ru.danbeldev.h1hackapi.features.services.importData.update.CsvTaskUpdateImportService;
import ru.danbeldev.h1hackapi.features.services.importData.update.JsonTaskUpdateImportService;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    private final ProjectEntityMapper projectEntityMapper;
    private final CsvTaskImportService csvTaskImportService;
    private final ReportService reportService;
    private final ReportEntityMapper reportEntityMapper;
    private final JsonTaskImportService jsonTaskImportService;
    private final JsonTaskUpdateImportService jsonTaskUpdateImportService;
    private final CsvTaskUpdateImportService csvTaskUpdateImportService;

    @GetMapping("/{id}")
    public ProjectEntityDto getById(@PathVariable Long id) {
        return projectEntityMapper.toDto(projectService.getById(id));
    }

    @GetMapping
    public List<ProjectEntityDto> getAll() {
        return projectService.getAll().stream().map(projectEntityMapper::toDto).toList();
    }

    @GetMapping("/{id}/properties")
    public List<Prop> getAllProperties(
            @PathVariable Long id
    ) {
        return projectService.getAllProperties(id);
    }

    @GetMapping("/{id}/tasks")
    public Map<String, List<String>> getTasks(
            @PathVariable Long id
    ) {
        return projectService.getTasks(id);
    }

    @GetMapping("/{id}/reports")
    public List<ReportEntityDto> getAllReports(@PathVariable Long id) {
        return reportService.getAllByProjectId(id).stream().map(reportEntityMapper::toDto).toList();
    }

    @PostMapping
    public Long create(@RequestParam String name) {
        return projectService.create(name).getId();
    }

    @PostMapping(value = "/import", consumes = "multipart/form-data")
    public void importData(
            @RequestParam Long projectId,
            @RequestParam("file") MultipartFile file
    ) {
        if (file.getContentType() != null) {
            if (file.getContentType().equals("application/json")) {
                jsonTaskImportService.importData(projectId, file);
            } else if (file.getContentType().equals("text/csv")) {
                csvTaskImportService.importData(projectId, file);
            } else {
                throw new IllegalArgumentException("Unsupported file type: " + file.getContentType());
            }
        } else {
            throw new IllegalArgumentException("File type is unknown.");
        }
    }

    @PostMapping(value = "/task-update/import", consumes = "multipart/form-data")
    public void importDataTaskUpdate(
            @RequestParam Long projectId,
            @RequestParam("file") MultipartFile file
    ) {
        if (file.getContentType() != null) {
            if (file.getContentType().equals("application/json")) {
                jsonTaskUpdateImportService.importData(projectId, file);
            } else if (file.getContentType().equals("text/csv")) {
                csvTaskUpdateImportService.importData(projectId, file);
            } else {
                throw new IllegalArgumentException("Unsupported file type: " + file.getContentType());
            }
        } else {
            throw new IllegalArgumentException("File type is unknown.");
        }
    }
}
