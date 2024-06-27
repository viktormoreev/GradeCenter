import { createSlice, createAsyncThunk } from "@reduxjs/toolkit";
import axios from "axios";

const domain = "http://localhost:8082/teachers";

// Async thunk to fetch teachers
export const fetchTeachers = createAsyncThunk(
  "teachers/fetchTeachers",
  async (_, { rejectWithValue }) => {
    try {
      const token = localStorage.getItem("accessToken");
      if (!token) throw new Error("No token found");
      const response = await axios.get(`${domain}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      return response.data.map((teacher) => ({
        id: teacher.id, // Ensure the correct field is used for id
        name: teacher.name,
        schoolName: teacher.schoolName,
        courses: teacher.courses,
        qualifications: teacher.qualifications,
      }));
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to fetch teachers";
      return rejectWithValue(message);
    }
  }
);

// Async thunk to add a new teacher
export const addTeacher = createAsyncThunk(
  "teachers/addTeacher",
  async (teacherData, { rejectWithValue }) => {
    try {
      const token = localStorage.getItem("accessToken");
      if (!token) throw new Error("No token found");
      const response = await axios.post(`${domain}`, teacherData, {
        headers: { Authorization: `Bearer ${token}` },
      });
      return response.data;
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to add teacher";
      return rejectWithValue(message);
    }
  }
);

// Async thunk to update teacher details
export const updateTeacher = createAsyncThunk(
  "teachers/updateTeacher",
  async ({ id, data }, { rejectWithValue }) => {
    try {
      const token = localStorage.getItem("accessToken");
      if (!token) throw new Error("No token found");
      const response = await axios.patch(`${domain}/${id}`, data, {
        headers: { Authorization: `Bearer ${token}` },
      });
      return response.data;
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to update teacher";
      return rejectWithValue(message);
    }
  }
);

// Async thunk to delete a teacher
export const deleteTeacher = createAsyncThunk(
  "teachers/deleteTeacher",
  async (id, { rejectWithValue }) => {
    try {
      const token = localStorage.getItem("accessToken");
      if (!token) throw new Error("No token found");
      const response = await axios.delete(`${domain}/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      return { id };
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to delete teacher";
      return rejectWithValue(message);
    }
  }
);

// Async thunk to fetch teachers by school ID
export const fetchTeachersBySchoolId = createAsyncThunk(
  "teachers/fetchTeachersBySchoolId",
  async (schoolId, { rejectWithValue }) => {
    try {
      const token = localStorage.getItem("accessToken");
      if (!token) throw new Error("No token found");
      const response = await axios.get(
        `http://localhost:8082/schools/id=${schoolId}/teachers`,
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      return response.data;
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to fetch teachers by school ID";
      return rejectWithValue(message);
    }
  }
);

// Async thunk to assign a teacher to a school
export const assignTeacherToSchool = createAsyncThunk(
  "teachers/assignTeacherToSchool",
  async (
    { teacherId, schoolId, courseIds = [], qualificationsIds = [] },
    { rejectWithValue }
  ) => {
    try {
      const token = localStorage.getItem("accessToken");
      if (!token) throw new Error("No token found");
      const response = await axios.put(
        `${domain}/id=${teacherId}`,
        { schoolId, courseIds, qualificationsIds },
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      return response.data;
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to assign teacher to school";
      return rejectWithValue(message);
    }
  }
);

// Async thunk to remove a teacher from a school
export const removeTeacherFromSchool = createAsyncThunk(
  "teachers/removeTeacherFromSchool",
  async ({ teacherId }, { rejectWithValue }) => {
    try {
      const token = localStorage.getItem("accessToken");
      if (!token) throw new Error("No token found");
      const response = await axios.patch(
        `${domain}/remove-teacher-school/id=${teacherId}`,
        {},
        {
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      return response.data;
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to remove teacher from school";
      return rejectWithValue(message);
    }
  }
);

// Async thunk to fetch personal details of the logged-in teacher
export const fetchTeacherDetails = createAsyncThunk(
  "teachers/fetchTeacherDetails",
  async (_, { rejectWithValue }) => {
    try {
      const token = localStorage.getItem("accessToken");
      if (!token) throw new Error("No token found");
      const response = await axios.get(`${domain}/me`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      return response.data;
    } catch (error) {
      const message =
        error.response?.data?.message ||
        error.message ||
        "Failed to fetch teacher details";
      return rejectWithValue(message);
    }
  }
);

const teacherSlice = createSlice({
  name: "teachers",
  initialState: {
    teachers: [],
    teacherDetails: null,
    status: "idle",
    error: null,
  },
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchTeachers.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchTeachers.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.teachers = action.payload;
      })
      .addCase(fetchTeachers.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })
      .addCase(addTeacher.pending, (state) => {
        state.status = "loading";
      })
      .addCase(addTeacher.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.teachers.push(action.payload);
      })
      .addCase(addTeacher.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })
      .addCase(updateTeacher.pending, (state) => {
        state.status = "loading";
      })
      .addCase(updateTeacher.fulfilled, (state, action) => {
        state.status = "succeeded";
        const updatedTeacher = action.payload;
        const existingTeacher = state.teachers.find(
          (teacher) => teacher.id === updatedTeacher.id
        );
        if (existingTeacher) {
          Object.assign(existingTeacher, updatedTeacher);
        }
      })
      .addCase(updateTeacher.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })
      .addCase(deleteTeacher.pending, (state) => {
        state.status = "loading";
      })
      .addCase(deleteTeacher.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.teachers = state.teachers.filter(
          (teacher) => teacher.id !== action.payload.id
        );
      })
      .addCase(deleteTeacher.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })
      .addCase(fetchTeachersBySchoolId.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchTeachersBySchoolId.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.teachers = action.payload;
      })
      .addCase(fetchTeachersBySchoolId.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })
      .addCase(assignTeacherToSchool.pending, (state) => {
        state.status = "loading";
      })
      .addCase(assignTeacherToSchool.fulfilled, (state, action) => {
        state.status = "succeeded";
      })
      .addCase(assignTeacherToSchool.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })
      .addCase(removeTeacherFromSchool.pending, (state) => {
        state.status = "loading";
      })
      .addCase(removeTeacherFromSchool.fulfilled, (state, action) => {
        state.status = "succeeded";
      })
      .addCase(removeTeacherFromSchool.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      })
      .addCase(fetchTeacherDetails.pending, (state) => {
        state.status = "loading";
      })
      .addCase(fetchTeacherDetails.fulfilled, (state, action) => {
        state.status = "succeeded";
        state.teacherDetails = action.payload;
      })
      .addCase(fetchTeacherDetails.rejected, (state, action) => {
        state.status = "failed";
        state.error = action.payload;
      });
  },
});

export default teacherSlice.reducer;
