import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";

const domain = "http://localhost:8082";

// Async thunk to fetch grades by student ID
export const fetchGradesByStudentId = createAsyncThunk(
  "grades/fetchGradesByStudentId",
  async (studentId, { rejectWithValue }) => {
    try {
      const token = localStorage.getItem("accessToken");
      if (!token) throw new Error("No token found");
      const response = await axios.get(
        `${domain}/grades/student/${studentId}`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      return response.data;
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to fetch grades by student ID";
      return rejectWithValue(message);
    }
  }
);

// Async thunk to fetch grades by the logged-in student
export const fetchGradesByStudent = createAsyncThunk(
  "grades/fetchGradesByStudent",
  async (_, { rejectWithValue }) => {
    try {
      const token = localStorage.getItem("accessToken");
      const response = await axios.get(`${domain}/grades/me`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      return response.data;
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to fetch grades";
      return rejectWithValue(message);
    }
  }
);

// Async thunk to create a grade
export const createGrade = createAsyncThunk(
  "grades/createGrade",
  async (gradeData, { rejectWithValue }) => {
    try {
      const token = localStorage.getItem("accessToken");
      if (!token) throw new Error("No token found");
      const response = await axios.post(`${domain}/grades`, gradeData, {
        headers: { Authorization: `Bearer ${token}` },
      });
      return response.data;
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to create grade";
      return rejectWithValue(message);
    }
  }
);

// Async thunk to delete a grade
export const deleteGrade = createAsyncThunk(
  "grades/deleteGrade",
  async (gradeId, { rejectWithValue }) => {
    try {
      const token = localStorage.getItem("accessToken");
      const response = await axios.delete(`${domain}/grades/id=${gradeId}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      return gradeId;
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to delete grade";
      return rejectWithValue(message);
    }
  }
);

const gradeSlice = createSlice({
  name: "grades",
  initialState: {
    grades: [],
    status: "idle",
    error: null,
  },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchGradesByStudentId.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchGradesByStudentId.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.grades = action.payload;
      })
      .addCase(fetchGradesByStudentId.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })
      .addCase(fetchGradesByStudent.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchGradesByStudent.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.grades = action.payload;
      })
      .addCase(fetchGradesByStudent.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })
      .addCase(createGrade.pending, (state) => {
        state.status = "loading";
      })
      .addCase(createGrade.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.grades.push(action.payload);
      })
      .addCase(createGrade.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })
      .addCase(deleteGrade.pending, (state) => {
        state.status = "loading";
      })
      .addCase(deleteGrade.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.grades = state.grades.filter(
          (grade) => grade.id !== action.payload
        );
      })
      .addCase(deleteGrade.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      });
  },
});

export default gradeSlice.reducer;
