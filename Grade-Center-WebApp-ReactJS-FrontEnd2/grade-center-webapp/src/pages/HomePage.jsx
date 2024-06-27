import React, { useEffect } from "react";
import Navbar from "../components/Navbar.jsx";
import gradeImage from "../assets/images/gradeImage.jpg";

function HomePage() {
  useEffect(() => {
    // Set the body style on component mount
    document.body.style.margin = "0";
    document.body.style.display = "flex";
    document.body.style.placeItems = "center";
    document.body.style.minWidth = "320px";
    document.body.style.minHeight = "100vh";

    // Reset the body style on component unmount
    return () => {
      document.body.style.margin = "";
      document.body.style.display = "";
      document.body.style.placeItems = "";
      document.body.style.minWidth = "";
      document.body.style.minHeight = "";
    };
  }, []);

  return (
    <div
      style={{
        width: "100vw",
        height: "100vh",
        overflow: "hidden",
        position: "relative",
      }}
    >
      <img
        src={gradeImage}
        alt="Grade Center WebApp React"
        style={{
          position: "absolute",
          width: "100%",
          height: "100vh",
          objectFit: "cover",
          top: 0,
          left: 0,
        }}
      />

      <div
        style={{
          position: "absolute",
          top: "4%",
          left: "50%",
          transform: "translate(-50%, -50%)",
          color: "white",
          textAlign: "center",
          width: "100%",
          fontSize: "2rem",
          fontWeight: "bold",
          textShadow: "2px 2px 4px rgba(0,0,0,0.6)",
        }}
      >
        Grade Center WebApp React
      </div>

      <Navbar
        style={{
          width: "100%",
          position: "fixed",
          top: 0,
          left: 0,
        }}
      />
    </div>
  );
}

export default HomePage;
