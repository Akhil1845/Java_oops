-- ============================================================================
-- SKILLBRIDGE ADVANCED FEATURES - DATABASE SCHEMA
-- Run this AFTER your existing tables are working
-- ============================================================================

USE skill_bridge;

-- ============================================================================
-- 1. STUDENT SKILLS & PROFILE ENHANCEMENTS
-- ============================================================================

-- Skills Master Table
CREATE TABLE IF NOT EXISTS skills (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    category VARCHAR(50), -- 'technical', 'soft', 'language', 'tool'
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Student Skills (Many-to-Many)
CREATE TABLE IF NOT EXISTS student_skills (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    proficiency_level VARCHAR(20) DEFAULT 'beginner', -- 'beginner', 'intermediate', 'advanced', 'expert'
    verified BOOLEAN DEFAULT FALSE,
    added_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE,
    UNIQUE KEY unique_student_skill (student_id, skill_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Resume/Documents Storage
CREATE TABLE IF NOT EXISTS student_documents (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    document_type VARCHAR(50) NOT NULL, -- 'resume', 'certificate', 'project', 'portfolio'
    file_name VARCHAR(255) NOT NULL,
    file_path VARCHAR(500) NOT NULL,
    file_size BIGINT,
    uploaded_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    INDEX idx_student_type (student_id, document_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================================
-- 2. GAMIFICATION SYSTEM
-- ============================================================================

-- Student Points/XP
CREATE TABLE IF NOT EXISTS student_gamification (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL UNIQUE,
    total_points INT DEFAULT 0,
    level INT DEFAULT 1,
    current_streak INT DEFAULT 0,
    longest_streak INT DEFAULT 0,
    last_activity_date DATE,
    PRIMARY KEY (id),
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Badges/Achievements
CREATE TABLE IF NOT EXISTS badges (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    icon_url VARCHAR(255),
    points_required INT,
    badge_type VARCHAR(50), -- 'skill', 'activity', 'achievement', 'special'
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Student Badges (Earned)
CREATE TABLE IF NOT EXISTS student_badges (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    badge_id BIGINT NOT NULL,
    earned_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (badge_id) REFERENCES badges(id) ON DELETE CASCADE,
    UNIQUE KEY unique_student_badge (student_id, badge_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Activity Log (for points calculation)
CREATE TABLE IF NOT EXISTS activity_log (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    user_type VARCHAR(20) NOT NULL, -- 'student', 'client'
    activity_type VARCHAR(50) NOT NULL, -- 'test_completed', 'internship_applied', 'profile_updated', etc.
    points_earned INT DEFAULT 0,
    reference_id BIGINT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_user (user_type, user_id),
    INDEX idx_activity (activity_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================================
-- 3. COURSES & LEARNING PATHS
-- ============================================================================

-- Courses/Training
CREATE TABLE IF NOT EXISTS courses (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    provider VARCHAR(100), -- 'Coursera', 'Udemy', 'Internal', etc.
    category VARCHAR(50),
    difficulty_level VARCHAR(20), -- 'beginner', 'intermediate', 'advanced'
    duration_hours INT,
    external_url VARCHAR(500),
    thumbnail_url VARCHAR(500),
    is_free BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_category (category),
    INDEX idx_provider (provider)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Course Skills (What skills this course teaches)
CREATE TABLE IF NOT EXISTS course_skills (
    id BIGINT NOT NULL AUTO_INCREMENT,
    course_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE,
    UNIQUE KEY unique_course_skill (course_id, skill_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Student Course Enrollment
CREATE TABLE IF NOT EXISTS student_courses (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    enrollment_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    completion_date DATETIME,
    progress_percentage INT DEFAULT 0,
    status VARCHAR(20) DEFAULT 'enrolled', -- 'enrolled', 'in_progress', 'completed', 'dropped'
    certificate_url VARCHAR(500),
    PRIMARY KEY (id),
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    UNIQUE KEY unique_enrollment (student_id, course_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Learning Paths/Roadmaps
CREATE TABLE IF NOT EXISTS learning_paths (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    target_role VARCHAR(100), -- 'Data Analyst', 'Frontend Developer', etc.
    difficulty_level VARCHAR(20),
    estimated_duration_weeks INT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Learning Path Courses (Ordered)
CREATE TABLE IF NOT EXISTS learning_path_courses (
    id BIGINT NOT NULL AUTO_INCREMENT,
    path_id BIGINT NOT NULL,
    course_id BIGINT NOT NULL,
    sequence_order INT NOT NULL,
    is_mandatory BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (id),
    FOREIGN KEY (path_id) REFERENCES learning_paths(id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================================
-- 4. COMPANY REVIEWS & RATINGS
-- ============================================================================

-- Company Reviews
CREATE TABLE IF NOT EXISTS company_reviews (
    id BIGINT NOT NULL AUTO_INCREMENT,
    client_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    review_text TEXT,
    work_culture_rating INT CHECK (work_culture_rating BETWEEN 1 AND 5),
    learning_rating INT CHECK (learning_rating BETWEEN 1 AND 5),
    stipend_rating INT CHECK (stipend_rating BETWEEN 1 AND 5),
    is_approved BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    INDEX idx_client (client_id),
    INDEX idx_approved (is_approved)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Company Verification
CREATE TABLE IF NOT EXISTS company_verification (
    id BIGINT NOT NULL AUTO_INCREMENT,
    client_id BIGINT NOT NULL UNIQUE,
    is_verified BOOLEAN DEFAULT FALSE,
    verification_date DATETIME,
    verification_documents TEXT,
    verified_by VARCHAR(100),
    notes TEXT,
    PRIMARY KEY (id),
    FOREIGN KEY (client_id) REFERENCES clients(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================================
-- 5. MENTORSHIP SYSTEM
-- ============================================================================

-- Mentors
CREATE TABLE IF NOT EXISTS mentors (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    company VARCHAR(255),
    designation VARCHAR(100),
    expertise_area VARCHAR(255),
    years_of_experience INT,
    linkedin_url VARCHAR(500),
    bio TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Mentor-Student Matching
CREATE TABLE IF NOT EXISTS mentorship_connections (
    id BIGINT NOT NULL AUTO_INCREMENT,
    mentor_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    status VARCHAR(20) DEFAULT 'pending', -- 'pending', 'active', 'completed', 'cancelled'
    requested_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    started_at DATETIME,
    ended_at DATETIME,
    PRIMARY KEY (id),
    FOREIGN KEY (mentor_id) REFERENCES mentors(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================================
-- 6. PROJECTS & PORTFOLIO
-- ============================================================================

-- Student Projects
CREATE TABLE IF NOT EXISTS student_projects (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    project_url VARCHAR(500),
    github_url VARCHAR(500),
    demo_url VARCHAR(500),
    technologies_used TEXT,
    start_date DATE,
    end_date DATE,
    is_featured BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Project Skills
CREATE TABLE IF NOT EXISTS project_skills (
    id BIGINT NOT NULL AUTO_INCREMENT,
    project_id BIGINT NOT NULL,
    skill_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (project_id) REFERENCES student_projects(id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================================
-- 7. ANALYTICS & RECOMMENDATIONS
-- ============================================================================

-- Recommendation History (for ML tracking)
CREATE TABLE IF NOT EXISTS recommendation_history (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    user_type VARCHAR(20) NOT NULL,
    recommendation_type VARCHAR(50) NOT NULL, -- 'internship', 'course', 'skill', 'mentor'
    recommended_id BIGINT NOT NULL,
    score DECIMAL(5,2),
    was_clicked BOOLEAN DEFAULT FALSE,
    was_applied BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_user (user_type, user_id),
    INDEX idx_type (recommendation_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Skill Gap Analysis
CREATE TABLE IF NOT EXISTS skill_gap_analysis (
    id BIGINT NOT NULL AUTO_INCREMENT,
    student_id BIGINT NOT NULL,
    target_role VARCHAR(100) NOT NULL,
    required_skills TEXT, -- JSON array of skill IDs
    missing_skills TEXT, -- JSON array of skill IDs
    proficiency_gaps TEXT, -- JSON object
    recommended_courses TEXT, -- JSON array of course IDs
    analysis_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================================
-- 8. ADMIN & SYSTEM TABLES
-- ============================================================================

-- Admin Users
CREATE TABLE IF NOT EXISTS admin_users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) DEFAULT 'admin', -- 'super_admin', 'admin', 'moderator'
    is_active BOOLEAN DEFAULT TRUE,
    last_login DATETIME,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- System Settings
CREATE TABLE IF NOT EXISTS system_settings (
    id BIGINT NOT NULL AUTO_INCREMENT,
    setting_key VARCHAR(100) NOT NULL UNIQUE,
    setting_value TEXT,
    description TEXT,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================================
-- 9. EVENTS & WEBINARS
-- ============================================================================

-- Events/Webinars
CREATE TABLE IF NOT EXISTS events (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    event_type VARCHAR(50), -- 'webinar', 'workshop', 'hackathon', 'seminar'
    organizer_id BIGINT,
    organizer_type VARCHAR(20), -- 'client', 'admin', 'mentor'
    event_date DATETIME NOT NULL,
    duration_minutes INT,
    meeting_link VARCHAR(500),
    max_participants INT,
    is_free BOOLEAN DEFAULT TRUE,
    status VARCHAR(20) DEFAULT 'upcoming', -- 'upcoming', 'ongoing', 'completed', 'cancelled'
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    INDEX idx_date (event_date),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Event Registrations
CREATE TABLE IF NOT EXISTS event_registrations (
    id BIGINT NOT NULL AUTO_INCREMENT,
    event_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    registration_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    attendance_status VARCHAR(20) DEFAULT 'registered', -- 'registered', 'attended', 'absent'
    certificate_issued BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (id),
    FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    UNIQUE KEY unique_registration (event_id, student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================================
-- INSERT SAMPLE DATA
-- ============================================================================

-- Sample Skills
INSERT INTO skills (name, category, description) VALUES
('Java', 'technical', 'Object-oriented programming language'),
('Python', 'technical', 'High-level programming language'),
('JavaScript', 'technical', 'Web programming language'),
('React', 'technical', 'Frontend JavaScript library'),
('Node.js', 'technical', 'JavaScript runtime'),
('SQL', 'technical', 'Database query language'),
('Communication', 'soft', 'Effective communication skills'),
('Teamwork', 'soft', 'Collaboration and teamwork'),
('Problem Solving', 'soft', 'Analytical thinking'),
('Leadership', 'soft', 'Team leadership abilities');

-- Sample Badges
INSERT INTO badges (name, description, points_required, badge_type) VALUES
('First Steps', 'Complete your profile', 50, 'achievement'),
('Test Master', 'Complete 5 tests', 200, 'activity'),
('Internship Hunter', 'Apply to 10 internships', 300, 'activity'),
('Skill Collector', 'Add 10 skills to profile', 150, 'skill'),
('Rising Star', 'Reach level 5', 500, 'achievement'),
('Perfect Score', 'Score 100% on any test', 250, 'achievement'),
('Early Bird', 'Login for 7 consecutive days', 100, 'special'),
('Networker', 'Connect with 5 mentors', 200, 'activity');

-- Sample Learning Paths
INSERT INTO learning_paths (title, description, target_role, difficulty_level, estimated_duration_weeks) VALUES
('Full Stack Web Developer', 'Complete path to become a full stack developer', 'Full Stack Developer', 'intermediate', 24),
('Data Science Fundamentals', 'Learn data science from scratch', 'Data Scientist', 'beginner', 16),
('Frontend Developer', 'Master modern frontend development', 'Frontend Developer', 'beginner', 12),
('Backend Developer', 'Become a backend development expert', 'Backend Developer', 'intermediate', 16);

-- ============================================================================
-- VERIFICATION QUERIES
-- ============================================================================

-- Check all new tables
SHOW TABLES;

-- Count records
SELECT 'skills' as table_name, COUNT(*) as count FROM skills
UNION ALL SELECT 'badges', COUNT(*) FROM badges
UNION ALL SELECT 'learning_paths', COUNT(*) FROM learning_paths;
