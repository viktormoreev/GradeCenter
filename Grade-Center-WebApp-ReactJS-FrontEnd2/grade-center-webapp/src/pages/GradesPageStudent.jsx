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
} from "@mui/material";
import { DataGrid, GridToolbar } from "@mui/x-data-grid";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { fetchGradesByStudent } from "../features/grades/gradeSlice"; // Adjust the import path as necessary

export default function GradesPageStudent() {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { grades, status } = useSelector(
    (state) => state.grades || { grades: [], status: "idle" }
  );

  const [open, setOpen] = useState(false);
  const [gradeData, setGradeData] = useState({
    course: "",
    grade: "",
  });

  useEffect(() => {
    dispatch(fetchGradesByStudent());
  }, [dispatch]);

  const columns = [
    { field: "courseName", headerName: "Course", width: 200 },
    { field: "grade", headerName: "Grade", width: 200 },
  ];

  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setGradeData({ ...gradeData, [name]: value });
  };

  const handleSubmit = () => {
    console.log("Submitting grade data:", gradeData); // Debugging log

    handleClose();
  };

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
          Grades
        </Typography>
        <Button
          variant="contained"
          color="success"
          onClick={() => navigate("/student/dashboard")}
        >
          Back to Dashboard
        </Button>
      </Box>
      <DataGrid
        rows={grades.map((grade, index) => ({
          id: index,
          courseName: grade.courseName,
          grade: grade.grade,
        }))}
        columns={columns}
        pageSize={5}
        components={{ Toolbar: GridToolbar }}
        disableSelectionOnClick
        loading={status === "loading"}
      />

      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>Add Grade</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            name="course"
            label="Course"
            type="text"
            fullWidth
            variant="standard"
            value={gradeData.course}
            onChange={handleChange}
          />
          <TextField
            margin="dense"
            name="grade"
            label="Grade"
            type="number"
            fullWidth
            variant="standard"
            value={gradeData.grade}
            onChange={handleChange}
            inputProps={{ min: 2, max: 6 }}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleSubmit}>Submit</Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}
