package com.daa.jobscheduler.repository;

import com.daa.jobscheduler.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ============================================================
 * JobRepository - Data Access Layer
 * ============================================================
 * Extends JpaRepository to get CRUD operations for FREE:
 *   - save(job)        → INSERT / UPDATE
 *   - findAll()        → SELECT *
 *   - findById(id)     → SELECT by primary key
 *   - deleteById(id)   → DELETE
 *   - count()          → COUNT(*)
 *
 * Spring Data JPA auto-implements this interface at runtime.
 * No SQL queries needed for basic operations!
 *
 * Generic types: JpaRepository<EntityClass, PrimaryKeyType>
 * ============================================================
 */
@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    /**
     * Custom query method - Spring generates SQL automatically:
     * SELECT * FROM jobs WHERE job_id = ?
     *
     * @param jobId the user-defined job identifier
     * @return Optional<Job> (empty if not found)
     */
    Optional<Job> findByJobId(String jobId);

    /**
     * Check if a job with the given jobId already exists.
     * Prevents duplicate job IDs.
     *
     * Generated SQL: SELECT COUNT(*) > 0 FROM jobs WHERE job_id = ?
     *
     * @param jobId the user-defined job identifier
     * @return true if exists, false otherwise
     */
    boolean existsByJobId(String jobId);
}