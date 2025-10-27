-- Create new tables for notifications and internship applications

USE skill_bridge;

-- Notifications table
CREATE TABLE IF NOT EXISTS notifications (
    id BIGINT NOT NULL AUTO_INCREMENT,
    user_type VARCHAR(20) NOT NULL,
    user_id BIGINT NOT NULL,
    message VARCHAR(500) NOT NULL,
    notification_type VARCHAR(50),
    reference_id BIGINT,
    is_read BOOLEAN DEFAULT FALSE,
    created_at DATETIME,
    PRIMARY KEY (id),
    INDEX idx_user (user_type, user_id),
    INDEX idx_read (is_read)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Internship Applications table
CREATE TABLE IF NOT EXISTS internship_applications (
    id BIGINT NOT NULL AUTO_INCREMENT,
    internship_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'pending',
    applied_at DATETIME,
    cover_letter VARCHAR(1000),
    PRIMARY KEY (id),
    FOREIGN KEY (internship_id) REFERENCES internships(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    UNIQUE KEY unique_application (internship_id, student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Show tables
SHOW TABLES;
DESCRIBE notifications;
DESCRIBE internship_applications;
