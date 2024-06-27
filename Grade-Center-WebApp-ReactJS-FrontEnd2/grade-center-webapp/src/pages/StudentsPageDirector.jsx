import React, { useEffect, useState } from "react";
import {
  Box,
  Button,
  Typography,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  TextField,
  MenuItem,
} from "@mui/material";
import { DataGrid } from "@mui/x-data-grid";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { fetchStudentsBySchoolId } from "../features/students/studentSlice";
import { fetchDirectorDetails } from "../features/directors/directorSlice";

export default function StudentsPageDirector() {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { directorDetails, status: directorStatus } = useSelector(
    (state) => state.directors
  );
  const { students, status: studentStatus } = useSelector(
    (state) => state.students
  );
  const [open, setOpen] = useState(false);
  const [assignmentData, setAssignmentData] = useState({
    studentId: "",
    grade: "",
  });

  useEffect(() => {
    if (directorDetails && directorDetails.schoolId) {
      dispatch(fetchStudentsBySchoolId(directorDetails.schoolId));
    }
  }, [directorDetails, dispatch]);

  useEffect(() => {
    dispatch(fetchDirectorDetails());
  }, [dispatch]);

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleSave = () => {
    console.log("Assigning Data", assignmentData);
    setOpen(false);
  };

  const handleChange = (event) => {
    setAssignmentData({
      ...assignmentData,
      [event.target.name]: event.target.value,
    });
  };

  const columns = [
    { field: "username", headerName: "Name", width: 200 },
    { field: "grade", headerName: "Grade", width: 100 },
    {
      field: "parents",
      headerName: "Parents",
      width: 300,
      renderCell: (params) =>
        Array.isArray(params.value) ? params.value.join(", ") : "None",
    },
    {
      field: "courses",
      headerName: "Courses",
      width: 300,
      renderCell: (params) =>
        Array.isArray(params.value)
          ? params.value.map((course) => course.name).join(", ")
          : "",
    },
  ];

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
          Students
        </Typography>
        <Box>
          <Button variant="contained" color="primary" onClick={handleClickOpen}>
            Assign Student to Grade/Class
          </Button>
          <Button
            variant="contained"
            color="success"
            onClick={() => navigate("/director/dashboard")}
            sx={{ marginLeft: 2 }}
          >
            Back to Dashboard
          </Button>
        </Box>
      </Box>

      <DataGrid
        rows={students.map((student) => ({
          id: student.id,
          username: student.username,
          grade: student.grade,
          parents: student.parent,
          courses: student.courses,
        }))}
        columns={columns}
        pageSize={5}
        loading={studentStatus === "loading" || directorStatus === "loading"}
      />

      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>Assign to Grade/Class</DialogTitle>
        <DialogContent>
          <TextField
            select
            autoFocus
            margin="dense"
            name="studentId"
            label="Select Student"
            fullWidth
            value={assignmentData.studentId}
            onChange={handleChange}
          >
            {students.map((student) => (
              <MenuItem key={student.id} value={student.id}>
                {student.username}
              </MenuItem>
            ))}
          </TextField>
          <TextField
            select
            margin="dense"
            name="grade"
            label="Grade"
            fullWidth
            value={assignmentData.grade}
            onChange={handleChange}
          >
            {Array.from({ length: 12 }, (_, i) => (
              <MenuItem key={i + 1} value={i + 1}>
                Grade {i + 1}
              </MenuItem>
            ))}
          </TextField>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleSave}>Save</Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}
