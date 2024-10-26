package ru.danbeldev.h1hackapi.features.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.danbeldev.h1hackapi.features.entities.ReportElementEntity;

public interface ReportElementEntityRepository extends JpaRepository<ReportElementEntity, Long> {
}