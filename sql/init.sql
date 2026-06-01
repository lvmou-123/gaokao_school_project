-- ========================================
-- 高考志愿填报小程序 - 数据库初始化脚本
-- ========================================

CREATE DATABASE IF NOT EXISTS gaokao_advisor
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_unicode_ci;

USE gaokao_advisor;

-- ==================== 用户表 ====================
DROP TABLE IF EXISTS gaokao_user;
CREATE TABLE gaokao_user
(
    id               BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    username         VARCHAR(50)          DEFAULT NULL COMMENT '用户名',
    password         VARCHAR(255)         DEFAULT NULL COMMENT '密码',
    real_name        VARCHAR(50)          DEFAULT NULL COMMENT '真实姓名',
    phone            VARCHAR(20)          DEFAULT NULL COMMENT '手机号',
    open_id          VARCHAR(100)         DEFAULT NULL COMMENT '微信OpenID',
    nickname         VARCHAR(100)         DEFAULT NULL COMMENT '微信昵称',
    avatar           VARCHAR(500)         DEFAULT NULL COMMENT '头像URL',
    student_id       VARCHAR(50)          DEFAULT NULL COMMENT '考生号',
    exam_type        VARCHAR(20)          DEFAULT NULL COMMENT '考试类型(物理类/历史类)',
    province         VARCHAR(20)          DEFAULT NULL COMMENT '省份',
    total_score      INT                  DEFAULT NULL COMMENT '高考总分',
    rank_num         INT                  DEFAULT NULL COMMENT '全省排名',
    graduation_year  INT                  DEFAULT NULL COMMENT '毕业年份',
    created_at       DATETIME(6) NOT NULL COMMENT '创建时间',
    updated_at       DATETIME(6) NOT NULL COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_open_id (open_id),
    INDEX idx_phone (phone)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='用户表';

-- ==================== 院校表 ====================
DROP TABLE IF EXISTS gaokao_school;
CREATE TABLE gaokao_school
(
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '院校ID',
    name        VARCHAR(100) NOT NULL COMMENT '院校名称',
    code        VARCHAR(20)           DEFAULT NULL COMMENT '院校代码',
    province    VARCHAR(20)           DEFAULT NULL COMMENT '所在省份',
    city        VARCHAR(20)           DEFAULT NULL COMMENT '所在城市',
    category    VARCHAR(50)           DEFAULT NULL COMMENT '院校类别(综合/理工/医药等)',
    description TEXT                   DEFAULT NULL COMMENT '院校简介',
    logo        VARCHAR(255)          DEFAULT NULL COMMENT 'Logo URL',
    website     VARCHAR(255)          DEFAULT NULL COMMENT '官网地址',
    created_at  DATETIME(6)  NOT NULL COMMENT '创建时间',
    updated_at  DATETIME(6)  NOT NULL COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_code (code),
    INDEX idx_name (name),
    INDEX idx_province (province)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='院校表';

-- ==================== 院校标签表 (ElementCollection) ====================
DROP TABLE IF EXISTS gaokao_school_tag;
CREATE TABLE gaokao_school_tag
(
    school_id BIGINT      NOT NULL COMMENT '院校ID',
    tag       VARCHAR(20) NOT NULL COMMENT '标签(985/211/双一流/普通本科)',
    PRIMARY KEY (school_id, tag),
    INDEX idx_tag (tag),
    CONSTRAINT fk_school_tag_school FOREIGN KEY (school_id) REFERENCES gaokao_school (id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='院校标签表';

-- ==================== 专业表 ====================
DROP TABLE IF EXISTS gaokao_major;
CREATE TABLE gaokao_major
(
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '专业ID',
    name        VARCHAR(100) NOT NULL COMMENT '专业名称',
    code        VARCHAR(20)           DEFAULT NULL COMMENT '专业代码',
    category    VARCHAR(50)           DEFAULT NULL COMMENT '学科门类(工学/理学/医学等)',
    description TEXT                   DEFAULT NULL COMMENT '专业描述',
    school_id   BIGINT                DEFAULT NULL COMMENT '所属院校ID',
    duration    VARCHAR(10)           DEFAULT NULL COMMENT '学制(四年/五年)',
    degree      VARCHAR(50)           DEFAULT NULL COMMENT '授予学位',
    created_at  DATETIME(6)  NOT NULL COMMENT '创建时间',
    updated_at  DATETIME(6)  NOT NULL COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_code (code),
    INDEX idx_name (name),
    INDEX idx_school_id (school_id),
    INDEX idx_category (category),
    CONSTRAINT fk_major_school FOREIGN KEY (school_id) REFERENCES gaokao_school (id) ON DELETE SET NULL
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='专业表';

-- ==================== 推荐记录表 ====================
DROP TABLE IF EXISTS gaokao_recommendation;
CREATE TABLE gaokao_recommendation
(
    id          BIGINT      NOT NULL AUTO_INCREMENT COMMENT '推荐ID',
    user_id     BIGINT      NOT NULL COMMENT '用户ID',
    school_id   BIGINT      NOT NULL COMMENT '院校ID',
    major_id    BIGINT               DEFAULT NULL COMMENT '专业ID',
    probability VARCHAR(20)          DEFAULT NULL COMMENT '录取概率(冲刺/稳妥/保底)',
    reason      TEXT                  DEFAULT NULL COMMENT '推荐理由',
    created_at  DATETIME(6) NOT NULL COMMENT '创建时间',
    PRIMARY KEY (id),
    INDEX idx_user_id (user_id),
    INDEX idx_school_id (school_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='推荐记录表';

-- ==================== 志愿表 ====================
DROP TABLE IF EXISTS gaokao_application;
CREATE TABLE gaokao_application
(
    id              BIGINT      NOT NULL AUTO_INCREMENT COMMENT '志愿ID',
    user_id         BIGINT      NOT NULL COMMENT '用户ID',
    school_id       BIGINT      NOT NULL COMMENT '院校ID',
    major_id        BIGINT               DEFAULT NULL COMMENT '专业ID',
    priority        INT         NOT NULL COMMENT '志愿序号(1-10)',
    status          VARCHAR(20)          DEFAULT NULL COMMENT '状态(draft/submitted/admitted)',
    admission_score INT                  DEFAULT NULL COMMENT '录取分数',
    admission_rank  INT                  DEFAULT NULL COMMENT '录取排名',
    created_at      DATETIME(6) NOT NULL COMMENT '创建时间',
    updated_at      DATETIME(6) NOT NULL COMMENT '更新时间',
    PRIMARY KEY (id),
    INDEX idx_user_id (user_id),
    INDEX idx_school_id (school_id),
    UNIQUE KEY uk_user_priority (user_id, priority)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='志愿表';
