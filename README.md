# 🚀 JobHub - Portfolio-Grade Full-Stack Job Portal Platform

JobHub is a production-grade full-stack job portal application designed for Software Developer / SDE / Java Developer candidates. It features role-based access control (Candidates & Recruiters), JWT token security, dynamic JPA search filtering, live status tracking, and an AI-powered job recommendation engine with fallback capability.

---

## 🏗️ Architecture & Project Structure

```text
JobHub
│
├── backend (Spring Boot 3 + Java 21)
│   ├── src/main/java/com/jobhub/jobhub
│   │   ├── config/          # SecurityConfig, CORS
│   │   ├── controller/      # AuthController, UserController, JobController, ApplicationController, SavedJobController, DashboardController, AiRecommendationController
│   │   ├── dto/             # ApiResponse, RegisterRequest, LoginRequest, AuthResponse, UserRequestDto, UserResponseDto, JobRequestDto, JobResponseDto, ApplicationRequestDto, ApplicationResponseDto, UpdateStatusDto, SavedJobResponseDto, CandidateDashboardDto, RecruiterDashboardDto, JobRecommendationDto, AiRecommendationResponseDto, PageResponse
│   │   ├── entity/          # User, Job, JobType, Application, ApplicationStatus, SavedJob
│   │   ├── exception/       # ResourceNotFoundException, DuplicateEmailException, InvalidCredentialsException, UnauthorizedAccessException, DuplicateApplicationException, GlobalExceptionHandler
│   │   ├── repository/      # UserRepository, JobRepository, ApplicationRepository, SavedJobRepository
│   │   ├── security/        # JwtService, CustomUserDetailsService, JwtAuthenticationFilter
│   │   ├── service/         # AuthService, UserService, JobService, ApplicationService, SavedJobService, DashboardService, AiRecommendationService
│   │   └── specification/  # JobSpecification (Criteria API dynamic queries)
│   └── pom.xml
│
├── frontend (React.js + Vite + Vanilla CSS)
│   ├── src/
│   │   ├── components/      # Navbar, Footer, JobCard, SearchBar, ProtectedRoute, Loading
│   │   ├── pages/           # Home, Login, Register, Jobs, JobDetails, CandidateDashboard, RecruiterDashboard, Profile, Applications, SavedJobs, PostJob
│   │   ├── context/         # AuthContext
│   │   ├── services/        # api.js (Axios Client with Interceptors)
│   │   ├── App.jsx
│   │   ├── main.jsx
│   │   └── index.css
│   └── vite.config.js
│
└── MySQL 8 Database (jobhub_db)
    ├── users
    ├── jobs
    ├── applications
    └── saved_jobs
```

---

## 🛠️ Technology Stack

### Backend:
- **Language:** Java 21
- **Framework:** Spring Boot 3.4+
- **Security:** Spring Security 6, JWT (JSON Web Token), BCrypt Password Hashing
- **ORM / Persistence:** Spring Data JPA, Hibernate, Criteria API (`Specification`)
- **Validation:** Jakarta Bean Validation (`@Valid`, `@NotBlank`, `@Email`)
- **Database:** MySQL 8

### Frontend:
- **Core:** React.js, Vite
- **Routing:** React Router v7
- **HTTP Client:** Axios with Request & Response Interceptors
- **Styling:** Custom Glassmorphism Vanilla CSS3
- **Icons:** Lucide React

---

## 🗄️ Database Relationships (ERD)

```text
+-------------------+             +--------------------+
|       users       | 1         * |       jobs         |
+-------------------+-------------+--------------------+
| id (PK)           |             | id (PK)            |
| name              |             | recruiter_id (FK)  |
| email (UNIQUE)    |             | title              |
| password (BCrypt) |             | company_name       |
| role              |             | job_type           |
+-------------------+             +--------------------+
   | 1                               | 1
   |                                 |
   | *                               | *
+------------------------------------------------------+
|                    applications                      |
+------------------------------------------------------+
| id (PK)                                              |
| candidate_id (FK -> users.id)                        |
| job_id (FK -> jobs.id)                               |
| resume_url                                           |
| status (APPLIED, REVIEWING, SHORTLISTED, etc.)       |
+------------------------------------------------------+
```

---

## 🔑 Environment Variables Setup

Create a `.env` file or export environment variables:

```properties
# Database Credentials
DB_USERNAME=root
DB_PASSWORD=your_mysql_password

# Security Secret Key
JWT_SECRET=404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970

# Optional AI API Key (Fallback skill-matching algorithm auto-triggers if omitted)
AI_API_KEY=your_gemini_or_openai_api_key
```

---

## 🚀 How to Run locally

### 1. Database Setup
Create database in MySQL Workbench / CLI:
```sql
CREATE DATABASE IF NOT EXISTS jobhub_db;
```

### 2. Run Backend (Spring Boot)
Navigates to `backend` folder:
```bash
cd backend
.\mvnw.cmd spring-boot:run
```
*Server starts on `http://localhost:8080`*

### 3. Run Frontend (React + Vite)
Navigates to `frontend` folder:
```bash
cd frontend
npm run dev
```
*Frontend opens on `http://localhost:5173`*

---

## 📡 API Documentation Summary

| Method | Endpoint | Access | Purpose |
| :--- | :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Public | Register Candidate or Recruiter |
| `POST` | `/api/auth/login` | Public | Authenticate user & receive JWT token |
| `GET` | `/api/jobs` | Public | Get all active job listings |
| `GET` | `/api/jobs/search` | Public | Dynamic job search & pagination |
| `GET` | `/api/jobs/{id}` | Public | Get job details |
| `POST` | `/api/jobs` | Recruiter | Post new job position |
| `PUT` | `/api/jobs/{id}` | Recruiter | Update owned job position |
| `DELETE` | `/api/jobs/{id}` | Recruiter | Delete owned job position |
| `POST` | `/api/applications` | Candidate | Apply for a job |
| `GET` | `/api/applications/my` | Candidate | View my submitted applications |
| `GET` | `/api/applications/job/{jobId}` | Recruiter | View applicants for owned job |
| `PUT` | `/api/applications/{id}/status` | Recruiter | Update application status |
| `POST` | `/api/saved-jobs/{jobId}` | Candidate | Bookmark a job |
| `GET` | `/api/saved-jobs` | Candidate | View bookmarked jobs |
| `GET` | `/api/dashboard/candidate` | Candidate | Get candidate metrics & AI insights |
| `GET` | `/api/dashboard/recruiter` | Recruiter | Get recruiter analytics & stats |
| `GET` | `/api/ai/recommendations` | Candidate | Run AI recommendation engine |

---

## 📝 Resume Project Description

**JobHub – Full-Stack Job Portal & AI Recommendation Platform**  
*Tech Stack: Java 21, Spring Boot 3, Spring Security, JWT, Hibernate, MySQL, React.js, Vite, Axios*
- Designed and built a portfolio-grade job portal featuring JWT authentication, BCrypt password hashing, and role-based authorization for Candidates and Recruiters.
- Implemented high-performance dynamic search and filtering utilizing Spring Data JPA `Specification` (Criteria API) and Pageable pagination.
- Built a dual-layer AI job recommendation engine with seamless rule-based skill-matching fallback for missing/unreachable AI services.
- Created responsive React SPA frontend with custom glassmorphism design system, Axios interceptors, and protected routing.
