package com.microinternship.skillbridge.service;

import com.microinternship.skillbridge.entity.Internship;
import com.microinternship.skillbridge.entity.InternshipApplication;
import com.microinternship.skillbridge.entity.Student;
import com.microinternship.skillbridge.repository.InternshipApplicationRepository;
import com.microinternship.skillbridge.repository.InternshipRepository;
import com.microinternship.skillbridge.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InternshipApplicationService {

    @Autowired
    private InternshipApplicationRepository applicationRepository;

    @Autowired
    private InternshipRepository internshipRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private NotificationService notificationService;

    public InternshipApplication applyForInternship(Long internshipId, Long studentId, String coverLetter) {
        // Check if already applied
        if (applicationRepository.existsByInternship_IdAndStudent_Id(internshipId, studentId)) {
            throw new RuntimeException("Already applied for this internship");
        }

        Internship internship = internshipRepository.findById(internshipId)
                .orElseThrow(() -> new RuntimeException("Internship not found"));

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        InternshipApplication application = new InternshipApplication();
        application.setInternship(internship);
        application.setStudent(student);
        application.setCoverLetter(coverLetter);

        InternshipApplication saved = applicationRepository.save(application);

        // Create notification for client
        String message = String.format("%s applied for %s internship", 
                student.getName(), internship.getTitle());
        notificationService.createNotification("client", internship.getClient().getId(), 
                message, "internship_application", saved.getId());

        return saved;
    }

    public List<InternshipApplication> getStudentApplications(Long studentId) {
        return applicationRepository.findByStudent_Id(studentId);
    }

    public List<InternshipApplication> getInternshipApplications(Long internshipId) {
        return applicationRepository.findByInternship_Id(internshipId);
    }

    public List<InternshipApplication> getClientApplications(Long clientId) {
        return applicationRepository.findByClientId(clientId);
    }

    public InternshipApplication updateApplicationStatus(Long applicationId, String status) {
        InternshipApplication application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        
        application.setStatus(status);
        InternshipApplication updated = applicationRepository.save(application);

        // Notify student
        String message = String.format("Your application for %s has been %s", 
                application.getInternship().getTitle(), status);
        notificationService.createNotification("student", application.getStudent().getId(), 
                message, "application_status", applicationId);

        return updated;
    }

    public boolean hasApplied(Long internshipId, Long studentId) {
        return applicationRepository.existsByInternship_IdAndStudent_Id(internshipId, studentId);
    }
}
