package com.microinternship.skillbridge.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.time.LocalDate;

@Entity
@Table(name = "internship_enrollments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "internship_id"}))
public class InternshipEnrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many enrollments belong to one student
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_student"))
    @JsonIgnoreProperties({"interests", "tests", "internships"})
    private Student student;

    // Many enrollments belong to one internship
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "internship_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_internship"))
    @JsonIgnoreProperties({"enrollments"})
    private Internship internship;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status = Status.APPLIED;

    @Column(name = "applied_on", nullable = false)
    private LocalDate appliedOn = LocalDate.now();

    @Column(name = "completed_on")
    private LocalDate completedOn;

    public enum Status {
        APPLIED,
        APPROVED,
        REJECTED,
        IN_PROGRESS,
        COMPLETED
    }

    public InternshipEnrollment() {}

    public InternshipEnrollment(Student student, Internship internship) {
        this.student = student;
        this.internship = internship;
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    public Internship getInternship() { return internship; }
    public void setInternship(Internship internship) { this.internship = internship; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDate getAppliedOn() { return appliedOn; }
    public void setAppliedOn(LocalDate appliedOn) { this.appliedOn = appliedOn; }

    public LocalDate getCompletedOn() { return completedOn; }
    public void setCompletedOn(LocalDate completedOn) { this.completedOn = completedOn; }

    @Override
    public String toString() {
        return "InternshipEnrollment{" +
                "id=" + id +
                ", student=" + (student != null ? student.getId() : null) +
                ", internship=" + (internship != null ? internship.getId() : null) +
                ", status=" + status +
                ", appliedOn=" + appliedOn +
                ", completedOn=" + completedOn +
                '}';
    }
}
