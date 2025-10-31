package com.microinternship.skillbridge.controller;

import com.microinternship.skillbridge.entity.Interest;
import com.microinternship.skillbridge.repository.InterestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interests")
@CrossOrigin(origins = "*")
public class InterestController {

    @Autowired
    private InterestRepository interestRepository;

    // GET all interests
    @GetMapping
    public List<Interest> getAllInterests() {
        return interestRepository.findAll();
    }
}
