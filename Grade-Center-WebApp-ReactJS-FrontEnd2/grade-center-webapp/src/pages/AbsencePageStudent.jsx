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
import axios from "axios";

const domain = "http://localhost:8082";

export default function AbsencePageStudent() {
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);
  const [absenceData, setAbsenceData] = useState({
    date: "",
    course: "",
  });
  const [absences, setAbsences] = useState([]);

  // Fetch absences from the API
  useEffect(() => {
    const fetchAbsences = async () => {
      try {
        const token = localStorage.getItem("accessToken");
        const response = await axios.get(`${domain}/absences/me`, {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (response.status === 204) {
          setAbsences([]);
        } else {
          setAbsences(response.data);
        }
      } catch (error) {
        console.error("Failed to fetch absences", error);
        setAbsences([]); // Set empty array on error
      }
    };

    fetchAbsences();
  }, []);

  const columns = [
    { field: "date", headerName: "Date", width: 200 },
    { field: "courseName", headerName: "Course", width: 300 },
  ];

  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const handleChange = (event) => {
    const { name, value } = event.target;
    setAbsenceData({ ...absenceData, [name]: value });
  };

  const handleSubmit = () => {
    console.log("Submitting absence data:", absenceData); // Debugging log

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
          Absences
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
        rows={
          absences.length
            ? absences.map((absence, index) => ({
                id: index,
                date: absence.date,
                courseName: absence.courseName,
              }))
            : []
        }
        columns={columns}
        pageSize={5}
        components={{ Toolbar: GridToolbar }}
        disableSelectionOnClick
      />

      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>Add Absence</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            name="date"
            label="Date"
            type="date"
            fullWidth
            variant="standard"
            value={absenceData.date}
            onChange={handleChange}
            InputLabelProps={{
              shrink: true,
            }}
          />
          <TextField
            margin="dense"
            name="course"
            label="Course"
            type="text"
            fullWidth
            variant="standard"
            value={absenceData.course}
            onChange={handleChange}
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
