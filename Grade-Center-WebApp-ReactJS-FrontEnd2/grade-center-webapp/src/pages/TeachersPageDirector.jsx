import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { Box, Button, Typography } from "@mui/material";
import { DataGrid, GridToolbar } from "@mui/x-data-grid";
import { useNavigate } from "react-router-dom";
import { fetchTeachers } from "../features/teachers/teacherSlice";

export default function TeachersPage() {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { teachers, status, error } = useSelector((state) => state.teachers);

  useEffect(() => {
    dispatch(fetchTeachers());
  }, [dispatch]);

  const columns = [
    { field: "id", headerName: "Teacher ID", width: 150 },
    { field: "name", headerName: "Name", width: 200 },
    { field: "schoolName", headerName: "School Name", width: 200 },
  ];

  const rows = teachers.map((teacher) => ({
    id: teacher.id,
    name: teacher.name,
    schoolName: teacher.schoolName,
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
          Teachers
        </Typography>
        <Button
          variant="contained"
          color="success"
          onClick={() => navigate("/director/dashboard")}
        >
          Back to Dashboard
        </Button>
      </Box>
      {status === "loading" && <p>Loading...</p>}
      {status === "failed" && <p>Error: {error}</p>}
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
