package com.microinternship.skillbridge.repository;

import com.microinternship.skillbridge.entity.InternshipApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InternshipApplicationRepository extends JpaRepository<InternshipApplication, Long> {
    
    List<InternshipApplication> findByStudent_Id(Long studentId);
    
    List<InternshipApplication> findByInternship_Id(Long internshipId);
    
    @Query("SELECT a FROM InternshipApplication a WHERE a.internship.client.id = :clientId ORDER BY a.appliedAt DESC")
    List<InternshipApplication> findByClientId(Long clientId);
    
    Optional<InternshipApplication> findByInternship_IdAndStudent_Id(Long internshipId, Long studentId);
    
    boolean existsByInternship_IdAndStudent_Id(Long internshipId, Long studentId);
}
