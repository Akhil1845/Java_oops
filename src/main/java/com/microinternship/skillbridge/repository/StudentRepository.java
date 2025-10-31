package com.microinternship.skillbridge.repository;

import com.microinternship.skillbridge.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    // Find by email (for registration check)
    Optional<Student> findByEmail(String email);

    // Find by email and password (for login)
    Optional<Student> findByEmailAndPassword(String email, String password);

    // Find students by class
    List<Student> findByClassName(String className);

    // Find students by stream
    List<Student> findByStream(String stream);

    // Find students by program
    List<Student> findByProgram(String program);

    // Find students by semester
    List<Student> findBySemester(String semester);

    // Find students by skills (partial match, case-insensitive)
    List<Student> findBySkillsContainingIgnoreCase(String skill);

    // 🔹 Extra useful queries

    // Find by phone number
    Optional<Student> findByPhone(String phone);

    // Find students by name (partial match, case-insensitive)
    List<Student> findByNameContainingIgnoreCase(String name);
}
