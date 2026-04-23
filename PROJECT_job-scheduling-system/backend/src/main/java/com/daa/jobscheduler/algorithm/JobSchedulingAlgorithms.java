package com.daa.jobscheduler.algorithm;

import com.daa.jobscheduler.model.Job;

import java.util.*;

/**
 * ============================================================
 * JobSchedulingAlgorithms
 * ============================================================
 * Contains two approaches to solve Job Sequencing with Deadlines:
 *
 *  1. GREEDY ALGORITHM  → Time Complexity: O(n log n)
 *  2. BRUTE FORCE       → Time Complexity: O(2^n * n)
 *
 * Problem Statement:
 *   Given n jobs, each with a deadline and profit,
 *   schedule jobs to MAXIMIZE total profit such that:
 *   - Each job takes exactly 1 unit of time
 *   - Only one job runs at a time
 *   - A job must complete by its deadline
 * ============================================================
 */
public class JobSchedulingAlgorithms {

    // =========================================================
    // ALGORITHM 1: GREEDY APPROACH
    // =========================================================
    /**
     * Greedy Algorithm: Job Sequencing with Deadlines
     *
     * STRATEGY:
     *   - Sort jobs by profit in DESCENDING order (Max Heap logic)
     *   - For each job (highest profit first), assign it to the
     *     latest available slot ≤ its deadline
     *   - If a slot is available → schedule the job
     *   - If no slot is available → reject the job
     *
     * WHY GREEDY WORKS HERE:
     *   Choosing the highest-profit job first and placing it as
     *   late as possible (to keep earlier slots free) always
     *   leads to an optimal solution. This is proven by an
     *   exchange argument.
     *
     * TIME COMPLEXITY:
     *   - Sorting: O(n log n)          ← dominant step
     *   - Slot assignment: O(n * d)    ← d = max deadline
     *   - Overall: O(n log n)
     *
     * SPACE COMPLEXITY: O(n) for the slot array
     *
     * @param jobs List of all jobs from the database
     * @return AlgorithmResult containing scheduled & rejected jobs
     */
    public static AlgorithmResult greedySchedule(List<Job> jobs) {

        if (jobs == null || jobs.isEmpty()) {
            return new AlgorithmResult(new ArrayList<>(), new ArrayList<>(), 0);
        }

        int n = jobs.size();

        // ── Step 1: Use a Max Heap (Priority Queue) sorted by profit ──
        // PriorityQueue with custom comparator: highest profit first
        // Time: O(n log n) to insert all elements
        PriorityQueue<Job> maxHeap = new PriorityQueue<>(
            (a, b) -> b.getProfit() - a.getProfit()  // Descending profit
        );
        maxHeap.addAll(jobs);  // Add all jobs to the heap

        // ── Step 2: Find maximum deadline ──
        // This tells us how many time slots we have (slot 1 to maxDeadline)
        int maxDeadline = jobs.stream()
                              .mapToInt(Job::getDeadline)
                              .max()
                              .orElse(0);

        // ── Step 3: Initialize time slots ──
        // slot[i] = null means slot i is FREE
        // slot[i] = job  means slot i is OCCUPIED by that job
        // Slots are 1-indexed: slot[1] to slot[maxDeadline]
        Job[] slot = new Job[maxDeadline + 1];  // index 0 unused

        List<Job> scheduledJobs = new ArrayList<>();
        List<Job> rejectedJobs = new ArrayList<>();
        int totalProfit = 0;

        // ── Step 4: Process jobs from highest to lowest profit ──
        // Time: O(n log n) total for all heap extractions
        while (!maxHeap.isEmpty()) {
            Job job = maxHeap.poll();  // Extract job with max profit: O(log n)

            // Try to place this job in the latest available slot ≤ deadline
            // Going backwards from deadline ensures we keep earlier slots free
            boolean placed = false;
            for (int t = job.getDeadline(); t >= 1; t--) {
                if (slot[t] == null) {       // Slot t is free
                    slot[t] = job;           // Assign job to this slot
                    scheduledJobs.add(job);  // Mark as scheduled
                    totalProfit += job.getProfit();
                    placed = true;
                    break;  // Stop looking once placed
                }
            }

            if (!placed) {
                rejectedJobs.add(job);  // No free slot found → reject
            }
        }

        return new AlgorithmResult(scheduledJobs, rejectedJobs, totalProfit);
    }


    // =========================================================
    // ALGORITHM 2: BRUTE FORCE APPROACH
    // =========================================================
    /**
     * Brute Force Algorithm: Try all possible subsets of jobs
     *
     * STRATEGY:
     *   - Generate all 2^n subsets of jobs
     *   - For each subset, check if it's a valid schedule
     *     (all jobs can be placed within their deadlines)
     *   - Track the subset with maximum profit
     *
     * WHY BRUTE FORCE:
     *   Guarantees the optimal answer by exhaustive search.
     *   Used here only for COMPARISON with greedy.
     *   Impractical for large n due to exponential time.
     *
     * TIME COMPLEXITY:
     *   - Total subsets: 2^n
     *   - Validating each subset: O(n log n) (sort + assign)
     *   - Overall: O(2^n * n)   ← extremely slow for n > 20
     *
     * SPACE COMPLEXITY: O(2^n) for storing subsets
     *
     * NOTE: Limited to n ≤ 20 jobs to prevent timeout.
     *
     * @param jobs List of all jobs
     * @return AlgorithmResult with best subset found
     */
    public static AlgorithmResult bruteForceSchedule(List<Job> jobs) {

        if (jobs == null || jobs.isEmpty()) {
            return new AlgorithmResult(new ArrayList<>(), new ArrayList<>(), 0);
        }

        int n = jobs.size();

        // Safety limit: Brute force is O(2^n), too slow beyond 20 jobs
        if (n > 20) {
            // Fall back to greedy for large inputs
            return greedySchedule(jobs);
        }

        List<Job> bestSchedule = new ArrayList<>();
        int bestProfit = 0;

        // ── Iterate over all 2^n possible subsets ──
        // Each subset is represented as a bitmask (integer)
        // Bit i = 1 means job[i] is INCLUDED in this subset
        for (int mask = 0; mask < (1 << n); mask++) {

            // Build the current subset from bitmask
            List<Job> subset = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {  // Bit i is set
                    subset.add(jobs.get(i));
                }
            }

            // ── Check if this subset is a valid schedule ──
            // A subset is valid if all its jobs can be assigned to distinct slots
            if (isValidSchedule(subset)) {
                int profit = subset.stream().mapToInt(Job::getProfit).sum();
                if (profit > bestProfit) {
                    bestProfit = profit;
                    bestSchedule = new ArrayList<>(subset);
                }
            }
        }

        // Determine rejected jobs (those not in the best schedule)
        List<String> scheduledIds = new ArrayList<>();
        for (Job j : bestSchedule) scheduledIds.add(j.getJobId());

        List<Job> rejectedJobs = new ArrayList<>();
        for (Job j : jobs) {
            if (!scheduledIds.contains(j.getJobId())) {
                rejectedJobs.add(j);
            }
        }

        return new AlgorithmResult(bestSchedule, rejectedJobs, bestProfit);
    }

    /**
     * Helper: Check if a given subset of jobs forms a valid schedule.
     *
     * Uses the same greedy slot-assignment logic:
     *   Sort by deadline, then try to assign each job to a slot.
     *
     * TIME COMPLEXITY: O(k^2) where k = subset size
     *
     * @param subset List of jobs to validate
     * @return true if all jobs can be scheduled, false otherwise
     */
    private static boolean isValidSchedule(List<Job> subset) {
        if (subset.isEmpty()) return true;

        // Sort by deadline (ascending) to assign earlier deadlines first
        subset.sort(Comparator.comparingInt(Job::getDeadline));

        int maxDeadline = subset.stream().mapToInt(Job::getDeadline).max().orElse(0);
        boolean[] slot = new boolean[maxDeadline + 1];  // true = occupied

        for (Job job : subset) {
            boolean placed = false;
            for (int t = job.getDeadline(); t >= 1; t--) {
                if (!slot[t]) {
                    slot[t] = true;
                    placed = true;
                    break;
                }
            }
            if (!placed) return false;  // This subset is invalid
        }
        return true;
    }


    // =========================================================
    // Inner Class: AlgorithmResult
    // =========================================================
    /**
     * Simple container to hold the result of either algorithm.
     */
    public static class AlgorithmResult {
        public final List<Job> scheduledJobs;
        public final List<Job> rejectedJobs;
        public final int totalProfit;

        public AlgorithmResult(List<Job> scheduledJobs, List<Job> rejectedJobs, int totalProfit) {
            this.scheduledJobs = scheduledJobs;
            this.rejectedJobs = rejectedJobs;
            this.totalProfit = totalProfit;
        }
    }
}