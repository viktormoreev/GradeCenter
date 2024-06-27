import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";

const domain = "http://localhost:8082/schools";

// Async thunk to create a new school
export const createSchool = createAsyncThunk(
  "schools/createSchool",
  async (schoolData, { rejectWithValue }) => {
    try {
      const token = localStorage.getItem("accessToken");
      if (!token) throw new Error("No token found");
      const response = await axios.post(`${domain}`, schoolData, {
        headers: { Authorization: `Bearer ${token}` },
      });
      return response.data;
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to create school";
      return rejectWithValue(message);
    }
  }
);

// Async thunk to fetch the list of schools
export const fetchSchools = createAsyncThunk(
  "schools/fetchSchools",
  async (_, { rejectWithValue }) => {
    try {
      const token = localStorage.getItem("accessToken");
      if (!token) throw new Error("No token found");
      const response = await axios.get(`${domain}/names`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      return response.data;
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to fetch schools";
      return rejectWithValue(message);
    }
  }
);

// Async thunk to delete a school
export const deleteSchool = createAsyncThunk(
  "schools/deleteSchool",
  async (id, { rejectWithValue }) => {
    try {
      const token = localStorage.getItem("accessToken");
      if (!token) throw new Error("No token found");
      await axios.delete(`${domain}/id=${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      return id;
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to delete school";
      return rejectWithValue(message);
    }
  }
);

const schoolSlice = createSlice({
  name: "schools",
  initialState: {
    schools: [],
    status: "idle",
    error: null,
  },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(createSchool.pending, (state) => {
        state.status = "loading";
      })
      .addCase(createSchool.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.schools.push(action.payload);
      })
      .addCase(createSchool.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })
      .addCase(fetchSchools.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchSchools.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.schools = action.payload;
      })
      .addCase(fetchSchools.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })
      .addCase(deleteSchool.pending, (state) => {
        state.status = "loading";
      })
      .addCase(deleteSchool.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.schools = state.schools.filter(
          (school) => school.id !== action.payload
        );
      })
      .addCase(deleteSchool.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      });
  },
});

export default schoolSlice.reducer;
