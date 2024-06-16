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
            directorDto.setSchoolID(director.getSchool().getId());
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
            parentDto.setStudentsID(parent.getStudents().stream().map(student -> student.getId()).collect(Collectors.toList()));
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

    public List<AbsenceDto> mapToAbsenceListDto(List<Absence> absenceList){
        return absenceList.stream().map(this::mapToAbsenceDto).collect(Collectors.toList());
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
}
