# 🐳 Job Scheduling System — Docker Setup

Run the entire project (MySQL + Spring Boot + Nginx) with a **single command**.

---

## What Changes for Docker

| File | What Changed | Why |
|------|-------------|-----|
| `backend/Dockerfile` | NEW | Compiles & packages the Spring Boot app |
| `frontend/Dockerfile` | NEW | Serves static files via Nginx |
| `frontend/nginx.conf` | NEW | Proxies `/api/*` to the backend container |
| `docker-compose.yml` | NEW | Wires all 3 containers together |
| `.env` | NEW | Stores DB credentials (don't commit to Git!) |
| `application.properties` | UPDATED | Reads DB config from env variables |
| `frontend/script.js` | UPDATED | `API_BASE` changed from `localhost:8080` to `/api` |

---

## Project Structure (with Docker files)

```
job-scheduling-system/
│
├── docker-compose.yml          ← Orchestrates all 3 containers
├── .env                        ← DB credentials (gitignored)
├── .gitignore
│
├── backend/
│   ├── Dockerfile              ← Builds Spring Boot JAR
│   ├── pom.xml
│   └── src/...
│
└── frontend/
    ├── Dockerfile              ← Nginx image
    ├── nginx.conf              ← Proxy /api → backend
    ├── index.html
    ├── style.css
    └── script.js               ← API_BASE = "/api" (relative)
```

---

## Prerequisites

Install Docker Desktop (includes Docker Compose):
- **Windows / Mac:** https://www.docker.com/products/docker-desktop
- **Linux:** https://docs.docker.com/engine/install/

Verify installation:
```bash
docker --version
docker compose version
```

---

## 🚀 Run with Docker (3 steps)

### Step 1 — Clone / navigate to project root
```bash
cd job-scheduling-system
```

### Step 2 — (Optional) Edit credentials in `.env`
```env
MYSQL_ROOT_PASSWORD=rootpassword123
MYSQL_DATABASE=job_scheduler_db
MYSQL_USER=jobuser
MYSQL_PASSWORD=jobpassword123
```

### Step 3 — Start everything
```bash
docker compose up --build
```

That's it. Docker will:
1. Pull MySQL 8 image
2. Build the Spring Boot JAR (Maven inside Docker)
3. Build the Nginx frontend image
4. Start all 3 containers in the correct order

**First run takes ~3–5 minutes** (Maven downloads dependencies).
Subsequent starts take ~30 seconds.

---

## Access the App

| Service | URL |
|---------|-----|
| Frontend (UI) | http://localhost |
| Backend API | http://localhost:8080/api |
| MySQL | localhost:3306 (connect via MySQL Workbench) |

---

## Common Commands

```bash
# Start in background (detached mode)
docker compose up --build -d

# View logs
docker compose logs -f

# View logs for one service only
docker compose logs -f backend

# Stop all containers (keeps data)
docker compose down

# Stop AND delete database volume (fresh start)
docker compose down -v

# Rebuild after code changes
docker compose up --build

# Check running containers
docker compose ps
```

---

## How the Containers Talk to Each Other

```
Browser
   │
   │  http://localhost
   ▼
┌──────────────┐
│   Nginx      │  port 80
│  (frontend)  │
│              │──── /api/* ──────────────────────────┐
│  serves      │                                      │
│  HTML/CSS/JS │                                      ▼
└──────────────┘                           ┌──────────────────┐
                                           │  Spring Boot     │  port 8080
                                           │  (backend)       │
                                           │                  │
                                           └────────┬─────────┘
                                                    │
                                                    │  jdbc:mysql://mysql:3306
                                                    ▼
                                           ┌──────────────────┐
                                           │  MySQL 8         │  port 3306
                                           │  (database)      │
                                           └──────────────────┘
```

**Key insight:** Inside Docker's network, containers reach each other by **service name**, not `localhost`. That's why:
- `application.properties` uses `jdbc:mysql://mysql:3306` (not `localhost`)
- `nginx.conf` proxies to `http://backend:8080` (not `localhost:8080`)
- `script.js` uses `API_BASE = "/api"` (relative — nginx handles routing)

---

## Troubleshooting

**Backend crashes on startup?**
MySQL takes ~20s to initialize. The `depends_on: condition: service_healthy` + `restart: on-failure` in `docker-compose.yml` handles this automatically.

**Port 80 already in use?**
Change frontend port in `docker-compose.yml`:
```yaml
ports:
  - "3000:80"   # Access at http://localhost:3000
```

**Port 8080 already in use?**
```yaml
ports:
  - "9090:8080"  # Access API at http://localhost:9090
```

**Want to run locally (without Docker) again?**
In `script.js`, change:
```js
const API_BASE = "/api";
// back to:
const API_BASE = "http://localhost:8080/api";
```
And `application.properties` still works locally because of the `:default` fallback values.