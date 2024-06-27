import React from "react";
import { useDispatch } from "react-redux";
import { logoutUser } from "../features/auth/authSlice"; // Adjust the path as necessary
import Button from "@mui/material/Button";
import { useNavigate } from "react-router-dom";

export default function LogoutBtn() {
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleLogout = () => {
    dispatch(logoutUser());
    navigate("/login"); // Redirect to login page after logout
  };

  return (
    <Button variant="contained" color="error" onClick={handleLogout}>
      Logout
    </Button>
  );
}
