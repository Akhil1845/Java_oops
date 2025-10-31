package com.microinternship.skillbridge.service;

import com.microinternship.skillbridge.entity.InternshipEnrollment;
import com.microinternship.skillbridge.entity.Student;
import com.microinternship.skillbridge.entity.Internship;
import com.microinternship.skillbridge.repository.InternshipEnrollmentRepository;
import com.microinternship.skillbridge.repository.StudentRepository;
import com.microinternship.skillbridge.repository.InternshipRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class InternshipEnrollmentService {

    private final InternshipEnrollmentRepository enrollmentRepo;
    private final StudentRepository studentRepo;
    private final InternshipRepository internshipRepo;

    public InternshipEnrollmentService(InternshipEnrollmentRepository enrollmentRepo,
                                       StudentRepository studentRepo,
                                       InternshipRepository internshipRepo) {
        this.enrollmentRepo = enrollmentRepo;
        this.studentRepo = studentRepo;
        this.internshipRepo = internshipRepo;
    }

    // ==================== APPLY ====================
    @Transactional
    public InternshipEnrollment apply(Long studentId, Long internshipId) {
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        Internship internship = internshipRepo.findById(internshipId)
                .orElseThrow(() -> new RuntimeException("Internship not found"));

        // Check if already applied
        Optional<InternshipEnrollment> existing = enrollmentRepo
                .findByStudentIdAndInternshipId(studentId, internshipId);
        if (existing.isPresent()) {
            throw new RuntimeException("Student already applied for this internship");
        }

        InternshipEnrollment enrollment = new InternshipEnrollment(student, internship);
        return enrollmentRepo.save(enrollment);
    }

    // ==================== GET BY STUDENT ====================
    public List<InternshipEnrollment> getByStudent(Long studentId) {
        return enrollmentRepo.findByStudentId(studentId);
    }

    // ==================== GET BY CLIENT ====================
    public List<InternshipEnrollment> getByClient(Long clientId) {
        return enrollmentRepo.findByInternship_ClientId(clientId);
    }

    // ==================== UPDATE STATUS ====================
    @Transactional
    public InternshipEnrollment updateStatus(Long enrollmentId, InternshipEnrollment.Status status) {
        InternshipEnrollment enrollment = enrollmentRepo.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        enrollment.setStatus(status);

        // Set completedOn if approved or rejected
        if (status == InternshipEnrollment.Status.APPROVED || status == InternshipEnrollment.Status.REJECTED) {
            enrollment.setCompletedOn(LocalDate.now());
        }

        return enrollmentRepo.save(enrollment);
    }

    // ==================== GET ALL ====================
    public List<InternshipEnrollment> getAllEnrollments() {
        return enrollmentRepo.findAll();
    }
}
