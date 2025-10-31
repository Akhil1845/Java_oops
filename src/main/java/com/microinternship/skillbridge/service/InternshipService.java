package com.microinternship.skillbridge.service;

import com.microinternship.skillbridge.entity.Client;
import com.microinternship.skillbridge.entity.Internship;
import com.microinternship.skillbridge.entity.Student;
import com.microinternship.skillbridge.repository.ClientRepository;
import com.microinternship.skillbridge.repository.InternshipRepository;
import com.microinternship.skillbridge.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class InternshipService {

    @Autowired
    private InternshipRepository internshipRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private StudentRepository studentRepository;


    public Internship saveInternship(Internship internship, Long clientId) {
        if (internship.getTitle() == null || internship.getTitle().isBlank()) {
            throw new IllegalArgumentException("Internship title is required");
        }
        if (clientId == null) {
            throw new IllegalArgumentException("Client ID is required");
        }

        // Fetch the Client entity
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + clientId));
        
        internship.setClient(client);

        try {
            Internship saved = internshipRepository.save(internship);
            
            // Notify all students about new internship
            List<Student> students = studentRepository.findAll();
            for (Student student : students) {
                String message = String.format("New internship posted: %s at %s", 
                        saved.getTitle(), saved.getCompany());
                notificationService.createNotification("student", student.getId(), 
                        message, "new_internship", saved.getId());
            }
            
            return saved;
        } catch (Exception e) {
            throw new RuntimeException("Failed to save internship: " + e.getMessage(), e);
        }
    }


    public List<Internship> getAllInternships() {
        return internshipRepository.findAll();
    }


    public List<Internship> getByClientId(Long clientId) {
        if (clientId == null) {
            throw new IllegalArgumentException("Client ID is required");
        }
        return internshipRepository.findByClient_Id(clientId);
    }

    public Internship getInternshipById(Long id) {
        return internshipRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Internship not found with id: " + id));
    }


    public Internship updateInternship(Long id, Internship updated, Long clientId) {
        Internship existing = getInternshipById(id);

        existing.setTitle(updated.getTitle());
        existing.setCompany(updated.getCompany());
        existing.setDescription(updated.getDescription());
        existing.setDomain(updated.getDomain());
        existing.setLocation(updated.getLocation());
        existing.setContactEmail(updated.getContactEmail());
        existing.setContactPhone(updated.getContactPhone());
        existing.setWhatsapp(updated.getWhatsapp());
        
        if (clientId != null) {
            Client client = clientRepository.findById(clientId)
                    .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + clientId));
            existing.setClient(client);
        }
        
        existing.setSeats(updated.getSeats());
        existing.setStartDate(updated.getStartDate());
        existing.setEndDate(updated.getEndDate());

        return internshipRepository.save(existing);
    }

    public void deleteInternship(Long id) {
        internshipRepository.deleteById(id);
    }
}
