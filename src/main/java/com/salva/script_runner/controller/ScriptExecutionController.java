package com.salva.script_runner.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salva.script_runner.service.ScriptExecutionService;

@RestController
@RequestMapping("/api/scripts")
public class ScriptExecutionController {

    private final ScriptExecutionService scriptExecutionService;

    public ScriptExecutionController(ScriptExecutionService scriptExecutionService) {
        this.scriptExecutionService = scriptExecutionService;
    }


    @PostMapping("/trigger/{scriptName:.+}")
    public ResponseEntity<Long> executeScript(@PathVariable String scriptName) {
    Long id = scriptExecutionService.executeScript(scriptName);
    return ResponseEntity.ok(id);
    }

}
