import React from "react";
import {
  Box,
  Card,
  CardContent,
  Typography,
  Grid,
  List,
  ListItem,
  ListItemText,
  Button,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import UserInfo from "../components/UserInfo";

export default function ParentDashboard() {
  const navigate = useNavigate();

  const scheduleItems = [
    { time: "Monday 10 AM", detail: "Math Class" },
    { time: "Tuesday 2 PM", detail: "Physics Class" },
    { time: "Wednesday 1 PM", detail: "History Class" },
  ];

  const handleViewFullSchedule = () => {
    navigate("/parent/schedule");
  };

  return (
    <Box padding={3} sx={{ position: "relative", minHeight: "100vh" }}>
      <Typography variant="h4" component="h1" gutterBottom>
        Parent Dashboard
      </Typography>
      <Grid container spacing={2}>
        <Grid item xs={3}>
          <Card>
            <CardContent>
              <Typography variant="h5" component="h2">
                Children's Grades and Schedule
              </Typography>

              <Button
                variant="contained"
                color="primary"
                onClick={handleViewFullSchedule}
                sx={{ mt: 2 }}
              >
                See Full Schedule and Grades
              </Button>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
      <UserInfo />
    </Box>
  );
}
