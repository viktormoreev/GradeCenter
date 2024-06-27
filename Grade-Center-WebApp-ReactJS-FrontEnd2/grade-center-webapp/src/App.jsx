import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import UsersPage from "./pages/UsersPage";
import SchoolsPageAdmin from "./pages/SchoolsPageAdmin.jsx";
import StudentDashboard from "./pages/StudentDashboard";
import SchedulePageStudent from "./pages/SchedulePageStudent.jsx";
import StudentsPageDirector from "./pages/StudentsPageDirector.jsx";
import ParentDashboard from "./pages/ParentDashboard";
import TeacherDashboard from "./pages/TeacherDashboard";
import ProtectedRoute from "./components/ProtectedRoute";
import AdminDashboard from "./pages/AdminDashboard";
import DirectorDashboard from "./pages/DirectorDashboard.jsx";
import { useDispatch } from "react-redux";
import { loadUser } from "./features/auth/authSlice";
import { useEffect } from "react";
import SchedulePageParent from "./pages/SchedulePageParent.jsx";
import TeachersPage from "./pages/TeachersPageDirector.jsx";
import AbsencePageStudent from "./pages/AbsencePageStudent.jsx";
import StudentsPageTeacher from "./pages/StudentsPageTeacher.jsx";
import GradesPageStudent from "./pages/GradesPageStudent.jsx";
import AbsencePageParent from "./pages/AbsencePageParent.jsx";
import GradesPageParent from "./pages/GradesPageParent.jsx";
import WeeklyScheduleParent from "./pages/WeeklyScheduleParent.jsx";
import StudentsPageAdmin from "./pages/StudentsPageAdmin.jsx";
import StudyGroupsAdmin from "./pages/StudyGroupsAdmin.jsx";

function App() {
  const dispatch = useDispatch();

  useEffect(() => {
    if (localStorage.getItem("accessToken")) {
      dispatch(loadUser());
    }
  }, [dispatch]);

  return (
    <Router>
      <Routes>
        <Route path="/" element={<HomePage />} />
        <Route path="/login" element={<LoginPage />} />

        {/* Protected routes */}
        <Route element={<ProtectedRoute allowedRoles={["admin"]} />}>
          <Route path="/admin/users" element={<UsersPage />} />
          <Route path="/admin/dashboard" element={<AdminDashboard />} />
          <Route path="/admin/schools" element={<SchoolsPageAdmin />} />
          <Route path="/admin/students" element={<StudentsPageAdmin />} />
          <Route path="/admin/studygroups" element={<StudyGroupsAdmin />} />
        </Route>
        <Route element={<ProtectedRoute allowedRoles={["director"]} />}>
          <Route path="/director/dashboard" element={<DirectorDashboard />} />
          <Route path="/director/teachers" element={<TeachersPage />} />
          <Route path="/director/students" element={<StudentsPageDirector />} />
        </Route>
        <Route element={<ProtectedRoute allowedRoles={["teacher"]} />}>
          <Route path="/teacher/dashboard" element={<TeacherDashboard />} />
          <Route path="/teacher/students" element={<StudentsPageTeacher />} />
        </Route>
        <Route element={<ProtectedRoute allowedRoles={["parent"]} />}>
          <Route path="/parent/dashboard" element={<ParentDashboard />} />
          <Route path="/parent/schedule" element={<SchedulePageParent />} />
          <Route
            path="/parent/:studentId/absences"
            element={<AbsencePageParent />}
          />
          <Route
            path="/parent/:studentId/grades"
            element={<GradesPageParent />}
          />
          <Route
            path="/parent/:studentId/schedule"
            element={<WeeklyScheduleParent />}
          />
        </Route>
        <Route element={<ProtectedRoute allowedRoles={["student"]} />}>
          <Route path="/student/dashboard" element={<StudentDashboard />} />
          <Route path="/student/schedule" element={<SchedulePageStudent />} />
          <Route path="/student/absences" element={<AbsencePageStudent />} />
          <Route path="/student/grades" element={<GradesPageStudent />} />
        </Route>
      </Routes>
    </Router>
  );
}

export default App;
