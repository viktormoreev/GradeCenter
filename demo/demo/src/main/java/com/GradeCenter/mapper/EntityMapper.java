package com.GradeCenter.mapper;

import com.GradeCenter.dtos.*;
import com.GradeCenter.entity.*;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EntityMapper {
    private final ModelMapper modelMapper = new ModelMapper();

    public CourseDto mapToCourseDto(Course course){
        CourseDto courseDto = new CourseDto();
        modelMapper.map(course, courseDto);

        return courseDto;
    }

    public StudentDto mapToStudentDto(Student student){
        StudentDto studentDto = new StudentDto();
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
        directorDto.setUserID(director.getUserID());
        if (director.getSchool() != null){
            directorDto.setSchoolName(director.getSchool().getName());
        }
        return directorDto;
    }

    public TeacherDto mapToTeacherDto(Teacher teacher){
        TeacherDto teacherDto = new TeacherDto();
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
        parentDto.setUserID(parent.getUserID());
        if (parent.getStudents() != null){
            parentDto.setStudents(mapToStudentListDto(parent.getStudents()));
        }

        return parentDto;
    }

    public SchoolDto mapToSchoolDto(School school){
        SchoolDto schoolDto = new SchoolDto();
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

    public AbsenceDto mapToAbsenceDto(Absence absence){
        AbsenceDto absenceDto = new AbsenceDto();
        absenceDto.setCourseId(absence.getCourse().getId());
        absenceDto.setStudentId(absence.getStudent().getId());
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

    public StudyGroupDto mapToStudyGroupDto(StudyGroup studyGroup){
        StudyGroupDto studyGroupDto = new StudyGroupDto();
        modelMapper.map(studyGroup, studyGroupDto);

        return studyGroupDto;
    }

    public List<StudyGroupDto> mapToStudyGroupDtoList(List<StudyGroup> studyGroupList){
        return studyGroupList.stream().map(this::mapToStudyGroupDto).collect(Collectors.toList());
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
        fetchTeacherDto.setCourses(mapToCourseListDto(teacher.getCourses()));
        fetchTeacherDto.setQualifications(mapToQualificationListDto(teacher.getQualifications()));
        fetchTeacherDto.setUserID(teacher.getUserID());
        fetchTeacherDto.setSchoolName(teacher.getSchool().getName());
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
}
