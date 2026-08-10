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

// In-Memory Fast Cache Map & TTL (2 Minutes)
const cache = new Map();
const CACHE_TTL_MS = 2 * 60 * 1000; // 2 Minutes

const getCachedData = (key) => {
  const cached = cache.get(key);
  if (!cached) return null;
  if (Date.now() - cached.timestamp > CACHE_TTL_MS) {
    cache.delete(key);
    return null;
  }
  return cached.data;
};

const setCachedData = (key, data) => {
  cache.set(key, { data, timestamp: Date.now() });
};

export const clearApiCache = () => {
  cache.clear();
};

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

// Response Interceptor: Global error handling & automatic cache invalidation on mutations
API.interceptors.response.use(
  (response) => {
    // Invalidate cache on mutations (POST, PUT, DELETE)
    if (['post', 'put', 'delete'].includes(response.config.method?.toLowerCase())) {
      cache.clear();
    }
    return response;
  },
  (error) => {
    if (error.response && error.response.status === 401) {
      // Token expired or invalid
      localStorage.removeItem('jobhub_token');
      localStorage.removeItem('jobhub_user');
      cache.clear();
    }
    return Promise.reject(error);
  }
);

// Helper for Cached GET Requests (Instant 0ms Response!)
const cachedGet = async (url, config = {}) => {
  const cacheKey = url + JSON.stringify(config.params || {});
  const cached = getCachedData(cacheKey);
  if (cached) {
    // Return cached response instantly and fetch fresh data in background
    API.get(url, config).then(res => setCachedData(cacheKey, res)).catch(() => {});
    return cached;
  }
  const response = await API.get(url, config);
  setCachedData(cacheKey, response);
  return response;
};

// API Endpoints Services
export const authService = {
  login: (data) => API.post('/auth/login', data),
  register: (data) => API.post('/auth/register', data),
};

export const userService = {
  getUserById: (id) => cachedGet(`/users/${id}`),
  updateUser: (id, data) => API.put(`/users/${id}`, data),
};

export const jobService = {
  getAllJobs: () => cachedGet('/jobs'),
  getLatestJobs: () => cachedGet('/jobs/latest'),
  getCategories: () => cachedGet('/jobs/categories'),
  getJobById: (id) => cachedGet(`/jobs/${id}`),
  searchJobs: (params) => cachedGet('/jobs/search', { params }),
  createJob: (data) => API.post('/jobs', data),
  updateJob: (id, data) => API.put(`/jobs/${id}`, data),
  deleteJob: (id) => API.delete(`/jobs/${id}`),
  getRecruiterJobs: (recruiterId) => cachedGet(`/jobs/recruiter/${recruiterId}`),
};

export const companyService = {
  getAllCompanies: (params) => cachedGet('/companies', { params }),
  searchCompanies: (params) => cachedGet('/companies/search', { params }),
  getCompanyById: (id) => cachedGet(`/companies/${id}`),
  getCompanyJobs: (id, params) => cachedGet(`/companies/${id}/jobs`, { params }),
  getCompanyStats: () => cachedGet('/companies/stats'),
  createOrUpdateCompany: (data) => API.post('/companies', data),
  deleteCompany: (id) => API.delete(`/companies/${id}`),
};

export const homeService = {
  getHomeStats: () => cachedGet('/home/stats'),
};

export const applicationService = {
  applyForJob: (data) => API.post('/applications', data),
  getMyApplications: () => cachedGet('/applications/my'),
  getApplicationById: (id) => cachedGet(`/applications/${id}`),
  getJobApplications: (jobId) => cachedGet(`/applications/job/${jobId}`),
  updateStatus: (id, status) => API.put(`/applications/${id}/status`, { status }),
  withdrawApplication: (id) => API.delete(`/applications/${id}`),
};

export const savedJobService = {
  saveJob: (jobId) => API.post(`/saved-jobs/${jobId}`),
  getSavedJobs: () => cachedGet('/saved-jobs'),
  removeSavedJob: (jobId) => API.delete(`/saved-jobs/${jobId}`),
};

export const dashboardService = {
  getCandidateDashboard: () => cachedGet('/dashboard/candidate'),
  getRecruiterDashboard: () => cachedGet('/dashboard/recruiter'),
};

export const aiService = {
  getRecommendations: () => cachedGet('/ai/recommendations'),
};

export default API;
