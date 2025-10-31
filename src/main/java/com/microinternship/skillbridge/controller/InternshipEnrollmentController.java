package com.microinternship.skillbridge.controller;

import com.microinternship.skillbridge.entity.InternshipEnrollment;
import com.microinternship.skillbridge.service.InternshipEnrollmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin(origins = "*")
public class InternshipEnrollmentController {

    private final InternshipEnrollmentService service;

    public InternshipEnrollmentController(InternshipEnrollmentService service) {
        this.service = service;
    }

    // ==================== DTOs ====================
    public static class EnrollmentRequest {
        public Long studentId;
        public Long internshipId;
    }

    public static class StatusUpdateRequest {
        public String status; // "APPROVED" or "REJECTED"
    }

    // ==================== APPLY ====================
    @PostMapping("/apply")
    public ResponseEntity<?> apply(@RequestBody EnrollmentRequest request) {
        try {
            InternshipEnrollment enrollment = service.apply(request.studentId, request.internshipId);
            return ResponseEntity.ok(enrollment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error applying for internship: " + e.getMessage());
        }
    }

    // ==================== GET BY STUDENT ====================
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<InternshipEnrollment>> getByStudent(@PathVariable Long studentId) {
        return ResponseEntity.ok(service.getByStudent(studentId));
    }

    // ==================== GET BY CLIENT ====================
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<InternshipEnrollment>> getByClient(@PathVariable Long clientId) {
        try {
            List<InternshipEnrollment> enrollments = service.getByClient(clientId);
            return ResponseEntity.ok(enrollments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }

    // ==================== UPDATE STATUS ====================
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id,
                                          @RequestBody StatusUpdateRequest request) {
        try {
            InternshipEnrollment.Status status = InternshipEnrollment.Status.valueOf(request.status.toUpperCase());
            InternshipEnrollment updated = service.updateStatus(id, status);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid status value. Allowed: APPLIED, APPROVED, REJECTED, IN_PROGRESS, COMPLETED");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error updating status: " + e.getMessage());
        }
    }

    // ==================== GET ALL ====================
    @GetMapping
    public ResponseEntity<List<InternshipEnrollment>> getAllEnrollments() {
        return ResponseEntity.ok(service.getAllEnrollments());
    }
}
