package ru.danbeldev.h1hackapi.features.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.danbeldev.h1hackapi.features.entities.ReportEntity;

import java.util.List;

public interface ReportRepository extends JpaRepository<ReportEntity, Long> {

    List<ReportEntity> findAllByProjectId(Long id);
}
