-- MySQL 초기화 스크립트
-- 데이터베이스가 존재하지 않으면 생성
CREATE DATABASE IF NOT EXISTS gatheria CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- gatheria 데이터베이스 사용
USE gatheria;

-- 기존 테이블 제거 (만약 있다면)
DROP TABLE IF EXISTS lecture_students;
DROP TABLE IF EXISTS team_members;
DROP TABLE IF EXISTS teams;
DROP TABLE IF EXISTS lectures;
DROP TABLE IF EXISTS instructors;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS members;

-- 회원 테이블 생성
CREATE TABLE members
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '멤버 ID',
    email      VARCHAR(255) NOT NULL UNIQUE COMMENT '이메일',
    password   VARCHAR(255) NOT NULL COMMENT '비밀번호',
    name       VARCHAR(100) NOT NULL COMMENT '이름',
    phone      VARCHAR(20) COMMENT '전화번호',
    active     BOOLEAN   DEFAULT FALSE COMMENT '활성화 여부',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    INDEX idx_member_email (email)
);

-- 학생 테이블 생성
CREATE TABLE students
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '학생 ID',
    member_id  BIGINT NOT NULL UNIQUE COMMENT '멤버 ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    FOREIGN KEY (member_id) REFERENCES members (id) ON DELETE CASCADE
);

-- 교수 테이블 생성
CREATE TABLE instructors
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '교수 ID',
    member_id   BIGINT       NOT NULL UNIQUE COMMENT '멤버 ID',
    affiliation VARCHAR(255) NOT NULL COMMENT '소속 학과',
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    FOREIGN KEY (member_id) REFERENCES members (id) ON DELETE CASCADE
);

-- 수업 테이블 생성
CREATE TABLE lectures
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '수업 ID',
    name          VARCHAR(255) NOT NULL COMMENT '수업 이름',
    code          VARCHAR(10)  NOT NULL UNIQUE COMMENT '수업 코드',
    instructor_id BIGINT       NOT NULL COMMENT '교수자 ID',
    status        VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE' COMMENT '수업 상태 (ACTIVE, ARCHIVED)',
    classSize     INT          NOT NULL DEFAULT 30 COMMENT '수업 최대 인원수',
    created_at    TIMESTAMP             DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    updated_at    TIMESTAMP             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    FOREIGN KEY (instructor_id) REFERENCES instructors (id),
    INDEX idx_lecture_code (code),
    INDEX idx_lecture_status (status)
);

-- 팀 테이블 생성
CREATE TABLE teams
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '팀 ID',
    name       VARCHAR(255) NOT NULL COMMENT '팀 이름',
    lecture_id BIGINT       NOT NULL COMMENT '수업 ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    FOREIGN KEY (lecture_id) REFERENCES lectures (id) ON DELETE CASCADE,
    INDEX idx_team_lecture (lecture_id)
);

-- 수업 수강생 테이블 생성
CREATE TABLE lecture_students
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '수강생 ID',
    lecture_id BIGINT NOT NULL COMMENT '수업 ID',
    student_id BIGINT NOT NULL COMMENT '학생 ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    FOREIGN KEY (lecture_id) REFERENCES lectures (id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE,
    UNIQUE KEY uk_lecture_student (lecture_id, student_id),
    INDEX idx_lecture_students_lecture (lecture_id),
    INDEX idx_lecture_students_student (student_id)
);

-- 팀 멤버 테이블 생성
CREATE TABLE team_members
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '팀 멤버 ID',
    team_id    BIGINT NOT NULL COMMENT '팀 ID',
    student_id BIGINT NOT NULL COMMENT '학생 ID',
    lecture_id BIGINT NOT NULL COMMENT '수업 ID',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',
    FOREIGN KEY (team_id) REFERENCES teams (id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students (id) ON DELETE CASCADE,
    FOREIGN KEY (lecture_id) REFERENCES lectures (id) ON DELETE CASCADE,
    UNIQUE KEY uk_lecture_student_team (lecture_id, student_id),
    INDEX idx_team_members_team (team_id),
    INDEX idx_team_members_student (student_id),
    INDEX idx_team_members_lecture (lecture_id)
);