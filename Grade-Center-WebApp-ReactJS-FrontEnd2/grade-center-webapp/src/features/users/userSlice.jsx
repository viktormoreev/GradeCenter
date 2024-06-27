import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";

const domain = "http://localhost:8082/api/v1/auth";

// Async thunk to fetch users
export const fetchUsers = createAsyncThunk(
  "users/fetchUsers",
  async (_, { rejectWithValue }) => {
    try {
      const response = await axios.get(`${domain}/users`);
      // Transform the response to an array of users
      const users = Object.entries(response.data).map(([username, role]) => ({
        username,
        role,
      }));
      return users;
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to fetch users";
      return rejectWithValue(message);
    }
  }
);

// Async thunk to change user role
export const changeUserRole = createAsyncThunk(
  "users/changeUserRole",
  async ({ username, role }, { rejectWithValue }) => {
    try {
      const response = await axios.patch(`${domain}/switch-role-username`, {
        username,
        role,
      });
      return response.data;
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to change user role";
      return rejectWithValue(message);
    }
  }
);

// Async thunk to delete user
export const deleteUser = createAsyncThunk(
  "users/deleteUser",
  async (username, { rejectWithValue }) => {
    try {
      const response = await axios.delete(
        `${domain}/delete-user/username=${username}`
      );
      return { username };
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to delete user";
      return rejectWithValue(message);
    }
  }
);

const userSlice = createSlice({
  name: "users",
  initialState: {
    users: [],
    status: "idle",
    error: null,
  },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchUsers.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchUsers.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.users = action.payload;
      })
      .addCase(fetchUsers.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })
      .addCase(changeUserRole.pending, (state) => {
        state.status = "loading";
      })
      .addCase(changeUserRole.fulfilled, (state, action) => {
        state.status = "succeeded";
        const { username, role } = action.payload;
        const existingUser = state.users.find(
          (user) => user.username === username
        );
        if (existingUser) {
          existingUser.role = role;
        }
      })
      .addCase(changeUserRole.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })
      .addCase(deleteUser.pending, (state) => {
        state.status = "loading";
      })
      .addCase(deleteUser.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.users = state.users.filter(
          (user) => user.username !== action.payload.username
        );
      })
      .addCase(deleteUser.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      });
  },
});

export default userSlice.reducer;
