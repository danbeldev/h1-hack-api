package ru.danbeldev.h1hackapi.features.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Entity(name = "reports")
public class ReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 256)
    private String name;

    @Column(name = "date_creation", nullable = false)
    private Instant dateCreation;

    @Column(name = "date_updated")
    private Instant dateUpdated;

    @ManyToOne(fetch = FetchType.LAZY)
    private ProjectEntity project;

    @OneToMany(mappedBy = "report", fetch = FetchType.LAZY)
    private List<ReportElementEntity> elements;
}