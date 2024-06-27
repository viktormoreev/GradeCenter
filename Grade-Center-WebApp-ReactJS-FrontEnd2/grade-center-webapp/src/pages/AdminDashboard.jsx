import React, { useEffect } from "react";
import {
  Box,
  Card,
  CardContent,
  Typography,
  Grid,
  Button,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { fetchUsers } from "../features/users/userSlice";
import { fetchStudents } from "../features/students/studentSlice";
import UserInfo from "../components/UserInfo";

export default function AdminDashboard() {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { users, status: userStatus } = useSelector((state) => state.user);
  const { students, status: studentStatus } = useSelector(
    (state) => state.students
  ); // Get students from state

  useEffect(() => {
    dispatch(fetchUsers());
    dispatch(fetchStudents()); // Fetch students
  }, [dispatch]);

  return (
    <Box padding={3} sx={{ position: "relative", minHeight: "100vh" }}>
      <Typography variant="h4" component="h1" gutterBottom>
        Admin Dashboard
      </Typography>
      <Grid container spacing={2}>
        <Grid item xs={12} sm={4}>
          <Card>
            <CardContent>
              <Typography variant="h5" component="h2">
                Total Users
              </Typography>
              {userStatus === "loading" ? (
                <Typography variant="h6">Loading...</Typography>
              ) : (
                <Typography variant="h6">
                  {Object.keys(users).length}
                </Typography>
              )}
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={4}>
          <Card>
            <CardContent>
              <Typography variant="h5" component="h2">
                Users
              </Typography>
              <Button
                variant="contained"
                color="primary"
                sx={{ mt: 2 }}
                onClick={() => navigate("/admin/users")}
              >
                Manage Users
              </Button>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={4}>
          <Card>
            <CardContent>
              <Typography variant="h5" component="h2">
                Schools
              </Typography>
              <Button
                variant="contained"
                color="primary"
                sx={{ mt: 2 }}
                onClick={() => navigate("/admin/schools")}
              >
                Manage Schools
              </Button>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={4}>
          {" "}
          <Card>
            <CardContent>
              <Typography variant="h5" component="h2">
                Total Students
              </Typography>
              {studentStatus === "loading" ? (
                <Typography variant="h6">Loading...</Typography>
              ) : (
                <Typography variant="h6">{students.length}</Typography>
              )}
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={4}>
          <Card>
            <CardContent>
              <Typography variant="h5" component="h2">
                Students
              </Typography>
              <Button
                variant="contained"
                color="primary"
                sx={{ mt: 2 }}
                onClick={() => navigate("/admin/students")}
              >
                Manage Students
              </Button>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={4}>
          {" "}
          <Card>
            <CardContent>
              <Typography variant="h5" component="h2">
                Study Groups
              </Typography>
              <Button
                variant="contained"
                color="primary"
                sx={{ mt: 2 }}
                onClick={() => navigate("/admin/studygroups")}
              >
                Manage Study Groups
              </Button>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
      <UserInfo />
    </Box>
  );
}
