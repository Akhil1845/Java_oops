package com.microinternship.skillbridge.controller;

import com.microinternship.skillbridge.entity.Test;
import com.microinternship.skillbridge.entity.TestResult;
import com.microinternship.skillbridge.service.TestResultService;
import com.microinternship.skillbridge.service.TestService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/tests")
@CrossOrigin(origins = "*")
public class TestController {

    private final TestService testService;
    private final TestResultService testResultService;

    public TestController(TestService testService, TestResultService testResultService) {
        this.testService = testService;
        this.testResultService = testResultService;
    }

    // Upload a new test
    @PostMapping
    public Test uploadTest(@RequestBody Test test, @RequestParam Long clientId) {
        return testService.saveTest(test, clientId);
    }

    // Get ALL tests (for students to see)
    @GetMapping
    public List<Test> getAllTests() {
        return testService.getAllTests();
    }

    // Get tests uploaded by a specific client
    @GetMapping("/client/{clientId}")
    public List<Test> getTestsByClient(@PathVariable Long clientId) {
        return testService.getTestsByClient(clientId);
    }

    // Get a single test by ID
    @GetMapping("/{id}")
    public Optional<Test> getTest(@PathVariable Long id) {
        return testService.getTestById(id);
    }

    // ======================== Submit Test Endpoint ========================
    @PostMapping("/{testId}/submit")
    public TestResult submitTest(
            @PathVariable Long testId,
            @RequestBody Map<String, Object> payload
    ) {
        Long studentId = Long.valueOf(payload.get("studentId").toString());

        // Safe conversion of answers to List<String>
        List<String> answers = new ArrayList<>();
        Object answersObj = payload.get("answers");
        if (answersObj instanceof List<?>) {
            for (Object obj : (List<?>) answersObj) {
                answers.add(obj.toString());
            }
        }

        // Evaluate and save result
        return testResultService.evaluateAndSaveResult(studentId, testId, answers);
    }
}
