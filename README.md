# 💼 JobHub — Full-Stack Job Portal & AI Recommendation Engine

![Java](https://img.shields.io/badge/Java-21-orange.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)
![React](https://img.shields.io/badge/React-18-blue.svg)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue.svg)
![License](https://img.shields.io/badge/License-MIT-green.svg)

**JobHub** is a modern, production-grade full-stack job portal web application designed to connect developers with top hiring companies. Built with **Java Spring Boot**, **MySQL**, **React.js**, and an **AI-Assisted Candidate Job Recommendation & Skill Gap Analysis Engine**, JobHub provides transparent application tracking, real-time database search, and personalized career growth insights.

---

## 🌟 Key Features

### 👨‍💻 For Candidates:
- **Smart Multi-Criteria Job Search:** Filter jobs by Keyword, Location, Job Type (*Full Time, Part Time, Internship, Contract*), Experience Level (*0-2 Yrs, 2-5 Yrs, 5+ Yrs*), Minimum Salary, and Required Skills.
- **AI-Powered Skill Match Score:** View dynamic **Match Percentage (e.g. 92% Match)** for each job position based on profile tech skills.
- **Skill Gap & Learning Advice:** Receive automated feedback on missing technical skills and recommended topics to learn.
- **Transparent Application Pipeline:** Track application status in real-time (*APPLIED ➔ REVIEWING ➔ SHORTLISTED ➔ SELECTED / REJECTED*).
- **PDF Resume Upload & Profile Customization:** Upload PDF resumes, add portfolio links, expected CTC, notice period, and immediate joiner badges.
- **Bookmark & Saved Jobs:** Save interesting jobs to review later from the candidate dashboard.

### 🏢 For Recruiters:
- **Company Profile Management:** Create, edit, and update company profiles (*Logo, Industry, Website, Employee Count, Founded Year*).
- **Job Posting & Management:** Post new jobs, edit existing postings, and manage open positions.
- **Applicant Inspection:** Review candidate profiles, cover letters, and inspect uploaded PDF resumes with 1-click preview links.
- **Hiring Workflow Pipeline:** Update applicant statuses dynamically (*Shortlist, Select, or Reject*).
- **Recruiter Analytics Dashboard:** Monitor total posted jobs, received applications, active candidates, and recruitment stats.

---

## 🤖 AI Recommendation Engine (Technical Architecture)

JobHub features an **AI-Assisted Recommendation & Skill Gap Analysis Service** (`AiRecommendationService.java`) that eliminates generic search noise by matching developer profiles with real job requirements.

```
Candidate Profile (Skills, Experience, Location)
                       │
                       ▼
         Text Tokenization & Normalization (NLP)
                       │
                       ▼
      Jaccard & Cosine Similarity Set Analysis
                       │
                       ▼
         Multi-Factor Weighted Scoring Engine
     [70% Skill Match + 20% Exp Match + 10% Location]
                       │
                       ▼
   Match Percentage (92%) + Skill Gap (Docker, AWS)
```

### 🧮 Mathematical & NLP Matching Model:
1. **NLP Text Normalization & Tokenization:**
   Converts Candidate Bio/Skills string (e.g., `"Java, Spring Boot, MySQL, React"`) into canonical normalized skill sets:
   $$\text{CandidateSkills} = \{\text{"java"}, \text{"springboot"}, \text{"mysql"}, \text{"react"}\}$$

2. **Jaccard Similarity Index:**
   Computes Set Intersection over Union between Candidate Skills ($A$) and Job Required Skills ($B$):
   $$J(A, B) = \frac{|A \cap B|}{|A \cup B|}$$

3. **Weighted Multi-Factor Scoring Formula:**
   $$\text{Final Score} = (0.70 \times \text{SkillMatch}) + (0.20 \times \text{ExpMatch}) + (0.10 \times \text{LocationMatch})$$

4. **Skill Gap Extraction:**
   Extracts missing skills set $(B \setminus A)$ to generate actionable recommendations:
   > *"Learning Docker & AWS Fundamentals will increase your match score to 100%!"*

---

## 🛠️ Full-Stack Technology Stack

### Backend:
- **Language:** Java 21
- **Framework:** Spring Boot 3.x, Spring Web (REST APIs)
- **Security:** Spring Security with Stateless **JWT Bearer Token** Authorization & **BCrypt Password Hashing**
- **Persistence:** Spring Data JPA, Hibernate ORM
- **Database:** MySQL 8.0 Relational Database

### Frontend:
- **Library:** React.js v18
- **Build Tool:** Vite
- **Routing:** React Router v6 (SPA)
- **HTTP Client:** Axios with Request/Response Interceptors
- **Icons:** Lucide React
- **Styling:** Custom Modern CSS3 (Variables, Responsive Grid, Flexbox, Glassmorphism)

---

## 🔌 REST API Endpoints Overview

| Category | Method | Endpoint | Description | Access |
| :--- | :--- | :--- | :--- | :--- |
| **Auth** | `POST` | `/api/auth/register` | Register new Candidate or Recruiter | Public |
| **Auth** | `POST` | `/api/auth/login` | Authenticate & receive JWT Token | Public |
| **Jobs** | `GET` | `/api/jobs/search` | Search & filter jobs with pagination | Public |
| **Jobs** | `GET` | `/api/jobs/{id}` | Get single job details | Public |
| **Jobs** | `POST` | `/api/jobs` | Post a new job | Recruiter |
| **AI** | `GET` | `/api/ai/recommendations` | Get AI candidate match recommendations | Candidate |
| **Companies**| `GET` | `/api/companies/search` | Search companies with open job counts | Public |
| **Companies**| `POST` | `/api/companies` | Create/update recruiter company profile | Recruiter |
| **Applications**|`POST` | `/api/applications` | Apply for a job with cover letter & resume | Candidate |
| **Applications**|`PUT` | `/api/applications/{id}/status`| Update application status | Recruiter |

---

## 💻 Local Installation & Setup Guide

### Prerequisites:
- Java JDK 21+
- Node.js v18+ & npm
- MySQL Server 8.0+

### 1. Database Configuration
Create MySQL database `jobhub_db`:
```sql
CREATE DATABASE jobhub_db;
```

### 2. Backend Setup (Spring Boot)
Navigating to backend directory:
```bash
cd backend
```
Configure database credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/jobhub_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```
Run the Spring Boot application:
```bash
./mvnw spring-boot:run
```
*(Backend runs at `http://localhost:8080`)*

### 3. Frontend Setup (React + Vite)
In a new terminal, navigate to frontend directory:
```bash
cd frontend
npm install
npm run dev
```
*(Frontend runs at `http://localhost:5173` or `http://localhost:5174`)*

---

## 🚀 Cloud Live Deployment Guide

- **Frontend Deployment:** Vercel / Netlify (preset: Vite, root directory: `frontend`, environment variable: `VITE_API_BASE_URL`)
- **Backend Deployment:** Render / Railway / Docker Container (uses included `Dockerfile`)
- **Database Cloud:** Aiven for MySQL / Railway MySQL

---

## 📄 License
This project is open source and available under the [MIT License](LICENSE).
