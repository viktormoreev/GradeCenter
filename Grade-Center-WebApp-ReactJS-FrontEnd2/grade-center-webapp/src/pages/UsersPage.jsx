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
  Select,
  InputLabel,
  FormControl,
} from "@mui/material";
import { DataGrid, GridToolbar } from "@mui/x-data-grid";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { registerUser } from "../features/auth/authSlice";
import {
  fetchUsers,
  changeUserRole,
  deleteUser,
} from "../features/users/userSlice";

export default function UsersPage() {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { users, status } = useSelector((state) => state.user);
  const [open, setOpen] = useState(false);
  const [userData, setUserData] = useState({
    username: "",
    password: "",
    role: "",
  });
  const [changeRoleOpen, setChangeRoleOpen] = useState(false);
  const [selectedUser, setSelectedUser] = useState(null);
  const [newRole, setNewRole] = useState("");

  useEffect(() => {
    dispatch(fetchUsers());
  }, [dispatch]);

  const columns = [
    { field: "username", headerName: "Username", width: 200 },
    { field: "role", headerName: "Role", width: 130 },
    {
      field: "actions",
      headerName: "Actions",
      width: 300,
      renderCell: (params) => (
        <Box>
          <Button
            variant="contained"
            color="secondary"
            onClick={() => handleDelete(params.row.username)}
            sx={{ marginRight: 1 }}
          >
            Delete User
          </Button>
          <Button
            variant="contained"
            color="primary"
            onClick={() => handleOpenChangeRole(params.row)}
          >
            Change Role
          </Button>
        </Box>
      ),
    },
  ];

  const handleChange = (event) => {
    const { name, value } = event.target;
    setUserData({ ...userData, [name]: value });
  };

  const handleOpen = () => setOpen(true);
  const handleClose = () => setOpen(false);

  const handleSubmit = () => {
    console.log("Submitting user data:", userData); // Debugging log
    dispatch(registerUser(userData))
      .unwrap()
      .then((response) => {
        console.log("User registered successfully", response);
        dispatch(fetchUsers()); // Refresh the user list
      })
      .catch((error) => {
        console.error("Failed to register user", error);
      });
    handleClose();
  };

  const handleOpenChangeRole = (user) => {
    setSelectedUser(user);
    setNewRole(user.role);
    setChangeRoleOpen(true);
  };

  const handleChangeRoleClose = () => {
    setChangeRoleOpen(false);
  };

  const handleChangeRoleSubmit = () => {
    if (selectedUser) {
      dispatch(
        changeUserRole({ username: selectedUser.username, role: newRole })
      )
        .unwrap()
        .then(() => {
          console.log("Role changed successfully");
          dispatch(fetchUsers()); // Refresh the user list
        })
        .catch((error) => {
          console.error("Failed to change role", error);
        });
    }
    setChangeRoleOpen(false);
  };

  const handleDelete = (username) => {
    dispatch(deleteUser(username))
      .unwrap()
      .then(() => {
        console.log("User deleted successfully");
        dispatch(fetchUsers()); // Refresh the user list
      })
      .catch((error) => {
        console.error("Failed to delete user", error);
      });
  };

  const handleRoleChange = (event) => {
    setNewRole(event.target.value);
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
          Users
        </Typography>
        <Box>
          <Button
            variant="contained"
            color="success"
            onClick={() => navigate("/admin/dashboard")}
            sx={{ marginRight: 2 }}
          >
            Back to Dashboard
          </Button>
          <Button variant="contained" color="primary" onClick={handleOpen}>
            Register New User
          </Button>
        </Box>
      </Box>
      <DataGrid
        rows={users.map((user, index) => ({
          id: index,
          username: user.username,
          role: user.role,
        }))}
        columns={columns}
        pageSize={5}
        components={{ Toolbar: GridToolbar }}
        disableSelectionOnClick
        loading={status === "loading"}
      />

      <Dialog open={open} onClose={handleClose}>
        <DialogTitle>Register New User</DialogTitle>
        <DialogContent>
          <TextField
            autoFocus
            margin="dense"
            name="username"
            label="Username"
            type="text"
            fullWidth
            variant="standard"
            value={userData.username}
            onChange={handleChange}
          />
          <TextField
            margin="dense"
            name="password"
            label="Password"
            type="password"
            fullWidth
            variant="standard"
            value={userData.password}
            onChange={handleChange}
          />
          <FormControl fullWidth margin="dense">
            <InputLabel>Role</InputLabel>
            <Select
              name="role"
              value={userData.role}
              label="Role"
              onChange={handleChange}
            >
              <MenuItem value="admin">admin</MenuItem>
              <MenuItem value="director">director</MenuItem>
              <MenuItem value="teacher">teacher</MenuItem>
              <MenuItem value="parent">parent</MenuItem>
              <MenuItem value="student">student</MenuItem>
            </Select>
          </FormControl>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleClose}>Cancel</Button>
          <Button onClick={handleSubmit}>Submit</Button>
        </DialogActions>
      </Dialog>

      <Dialog open={changeRoleOpen} onClose={handleChangeRoleClose}>
        <DialogTitle>Change Role</DialogTitle>
        <DialogContent>
          <FormControl fullWidth margin="dense">
            <InputLabel>Role</InputLabel>
            <Select value={newRole} label="Role" onChange={handleRoleChange}>
              <MenuItem value="admin">admin</MenuItem>
              <MenuItem value="director">director</MenuItem>
              <MenuItem value="teacher">teacher</MenuItem>
              <MenuItem value="parent">parent</MenuItem>
              <MenuItem value="student">student</MenuItem>
            </Select>
          </FormControl>
        </DialogContent>
        <DialogActions>
          <Button onClick={handleChangeRoleClose}>Cancel</Button>
          <Button onClick={handleChangeRoleSubmit}>Submit</Button>
        </DialogActions>
      </Dialog>
    </Box>
  );
}
