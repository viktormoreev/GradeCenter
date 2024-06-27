// scheduleSlice.jsx
import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";

const domain = "http://localhost:8082";

// Async thunk to fetch the schedule
export const fetchSchedule = createAsyncThunk(
  "schedule/fetchSchedule",
  async (_, { rejectWithValue }) => {
    try {
      const token = localStorage.getItem("accessToken");
      if (!token) throw new Error("No token found");
      const response = await axios.get(`${domain}/schedule`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      return response.data;
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to fetch schedule";
      return rejectWithValue(message);
    }
  }
);

// Async thunk to fetch schedule by student ID
export const fetchScheduleByStudentId = createAsyncThunk(
  "schedule/fetchScheduleByStudentId",
  async (studentId, { rejectWithValue }) => {
    try {
      const token = localStorage.getItem("accessToken");
      if (!token) throw new Error("No token found");
      const response = await axios.get(
        `${domain}/schedule/studentId=${studentId}`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      return response.data;
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to fetch schedule by student ID";
      return rejectWithValue(message);
    }
  }
);

// Async thunk to fetch the student's own schedule
export const fetchOwnSchedule = createAsyncThunk(
  "schedule/fetchOwnSchedule",
  async (_, { rejectWithValue }) => {
    try {
      const token = localStorage.getItem("accessToken");
      if (!token) throw new Error("No token found");
      const response = await axios.get(`${domain}/schedule/me`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      return response.data;
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to fetch own schedule";
      return rejectWithValue(message);
    }
  }
);

const scheduleSlice = createSlice({
  name: "schedule",
  initialState: {
    schedule: [],
    studentSchedule: [],
    ownSchedule: [],
    status: "idle",
    error: null,
  },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchSchedule.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchSchedule.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.schedule = action.payload;
      })
      .addCase(fetchSchedule.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })
      .addCase(fetchScheduleByStudentId.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchScheduleByStudentId.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.studentSchedule = action.payload;
      })
      .addCase(fetchScheduleByStudentId.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })
      .addCase(fetchOwnSchedule.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchOwnSchedule.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.ownSchedule = action.payload;
      })
      .addCase(fetchOwnSchedule.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      });
  },
});

export default scheduleSlice.reducer;
