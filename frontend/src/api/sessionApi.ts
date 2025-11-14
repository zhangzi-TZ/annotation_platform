import axios from 'axios';
import { API_BASE_URL } from '../config';
import {
  AnnotationSession,
  CreateIdentityPayload,
  CreateSessionPayload,
  PersonIdentity,
  VideoResource,
  VideoUploadResponse
} from '../types';

const client = axios.create({
  baseURL: `${API_BASE_URL}/api`,
  timeout: 15000
});

// Add request interceptor for debugging
client.interceptors.request.use(
  (config) => {
    console.log(`[API] ${config.method?.toUpperCase()} ${config.url}`, config.data);
    return config;
  },
  (error) => {
    console.error('[API] Request error:', error);
    return Promise.reject(error);
  }
);

// Add response interceptor for error handling
client.interceptors.response.use(
  (response) => {
    console.log(`[API] Response:`, response.status, response.data);
    return response;
  },
  (error) => {
    console.error('[API] Response error:', error.response?.status, error.response?.data || error.message);
    return Promise.reject(error);
  }
);

export const SessionApi = {
  async getSessions() {
    const { data } = await client.get<AnnotationSession[]>('/sessions');
    return data;
  },

  async createSession(payload: CreateSessionPayload) {
    const { data } = await client.post<AnnotationSession>('/sessions', payload);
    return data;
  },

  async getSession(sessionId: string) {
    const { data } = await client.get<AnnotationSession>(`/sessions/${sessionId}`);
    return data;
  },

  async uploadVideo(sessionId: string, slot: 'a' | 'b' | 'A' | 'B', files: { video: File; boxes?: File }) {
    const formData = new FormData();
    formData.append('video', files.video);
    if (files.boxes) {
      formData.append('boxes', files.boxes);
    }
    const { data } = await client.post<VideoUploadResponse>(`/sessions/${sessionId}/videos/${slot}`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    });
    return data;
  },

  async createIdentity(sessionId: string, payload: CreateIdentityPayload) {
    const { data } = await client.post<PersonIdentity>(`/sessions/${sessionId}/identities`, payload);
    return data;
  },

  async updateIdentity(sessionId: string, identityId: string, payload: Partial<CreateIdentityPayload> & { label?: string }) {
    const { data } = await client.put<PersonIdentity>(`/sessions/${sessionId}/identities/${identityId}`, payload);
    return data;
  },

  async deleteIdentity(sessionId: string, identityId: string) {
    await client.delete(`/sessions/${sessionId}/identities/${identityId}`);
  },

  async exportSession(sessionId: string) {
    const { data } = await client.get(`/sessions/${sessionId}/export`, { responseType: 'blob' });
    return data;
  }
};
