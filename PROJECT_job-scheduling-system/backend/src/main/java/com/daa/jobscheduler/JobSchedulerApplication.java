package com.daa.jobscheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Job Scheduling System.
 * 
 * This system demonstrates:
 *  - Greedy Algorithm: Job Sequencing with Deadlines
 *  - Brute Force comparison
 *  - REST API with Spring Boot + MySQL
 */
@SpringBootApplication
public class JobSchedulerApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobSchedulerApplication.class, args);
        System.out.println("==============================================");
        System.out.println("  Job Scheduling System - DAA Project");
        System.out.println("  Server running at: http://localhost:8080");
        System.out.println("==============================================");
    }
}