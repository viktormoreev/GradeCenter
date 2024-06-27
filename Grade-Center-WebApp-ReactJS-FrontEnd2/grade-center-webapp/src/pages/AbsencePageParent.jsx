import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useParams, useNavigate } from "react-router-dom";
import { Box, Typography, Button, CircularProgress } from "@mui/material";
import { DataGrid, GridToolbar } from "@mui/x-data-grid";
import { fetchAbsencesByStudentId } from "../features/absences/absenceSlice";
import { fetchCourseById } from "../features/courses/courseSlice";
import { fetchStudentById } from "../features/students/studentSlice";

export default function AbsencePageParent() {
  const { studentId } = useParams();
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { absences, status, error } = useSelector((state) => state.absence);
  const { course, status: courseStatus } = useSelector((state) => state.course);
  const { student, status: studentStatus } = useSelector(
    (state) => state.students
  );

  useEffect(() => {
    if (studentId) {
      dispatch(fetchAbsencesByStudentId(studentId));
      dispatch(fetchStudentById(studentId)); // Fetch the student details
    }
  }, [dispatch, studentId]);

  useEffect(() => {
    if (absences.length > 0) {
      absences.forEach((absence) => {
        dispatch(fetchCourseById(absence.courseId));
      });
    }
  }, [dispatch, absences]);

  const columns = [
    { field: "date", headerName: "Date", width: 200 },
    { field: "courseName", headerName: "Course", width: 300 },
  ];

  const rows = absences.map((absence, index) => ({
    id: index,
    date: absence.date,
    courseName: course?.name || "Loading...",
  }));

  return (
    <Box sx={{ height: "calc(100vh - 64px - 16px - 16px)", width: "100%" }}>
      <Box
        sx={{
          display: "flex",
          justifyContent: "space-between",
          alignItems: "center",
          p: 2,
        }}
      >
        <Typography variant="h4" component="h1">
          Absences for{" "}
          {studentStatus === "succeeded" && student
            ? student.username
            : `Student ID: ${studentId}`}
        </Typography>
        <Button
          variant="contained"
          color="success"
          onClick={() => navigate("/parent/dashboard")}
        >
          Back to Dashboard
        </Button>
      </Box>
      {status === "loading" && (
        <Box sx={{ display: "flex", justifyContent: "center", mt: 4 }}>
          <CircularProgress />
        </Box>
      )}
      {status === "failed" && (
        <Typography variant="h6" sx={{ m: 2 }}>
          Error: {error}
        </Typography>
      )}
      {status === "succeeded" && absences.length === 0 && (
        <Typography variant="h6" sx={{ m: 2 }}>
          No absences found for this student.
        </Typography>
      )}
      {status === "succeeded" && (
        <DataGrid
          rows={rows}
          columns={columns}
          pageSize={5}
          components={{ Toolbar: GridToolbar }}
          disableSelectionOnClick
        />
      )}
    </Box>
  );
}
