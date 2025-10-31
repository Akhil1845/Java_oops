package com.microinternship.skillbridge.service;

import com.microinternship.skillbridge.entity.Client;
import com.microinternship.skillbridge.entity.Test;
import com.microinternship.skillbridge.repository.ClientRepository;
import com.microinternship.skillbridge.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private ClientRepository clientRepository;

    // Save test with client
    public Test saveTest(Test test, Long clientId) {
        Optional<Client> clientOpt = clientRepository.findById(clientId);
        if (clientOpt.isEmpty()) throw new RuntimeException("Client not found");

        test.setClient(clientOpt.get());

        if (test.getStudents() == null) test.setStudents(new HashSet<>());

        // If maxMarks not set, default to number of questions
        if (test.getMaxMarks() == null && test.getQuestions() != null) {
            test.setMaxMarks(test.getQuestions().size());
        }

        return testRepository.save(test);
    }

    // Get all tests
    public List<Test> getAllTests() {
        return testRepository.findAll();
    }

    // Get tests by client
    public List<Test> getTestsByClient(Long clientId) {
        return testRepository.findByClientId(clientId);
    }

    // Get test by ID
    public Optional<Test> getTestById(Long id) {
        return testRepository.findById(id);
    }

    // =================== Helper ===================
    // Safely get number of questions in a test
    public int getNumberOfQuestions(Long testId) {
        Optional<Test> testOpt = testRepository.findById(testId);
        if (testOpt.isEmpty()) return 0;
        Test test = testOpt.get();
        return test.getQuestions() != null ? test.getQuestions().size() : 0;
    }
}
