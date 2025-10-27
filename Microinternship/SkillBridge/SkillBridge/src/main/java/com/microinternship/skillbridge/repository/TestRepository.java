package com.microinternship.skillbridge.repository;

import com.microinternship.skillbridge.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {

    // Get all tests belonging to a specific client
    List<Test> findByClientId(Long clientId);

    // (Optional) Find tests by topic (useful for filtering in student dashboard)
    List<Test> findByTopic(String topic);

    // (Optional) Find tests by title (case-insensitive if you configure properly)
    List<Test> findByTitleContainingIgnoreCase(String title);
}
