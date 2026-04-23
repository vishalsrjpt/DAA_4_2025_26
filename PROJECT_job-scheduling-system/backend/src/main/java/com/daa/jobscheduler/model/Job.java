package com.daa.jobscheduler.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ============================================================
 * Job Entity - Database Model
 * ============================================================
 * Represents a job with:
 *  - jobId   : Unique identifier (e.g., J1, J2)
 *  - deadline: Latest time slot by which job must be completed
 *  - profit  : Profit earned if job is completed on time
 *
 * Mapped to MySQL table: jobs
 * ============================================================
 */
@Entity
@Table(name = "jobs")
@Data                   // Lombok: generates getters, setters, toString, equals, hashCode
@NoArgsConstructor      // Lombok: generates no-arg constructor
@AllArgsConstructor     // Lombok: generates all-arg constructor
public class Job {

    /**
     * Auto-generated primary key (database ID).
     * Different from jobId which is user-defined.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * User-defined Job ID (e.g., "J1", "J2", "Task-A")
     * Must be unique.
     */
    @Column(name = "job_id", nullable = false, unique = true)
    private String jobId;

    /**
     * Deadline - the latest time slot by which this job must finish.
     * Example: deadline = 2 means job must be done in slot 1 or slot 2.
     */
    @Column(name = "deadline", nullable = false)
    private int deadline;

    /**
     * Profit earned if this job is scheduled and completed on time.
     */
    @Column(name = "profit", nullable = false)
    private int profit;
}