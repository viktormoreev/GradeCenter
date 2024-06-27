import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import Box from "@mui/material/Box";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import { DataGrid } from "@mui/x-data-grid";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogTitle from "@mui/material/DialogTitle";
import TextField from "@mui/material/TextField";
import MenuItem from "@mui/material/MenuItem";
import { useNavigate } from "react-router-dom";
import {
  fetchSchools,
  createSchool,
  deleteSchool,
} from "../features/schools/schoolSlice";
import {
  fetchDirectors,
  assignDirectorToSchool,
  removeDirectorFromSchool,
} from "../features/directors/directorSlice";
import {
  fetchTeachers,
  assignTeacherToSchool,
  removeTeacherFromSchool,
  fetchTeachersBySchoolId,
} from "../features/teachers/teacherSlice";

export default function SchoolsPageAdmin() {
  const [open, setOpen] = useState(false);
  const [directorOpen, setDirectorOpen] = useState(false);
  const [teacherOpen, setTeacherOpen] = useState(false);
  const [removeTeacherOpen, setRemoveTeacherOpen] = useState(false);
  const [schoolData, setSchoolData] = useState({ name: "", address: "" });
  const [selectedSchoolId, setSelectedSchoolId] = useState(null);
  const [selectedDirectorId, setSelectedDirectorId] = useState("");
  const [selectedTeacherId, setSelectedTeacherId] = useState("");
  const [teachersBySchool, setTeachersBySchool] = useState([]);
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const { schools, status, error } = useSelector((state) => state.schools);
  const { directors } = useSelector((state) => state.directors);
  const { teachers } = useSelector((state) => state.teachers);

  useEffect(() => {
    dispatch(fetchSchools());
    dispatch(fetchDirectors());
    dispatch(fetchTeachers());
  }, [dispatch]);

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleDirectorClickOpen = (schoolId) => {
    setSelectedSchoolId(schoolId);
    setDirectorOpen(true);
  };

  const handleTeacherClickOpen = (schoolId) => {
    setSelectedSchoolId(schoolId);
    setTeacherOpen(true);
  };

  const handleRemoveTeacherClickOpen = (schoolId) => {
    setSelectedSchoolId(schoolId);
    dispatch(fetchTeachersBySchoolId(schoolId))
      .unwrap()
      .then((data) => {
        setTeachersBySchool(data);
        setRemoveTeacherOpen(true);
      })
      .catch((error) => {
        console.error("Failed to fetch teachers by school ID", error);
      });
  };

  const handleDirectorClose = () => {
    setDirectorOpen(false);
  };

  const handleTeacherClose = () => {
    setTeacherOpen(false);
  };

  const handleRemoveTeacherClose = () => {
    setRemoveTeacherOpen(false);
  };

  const handleSave = () => {
    dispatch(createSchool(schoolData))
      .unwrap()
      .then(() => {
        dispatch(fetchSchools());
        setOpen(false);
      })
      .catch((error) => {
        console.error("Failed to create school", error);
      });
  };

  const handleAssignDirector = () => {
    dispatch(
      assignDirectorToSchool({
        directorId: selectedDirectorId,
        schoolId: selectedSchoolId,
      })
    )
      .unwrap()
      .then(() => {
        dispatch(fetchSchools());
        setDirectorOpen(false);
      })
      .catch((error) => {
        console.error("Failed to assign director", error);
      });
  };

  const handleAssignTeacher = () => {
    dispatch(
      assignTeacherToSchool({
        teacherId: selectedTeacherId,
        schoolId: selectedSchoolId,
        courseIds: [],
        qualificationsIds: [],
      })
    )
      .unwrap()
      .then(() => {
        dispatch(fetchSchools());
        setTeacherOpen(false);
      })
      .catch((error) => {
        console.error("Failed to assign teacher", error);
      });
  };

  const handleRemoveDirector = (schoolId) => {
    dispatch(removeDirectorFromSchool(schoolId))
      .unwrap()
      .then(() => {
        dispatch(fetchSchools());
      })
      .catch((error) => {
        console.error("Failed to remove director", error);
      });
  };

  const handleRemoveTeacher = () => {
    dispatch(removeTeacherFromSchool({ teacherId: selectedTeacherId }))
      .unwrap()
      .then(() => {
        dispatch(fetchSchools());
        setRemoveTeacherOpen(false);
      })
      .catch((error) => {
        console.error("Failed to remove teacher", error);
      });
  };

  const handleChange = (e) => {
    setSchoolData({ ...schoolData, [e.target.name]: e.target.value });
  };

  const handleDirectorChange = (e) => {
    setSelectedDirectorId(e.target.value);
  };

  const handleTeacherChange = (e) => {
    setSelectedTeacherId(e.target.value);
  };

  const handleDelete = (id) => {
    dispatch(deleteSchool(id))
      .unwrap()
      .then(() => {
        dispatch(fetchSchools());
      })
      .catch((error) => {
        console.error("Failed to delete school", error);
      });
  };

  const columns = [
    { field: "id", headerName: "ID", width: 90 },
    { field: "name", headerName: "Name", width: 200 },
    { field: "address", headerName: "Address", width: 300 },
    { field: "directorName", headerName: "Director", width: 200 },
    {
      field: "teachersNames",
      headerName: "Teachers",
      width: 200,
      renderCell: (params) =>
        params.value && params.value.length > 0 ? params.value.join(", ") : "",
    },
    {
      field: "actions",
      headerName: "Actions",
      width: 900,
      renderCell: (params) => (
        <>
          <Button
            variant="contained"
            color="error"
            onClick={() => handleDelete(params.row.id)}
            style={{ marginRight: 8 }}
          >
            Delete
          </Button>
          <Button
            variant="contained"
            color="primary"
            onClick={() => handleDirectorClickOpen(params.row.id)}
            style={{ marginRight: 8 }}
          >
            Assign Director
          </Button>
          <Button
            variant="contained"
            color="primary"
            onClick={() => handleTeacherClickOpen(params.row.id)}
            style={{ marginRight: 8 }}
          >
            Assign Teacher
          </Button>
          <Button
            variant="contained"
            color="warning"
            onClick={() => handleRemoveDirector(params.row.id)}
            style={{ marginRight: 8 }}
          >
            Remove Director
          </Button>
          <Button
            variant="contained"
            color="warning"
            onClick={() => handleRemoveTeacherClickOpen(params.row.id)} // Open modal to select teacher to remove
          >
            Remove Teacher
          </Button>
        </>
      ),
    },
  ];

  const rows = schools.map((school) => ({
    id: school.id,
    name: school.name,
    address: school.address,
    directorName: school.directorName || "",
    teachersNames: school.teachersNames || [],
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
          Schools
        </Typography>
        <Box>
          <Button variant="contained" color="primary" onClick={handleClickOpen}>
            Add School
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

      {status === "loading" && <p>Loading...</p>}
      {status === "failed" && <p>Error: {error}</p>}
      {status === "succeeded" && (
        <DataGrid rows={rows} columns={columns} pageSize={5} />
      )}

      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>Add New School</DialogTitle>
        <DialogContent>
          <DialogContentText>
            To add a school, please enter the name and address of the school
            here.
          </DialogContentText>
          <TextField
            autoFocus
            margin="dense"
            name="name"
            label="School Name"
            type="text"
            fullWidth
            variant="standard"
            value={schoolData.name}
            onChange={handleChange}
          />
          <TextField
            margin="dense"
            name="address"
            label="School Address"
            type="text"
            fullWidth
            variant="standard"
            value={schoolData.address}
            onChange={handleChange}
          />
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleSave}>Save</Button>
        </DialogActions>
      </Dialog>

      <Dialog open={directorOpen} onClose={handleDirectorClose}>
        <DialogTitle>Assign Director</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Please select a director to assign to this school.
          </DialogContentText>
          <TextField
            select
            label="Director"
            value={selectedDirectorId}
            onChange={handleDirectorChange}
            fullWidth
            variant="standard"
          >
            {directors.map((director) => (
              <MenuItem key={director.id} value={director.id}>
                {director.name}
              </MenuItem>
            ))}
          </TextField>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleDirectorClose}>Cancel</Button>
          <Button onClick={handleAssignDirector}>Assign</Button>
        </DialogActions>
      </Dialog>

      <Dialog open={teacherOpen} onClose={handleTeacherClose}>
        <DialogTitle>Assign Teacher</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Please select a teacher to assign to this school.
          </DialogContentText>
          <TextField
            select
            label="Teacher"
            value={selectedTeacherId}
            onChange={handleTeacherChange}
            fullWidth
            variant="standard"
          >
            {teachers.map((teacher) => (
              <MenuItem key={teacher.id} value={teacher.id}>
                {teacher.name}
              </MenuItem>
            ))}
          </TextField>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleTeacherClose}>Cancel</Button>
          <Button onClick={handleAssignTeacher}>Assign</Button>
        </DialogActions>
      </Dialog>

      <Dialog open={removeTeacherOpen} onClose={handleRemoveTeacherClose}>
        <DialogTitle>Remove Teacher</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Please select a teacher to remove from this school.
          </DialogContentText>
          <TextField
            select
            label="Teacher"
            value={selectedTeacherId}
            onChange={handleTeacherChange}
            fullWidth
            variant="standard"
          >
            {teachersBySchool.map((teacher) => (
              <MenuItem key={teacher.id} value={teacher.id}>
                {teacher.username}
              </MenuItem>
            ))}
          </TextField>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleRemoveTeacherClose}>Cancel</Button>
          <Button onClick={handleRemoveTeacher}>Remove</Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}
