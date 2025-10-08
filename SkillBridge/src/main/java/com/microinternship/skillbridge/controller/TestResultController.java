package com.microinternship.skillbridge.controller;

import com.microinternship.skillbridge.entity.TestResult;
import com.microinternship.skillbridge.service.TestResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/results")
@CrossOrigin(origins = "*")
public class TestResultController {

    @Autowired
    private TestResultService resultService;

    /** Submit a test with answers */
    @PostMapping("/submit")
    public ResponseEntity<TestResult> submitTest(@RequestBody Map<String, Object> payload) {
        try {
            Long studentId = payload.get("studentId") != null ? Long.valueOf(payload.get("studentId").toString()) : null;
            Long testId = payload.get("testId") != null ? Long.valueOf(payload.get("testId").toString()) : null;

            if (studentId == null || testId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            @SuppressWarnings("unchecked")
            List<Object> rawAnswers = (List<Object>) payload.getOrDefault("answers", Collections.emptyList());

            // Convert all answers to strings safely
            List<String> answers = rawAnswers.stream()
                    .map(a -> a != null ? a.toString() : "")
                    .collect(Collectors.toList());

            System.out.println("Received answers: " + answers); // Debug log

            TestResult savedResult = resultService.evaluateAndSaveResult(studentId, testId, answers);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedResult);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /** Get all results for a specific student */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<TestResult>> getByStudent(@PathVariable(required = false) Long studentId) {
        List<TestResult> results = resultService.getResultsByStudent(studentId);
        return ResponseEntity.ok(results != null ? results : Collections.emptyList());
    }

    /** Get all results for a specific test */
    @GetMapping("/test/{testId}")
    public ResponseEntity<List<TestResult>> getByTest(@PathVariable(required = false) Long testId) {
        List<TestResult> results = resultService.getResultsByTest(testId);
        return ResponseEntity.ok(results != null ? results : Collections.emptyList());
    }

    /** Get all results */
    @GetMapping("/all")
    public ResponseEntity<List<TestResult>> getAllResults() {
        List<TestResult> results = resultService.getAllResults();
        return ResponseEntity.ok(results != null ? results : Collections.emptyList());
    }
}
