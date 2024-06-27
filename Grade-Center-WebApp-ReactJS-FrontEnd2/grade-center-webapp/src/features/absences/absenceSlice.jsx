import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";

const domain = "http://localhost:8082";

// Async thunk to fetch absences by student ID
export const fetchAbsencesByStudentId = createAsyncThunk(
  "absences/fetchAbsencesByStudentId",
  async (studentId, { rejectWithValue }) => {
    try {
      const token = localStorage.getItem("accessToken");
      if (!token) throw new Error("No token found");
      const response = await axios.get(
        `${domain}/absences/student/${studentId}`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      return response.data;
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to fetch absences by student ID";
      return rejectWithValue(message);
    }
  }
);

// Async thunk to fetch absences
export const fetchAbsencesByStudent = createAsyncThunk(
  "absences/fetchAbsencesByStudent",
  async (_, { rejectWithValue }) => {
    try {
      const token = localStorage.getItem("accessToken");
      const response = await axios.get(`${domain}/absences/me`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      return response.data;
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to fetch absences";
      return rejectWithValue(message);
    }
  }
);

// Async thunk to fetch all absences
export const fetchAllAbsences = createAsyncThunk(
  "absences/fetchAllAbsences",
  async (_, { rejectWithValue }) => {
    try {
      const token = localStorage.getItem("accessToken");
      if (!token) throw new Error("No token found");
      const response = await axios.get(`${domain}/absences`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      return response.data;
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to fetch absences";
      return rejectWithValue(message);
    }
  }
);

// Async thunk to delete an absence
export const deleteAbsence = createAsyncThunk(
  "absences/deleteAbsence",
  async (id, { rejectWithValue }) => {
    try {
      const token = localStorage.getItem("accessToken");
      const response = await axios.delete(`${domain}/absences/id=${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      return id;
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to delete absence";
      return rejectWithValue(message);
    }
  }
);

// Async thunk to add an absence
export const addAbsence = createAsyncThunk(
  "absences/addAbsence",
  async (absenceData, { rejectWithValue }) => {
    try {
      const token = localStorage.getItem("accessToken");
      if (!token) throw new Error("No token found");
      const response = await axios.post(`${domain}/absences`, absenceData, {
        headers: { Authorization: `Bearer ${token}` },
      });
      return response.data;
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to add absence";
      return rejectWithValue(message);
    }
  }
);

const absenceSlice = createSlice({
  name: "absences",
  initialState: {
    absences: [],
    status: "idle",
    error: null,
  },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchAbsencesByStudentId.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchAbsencesByStudentId.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.absences = action.payload;
      })
      .addCase(fetchAbsencesByStudentId.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })
      .addCase(fetchAbsencesByStudent.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchAbsencesByStudent.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.absences = action.payload;
      })
      .addCase(fetchAbsencesByStudent.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })
      .addCase(fetchAllAbsences.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchAllAbsences.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.absences = action.payload;
      })
      .addCase(fetchAllAbsences.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })
      .addCase(deleteAbsence.pending, (state) => {
        state.status = "loading";
      })
      .addCase(deleteAbsence.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.absences = state.absences.filter(
          (absence) => absence.id !== action.payload
        );
      })
      .addCase(deleteAbsence.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })
      .addCase(addAbsence.pending, (state) => {
        state.status = "loading";
      })
      .addCase(addAbsence.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.absences.push(action.payload);
      })
      .addCase(addAbsence.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      });
  },
});

export default absenceSlice.reducer;
