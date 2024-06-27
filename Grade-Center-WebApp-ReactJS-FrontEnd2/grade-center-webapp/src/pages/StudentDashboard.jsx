import React, { useEffect, useState } from "react";
import {
  Box,
  Card,
  CardContent,
  Typography,
  Grid,
  Button,
} from "@mui/material";
import UserInfo from "../components/UserInfo";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { fetchAbsencesByStudent } from "../features/absences/absenceSlice";

export default function StudentDashboard() {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { absences, status } = useSelector(
    (state) => state.absences || { absences: [], status: "idle" }
  );
  const [studentInfo, setStudentInfo] = useState({
    grades: {
      recent: "A",
    },
    nextClass: {
      name: "",
      time: "",
    },
    absences: {
      total: 0,
    },
  });

  useEffect(() => {
    dispatch(fetchAbsencesByStudent())
      .unwrap()
      .then((fetchedAbsences) => {
        setStudentInfo((prevInfo) => ({
          ...prevInfo,
          absences: { total: fetchedAbsences.length },
        }));
      })
      .catch((error) => {
        console.error("Failed to fetch absences", error);
      });
  }, [dispatch]);

  return (
    <Box padding={3} sx={{ position: "relative", minHeight: "100vh" }}>
      <Typography variant="h4" component="h1" gutterBottom>
        Student Dashboard
      </Typography>
      <Grid container spacing={2}>
        <Grid item xs={12} sm={4}>
          <Card>
            <CardContent>
              <Typography variant="h5" component="h2">
                Grades
              </Typography>
              <Typography variant="h6"></Typography>
              <Button
                variant="contained"
                color="primary"
                sx={{ mt: 2 }}
                onClick={() => navigate("/student/grades")}
              >
                View All Grades
              </Button>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={4}>
          <Card>
            <CardContent>
              <Typography variant="h5" component="h2">
                Classes
              </Typography>
              <Typography variant="h6">{studentInfo.nextClass.name}</Typography>
              <Typography color="text.secondary">
                {studentInfo.nextClass.time}
              </Typography>
              <Button
                variant="contained"
                color="secondary"
                sx={{ mt: 2 }}
                onClick={() => navigate("/student/schedule")}
              >
                View Full Schedule
              </Button>
            </CardContent>
          </Card>
        </Grid>
        <Grid item xs={12} sm={4}>
          <Card>
            <CardContent>
              <Typography variant="h5" component="h2">
                Absences
              </Typography>
              <Typography variant="h6">{studentInfo.absences.total}</Typography>
              <Button
                variant="contained"
                color="primary"
                onClick={() => navigate("/student/absences")}
                sx={{ mt: 2 }}
              >
                View All Absences
              </Button>
            </CardContent>
          </Card>
        </Grid>
      </Grid>
      <Box marginTop={2} display="flex" justifyContent="space-between">
        <UserInfo />
      </Box>
    </Box>
  );
}
