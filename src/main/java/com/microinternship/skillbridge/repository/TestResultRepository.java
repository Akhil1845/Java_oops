package com.microinternship.skillbridge.repository;

import com.microinternship.skillbridge.entity.TestResult;
import com.microinternship.skillbridge.entity.Student;
import com.microinternship.skillbridge.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {

    // --- Fetch results by Student object ---
    List<TestResult> findByStudent(Student student);

    // --- Fetch results by Test object ---
    List<TestResult> findByTest(Test test);

    // --- Fetch results by Student ID ---
    List<TestResult> findByStudent_Id(Long studentId);

    // --- Fetch results by Test ID ---
    List<TestResult> findByTest_Id(Long testId);
}
