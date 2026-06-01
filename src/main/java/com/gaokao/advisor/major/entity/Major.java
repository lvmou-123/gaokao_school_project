package com.gaokao.advisor.major.entity;

import com.gaokao.advisor.school.entity.School;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "gaokao_major", indexes = {
        @Index(name = "idx_major_name", columnList = "name"),
        @Index(name = "idx_category", columnList = "category")
})
public class Major {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 20)
    private String code;

    @Column(length = 50)
    private String category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToMany(mappedBy = "majors")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<School> schools = new HashSet<>();

    @Column(length = 10)
    private String duration;

    @Column(length = 50)
    private String degree;

    @Column(name = "gender_ratio", length = 50)
    private String genderRatio;

    @Column(name = "avg_salary", length = 50)
    private String avgSalary;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
