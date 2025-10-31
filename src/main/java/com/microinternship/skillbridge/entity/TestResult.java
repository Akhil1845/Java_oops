package com.microinternship.skillbridge.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "test_results")
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Relation with Student ---
    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    @JsonIgnoreProperties({"tests", "hibernateLazyInitializer", "handler"})
    private Student student;

    // --- Relation with Test ---
    @ManyToOne(optional = false)
    @JoinColumn(name = "test_id")
    @JsonIgnoreProperties({"students", "hibernateLazyInitializer", "handler"})
    private Test test;

    @Column(nullable = false)
    private Integer marks;

    @Column(nullable = false)
    private Integer maxMarks;

    // --- Timestamp for test submission ---
    @Column(nullable = false)
    private LocalDateTime createdAt;

    public TestResult() {}

    public TestResult(Student student, Test test, Integer marks, Integer maxMarks) {
        this.student = student;
        this.test = test;
        this.marks = marks;
        this.maxMarks = maxMarks;
        this.createdAt = LocalDateTime.now(); // Automatically set timestamp
    }

    // Getters / Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Test getTest() { return test; }
    public void setTest(Test test) { this.test = test; }

    public Integer getMarks() { return marks; }
    public void setMarks(Integer marks) { this.marks = marks; }

    public Integer getMaxMarks() { return maxMarks; }
    public void setMaxMarks(Integer maxMarks) { this.maxMarks = maxMarks; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
