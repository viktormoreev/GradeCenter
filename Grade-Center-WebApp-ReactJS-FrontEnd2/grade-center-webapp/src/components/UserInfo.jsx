import React, { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { loadUser } from "../features/auth/authSlice";
import {
  Box,
  Card,
  CardContent,
  Typography,
  CircularProgress,
  Button,
} from "@mui/material";
import { useNavigate } from "react-router-dom";
import { logoutUser } from "../features/auth/authSlice";

export default function UserInfo() {
  const dispatch = useDispatch();
  const { user, status } = useSelector((state) => state.auth);
  const navigate = useNavigate();

  useEffect(() => {
    dispatch(loadUser());
  }, [dispatch]);

  const handleLogout = () => {
    dispatch(logoutUser());
    navigate("/login"); // Redirect to login page after logout
  };

  if (status === "loading") {
    return <CircularProgress />;
  }

  if (!user) {
    return <Typography>No user data available</Typography>;
  }

  return (
    <Box
      sx={{
        position: "fixed",
        bottom: 16,
        right: 16,
        width: 300,
      }}
    >
      <Card>
        <CardContent>
          <Typography variant="h5" component="div">
            User Information
          </Typography>
          <Typography sx={{ mb: 0 }} color="text.secondary">
            Username: {user.username}
          </Typography>
          <Typography sx={{ mb: 0 }} color="text.secondary">
            Role: {user.role}
          </Typography>
          <Button
            variant="contained"
            color="error"
            onClick={handleLogout}
            sx={{ marginTop: 2 }}
          >
            Logout
          </Button>
        </CardContent>
      </Card>
    </Box>
  );
}
