package com.microinternship.skillbridge.service;

import com.microinternship.skillbridge.entity.Student;
import com.microinternship.skillbridge.entity.Test;
import com.microinternship.skillbridge.entity.TestResult;
import com.microinternship.skillbridge.repository.StudentRepository;
import com.microinternship.skillbridge.repository.TestRepository;
import com.microinternship.skillbridge.repository.TestResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TestResultService {

    @Autowired
    private TestResultRepository resultRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private NotificationService notificationService;

    /**
     * Evaluate answers, save result, and return it.
     * Handles strings/numbers, trims whitespace, and missing answers.
     */
    public TestResult evaluateAndSaveResult(Long studentId, Long testId, List<String> studentAnswers) {
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        Optional<Test> testOpt = testRepository.findById(testId);

        if (studentOpt.isEmpty()) throw new RuntimeException("Student not found");
        if (testOpt.isEmpty()) throw new RuntimeException("Test not found");

        Student student = studentOpt.get();
        Test test = testOpt.get();

        List<Map<String, Object>> questions = test.getQuestions();
        if (questions == null || questions.isEmpty()) throw new RuntimeException("Test has no questions");

        if (studentAnswers == null) studentAnswers = Collections.emptyList();

        int score = 0;

        for (int i = 0; i < questions.size(); i++) {
            Object correctAnswerObj = questions.get(i).get("answer");
            if (correctAnswerObj == null) continue;

            String correctAnswer = correctAnswerObj.toString().trim();

            String studentAnswer = (i < studentAnswers.size() && studentAnswers.get(i) != null)
                    ? studentAnswers.get(i).toString().trim()
                    : "";

            if (correctAnswer.equalsIgnoreCase(studentAnswer)) {  // ignore case differences
                score++;
            }
        }

        int maxMarks = test.getMaxMarks() != null ? test.getMaxMarks() : questions.size();

        TestResult result = new TestResult(student, test, score, maxMarks);
        TestResult saved = resultRepository.save(result);

        // Create notification for client
        String message = String.format("%s completed %s test - Score: %d/%d", 
                student.getName(), test.getTitle(), score, maxMarks);
        notificationService.createNotification("client", test.getClient().getId(), 
                message, "test_submission", saved.getId());

        return saved;
    }

    /** Get all results for a student by studentId */
    public List<TestResult> getResultsByStudent(Long studentId) {
        if (studentId == null) return Collections.emptyList();
        Optional<Student> studentOpt = studentRepository.findById(studentId);
        if (studentOpt.isEmpty()) return Collections.emptyList();
        List<TestResult> results = resultRepository.findByStudent_Id(studentId);
        return results != null ? results : Collections.emptyList();
    }

    /** Get all results for a particular test by testId */
    public List<TestResult> getResultsByTest(Long testId) {
        if (testId == null) return Collections.emptyList();
        List<TestResult> results = resultRepository.findByTest_Id(testId);
        return results != null ? results : Collections.emptyList();
    }

    /** Get all results */
    public List<TestResult> getAllResults() {
        List<TestResult> results = resultRepository.findAll();
        return results != null ? results : Collections.emptyList();
    }
}
