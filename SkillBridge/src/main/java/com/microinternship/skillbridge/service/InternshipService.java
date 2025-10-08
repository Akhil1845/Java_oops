package com.microinternship.skillbridge.service;

import com.microinternship.skillbridge.entity.Internship;
import com.microinternship.skillbridge.repository.InternshipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class InternshipService {

    @Autowired
    private InternshipRepository internshipRepository;


    public Internship saveInternship(Internship internship) {
        if (internship.getTitle() == null || internship.getTitle().isBlank()) {
            throw new IllegalArgumentException("Internship title is required");
        }
        if (internship.getClientId() == null) {
            throw new IllegalArgumentException("Client ID is required");
        }

        try {
            return internshipRepository.save(internship);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save internship: " + e.getMessage(), e);
        }
    }


    public List<Internship> getAllInternships() {
        return internshipRepository.findAll();
    }


    public List<Internship> getByClientId(Integer clientId) {
        if (clientId == null) {
            throw new IllegalArgumentException("Client ID is required");
        }
        return internshipRepository.findByClientId(clientId);
    }

    public Internship getInternshipById(Long id) {
        return internshipRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Internship not found with id: " + id));
    }


    public Internship updateInternship(Long id, Internship updated) {
        Internship existing = getInternshipById(id);

        existing.setTitle(updated.getTitle());
        existing.setCompany(updated.getCompany());
        existing.setDescription(updated.getDescription());
        existing.setDomain(updated.getDomain());
        existing.setLocation(updated.getLocation());
        existing.setContactEmail(updated.getContactEmail());
        existing.setContactPhone(updated.getContactPhone());
        existing.setWhatsapp(updated.getWhatsapp());
        existing.setClientId(updated.getClientId());
        existing.setSeats(updated.getSeats());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());

        return internshipRepository.save(existing);
    }

    public void deleteInternship(Long id) {
        internshipRepository.deleteById(id);
    }
}
