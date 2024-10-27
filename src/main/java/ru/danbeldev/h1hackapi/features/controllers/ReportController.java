package ru.danbeldev.h1hackapi.features.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.danbeldev.h1hackapi.features.dto.ReportElementEntityDto;
import ru.danbeldev.h1hackapi.features.dto.ReportEntityDto;
import ru.danbeldev.h1hackapi.features.mappers.ReportElementEntityMapper;
import ru.danbeldev.h1hackapi.features.mappers.ReportEntityMapper;
import ru.danbeldev.h1hackapi.features.services.ReportService;
import ru.danbeldev.h1hackapi.features.services.importData.models.Prop;
import ru.danbeldev.h1hackapi.features.services.importData.models.ValueType;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportController {

    private final ReportEntityMapper reportEntityMapper;
    private final ReportElementEntityMapper reportElementEntityMapper;
    private final ReportService reportService;

    @GetMapping
    public List<ReportEntityDto> getAll() {
        return reportService.getAll().stream().map(reportEntityMapper::toDto).toList();
    }

    @GetMapping("/{id}/properties")
    public List<Prop> getAllProperties(
            @PathVariable Long id
    ) {
        return reportService.getAllProperties(id);
    }

    @GetMapping("/{id}/tasks")
    public Map<String, List<String>> getTasks(
            @PathVariable Long id
    ) {
        return reportService.getTasks(id);
    }

    @GetMapping("/{id}")
    public ReportEntityDto getById(@PathVariable Long id) {
        return reportEntityMapper.toDto(reportService.getById(id));
    }

    @PostMapping
    public ReportEntityDto create(@RequestParam Long projectId, @RequestParam String name) {
        return reportEntityMapper.toDto(reportService.create(projectId, name));
    }

    @GetMapping("/{id}/elements")
    public List<ReportElementEntityDto> getAllElements(@PathVariable Long id) {
        return reportService.getAllElements(id).stream().map(reportElementEntityMapper::toDto).toList();
    }

    @PostMapping("/{id}/elements")
    public void saveAllElements(@PathVariable Long id, @RequestBody List<ReportElementEntityDto> elements) {
        reportService.saveAllElements(id, elements);
    }
}
