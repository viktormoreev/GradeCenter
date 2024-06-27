import React from "react";
import {
  Box,
  Card,
  CardContent,
  Typography,
  Grid,
  Button,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import UserInfo from "../components/UserInfo";

export default function TeacherDashboard() {
  const navigate = useNavigate();

  // Mock data for demonstration
  const todayClasses = [
    { time: "09:00 AM", subject: "Mathematics", room: "101" },
    { time: "11:00 AM", subject: "Physics", room: "102" },
  ];

  const handleManageGrades = () => {
    navigate("/teacher/students"); // Navigation to the students management page
  };

  const handleViewSchedule = () => {
    console.log("View schedule clicked");
  };

  return (
    <Box padding={3} sx={{ position: "relative", minHeight: "100vh" }}>
      <Typography variant="h4" component="h1" gutterBottom>
        Teacher Dashboard
      </Typography>
      <Grid container spacing={2}>
        <Grid item xs={12} sm={6}>
          <Card>
            <CardContent>
              <Typography variant="h5" component="h2">
                Today's Classes
              </Typography>
              {todayClasses.map((cls, index) => (
                <Typography key={index}>
                  {cls.time} - {cls.subject} (Room {cls.room})
                </Typography>
              ))}
              <Button
                variant="contained"
                color="primary"
                onClick={handleViewSchedule}
                sx={{ mt: 2 }}
              >
                View Full Schedule
              </Button>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={6}>
          <Card>
            <CardContent>
              <Typography variant="h5" component="h2">
                Manage Students
              </Typography>
              <Typography>
                Quickly access tools to manage student grades and attendance.
              </Typography>
              <Button
                variant="contained"
                color="secondary"
                onClick={handleManageGrades}
                sx={{ mt: 2 }}
              >
                Manage Students
              </Button>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
      <UserInfo />
    </Box>
  );
}
