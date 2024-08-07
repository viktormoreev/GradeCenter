package com.GradeCenter.mapper;

import com.GradeCenter.dtos.*;
import com.GradeCenter.entity.*;
import com.GradeCenter.service.implementation.KeycloakAdminClientService;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EntityMapper {
    private final ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private KeycloakAdminClientService keycloakAdminClientService;
    public CourseDto mapToCourseDto(Course course){
        CourseDto courseDto = new CourseDto();
        modelMapper.map(course, courseDto);

        return courseDto;
    }

    public StudentDto mapToStudentDto(Student student){
        StudentDto studentDto = new StudentDto();
//        studentDto.setId(student.getId());
        studentDto.setUserID(student.getUserID());
        if (student.getParents() != null){
            studentDto.setParentsID(student.getParents().stream().map(parent -> parent.getId()).collect(Collectors.toList()));
        }
        if (student.getClasses() != null){
            studentDto.setClassesID(student.getClasses().getId());
        }


        return studentDto;
    }

    public DirectorDto mapToDirectorDto(Director director){
        DirectorDto directorDto = new DirectorDto();
        directorDto.setId(director.getId());
        directorDto.setName(keycloakAdminClientService.getUserFromUserID(director.getUserID()).getUsername());
        if (director.getSchool() != null){
            directorDto.setSchoolName(director.getSchool().getName());
            directorDto.setSchoolId(director.getSchool().getId());
        }
        return directorDto;
    }

    public TeacherDto mapToTeacherDto(Teacher teacher){
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(teacher.getId());
        teacherDto.setUsername(keycloakAdminClientService.getUserFromUserID(teacher.getUserID()).getUsername());
        teacherDto.setUserID(teacher.getUserID());
        if (teacher.getCourses() != null){
            teacherDto.setCourseIds(teacher.getCourses().stream().map(course -> course.getId()).collect(Collectors.toList()));
        }

        if (teacher.getSchool() != null){
            teacherDto.setSchoolId(teacher.getSchool().getId());
        }

        if (teacher.getQualifications() != null){
            teacherDto.setQualificationsIds(teacher.getQualifications().stream().map(qualification -> qualification.getId()).collect(Collectors.toList()));
        }
        return teacherDto;
    }



    public ParentDto mapToParentDto(Parent parent){
        ParentDto parentDto = new ParentDto();
        parentDto.setParentId(parent.getId());
        parentDto.setName(keycloakAdminClientService.getUserFromUserID(parent.getUserID()).getUsername());
        if (parent.getStudents() != null){
            parentDto.setStudents(mapToFetchStudentListDto(parent.getStudents()));
        }

        return parentDto;
    }

    private List<FetchStudentDto> mapToFetchStudentListDto(List<Student> students) {
        return students.stream().map(this::mapToFetchstudentDto).collect(Collectors.toList());
    }

    public StudentFullReturnDto mapToStudentFullReturnDto(Student student, Map<String, UserRepresentation> keycloakUserMap, List<StudentCourseDto> courses) {
        StudentFullReturnDto studentFullDto = new StudentFullReturnDto();
        studentFullDto.setId(student.getId());

        UserRepresentation keycloakUser = keycloakUserMap.get(student.getUserID());
        if (keycloakUser != null) {
            studentFullDto.setUsername(keycloakUser.getUsername());
        }

        studentFullDto.setCourses(courses);
        if (student.getClasses() != null){
            studentFullDto.setGrade(student.getClasses().getName());
            studentFullDto.setSchool(student.getClasses() != null ? student.getClasses().getName() : null);

        }

        studentFullDto.setAbsences(courses.stream().mapToInt(course -> course.getAbsences().size()).sum());

        List<String> parentIDs = student.getParents().stream()
                .map(Parent::getUserID)
                .collect(Collectors.toList());

        List<UserRepresentation> parentUsers = keycloakAdminClientService.getUsersFromIDs(parentIDs);
        List<String> parentNames = parentUsers.stream()
                .map(UserRepresentation::getUsername)
                .collect(Collectors.toList());

        studentFullDto.setParent(parentNames);

        return studentFullDto;
    }

    public List<StudentFullReturnDto> mapToStudentFullReturnDtoList(List<Student> students, Map<String, UserRepresentation> keycloakUserMap, Map<Long, List<StudentCourseDto>> studentCoursesMap) {
        return students.stream()
                .map(student -> mapToStudentFullReturnDto(student, keycloakUserMap, studentCoursesMap.get(student.getId())))
                .collect(Collectors.toList());
    }

    private FetchStudentDto mapToFetchstudentDto(Student student) {
        FetchStudentDto fetchStudentDto = new FetchStudentDto();
        if (student.getClasses() != null){
            fetchStudentDto.setStudyGroupName(student.getClasses().getName());
        }
        fetchStudentDto.setStudentId(student.getId());
        fetchStudentDto.setName(keycloakAdminClientService.getUserFromUserID(student.getUserID()).getUsername());
        return fetchStudentDto;
    }

    public SchoolDto mapToSchoolDto(School school){
        SchoolDto schoolDto = new SchoolDto();
        schoolDto.setId(school.getId());
        schoolDto.setName(school.getName());
        schoolDto.setAddress(school.getAddress());
        if (school.getDirector() != null){
            schoolDto.setDirectorId(school.getDirector().getId());
        }
        if (school.getTeachers() != null){
            schoolDto.setTeachersId(school.getTeachers().stream().map(teacher -> teacher.getId()).collect(Collectors.toList()));
        }

        return schoolDto;
    }

    public SchoolNamesDto mapToSchoolNamesDto(School school){
        SchoolNamesDto schoolDto = new SchoolNamesDto();
        schoolDto.setId(school.getId());
        schoolDto.setName(school.getName());
        schoolDto.setAddress(school.getAddress());

        if (school.getDirector() != null){
            UserRepresentation director = keycloakAdminClientService.getUserFromUserID(school.getDirector().getUserID());
            if (director != null) {
                schoolDto.setDirectorName(director.getUsername());
            } else {
                schoolDto.setDirectorName("Director not found");
            }
        }

        if (school.getTeachers() != null){
            List<String> teacherNames = school.getTeachers().stream()
                    .map(teacher -> {
                        UserRepresentation user = keycloakAdminClientService.getUserFromUserID(teacher.getUserID());
                        return user != null ? user.getUsername() : "Teacher not found";
                    })
                    .collect(Collectors.toList());
            schoolDto.setTeachersNames(teacherNames);
        }

        return schoolDto;
    }

    public AbsenceDto mapToAbsenceDto(Absence absence){
        AbsenceDto absenceDto = new AbsenceDto();
        absenceDto.setAbsenceId(absence.getId());
        if (absence.getCourse() != null){
            absenceDto.setCourseId(absence.getCourse().getId());
        }
        if (absence.getStudent() != null) {
            absenceDto.setStudentId(absence.getStudent().getId());
        }
        absenceDto.setDate(absence.getDate());
        return absenceDto;
    }

    public AbsenceStudentViewDto mapToAbsenceStudentViewDto(Absence absence) {
        return new AbsenceStudentViewDto(
                absence.getCourse().getName(),
                absence.getDate()
        );
    }

    public List<CourseDto> mapToCourseListDto(List<Course> courseList){
        return courseList.stream().map(this::mapToCourseDto).collect(Collectors.toList());
    }

    public List<StudentDto> mapToStudentListDto(List<Student> studentList){
        return studentList.stream().map(this::mapToStudentDto).collect(Collectors.toList());
    }

    public List<DirectorDto> mapToDirectorListDto(List<Director> directorList){
        return directorList.stream().map(this::mapToDirectorDto).collect(Collectors.toList());
    }

    public List<TeacherDto> mapToTeacherListDto(List<Teacher> teacherList){
        return teacherList.stream().map(this::mapToTeacherDto).collect(Collectors.toList());
    }

    public List<ParentDto> mapToParentListDto(List<Parent> parentList){
        return parentList.stream().map(this::mapToParentDto).collect(Collectors.toList());
    }

    public List<SchoolDto> mapToSchoolListDto(List<School> schoolList){
        return schoolList.stream().map(this::mapToSchoolDto).collect(Collectors.toList());
    }

    public StudyGroupDto mapToStudyGroupDto(StudyGroup studyGroup, List<StudentFullReturnDto> students){

        StudyGroupDto studyGroupDto = new StudyGroupDto();
        studyGroupDto.setId(studyGroup.getId());
        studyGroupDto.setName(studyGroup.getName());
        if (studyGroup.getSchool() != null){
            studyGroupDto.setSchoolId(studyGroup.getSchool().getId());
            studyGroupDto.setSchoolName(studyGroup.getSchool().getName());
        }
        studyGroupDto.setStudents(students);
        if (studyGroup.getCourses() != null){
            studyGroupDto.setCourses(mapToCourseListDto(studyGroup.getCourses()));
        }


        return studyGroupDto;
    }


    public List<AbsenceDto> mapToAbsenceListDto(List<Absence> absenceList){
        return absenceList.stream().map(this::mapToAbsenceDto).collect(Collectors.toList());
    }

    public List<AbsenceDto> mapToAbsenceDtoList(List<Absence> absences){
        return absences.stream().map(this::mapToAbsenceDto).collect(Collectors.toList());
    }


    public Absence mapToAbsenceEntity(AbsenceDto absenceDto, Student student, Course course) {
        return Absence.builder()
                .student(student)
                .course(course)
                .date(absenceDto.getDate())
                .build();
    }

    public GradeDto mapToGradeDto(Grade grade) {
        return new GradeDto(
                grade.getId(),
                grade.getGrade(),
                grade.getStudent().getId(),
                grade.getCourse().getId()
        );
    }

    public Grade mapToGradeEntity(GradeDto gradeDto, Student student, Course course) {
        return Grade.builder()
                .student(student)
                .course(course)
                .grade(gradeDto.getGrade())
                .build();
    }

    public List<SmallGradeDto> mapToSmallGradeDtoList(List<Grade> grades){
        return grades.stream().map(this::mapToSmallGradeDto).collect(Collectors.toList());
    }


    public SmallGradeDto mapToSmallGradeDto(Grade grade){
        SmallGradeDto gradeDto = new SmallGradeDto();
        modelMapper.map(grade, gradeDto);

        return gradeDto;
    }

    public List<StudentCourseDto> mapToStudentCourseListDto(List<Course> courses, Long studentId) {
        return courses.stream().map(course -> mapToStudentCourseDto(course,studentId)).collect(Collectors.toList());
    }

    public StudentCourseDto mapToStudentCourseDto(Course course, Long studentId) {
        StudentCourseDto studentCourseDto = new StudentCourseDto();
        studentCourseDto.setCourseId(course.getId());
        studentCourseDto.setAbsences(mapToAbsenceDtoList(course.getAbsences().stream().filter(absence -> absence.getStudent().getId().equals(studentId)).collect(Collectors.toList())));
        studentCourseDto.setGrades(mapToSmallGradeDtoList(course.getGrades().stream().filter(grade -> grade.getStudent().getId().equals(studentId)).collect(Collectors.toList())));
        studentCourseDto.setName(course.getName());
        return studentCourseDto;
    }

    public List<WeeklyScheduleDto> mapToWeeklyScheduleListDto(List<WeeklySchedule> weeklyScheduleDtos) {
        return weeklyScheduleDtos.stream().map(weeklyScheduleDto -> mapToWeeklyScheduleDto(weeklyScheduleDto)).collect(Collectors.toList());
    }

    public List<CourseTypeDto> mapToCourseTypeListDto(List<CourseType> courseTypes) {
        return courseTypes.stream().map(this::mapToCourseTypeDto).collect(Collectors.toList());
    }

    public CourseTypeDto mapToCourseTypeDto(CourseType courseType) {
        CourseTypeDto courseTypeDto = new CourseTypeDto();
        modelMapper.map(courseType, courseTypeDto);
        return courseTypeDto;

    }

    public WeeklyScheduleDto mapToWeeklyScheduleDto(WeeklySchedule weeklySchedule) {
        WeeklyScheduleDto weeklyScheduleDto = new WeeklyScheduleDto();
        modelMapper.map(weeklySchedule, weeklyScheduleDto);
        weeklyScheduleDto.setStudyGroupName(weeklySchedule.getSchoolClass().getName());
        weeklyScheduleDto.setWeekday(weeklySchedule.getDay());
        return weeklyScheduleDto;
    }

    public List<FetchTeacherDto> mapToFetchTeacherListDto(List<Teacher> teachers) {
        return teachers.stream().map(this::mapToFetchTeacherDto).collect(Collectors.toList());
    }

    public FetchTeacherDto mapToFetchTeacherDto(Teacher teacher) {
        FetchTeacherDto fetchTeacherDto = new FetchTeacherDto();
        if (teacher.getCourses() != null){
            fetchTeacherDto.setCourses(mapToCourseListDto(teacher.getCourses()));
        }
        if (teacher.getQualifications() != null){
            fetchTeacherDto.setQualifications(mapToQualificationListDto(teacher.getQualifications()));
        }
        fetchTeacherDto.setId(teacher.getId());
        UserRepresentation user = keycloakAdminClientService.getUserFromUserID(teacher.getUserID());
        if (user != null) {
            fetchTeacherDto.setName(user.getUsername());
        }
        if (teacher.getSchool() != null){
            fetchTeacherDto.setSchoolName(teacher.getSchool().getName());
            fetchTeacherDto.setSchoolId(teacher.getSchool().getId());
        }

        return fetchTeacherDto;
    }

    public List<QualificationDto> mapToQualificationListDto(List<Qualification> qualifications) {
        return qualifications.stream().map(this::mapToQualificationDto).collect(Collectors.toList());
    }

    public QualificationDto mapToQualificationDto(Qualification qualification) {
        QualificationDto qualificationDto = new QualificationDto();
        modelMapper.map(qualification, qualificationDto);
        return qualificationDto;
    }

    public Qualification mapToQualificationEntity(QualificationDto qualificationDto) {
        return Qualification.builder()
                .area(qualificationDto.getArea())
                .build();
    }

    public List<SchoolNamesDto> mapToSchoolNamesDtoList(List<School> schools) {
        return schools.stream().map(this::mapToSchoolNamesDto).collect(Collectors.toList());
    }

    public AbsenceTeacherViewDto mapToAbsenceTeacherViewDto(Absence absence) {
        AbsenceTeacherViewDto dto = new AbsenceTeacherViewDto();
        dto.setCourseName(absence.getCourse() != null ? absence.getCourse().getName() : null);
        dto.setDate(absence.getDate());

        if (absence.getStudent() != null) {
            UserRepresentation user = keycloakAdminClientService.getUserFromUserID(absence.getStudent().getUserID());
            dto.setStudentName(user != null ? user.getUsername() : null);
        }

        return dto;
    }

    public List<AbsenceTeacherViewDto> mapToAbsenceTeacherViewDtoList(List<Absence> absences) {
        return absences.stream()
                .map(this::mapToAbsenceTeacherViewDto)
                .collect(Collectors.toList());
    }





}
