import axios from 'axios';

// Ensure base URL always ends with /api regardless of Vercel env variable formatting
const getApiBaseUrl = () => {
  let url = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api';
  url = url.trim().replace(/\/+$/, ''); // Remove trailing slashes
  if (!url.endsWith('/api')) {
    url += '/api';
  }
  return url;
};

const API_BASE_URL = getApiBaseUrl();

const API = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Request Interceptor: Attach JWT Bearer Token
API.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('jobhub_token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response Interceptor: Global error handling
API.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      // Token expired or invalid
      localStorage.removeItem('jobhub_token');
      localStorage.removeItem('jobhub_user');
    }
    return Promise.reject(error);
  }
);

// API Endpoints Services
export const authService = {
  login: (data) => API.post('/auth/login', data),
  register: (data) => API.post('/auth/register', data),
};

export const userService = {
  getUserById: (id) => API.get(`/users/${id}`),
  updateUser: (id, data) => API.put(`/users/${id}`, data),
};

export const jobService = {
  getAllJobs: () => API.get('/jobs'),
  getLatestJobs: () => API.get('/jobs/latest'),
  getCategories: () => API.get('/jobs/categories'),
  getJobById: (id) => API.get(`/jobs/${id}`),
  searchJobs: (params) => API.get('/jobs/search', { params }),
  createJob: (data) => API.post('/jobs', data),
  updateJob: (id, data) => API.put(`/jobs/${id}`, data),
  deleteJob: (id) => API.delete(`/jobs/${id}`),
  getRecruiterJobs: (recruiterId) => API.get(`/jobs/recruiter/${recruiterId}`),
};

export const companyService = {
  getAllCompanies: (params) => API.get('/companies', { params }),
  searchCompanies: (params) => API.get('/companies/search', { params }),
  getCompanyById: (id) => API.get(`/companies/${id}`),
  getCompanyJobs: (id, params) => API.get(`/companies/${id}/jobs`, { params }),
  getCompanyStats: () => API.get('/companies/stats'),
  createOrUpdateCompany: (data) => API.post('/companies', data),
  deleteCompany: (id) => API.delete(`/companies/${id}`),
};

export const homeService = {
  getHomeStats: () => API.get('/home/stats'),
};

export const applicationService = {
  applyForJob: (data) => API.post('/applications', data),
  getMyApplications: () => API.get('/applications/my'),
  getApplicationById: (id) => API.get(`/applications/${id}`),
  getJobApplications: (jobId) => API.get(`/applications/job/${jobId}`),
  updateStatus: (id, status) => API.put(`/applications/${id}/status`, { status }),
  withdrawApplication: (id) => API.delete(`/applications/${id}`),
};

export const savedJobService = {
  saveJob: (jobId) => API.post(`/saved-jobs/${jobId}`),
  getSavedJobs: () => API.get('/saved-jobs'),
  removeSavedJob: (jobId) => API.delete(`/saved-jobs/${jobId}`),
};

export const dashboardService = {
  getCandidateDashboard: () => API.get('/dashboard/candidate'),
  getRecruiterDashboard: () => API.get('/dashboard/recruiter'),
};

export const aiService = {
  getRecommendations: () => API.get('/ai/recommendations'),
};

export default API;
