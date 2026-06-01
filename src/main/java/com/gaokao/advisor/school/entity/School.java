package com.gaokao.advisor.school.entity;

import com.gaokao.advisor.major.entity.Major;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "gaokao_school", indexes = {
        @Index(name = "idx_name", columnList = "name"),
        @Index(name = "idx_province", columnList = "province")
})
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(unique = true, length = 20)
    private String code;

    @Column(length = 20)
    private String province;

    @Column(length = 20)
    private String city;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "gaokao_school_tag", joinColumns = @JoinColumn(name = "school_id"))
    @Column(name = "tag", length = 20)
    private Set<String> tags = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "gaokao_school_major",
            joinColumns = @JoinColumn(name = "school_id"),
            inverseJoinColumns = @JoinColumn(name = "major_id"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Major> majors = new HashSet<>();

    @Column(length = 50)
    private String category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 255)
    private String logo;

    @Column(length = 255)
    private String website;

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
