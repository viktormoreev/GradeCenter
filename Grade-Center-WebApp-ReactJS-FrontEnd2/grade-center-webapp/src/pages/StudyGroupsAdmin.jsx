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
  FormControl,
  InputLabel,
  Select,
  MenuItem,
} from "@mui/material";
import { DataGrid, GridToolbar } from "@mui/x-data-grid";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import {
  fetchStudyGroups,
  createStudyGroup,
  deleteStudyGroup,
} from "../features/study groups/groupSlice";
import { fetchSchools } from "../features/schools/schoolSlice";
import {
  fetchStudents,
  addStudentToStudyGroup,
} from "../features/students/studentSlice";

export default function StudyGroupsAdmin() {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { studyGroups, status: studyGroupStatus } = useSelector(
    (state) => state.groups
  );
  const { schools, status: schoolStatus } = useSelector(
    (state) => state.schools
  );
  const { students, status: studentStatus } = useSelector(
    (state) => state.students
  );
  const [open, setOpen] = useState(false);
  const [openAddStudent, setOpenAddStudent] = useState(false);
  const [groupData, setGroupData] = useState({ name: "", schoolId: "" });
  const [selectedGroupId, setSelectedGroupId] = useState(null);
  const [selectedStudentId, setSelectedStudentId] = useState("");

  useEffect(() => {
    dispatch(fetchStudyGroups());
    dispatch(fetchSchools());
    dispatch(fetchStudents());
  }, [dispatch]);

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleChange = (e) => {
    setGroupData({ ...groupData, [e.target.name]: e.target.value });
  };

  const handleSave = () => {
    dispatch(createStudyGroup(groupData))
      .unwrap()
      .then(() => {
        dispatch(fetchStudyGroups());
        setOpen(false);
      })
      .catch((error) => {
        console.error("Failed to create study group", error);
      });
  };

  const handleDelete = (id) => {
    dispatch(deleteStudyGroup(id))
      .unwrap()
      .then(() => {
        dispatch(fetchStudyGroups());
      })
      .catch((error) => {
        console.error("Failed to delete study group", error);
      });
  };

  const handleOpenAddStudent = (groupId) => {
    setSelectedGroupId(groupId);
    setOpenAddStudent(true);
  };

  const handleCloseAddStudent = () => {
    setOpenAddStudent(false);
  };

  const handleAddStudentToGroup = () => {
    dispatch(
      addStudentToStudyGroup({
        studentId: selectedStudentId,
        studyGroupId: selectedGroupId,
      })
    )
      .unwrap()
      .then(() => {
        dispatch(fetchStudyGroups());
        setOpenAddStudent(false);
      })
      .catch((error) => {
        console.error("Failed to add student to study group", error);
      });
  };

  const columns = [
    { field: "id", headerName: "ID", width: 90 },
    { field: "name", headerName: "Name", width: 200 },
    { field: "schoolName", headerName: "School Name", width: 200 },
    {
      field: "students",
      headerName: "Students",
      width: 300,
      renderCell: (params) =>
        params.value && params.value.length > 0
          ? params.value.map((student) => student.username).join(", ")
          : "None",
    },
    {
      field: "actions",
      headerName: "Actions",
      width: 250,
      renderCell: (params) => (
        <Box>
          <Button
            variant="contained"
            color="secondary"
            onClick={() => handleDelete(params.row.id)}
            sx={{ marginRight: 1 }}
          >
            Delete
          </Button>
          <Button
            variant="contained"
            color="primary"
            onClick={() => handleOpenAddStudent(params.row.id)}
          >
            Add Student
          </Button>
        </Box>
      ),
    },
  ];

  const rows = studyGroups.map((group) => ({
    id: group.id,
    name: group.name,
    schoolName: group.schoolName || "",
    students: group.students,
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
          Study Groups
        </Typography>
        <Box>
          <Button variant="contained" color="primary" onClick={handleClickOpen}>
            Add Study Group
          </Button>
          <Button
            variant="contained"
            style={{ marginLeft: 8 }}
            color="success"
            onClick={() => navigate("/admin/dashboard")}
          >
            Back to Dashboard
          </Button>
        </Box>
      </Box>
      <DataGrid
        rows={rows}
        columns={columns}
        pageSize={5}
        components={{ Toolbar: GridToolbar }}
        disableSelectionOnClick
        loading={studyGroupStatus === "loading" || studentStatus === "loading"}
      />
      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>Add New Study Group</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            name="name"
            label="Group Name"
            type="text"
            fullWidth
            variant="standard"
            value={groupData.name}
            onChange={handleChange}
          />
          <FormControl fullWidth margin="dense">
            <InputLabel>School</InputLabel>
            <Select
              name="schoolId"
              value={groupData.schoolId}
              onChange={handleChange}
              label="School"
            >
              {schools.map((school) => (
                <MenuItem key={school.id} value={school.id}>
                  {school.name}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleSave}>Save</Button>
        </DialogActions>
      </Dialog>
      <Dialog open={openAddStudent} onClose={handleCloseAddStudent}>
        <DialogTitle>Add Student to Study Group</DialogTitle>
        <DialogContent>
          <FormControl fullWidth margin="dense">
            <InputLabel>Student</InputLabel>
            <Select
              name="studentId"
              value={selectedStudentId}
              onChange={(e) => setSelectedStudentId(e.target.value)}
              label="Student"
            >
              {students.map((student) => (
                <MenuItem key={student.id} value={student.id}>
                  {student.username}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleCloseAddStudent}>Cancel</Button>
          <Button onClick={handleAddStudentToGroup}>Add</Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}
