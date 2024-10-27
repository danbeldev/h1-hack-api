package ru.danbeldev.h1hackapi.features.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Getter
@Setter
@Entity
@Table(name = "report_elements")
public class ReportElementEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "\"order\"", nullable = false)
    private Integer order;

    @NotNull
    @Column(name = "type", nullable = false, length = 48)
    private String type;

    @Column(name = "data")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> data;

    @Column(name = "styles")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> styles;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "report_id", nullable = false)
    private ReportEntity report;

}