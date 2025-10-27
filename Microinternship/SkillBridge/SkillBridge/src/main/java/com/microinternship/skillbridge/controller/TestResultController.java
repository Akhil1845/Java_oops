package com.microinternship.skillbridge.controller;

import com.microinternship.skillbridge.entity.TestResult;
import com.microinternship.skillbridge.service.TestResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/results")
@CrossOrigin(origins = "*")
public class TestResultController {

    @Autowired
    private TestResultService resultService;

    /** Submit a test with answers */
    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitTest(@RequestBody Map<String, Object> payload) {
        try {
            Long studentId = payload.get("studentId") != null ? Long.valueOf(payload.get("studentId").toString()) : null;
            Long testId = payload.get("testId") != null ? Long.valueOf(payload.get("testId").toString()) : null;

            if (studentId == null || testId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            @SuppressWarnings("unchecked")
            List<Object> rawAnswers = (List<Object>) payload.getOrDefault("answers", Collections.emptyList());
            List<String> answers = rawAnswers.stream()
                    .map(a -> a != null ? a.toString() : "")
                    .collect(Collectors.toList());

            TestResult savedResult = resultService.evaluateAndSaveResult(studentId, testId, answers);

            // Flatten result
            Map<String, Object> flatResult = Map.of(
                    "id", savedResult.getId(),
                    "marks", savedResult.getMarks(),
                    "maxMarks", savedResult.getMaxMarks(),
                    "studentId", savedResult.getStudent().getId(),
                    "studentName", savedResult.getStudent().getName(),
                    "studentEmail", savedResult.getStudent().getEmail(),
                    "testId", savedResult.getTest().getId(),
                    "testTitle", savedResult.getTest().getTitle()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(flatResult);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /** Get all results for a specific student */
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<Map<String, Object>>> getByStudent(@PathVariable Long studentId) {
        List<TestResult> results = resultService.getResultsByStudent(studentId);
        List<Map<String, Object>> flatResults = results.stream().map(this::flattenResult).collect(Collectors.toList());
        return ResponseEntity.ok(flatResults);
    }

    /** Get all results for a specific test */
    @GetMapping("/test/{testId}")
    public ResponseEntity<List<Map<String, Object>>> getByTest(@PathVariable Long testId) {
        List<TestResult> results = resultService.getResultsByTest(testId);
        List<Map<String, Object>> flatResults = results.stream().map(this::flattenResult).collect(Collectors.toList());
        return ResponseEntity.ok(flatResults);
    }

    /** Get all results */
    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllResults() {
        List<TestResult> results = resultService.getAllResults();
        List<Map<String, Object>> flatResults = results.stream().map(this::flattenResult).collect(Collectors.toList());
        return ResponseEntity.ok(flatResults);
    }

    /** Utility: Flatten TestResult to frontend-friendly map */
    private Map<String, Object> flattenResult(TestResult r) {
        return Map.of(
                "id", r.getId(),
                "marks", r.getMarks(),
                "maxMarks", r.getMaxMarks(),
                "studentId", r.getStudent().getId(),
                "studentName", r.getStudent().getName(),
                "studentEmail", r.getStudent().getEmail(),
                "testId", r.getTest().getId(),
                "testTitle", r.getTest().getTitle()
        );
    }
}
