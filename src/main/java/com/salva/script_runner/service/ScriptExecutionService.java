package com.salva.script_runner.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.salva.script_runner.model.ExecutionDetailEntity;
import com.salva.script_runner.repository.ExecutionDetailRepository;

@Service
public class ScriptExecutionService {
    private static final Logger log = LoggerFactory.getLogger(ScriptExecutionService.class);

     private final ExecutionDetailRepository executionDetailRepository;
    private final ExecutorService executorService;

    public ScriptExecutionService(ExecutionDetailRepository executionDetailRepository) {
        this.executionDetailRepository = executionDetailRepository;
        this.executorService = Executors.newFixedThreadPool(10); // You can adjust the number of threads
    }


    public Long executeScript(String scriptName) {
        ExecutionDetailEntity execution = new ExecutionDetailEntity();
        execution.setScriptName(scriptName);
        execution.setStatus("RUNNING");
        execution.setResult("Script: " + scriptName);
        // Save the execution to the database
        ExecutionDetailEntity savedExecution = executionDetailRepository.save(execution);

        Long id = savedExecution.getId();

        executorService.submit(() -> {
            try {
                execution.setStartTime(LocalDateTime.now());
                // Get the thread name
                String thread = Thread.currentThread().getName();
                log.info("[{}] Starting script {}", thread, scriptName);
               
                // Path to the script
                String scriptPath = "C:\\scripts\\testScript\\" + scriptName;

                log.debug("Executing script: {}", scriptPath);
                System.out.println("Executing script: " + scriptPath);
                // Execute the script using ProcessBuilder and redirect error stream to stdout
                ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/c", scriptPath);
                pb.redirectErrorStream(true);
                Process process = pb.start();

                // Read the output
                String output = new BufferedReader(new InputStreamReader(process.getInputStream()))
                        .lines().collect(Collectors.joining("\n"));

                // Wait for the process to finish
                int exitCode = process.waitFor();

                execution.setResult(output);
                execution.setStatus(exitCode == 0 ? "COMPLETED" : "FAILED");

                log.info("[{}] Finished script {}", thread, scriptName);

            } catch (Exception e) {
                execution.setStatus("FAILED");
                execution.setResult(e.getMessage());
            }
            
            execution.setEndTime(LocalDateTime.now());

            
            executionDetailRepository.save(execution);
        });

        return id;
    }

}
