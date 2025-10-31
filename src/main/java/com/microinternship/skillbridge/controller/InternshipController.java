package com.microinternship.skillbridge.controller;

import com.microinternship.skillbridge.entity.Internship;
import com.microinternship.skillbridge.service.InternshipService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/internships")
@CrossOrigin(origins = "http://127.0.0.1:5500")   // allow calls from your frontend
public class InternshipController {

    private final InternshipService internshipService;

    public InternshipController(InternshipService internshipService) {
        this.internshipService = internshipService;
    }

    /**
     * Get all internships or by clientId
     */
    @GetMapping
    public List<Internship> getAllInternships(@RequestParam(required = false) Long clientId) {
        if(clientId != null){
            return internshipService.getByClientId(clientId);
        }
        return internshipService.getAllInternships();
    }

    /**
     * Get one internship by its id
     */
    @GetMapping("/{id}")
    public Internship getInternshipById(@PathVariable Long id) {
        return internshipService.getInternshipById(id);
    }

    /**
     * Create a new internship (used by your client dashboard form)
     */
    @PostMapping
    public Internship createInternship(@RequestBody Internship internship, @RequestParam Long clientId) {
        // Ensure clientId is present
        if(clientId == null){
            throw new IllegalArgumentException("client_id cannot be null");
        }
        return internshipService.saveInternship(internship, clientId);
    }

    /**
     * Update an internship
     */
    @PutMapping("/{id}")
    public Internship updateInternship(@PathVariable Long id,
                                       @RequestBody Internship updatedInternship,
                                       @RequestParam(required = false) Long clientId) {
        return internshipService.updateInternship(id, updatedInternship, clientId);
    }

    /**
     * Delete an internship
     */
    @DeleteMapping("/{id}")
    public void deleteInternship(@PathVariable Long id) {
        internshipService.deleteInternship(id);
    }
}
