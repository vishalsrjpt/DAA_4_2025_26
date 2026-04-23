/**
 * ============================================================
 * script.js — Job Scheduling System Frontend Logic
 * ============================================================
 * Handles:
 *   - Adding jobs via POST /api/jobs
 *   - Loading all jobs via GET /api/jobs
 *   - Running scheduling via POST /api/schedule
 *   - Rendering results (Greedy vs Brute Force comparison)
 * ============================================================
 */

// ── Configuration ────────────────────────────────────────────
// In Docker: Nginx proxies /api/* to the backend container
// Locally:   Change this to "http://localhost:8080/api"
const API_BASE = "/api";

// ── On Page Load ─────────────────────────────────────────────
document.addEventListener("DOMContentLoaded", () => {
    loadJobs(); // Fetch and display all existing jobs on startup
});

// ============================================================
// ADD JOB
// ============================================================
/**
 * Reads form inputs, validates, and sends a POST request to
 * add a new job to the database.
 *
 * API: POST /api/jobs
 * Body: { jobId, deadline, profit }
 */
async function addJob() {
    const jobId    = document.getElementById("jobId").value.trim();
    const deadline = parseInt(document.getElementById("deadline").value);
    const profit   = parseInt(document.getElementById("profit").value);

    // ── Client-side Validation ────────────────────────────
    if (!jobId) {
        showMsg("add-msg", "error", "Please enter a Job ID.");
        return;
    }
    if (isNaN(deadline) || deadline < 1) {
        showMsg("add-msg", "error", "Deadline must be at least 1.");
        return;
    }
    if (isNaN(profit) || profit <= 0) {
        showMsg("add-msg", "error", "Profit must be greater than 0.");
        return;
    }

    // ── Send to Backend ───────────────────────────────────
    try {
        const response = await fetch(`${API_BASE}/jobs`, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ jobId, deadline, profit })
        });

        const data = await response.json();

        if (response.ok) {
            // Success: clear form, reload jobs list
            showMsg("add-msg", "success", `Job "${jobId}" added successfully!`);
            clearForm();
            loadJobs();
        } else {
            // Server returned an error (e.g., duplicate ID)
            showMsg("add-msg", "error", data.error || "Failed to add job.");
        }
    } catch (err) {
        showMsg("add-msg", "error", "Cannot connect to server. Is Spring Boot running?");
        console.error("Add job error:", err);
    }
}

// ============================================================
// LOAD ALL JOBS
// ============================================================
/**
 * Fetches all jobs from the database and renders them in the table.
 *
 * API: GET /api/jobs
 */
async function loadJobs() {
    try {
        const response = await fetch(`${API_BASE}/jobs`);
        const jobs = await response.json();

        renderJobsTable(jobs);
        updateJobCount(jobs.length);
    } catch (err) {
        console.error("Load jobs error:", err);
        // Silently fail (server may not be started yet)
    }
}

// ============================================================
// RUN SCHEDULING ALGORITHMS
// ============================================================
/**
 * Triggers both Greedy and Brute Force scheduling on the backend.
 * Renders the comparison results.
 *
 * API: POST /api/schedule
 */
async function runSchedule() {
    // Hide old results, show loading spinner
    document.getElementById("results-section").classList.add("hidden");
    document.getElementById("loading").classList.remove("hidden");

    try {
        const response = await fetch(`${API_BASE}/schedule`, {
            method: "POST",
            headers: { "Content-Type": "application/json" }
        });

        const data = await response.json();

        document.getElementById("loading").classList.add("hidden");

        if (response.ok) {
            renderResults(data);
        } else {
            alert("Error: " + (data.error || "Scheduling failed."));
        }
    } catch (err) {
        document.getElementById("loading").classList.add("hidden");
        alert("Cannot connect to server. Make sure Spring Boot is running on port 8080.");
        console.error("Schedule error:", err);
    }
}

// ============================================================
// RESET ALL JOBS
// ============================================================
/**
 * Deletes all jobs from the database.
 *
 * API: DELETE /api/jobs
 */
async function resetJobs() {
    if (!confirm("Delete ALL jobs? This cannot be undone.")) return;

    try {
        const response = await fetch(`${API_BASE}/jobs`, { method: "DELETE" });

        if (response.ok) {
            loadJobs();
            document.getElementById("results-section").classList.add("hidden");
            showMsg("add-msg", "success", "All jobs deleted.");
        }
    } catch (err) {
        showMsg("add-msg", "error", "Failed to reset. Is the server running?");
    }
}

// ============================================================
// RENDER FUNCTIONS
// ============================================================

/**
 * Renders the "All Jobs" table with the given jobs array.
 * @param {Array} jobs - List of job objects from API
 */
function renderJobsTable(jobs) {
    const tbody = document.getElementById("jobs-tbody");

    if (!jobs || jobs.length === 0) {
        tbody.innerHTML = `
            <tr class="empty-row">
                <td colspan="4">No jobs added yet. Add some jobs to get started.</td>
            </tr>`;
        return;
    }

    // Sort by deadline for display
    const sorted = [...jobs].sort((a, b) => a.deadline - b.deadline);

    tbody.innerHTML = sorted.map((job, index) => `
        <tr>
            <td>${index + 1}</td>
            <td><strong>${escapeHtml(job.jobId)}</strong></td>
            <td>${job.deadline}</td>
            <td style="color: var(--accent); font-weight: 600;">₹${job.profit}</td>
        </tr>
    `).join("");
}

/**
 * Renders the full scheduling results section.
 * @param {Object} data - ScheduleResponse from the API
 */
function renderResults(data) {
    // ── Stat Cards ──────────────────────────────────────────
    const statsHtml = `
        <div class="stat-card">
            <div class="stat-value" style="color: var(--accent)">${data.totalJobsProcessed}</div>
            <div class="stat-label">Total Jobs</div>
        </div>
        <div class="stat-card">
            <div class="stat-value" style="color: var(--greedy)">${data.greedyScheduledJobs.length}</div>
            <div class="stat-label">Jobs Scheduled</div>
        </div>
        <div class="stat-card">
            <div class="stat-value" style="color: var(--danger)">${data.greedyRejectedJobs.length}</div>
            <div class="stat-label">Jobs Rejected</div>
        </div>
        <div class="stat-card">
            <div class="stat-value" style="color: var(--text)">D=${data.maxDeadline}</div>
            <div class="stat-label">Max Deadline</div>
        </div>
    `;
    document.getElementById("stats-cards").innerHTML = statsHtml;

    // ── Greedy Results ──────────────────────────────────────
    document.getElementById("greedy-profit-badge").textContent = `₹${data.greedyTotalProfit}`;
    document.getElementById("greedy-scheduled-tbody").innerHTML =
        renderResultRows(data.greedyScheduledJobs, "var(--greedy)");
    document.getElementById("greedy-rejected-tbody").innerHTML =
        renderResultRows(data.greedyRejectedJobs, "var(--danger)");
    document.getElementById("greedy-time").textContent =
        `Complexity: ${data.greedyTimeComplexity}  |  Runtime: ${data.greedyTimeMs}ms`;

    // ── Brute Force Results ─────────────────────────────────
    document.getElementById("brute-profit-badge").textContent = `₹${data.bruteForceTotalProfit}`;
    document.getElementById("brute-scheduled-tbody").innerHTML =
        renderResultRows(data.bruteForceScheduledJobs, "var(--brute)");
    document.getElementById("brute-rejected-tbody").innerHTML =
        renderResultRows(data.bruteForceRejectedJobs, "var(--danger)");
    document.getElementById("brute-time").textContent =
        `Complexity: ${data.bruteForceTimeComplexity}  |  Runtime: ${data.bruteForceTimeMs}ms`;

    // ── Verdict Box ─────────────────────────────────────────
    const same = data.greedyTotalProfit === data.bruteForceTotalProfit;
    const fasterBy = data.bruteForceTimeMs > data.greedyTimeMs
        ? ` (${data.bruteForceTimeMs - data.greedyTimeMs}ms faster)`
        : "";

    document.getElementById("verdict-box").innerHTML = `
        <div class="verdict-emoji">${same ? "✅" : "⚠️"}</div>
        <div class="verdict-title">
            ${same
                ? "Both algorithms found the same optimal profit!"
                : "Results differ — check job counts and deadlines."
            }
        </div>
        <p>
            Greedy (O(n log n)) produced <strong>₹${data.greedyTotalProfit}</strong>${fasterBy},
            while Brute Force (O(2ⁿ × n)) produced <strong>₹${data.bruteForceTotalProfit}</strong>.
            The Greedy algorithm is provably optimal for the Job Sequencing problem —
            it always produces the maximum profit by selecting the highest-profit job
            first and placing it in the latest available slot before its deadline.
        </p>
    `;

    // ── Show Results Section ────────────────────────────────
    const section = document.getElementById("results-section");
    section.classList.remove("hidden");
    section.classList.add("results-section-visible");

    // Scroll to results smoothly
    section.scrollIntoView({ behavior: "smooth", block: "start" });
}

/**
 * Generates table rows for a list of jobs.
 * @param {Array} jobs  - Array of job objects
 * @param {string} color - CSS color for profit column
 * @returns {string} HTML string of <tr> elements
 */
function renderResultRows(jobs, color) {
    if (!jobs || jobs.length === 0) {
        return `<tr><td colspan="3" style="text-align:center; color:var(--text-dim); padding:16px;">None</td></tr>`;
    }
    return jobs.map(job => `
        <tr>
            <td><strong>${escapeHtml(job.jobId)}</strong></td>
            <td>${job.deadline}</td>
            <td style="color:${color}; font-weight:600;">₹${job.profit}</td>
        </tr>
    `).join("");
}

// ============================================================
// UTILITY FUNCTIONS
// ============================================================

/**
 * Shows a success or error message in the given element.
 * Auto-hides after 4 seconds.
 *
 * @param {string} elementId - ID of the message container element
 * @param {string} type      - "success" or "error"
 * @param {string} text      - Message text to display
 */
function showMsg(elementId, type, text) {
    const el = document.getElementById(elementId);
    el.textContent = text;
    el.className = `message ${type}`;
    el.classList.remove("hidden");
    setTimeout(() => { el.classList.add("hidden"); }, 4000);
}

/** Clears all form input fields. */
function clearForm() {
    document.getElementById("jobId").value = "";
    document.getElementById("deadline").value = "";
    document.getElementById("profit").value = "";
    document.getElementById("jobId").focus();
}

/** Updates the job count badge. */
function updateJobCount(count) {
    document.getElementById("job-count").textContent =
        `${count} job${count !== 1 ? "s" : ""}`;
}

/**
 * Escapes HTML special characters to prevent XSS.
 * Always sanitize user-provided data before inserting into DOM.
 *
 * @param {string} str - Raw string from API
 * @returns {string} Escaped HTML-safe string
 */
function escapeHtml(str) {
    const div = document.createElement("div");
    div.textContent = String(str);
    return div.innerHTML;
}

// Allow pressing Enter in form fields to submit
document.addEventListener("keydown", (e) => {
    if (e.key === "Enter" && (
        document.activeElement.id === "jobId"   ||
        document.activeElement.id === "deadline" ||
        document.activeElement.id === "profit"
    )) {
        addJob();
    }
});