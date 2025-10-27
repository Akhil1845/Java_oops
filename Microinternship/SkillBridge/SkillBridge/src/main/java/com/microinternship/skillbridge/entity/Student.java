package com.microinternship.skillbridge.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "class_name")
    private String className;

    private String stream;
    private String phone;
    private String address;

    private String program;
    private String semester;
    private String skills;

    // ================== RELATIONSHIPS ==================

    // Many-to-Many with interests
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinTable(
            name = "student_interests",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "interest_id")
    )
    @JsonIgnoreProperties("students")
    private Set<Interest> interests = new HashSet<>();

    // Many-to-Many with tests
    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
    @JoinTable(
            name = "student_test",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "test_id")
    )
    @JsonIgnoreProperties("students")
    private Set<Test> tests = new HashSet<>();

    // ✅ One-to-Many with InternshipEnrollment
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("student")
    private Set<InternshipEnrollment> enrollments = new HashSet<>();

    // ================== CONSTRUCTORS ==================
    public Student() {}

    public Student(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // ================== GETTERS & SETTERS ==================
    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }

    public String getStream() { return stream; }
    public void setStream(String stream) { this.stream = stream; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getProgram() { return program; }
    public void setProgram(String program) { this.program = program; }

    public String getSemester() { return semester; }
    public void setSemester(String semester) { this.semester = semester; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public Set<Interest> getInterests() { return interests; }
    public void setInterests(Set<Interest> interests) { this.interests = interests; }

    public Set<Test> getTests() { return tests; }
    public void setTests(Set<Test> tests) { this.tests = tests; }

    public Set<InternshipEnrollment> getEnrollments() { return enrollments; }
    public void setEnrollments(Set<InternshipEnrollment> enrollments) { this.enrollments = enrollments; }
}
