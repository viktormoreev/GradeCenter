// groupSlice.jsx
import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";

const domain = "http://localhost:8082/studyGroup";

// Async thunk to fetch study groups
export const fetchStudyGroups = createAsyncThunk(
  "groups/fetchStudyGroups",
  async (_, { rejectWithValue }) => {
    try {
      const token = localStorage.getItem("accessToken");
      if (!token) throw new Error("No token found");
      const response = await axios.get(`${domain}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      return response.data;
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to fetch study groups";
      return rejectWithValue(message);
    }
  }
);

// Async thunk to create a new study group
export const createStudyGroup = createAsyncThunk(
  "groups/createStudyGroup",
  async (groupData, { rejectWithValue }) => {
    try {
      const token = localStorage.getItem("accessToken");
      if (!token) throw new Error("No token found");
      const response = await axios.post(`${domain}`, groupData, {
        headers: { Authorization: `Bearer ${token}` },
      });
      return response.data;
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to create study group";
      return rejectWithValue(message);
    }
  }
);

// Async thunk to delete a study group
export const deleteStudyGroup = createAsyncThunk(
  "groups/deleteStudyGroup",
  async (id, { rejectWithValue }) => {
    try {
      const token = localStorage.getItem("accessToken");
      if (!token) throw new Error("No token found");
      await axios.delete(`${domain}/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      return id;
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to delete study group";
      return rejectWithValue(message);
    }
  }
);

const groupSlice = createSlice({
  name: "groups",
  initialState: {
    studyGroups: [],
    status: "idle",
    error: null,
  },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchStudyGroups.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchStudyGroups.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.studyGroups = action.payload;
      })
      .addCase(fetchStudyGroups.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })
      .addCase(createStudyGroup.pending, (state) => {
        state.status = "loading";
      })
      .addCase(createStudyGroup.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.studyGroups.push(action.payload);
      })
      .addCase(createStudyGroup.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })
      .addCase(deleteStudyGroup.pending, (state) => {
        state.status = "loading";
      })
      .addCase(deleteStudyGroup.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.studyGroups = state.studyGroups.filter(
          (group) => group.id !== action.payload
        );
      })
      .addCase(deleteStudyGroup.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      });
  },
});

export default groupSlice.reducer;
