import React, { useEffect } from "react";
import { Typography, Paper, Box, Button } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { useDispatch, useSelector } from "react-redux";
import { fetchParentData } from "../features/parents/parentSlice";

export default function SchedulePageParent() {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const { parentData, status } = useSelector((state) => state.parent);

  useEffect(() => {
    dispatch(fetchParentData());
  }, [dispatch]);

  // Функция за премахване на дублиращите се ученици
  const uniqueStudents = (students) => {
    const seen = new Set();
    return students.filter((student) => {
      const duplicate = seen.has(student.studentId);
      seen.add(student.studentId);
      return !duplicate;
    });
  };

  return (
    <Box sx={{ width: "100%", overflow: "hidden", position: "relative" }}>
      <Typography variant="h4" sx={{ m: 2 }}>
        Your Children's Weekly Schedules
      </Typography>
      <Button
        variant="contained"
        color="success"
        sx={{ position: "absolute", top: 16, right: 16 }}
        onClick={() => navigate("/parent/dashboard")}
      >
        Back to Dashboard
      </Button>
      {status === "loading" && (
        <Typography variant="h6" sx={{ m: 2 }}>
          Loading...
        </Typography>
      )}
      {status === "succeeded" &&
        parentData &&
        uniqueStudents(parentData.students).map((child) => (
          <Paper
            key={child.studentId}
            elevation={3}
            sx={{ margin: 2, padding: 2 }}
          >
            <Typography variant="h5">Name: {child.name}</Typography>
            <Typography variant="h6">
              Study Group: {child.studyGroupName}
            </Typography>
            <Box
              sx={{ display: "flex", justifyContent: "start", gap: 2, mt: 2 }}
            >
              <Button
                variant="contained"
                onClick={() => navigate(`/parent/${child.studentId}/grades`)}
              >
                See Grades
              </Button>
              <Button
                variant="contained"
                onClick={() => navigate(`/parent/${child.studentId}/schedule`)}
              >
                See Full Schedule
              </Button>
              <Button
                variant="contained"
                onClick={() => navigate(`/parent/${child.studentId}/absences`)}
              >
                See Absences
              </Button>
            </Box>
          </Paper>
        ))}
      {status === "failed" && (
        <Typography variant="h6" sx={{ m: 2 }}>
          Failed to load parent data
        </Typography>
      )}
    </Box>
  );
}
