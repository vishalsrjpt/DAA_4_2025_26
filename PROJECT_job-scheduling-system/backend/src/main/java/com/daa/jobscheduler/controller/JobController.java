package com.daa.jobscheduler.controller;

import com.daa.jobscheduler.dto.ScheduleResponse;
import com.daa.jobscheduler.model.Job;
import com.daa.jobscheduler.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ============================================================
 * JobController - REST API Layer (Presentation Layer)
 * ============================================================
 * Exposes the following HTTP endpoints:
 *
 *   POST   /jobs      → Add a new job
 *   GET    /jobs      → Fetch all jobs
 *   DELETE /jobs      → Delete all jobs (reset)
 *   POST   /schedule  → Run scheduling algorithms
 *
 * @RestController = @Controller + @ResponseBody
 *   → All methods return JSON automatically
 *
 * @RequestMapping("/api") → All routes prefixed with /api
 *
 * @CrossOrigin → Allows frontend (different origin) to call API
 * ============================================================
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")  // Allow all origins (for local development)
public class JobController {

    /**
     * Spring injects JobService automatically via @Autowired.
     * Controller delegates ALL business logic to the Service layer.
     */
    @Autowired
    private JobService jobService;

    // =========================================================
    // POST /api/jobs  →  Add a new job
    // =========================================================
    /**
     * Accepts a JSON body with job details and saves to database.
     *
     * Example Request Body:
     * {
     *   "jobId": "J1",
     *   "deadline": 2,
     *   "profit": 100
     * }
     *
     * @param job Job object deserialized from request body
     * @return 201 Created with saved job, or 400 Bad Request on error
     */
    @PostMapping("/jobs")
    public ResponseEntity<?> addJob(@RequestBody Job job) {
        try {
            Job savedJob = jobService.addJob(job);
            return ResponseEntity
                    .status(HttpStatus.CREATED)  // HTTP 201
                    .body(savedJob);
        } catch (IllegalArgumentException e) {
            // Return error message as JSON
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)  // HTTP 400
                    .body(error);
        }
    }

    // =========================================================
    // GET /api/jobs  →  Fetch all jobs
    // =========================================================
    /**
     * Returns all jobs stored in the database.
     *
     * Example Response:
     * [
     *   {"id": 1, "jobId": "J1", "deadline": 2, "profit": 100},
     *   {"id": 2, "jobId": "J2", "deadline": 1, "profit": 80}
     * ]
     *
     * @return 200 OK with list of jobs
     */
    @GetMapping("/jobs")
    public ResponseEntity<List<Job>> getAllJobs() {
        List<Job> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);  // HTTP 200
    }

    // =========================================================
    // DELETE /api/jobs  →  Delete all jobs (reset)
    // =========================================================
    /**
     * Deletes all jobs from the database for a fresh start.
     *
     * @return 200 OK with success message
     */
    @DeleteMapping("/jobs")
    public ResponseEntity<Map<String, String>> deleteAllJobs() {
        jobService.deleteAllJobs();
        Map<String, String> response = new HashMap<>();
        response.put("message", "All jobs deleted successfully.");
        return ResponseEntity.ok(response);
    }

    // =========================================================
    // POST /api/schedule  →  Run scheduling algorithms
    // =========================================================
    /**
     * Triggers both scheduling algorithms on all stored jobs.
     * Returns a comparison of Greedy vs Brute Force results.
     *
     * Example Response:
     * {
     *   "greedyScheduledJobs": [...],
     *   "greedyTotalProfit": 250,
     *   "greedyTimeComplexity": "O(n log n)",
     *   "bruteForceScheduledJobs": [...],
     *   "bruteForceTotalProfit": 250,
     *   "bruteForceTimeComplexity": "O(2^n × n)",
     *   ...
     * }
     *
     * @return 200 OK with scheduling results, or 400 if no jobs exist
     */
    @PostMapping("/schedule")
    public ResponseEntity<?> runSchedule() {
        try {
            ScheduleResponse result = jobService.runScheduling();
            return ResponseEntity.ok(result);  // HTTP 200
        } catch (IllegalStateException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)  // HTTP 400
                    .body(error);
        }
    }

    // =========================================================
    // GET /api/health  →  Health check endpoint
    // =========================================================
    /**
     * Simple health check to verify the server is running.
     * Useful for debugging connection issues.
     *
     * @return 200 OK with status message
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("message", "Job Scheduling System is running!");
        return ResponseEntity.ok(status);
    }
}