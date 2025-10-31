package com.microinternship.skillbridge.repository;

import com.microinternship.skillbridge.entity.InternshipEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InternshipEnrollmentRepository extends JpaRepository<InternshipEnrollment, Long> {

    // Get all enrollments for a specific student
    List<InternshipEnrollment> findByStudentId(Long studentId);

    // Get all enrollments for a specific internship
    List<InternshipEnrollment> findByInternshipId(Long internshipId);

    // Check if a student already applied for a specific internship
    Optional<InternshipEnrollment> findByStudentIdAndInternshipId(Long studentId, Long internshipId);

    // Get all enrollments for all internships of a specific client
    List<InternshipEnrollment> findByInternship_ClientId(Long clientId);
}
