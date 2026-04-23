@echo off
set ROOT=job-scheduling-system

echo Creating directory structure for: %ROOT%...

:: 1. Create Directories (mkdir creates parent directories automatically)
mkdir "%ROOT%\backend\src\main\java\com\daa\jobscheduler\algorithm"
mkdir "%ROOT%\backend\src\main\java\com\daa\jobscheduler\controller"
mkdir "%ROOT%\backend\src\main\java\com\daa\jobscheduler\dto"
mkdir "%ROOT%\backend\src\main\java\com\daa\jobscheduler\model"
mkdir "%ROOT%\backend\src\main\java\com\daa\jobscheduler\repository"
mkdir "%ROOT%\backend\src\main\java\com\daa\jobscheduler\service"
mkdir "%ROOT%\backend\src\main\resources"
mkdir "%ROOT%\frontend"

:: 2. Create Root Files
type nul > "%ROOT%\README.md"

:: 3. Create Backend Files
type nul > "%ROOT%\backend\pom.xml"
type nul > "%ROOT%\backend\src\main\resources\application.properties"
type nul > "%ROOT%\backend\src\main\java\com\daa\jobscheduler\JobSchedulerApplication.java"
type nul > "%ROOT%\backend\src\main\java\com\daa\jobscheduler\WebConfig.java"
type nul > "%ROOT%\backend\src\main\java\com\daa\jobscheduler\algorithm\JobSchedulingAlgorithms.java"
type nul > "%ROOT%\backend\src\main\java\com\daa\jobscheduler\controller\JobController.java"
type nul > "%ROOT%\backend\src\main\java\com\daa\jobscheduler\dto\ScheduleResponse.java"
type nul > "%ROOT%\backend\src\main\java\com\daa\jobscheduler\model\Job.java"
type nul > "%ROOT%\backend\src\main\java\com\daa\jobscheduler\repository\JobRepository.java"
type nul > "%ROOT%\backend\src\main\java\com\daa\jobscheduler\service\JobService.java"

:: 4. Create Frontend Files
type nul > "%ROOT%\frontend\index.html"
type nul > "%ROOT%\frontend\style.css"
type nul > "%ROOT%\frontend\script.js"

echo.
echo ============================================
echo  Project structure created successfully!
echo ============================================
pause