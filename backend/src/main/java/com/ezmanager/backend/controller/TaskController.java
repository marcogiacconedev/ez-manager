package com.ezmanager.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class TaskController {
    
    @GetMapping("/task/{userId}")
    public String getTasksByUserId(@PathVariable String userId) {
        String endpointId = userId;
        return endpointId;
    }
}
