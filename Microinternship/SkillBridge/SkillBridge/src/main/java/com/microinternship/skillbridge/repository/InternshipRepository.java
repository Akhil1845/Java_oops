package com.microinternship.skillbridge.repository;

import com.microinternship.skillbridge.entity.Internship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface InternshipRepository extends JpaRepository<Internship, Long> {

    List<Internship> findByClient_Id(Long clientId);
    List<Internship> findByTitleContainingIgnoreCase(String title);
    List<Internship> findByLocationContainingIgnoreCase(String location);
    List<Internship> findByDomainContainingIgnoreCase(String domain);
}