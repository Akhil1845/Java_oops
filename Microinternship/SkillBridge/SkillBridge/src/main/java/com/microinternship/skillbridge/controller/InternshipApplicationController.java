package com.microinternship.skillbridge.controller;

import com.microinternship.skillbridge.entity.InternshipApplication;
import com.microinternship.skillbridge.service.InternshipApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "*")
public class InternshipApplicationController {

    @Autowired
    private InternshipApplicationService applicationService;

    @PostMapping
    public ResponseEntity<?> applyForInternship(@RequestBody Map<String, Object> request) {
        try {
            Long internshipId = Long.valueOf(request.get("internshipId").toString());
            Long studentId = Long.valueOf(request.get("studentId").toString());
            String coverLetter = request.getOrDefault("coverLetter", "").toString();

            InternshipApplication application = applicationService.applyForInternship(internshipId, studentId, coverLetter);
            return ResponseEntity.ok(application);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/student/{studentId}")
    public List<InternshipApplication> getStudentApplications(@PathVariable Long studentId) {
        return applicationService.getStudentApplications(studentId);
    }

    @GetMapping("/internship/{internshipId}")
    public List<InternshipApplication> getInternshipApplications(@PathVariable Long internshipId) {
        return applicationService.getInternshipApplications(internshipId);
    }

    @GetMapping("/client/{clientId}")
    public List<InternshipApplication> getClientApplications(@PathVariable Long clientId) {
        return applicationService.getClientApplications(clientId);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            String status = request.get("status");
            InternshipApplication updated = applicationService.updateApplicationStatus(id, status);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/check")
    public ResponseEntity<Map<String, Boolean>> checkApplication(
            @RequestParam Long internshipId, 
            @RequestParam Long studentId) {
        boolean hasApplied = applicationService.hasApplied(internshipId, studentId);
        return ResponseEntity.ok(Map.of("hasApplied", hasApplied));
    }
}
