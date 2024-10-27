package ru.danbeldev.h1hackapi.features.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.danbeldev.h1hackapi.features.dto.ReportElementEntityDto;
import ru.danbeldev.h1hackapi.features.entities.ReportElementEntity;
import ru.danbeldev.h1hackapi.features.entities.ReportEntity;
import ru.danbeldev.h1hackapi.features.repositories.ReportElementEntityRepository;
import ru.danbeldev.h1hackapi.features.repositories.ReportRepository;
import ru.danbeldev.h1hackapi.features.services.importData.models.Prop;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    private final ProjectService projectService;

    private final ReportElementEntityRepository reportElementEntityRepository;

    @Transactional(readOnly = true)
    public List<ReportEntity> getAll() {
        return reportRepository.findAll();
    }

    @Transactional(readOnly = true)
    public ReportEntity getById(Long id) {
        return reportRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Transactional
    public ReportEntity create(Long projectId, String name) {
        var project = projectService.getById(projectId);
        var report = new ReportEntity();
        report.setName(name);
        report.setProject(project);
        report.setDateCreation(Instant.now());
        report.setDateUpdated(null);
        return reportRepository.save(report);
    }

    @Transactional(readOnly = true)
    public List<ReportElementEntity> getAllElements(Long reportId) {
        var report = getById(reportId);
        return report.getElements();
    }

    @Transactional
    public void saveAllElements(Long reportId, List<ReportElementEntityDto> elements) {
        var report = getById(reportId);
        report.setElements(map(report, elements));
        reportElementEntityRepository.saveAll(report.getElements());
    }

    private List<ReportElementEntity> map(ReportEntity report ,List<ReportElementEntityDto> dtos) {
        var list = new ArrayList<ReportElementEntity>();
        for (int i = 0; i < dtos.size(); i++) {
            var dto = dtos.get(i);
            var en = new ReportElementEntity();
            en.setId(dto.id());
            en.setType(dto.type());
            en.setData(dto.data());
            en.setStyles(dto.styles());
            en.setOrder(i);
            en.setReport(report);
            list.add(en);
        }
        return list;
    }

    public List<ReportEntity> getAllByProjectId(Long id) {
        return reportRepository.findAllByProjectId(id);
    }

    @Transactional(readOnly = true)
    public List<Prop> getAllProperties(Long id) {
        var report = getById(id);
        return projectService.getAllProperties(report.getProject().getId());
    }

    @Transactional(readOnly = true)
    public Map<String, List<String>> getTasks(Long id) {
        var report = getById(id);
        return projectService.getTasks(report.getProject().getId());
    }
}
