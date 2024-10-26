package ru.danbeldev.h1hackapi.features.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.danbeldev.h1hackapi.features.entities.TaskUpdate;

public interface TaskUpdateRepository extends JpaRepository<TaskUpdate, Long> {
}