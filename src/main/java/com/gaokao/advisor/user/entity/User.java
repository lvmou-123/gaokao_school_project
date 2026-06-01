package com.gaokao.advisor.user.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "gaokao_user", indexes = {
        @Index(name = "idx_username", columnList = "username"),
        @Index(name = "idx_phone", columnList = "phone")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, unique = true)
    private String username;

    @Column(length = 255)
    private String password;

    @Column(name = "real_name", length = 50)
    private String realName;

    @Column(length = 20)
    private String phone;

    @Column(name = "open_id", length = 100, unique = true)
    private String openId;

    @Column(length = 100)
    private String nickname;

    @Column(length = 500)
    private String avatar;

    @Column(name = "student_id", length = 50)
    private String studentId;

    @Column(name = "exam_type", length = 20)
    private String examType;

    @Column(length = 20)
    private String province;

    @Column(name = "total_score")
    private Integer totalScore;

    @Column(name = "rank_num")
    private Integer rankNum;

    @Column(name = "graduation_year")
    private Integer graduationYear;

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
