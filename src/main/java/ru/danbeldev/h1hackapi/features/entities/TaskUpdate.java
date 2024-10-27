package ru.danbeldev.h1hackapi.features.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "task_updates")
public class TaskUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private ProjectEntity project;

    @Size(max = 64)
    @NotNull
    @Column(name = "property_name", nullable = false, length = 64)
    private String propertyName;

    @Size(max = 1080)
    @NotNull
    @Column(name = "old_value", nullable = false, length = 1080)
    private String oldValue;

    @Size(max = 1080)
    @Column(name = "new_value", length = 1080)
    private String newValue;

    @NotNull
    @Column(name = "data", nullable = false)
    private Instant data;

}