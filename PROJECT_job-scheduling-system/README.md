# Job Scheduling System — DAA Project

**Algorithm:** Job Sequencing with Deadlines (Greedy + Brute Force)  
**Stack:** Java Spring Boot · MySQL · Vanilla HTML/CSS/JS  
**Build Tool:** Maven

---

## 📁 Complete Folder Structure

```
job-scheduling-system/
│
├── backend/                          ← Spring Boot project
│   ├── pom.xml                       ← Maven dependencies
│   └── src/main/
│       ├── java/com/daa/jobscheduler/
│       │   ├── JobSchedulerApplication.java   ← Main entry point
│       │   ├── WebConfig.java                 ← CORS configuration
│       │   ├── algorithm/
│       │   │   └── JobSchedulingAlgorithms.java  ← Greedy + Brute Force
│       │   ├── controller/
│       │   │   └── JobController.java         ← REST API endpoints
│       │   ├── dto/
│       │   │   └── ScheduleResponse.java      ← API response model
│       │   ├── model/
│       │   │   └── Job.java                   ← Database Entity
│       │   ├── repository/
│       │   │   └── JobRepository.java         ← Data access layer
│       │   └── service/
│       │       └── JobService.java            ← Business logic
│       └── resources/
│           └── application.properties         ← DB config
│
└── frontend/                         ← Plain HTML/CSS/JS
    ├── index.html                    ← Main UI
    ├── style.css                     ← Styling
    └── script.js                     ← API calls & rendering
```

---

## ⚙️ Prerequisites

Make sure you have installed:

| Tool | Version | Download |
|------|---------|---------|
| Java JDK | 17+ | https://adoptium.net |
| Maven | 3.8+ | https://maven.apache.org |
| MySQL | 8.0+ | https://dev.mysql.com |

---

## 🗄️ Database Setup

1. Open MySQL shell (or MySQL Workbench)
2. Create the database:

```sql
CREATE DATABASE job_scheduler_db;
```

3. Open `backend/src/main/resources/application.properties`
4. Update your credentials:

```properties
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

> The table (`jobs`) is **auto-created** by Hibernate on first run.  
> You do NOT need to run any SQL CREATE TABLE statements.

---

## 🚀 Running the Backend

```bash
# Navigate to backend folder
cd job-scheduling-system/backend

# Build and run with Maven
mvn spring-boot:run
```

You should see:
```
==============================================
  Job Scheduling System - DAA Project
  Server running at: http://localhost:8080
==============================================
```

---

## 🌐 Running the Frontend

Open `frontend/index.html` directly in any browser.

**Option A:** Double-click `index.html`

**Option B (recommended):** Use VS Code Live Server
- Install the "Live Server" extension in VS Code
- Right-click `index.html` → "Open with Live Server"

---

## 🔗 REST API Reference

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/jobs` | Add a new job |
| `GET` | `/api/jobs` | Fetch all jobs |
| `DELETE` | `/api/jobs` | Delete all jobs |
| `POST` | `/api/schedule` | Run scheduling algorithms |
| `GET` | `/api/health` | Health check |

### Example: Add a Job

```bash
curl -X POST http://localhost:8080/api/jobs \
  -H "Content-Type: application/json" \
  -d '{"jobId":"J1","deadline":2,"profit":100}'
```

### Example: Run Scheduling

```bash
curl -X POST http://localhost:8080/api/schedule
```

---

## 🧠 Algorithm Explanation

### Greedy Algorithm — O(n log n)

```
1. Sort jobs by profit (descending) using a Max Heap (Priority Queue)
2. For each job (highest profit first):
   a. Try to place it in the latest free slot ≤ its deadline
   b. If found → schedule the job, add profit
   c. If not found → reject the job
```

**Why Greedy is Correct:**  
By always picking the highest-profit job and placing it as late as possible,
we leave earlier slots free for lower-priority jobs. An exchange argument proves
no other assignment can yield a higher total profit.

### Brute Force — O(2ⁿ × n)

```
1. Generate all 2^n subsets of jobs
2. For each subset:
   a. Check if all jobs fit within their deadlines
   b. Calculate total profit
3. Return subset with maximum profit
```

Both approaches yield the **same optimal result** — Greedy is just exponentially faster.

---

## 📊 Sample Input/Output

**Input Jobs:**

| Job ID | Deadline | Profit |
|--------|----------|--------|
| J1 | 2 | 100 |
| J2 | 1 | 80 |
| J3 | 2 | 90 |
| J4 | 1 | 70 |
| J5 | 3 | 60 |

**Expected Output:**
- Scheduled: J1 (slot 2), J3 (slot 1→ no, slot 2 taken → J3 takes slot 2? No — sorted by profit: J1=100 takes slot 2, J3=90 takes slot 1, J5=60 takes slot 3)
- Actually: J1 → slot 2, J3 → slot 1, J5 → slot 3
- Total Profit: 100 + 90 + 60 = **250**
- Rejected: J2 (80), J4 (70)

---

## 🎓 Time Complexity Summary

| Algorithm | Time | Space | Practical for |
|-----------|------|-------|---------------|
| Greedy (Max Heap) | O(n log n) | O(n) | Any size |
| Brute Force | O(2ⁿ × n) | O(2ⁿ) | n ≤ 20 only |

---

## 👨‍💻 Tech Stack Details

- **Spring Boot 3.2** — REST API framework
- **Spring Data JPA + Hibernate** — ORM for MySQL
- **Lombok** — Reduces boilerplate (getters/setters)
- **MySQL 8** — Persistent storage
- **Maven** — Build and dependency management
- **Vanilla JS** — Fetch API for HTTP requests
- **Google Fonts** — Syne + DM Mono typography