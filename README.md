# 💼 JobHub — Full-Stack Job Portal & AI Recommendation System

![Java](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)
![Spring Security](https://img.shields.io/badge/Spring%20Security-JWT-red.svg)
![React](https://img.shields.io/badge/React-18-blue.svg)
![AI Matching](https://img.shields.io/badge/AI-NLP%20%26%20Jaccard%20Similarity-purple.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)
![Vite](https://img.shields.io/badge/Vite-6.x-yellow.svg)
![License](https://img.shields.io/badge/License-MIT-green.svg)

**JobHub** is a production-grade full-stack job portal application built with **Java 21**, **Spring Boot 3**, **MySQL 8**, **React.js**, and an **AI Candidate Recommendation Engine**. 

Everything in this project connects dynamically to real MySQL database entities via Spring Boot REST APIs — with **zero hardcoded fake jobs, fake numbers, or fake companies**.

---

## 📂 Project Directory Structure

```
jobhub/
├── backend/                              # Java Spring Boot Backend
│   ├── src/main/java/com/jobhub/jobhub/
│   │   ├── config/                      # SecurityConfig (JWT & CORS Configuration)
│   │   ├── controller/                  # REST Controllers (Job, Company, AI, Application, Auth)
│   │   ├── dto/                         # Data Transfer Objects (Request / Response / Stats)
│   │   ├── entity/                      # JPA Entities (User, Job, Company, Application, SavedJob)
│   │   ├── exception/                   # Global Exception Handler & Custom Exceptions
│   │   ├── repository/                 # Spring Data JPA Repositories
│   │   ├── security/                   # JwtAuthenticationFilter & JwtProvider
│   │   └── service/                    # Business Logic & AiRecommendationService
│   ├── src/main/resources/              # application.properties & Database Config
│   ├── pom.xml                          # Maven Dependencies & Build Configuration
│   └── Dockerfile                       # Multi-stage Container Deployment File
│
├── frontend/                             # React.js + Vite Frontend
│   ├── src/
│   │   ├── components/                  # Reusable Components (JobCard, CompanyCard, JobFilters, etc.)
│   │   ├── context/                     # AuthContext (Authentication State & LocalStorage)
│   │   ├── pages/                       # Page Views (Home, Jobs, Companies, JobDetails, About, etc.)
│   │   ├── services/                    # Axios API Interceptor Service Layer
│   │   ├── App.jsx                      # React Router Navigation Routes
│   │   └── index.css                    # Responsive CSS3 Tokens & Breakpoints
│   ├── package.json                     # Frontend Dependencies
│   └── vercel.json                      # Vercel Deployment SPA Routing Config
│
└── README.md                             # Project Documentation
```

---

## 🛠️ Full Technology Stack (Including AI Tech)

### 🤖 AI & Matching Technology Stack:
- **AI Recommendation Engine:** Implemented in `AiRecommendationService.java` & `AiRecommendationController.java`.
- **NLP Text Tokenization:** Lowercasing, punctuation stripping, and canonical skill extraction.
- **Jaccard Similarity Index:** Mathematical set distance algorithm for skill overlap computation ($J(A,B) = \frac{|A \cap B|}{|A \cup B|}$).
- **Cosine Similarity Model:** Vector space similarity representation.
- **Weighted Match Engine:** $70\% \text{ Skill Match} + 20\% \text{ Experience Match} + 10\% \text{ Location Match}$.
- **Skill Gap Analysis:** Set difference operator $(B \setminus A)$ to generate automated developer learning advice.

### ⚙️ Backend Technology Stack:
- **Language:** Java 21
- **Framework:** Spring Boot 3.x (Spring Web, Spring Data JPA, Hibernate ORM)
- **Security:** Spring Security, Stateless **JWT Bearer Token** Authentication, **BCrypt** Password Hashing ($2^{10}$ rounds)
- **Database:** MySQL 8.0 Relational Database
- **Containerization:** Docker (Multi-stage Eclipse Temurin JRE build)

### 🎨 Frontend Technology Stack:
- **Library:** React.js 18
- **Build Tool:** Vite 6.x
- **Routing:** React Router v6 (Single Page Application)
- **HTTP Client:** Axios (With Request & Response Interceptors)
- **Icons:** Lucide React Icons
- **Styling:** Custom CSS3 (CSS Variables, Flexbox, Responsive Grid)

---

## 🌟 Implemented Features

### 👨‍💻 Candidate Features:
- **Real-Time Job Search:** Search jobs from MySQL by keyword, location, job type (*FULL_TIME, PART_TIME, INTERNSHIP, CONTRACT*), experience level (*Fresher, 0-2 Yrs, 2-5 Yrs, 5+ Yrs*), minimum salary, and skills.
- **AI Skill Match Score:** View dynamic **Match Percentage (e.g. 92% Match)** calculated by `AiRecommendationService.java` based on candidate skills vs job required skills.
- **Skill Gap & Learning Advice:** Receive automated insights on missing technical skills for each job.
- **Application Status Pipeline:** Submit applications with cover letters and track live status updates (*APPLIED ➔ REVIEWING ➔ SHORTLISTED ➔ SELECTED / REJECTED*).
- **PDF Resume Upload & Profile:** Upload PDF resumes, add portfolio links, expected CTC, notice period, and immediate joiner badges.
- **Saved Jobs (Bookmarks):** Save favorite jobs to review later from candidate dashboard.

### 🏢 Recruiter Features:
- **Company Profile Management:** Create, edit, and update company details (*Logo URL, Industry, Website, Location, Employee Size, Founded Year*).
- **Job Posting & Management:** Post new jobs, edit postings, and manage open positions.
- **Applicant Inspection:** Review candidate profiles, cover letters, and inspect uploaded PDF resumes with 1-click preview links.
- **Application Status Updates:** Update applicant statuses dynamically in real-time.
- **Recruiter Analytics Dashboard:** View total posted jobs, total applications received, active candidates, and recruitment stats.

---

## 🔌 Actual REST API Endpoints

| Category | Method | Endpoint | Access |
| :--- | :--- | :--- | :--- |
| **Auth** | `POST` | `/api/auth/register` | Public |
| **Auth** | `POST` | `/api/auth/login` | Public |
| **Jobs** | `GET` | `/api/jobs/search` | Public |
| **Jobs** | `GET` | `/api/jobs/latest` | Public |
| **Jobs** | `GET` | `/api/jobs/categories` | Public |
| **Jobs** | `GET` | `/api/jobs/{id}` | Public |
| **Jobs** | `POST` | `/api/jobs` | Recruiter |
| **AI** | `GET` | `/api/ai/recommendations` | Candidate |
| **Companies** | `GET` | `/api/companies/search` | Public |
| **Companies** | `GET` | `/api/companies/stats` | Public |
| **Companies** | `GET` | `/api/companies/{id}` | Public |
| **Companies** | `GET` | `/api/companies/{id}/jobs` | Public |
| **Companies** | `POST` | `/api/companies` | Recruiter |
| **Applications**| `POST` | `/api/applications` | Candidate |
| **Applications**| `GET` | `/api/applications/my` | Candidate |
| **Applications**| `GET` | `/api/applications/job/{jobId}` | Recruiter |
| **Applications**| `PUT` | `/api/applications/{id}/status` | Recruiter |
| **Saved Jobs** | `POST` | `/api/saved-jobs/{jobId}` | Candidate |
| **Saved Jobs** | `GET` | `/api/saved-jobs` | Candidate |
| **Dashboard** | `GET` | `/api/dashboard/candidate` | Candidate |
| **Dashboard** | `GET` | `/api/dashboard/recruiter` | Recruiter |

---

## 📄 Resume Project Description (Ready to Copy-Paste)

> **JobHub — Full-Stack Job Portal & AI Recommendation System**  
> *Tech Stack: Java 21, Spring Boot 3, Spring Security (JWT), MySQL 8, React 18, Vite, Axios, REST APIs, Jaccard Similarity Match Algorithm*
> - **Engineered** a production-ready full-stack job platform supporting Candidate & Recruiter workflows with stateless **JWT Authentication** and **BCrypt Password Encryption**.
> - **Implemented** an **AI Candidate Recommendation Engine** (`AiRecommendationService.java`) utilizing skill tokenization, Jaccard Similarity Indexing, and weighted multi-factor scoring (70% Skill + 20% Exp + 10% Location) to compute dynamic match percentages and skill gap insights.
> - **Developed** multi-criteria search, filtering, and sorting across MySQL database with dynamic pagination (`Page<Job>`).
> - **Designed** a transparent candidate application status lifecycle pipeline (*APPLIED ➔ REVIEWING ➔ SHORTLISTED ➔ SELECTED*) and 1-click recruiter PDF resume inspection.

---

## 💻 How to Run Locally

### 1. Database Setup
Create MySQL database `jobhub_db`:
```sql
CREATE DATABASE jobhub_db;
```

### 2. Backend Setup
```bash
cd backend
# Update MySQL password in src/main/resources/application.properties
./mvnw spring-boot:run
```
*(Backend runs at `http://localhost:8080`)*

### 3. Frontend Setup
```bash
cd frontend
npm install
npm run dev
```
*(Frontend runs at `http://localhost:5173` or `http://localhost:5174`)*

---

## 🚀 Live Deployment
- **Frontend:** Vercel (preset: Vite, root directory: `frontend`)
- **Backend:** Render / Railway / Docker (uses included `Dockerfile`)
- **Database:** Aiven for MySQL / Railway MySQL

---

## 📜 License
This project is open source and available under the [MIT License](LICENSE).
