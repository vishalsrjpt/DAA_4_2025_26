package com.daa.jobscheduler.dto;

import com.daa.jobscheduler.model.Job;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * ============================================================
 * ScheduleResponse DTO (Data Transfer Object)
 * ============================================================
 * Carries the scheduling result back to the frontend.
 *
 * Contains results from BOTH algorithms:
 *  1. Greedy Algorithm (optimal)
 *  2. Brute Force Algorithm (for comparison)
 *
 * DTO pattern: keeps API response separate from database Entity.
 * ============================================================
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleResponse {

    // ─── Greedy Algorithm Results ───────────────────────────

    /** Jobs selected by the Greedy algorithm */
    private List<Job> greedyScheduledJobs;

    /** Jobs NOT selected by the Greedy algorithm */
    private List<Job> greedyRejectedJobs;

    /** Total profit from Greedy scheduling */
    private int greedyTotalProfit;

    /** Time taken by Greedy algorithm in milliseconds */
    private long greedyTimeMs;

    /** Time complexity of Greedy: O(n log n) */
    private String greedyTimeComplexity;

    // ─── Brute Force Algorithm Results ──────────────────────

    /** Jobs selected by the Brute Force algorithm */
    private List<Job> bruteForceScheduledJobs;

    /** Jobs NOT selected by the Brute Force algorithm */
    private List<Job> bruteForceRejectedJobs;

    /** Total profit from Brute Force scheduling */
    private int bruteForceTotalProfit;

    /** Time taken by Brute Force algorithm in milliseconds */
    private long bruteForceTimeMs;

    /** Time complexity of Brute Force: O(2^n) */
    private String bruteForceTimeComplexity;

    // ─── Comparison ─────────────────────────────────────────

    /** Total number of jobs processed */
    private int totalJobsProcessed;

    /** Maximum number of time slots available */
    private int maxDeadline;
}