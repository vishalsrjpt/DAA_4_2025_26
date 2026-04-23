package com.daa.jobscheduler.service;

import com.daa.jobscheduler.algorithm.JobSchedulingAlgorithms;
import com.daa.jobscheduler.algorithm.JobSchedulingAlgorithms.AlgorithmResult;
import com.daa.jobscheduler.dto.ScheduleResponse;
import com.daa.jobscheduler.model.Job;
import com.daa.jobscheduler.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ============================================================
 * JobService - Business Logic Layer
 * ============================================================
 * Acts as the bridge between Controller (API layer) and
 * Repository (data layer). Contains all business rules.
 *
 * Responsibilities:
 *   - Validate incoming job data
 *   - Save jobs to MySQL via Repository
 *   - Call scheduling algorithms
 *   - Return structured results
 *
 * @Service tells Spring to manage this as a singleton bean.
 * ============================================================
 */
@Service
public class JobService {

    /**
     * @Autowired injects the JobRepository bean automatically.
     * Spring's Dependency Injection (DI) handles object creation.
     */
    @Autowired
    private JobRepository jobRepository;

    // =========================================================
    // ADD A JOB
    // =========================================================
    /**
     * Validates and saves a new job to the database.
     *
     * Validation rules:
     *   - Job ID must not already exist
     *   - Deadline must be ≥ 1
     *   - Profit must be > 0
     *
     * @param job The job object received from the API request
     * @return Saved job (with auto-generated DB id)
     * @throws IllegalArgumentException if validation fails
     */
    public Job addJob(Job job) {

        // Rule 1: Check for duplicate Job ID
        if (jobRepository.existsByJobId(job.getJobId())) {
            throw new IllegalArgumentException(
                "Job with ID '" + job.getJobId() + "' already exists!"
            );
        }

        // Rule 2: Deadline must be a positive integer
        if (job.getDeadline() < 1) {
            throw new IllegalArgumentException("Deadline must be at least 1.");
        }

        // Rule 3: Profit must be positive
        if (job.getProfit() <= 0) {
            throw new IllegalArgumentException("Profit must be greater than 0.");
        }

        // Save and return the job (Spring Data JPA generates INSERT SQL)
        return jobRepository.save(job);
    }

    // =========================================================
    // GET ALL JOBS
    // =========================================================
    /**
     * Fetches all jobs stored in the database.
     *
     * @return List of all Job objects
     */
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
        // Equivalent SQL: SELECT * FROM jobs;
    }

    // =========================================================
    // DELETE ALL JOBS
    // =========================================================
    /**
     * Deletes all jobs from the database (used for reset).
     */
    public void deleteAllJobs() {
        jobRepository.deleteAll();
    }

    // =========================================================
    // RUN SCHEDULING ALGORITHMS
    // =========================================================
    /**
     * Runs both Greedy and Brute Force scheduling on all stored jobs.
     * Records execution time for comparison.
     *
     * TIME COMPLEXITIES:
     *   Greedy:      O(n log n)   ← efficient
     *   Brute Force: O(2^n * n)  ← exponential
     *
     * @return ScheduleResponse containing results from both algorithms
     * @throws IllegalStateException if no jobs are in the database
     */
    public ScheduleResponse runScheduling() {

        List<Job> allJobs = jobRepository.findAll();

        if (allJobs.isEmpty()) {
            throw new IllegalStateException("No jobs found. Please add jobs first.");
        }

        // ── Run Greedy Algorithm ──────────────────────────────────
        // Start timer
        long greedyStart = System.currentTimeMillis();

        AlgorithmResult greedyResult = JobSchedulingAlgorithms.greedySchedule(allJobs);

        // Stop timer
        long greedyTime = System.currentTimeMillis() - greedyStart;

        // ── Run Brute Force Algorithm ─────────────────────────────
        long bruteStart = System.currentTimeMillis();

        AlgorithmResult bruteResult = JobSchedulingAlgorithms.bruteForceSchedule(allJobs);

        long bruteTime = System.currentTimeMillis() - bruteStart;

        // ── Find max deadline for display purposes ────────────────
        int maxDeadline = allJobs.stream()
                                 .mapToInt(Job::getDeadline)
                                 .max()
                                 .orElse(0);

        // ── Build and return response ─────────────────────────────
        ScheduleResponse response = new ScheduleResponse();

        // Greedy results
        response.setGreedyScheduledJobs(greedyResult.scheduledJobs);
        response.setGreedyRejectedJobs(greedyResult.rejectedJobs);
        response.setGreedyTotalProfit(greedyResult.totalProfit);
        response.setGreedyTimeMs(greedyTime);
        response.setGreedyTimeComplexity("O(n log n)");

        // Brute Force results
        response.setBruteForceScheduledJobs(bruteResult.scheduledJobs);
        response.setBruteForceRejectedJobs(bruteResult.rejectedJobs);
        response.setBruteForceTotalProfit(bruteResult.totalProfit);
        response.setBruteForceTimeMs(bruteTime);
        response.setBruteForceTimeComplexity("O(2^n × n)");

        // Metadata
        response.setTotalJobsProcessed(allJobs.size());
        response.setMaxDeadline(maxDeadline);

        return response;
    }
}