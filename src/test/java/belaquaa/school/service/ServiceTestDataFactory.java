package belaquaa.school.service;

import belaquaa.school.dto.ClassDto;
import belaquaa.school.dto.StudentDto;
import belaquaa.school.dto.SubjectDto;
import belaquaa.school.dto.TeacherDto;
import belaquaa.school.model.ClassEntity;
import belaquaa.school.model.Student;
import belaquaa.school.model.Subject;
import belaquaa.school.model.Teacher;

import java.util.List;
import java.util.Set;

public class ServiceTestDataFactory {
    public static ClassDto createClassDTO(Long id, Integer number, String letter, Long profileSubjectId, Long classTeacherId) {
        ClassDto dto = new ClassDto();
        dto.setId(id);
        dto.setNumber(number);
        dto.setLetter(letter);
        dto.setProfileSubjectId(profileSubjectId);
        dto.setClassTeacherId(classTeacherId);
        return dto;
    }

    public static ClassEntity createClassEntity(Long id, int number, String letter, Subject profileSubject, Teacher classTeacher, List<Student> students) {
        ClassEntity entity = new ClassEntity();
        entity.setId(id);
        entity.setNumber(number);
        entity.setLetter(letter);
        entity.setProfileSubject(profileSubject);
        entity.setClassTeacher(classTeacher);
        entity.setStudents(students);
        return entity;
    }

    public static SubjectDto createSubjectDTO(Long id, String name, boolean profile) {
        SubjectDto dto = new SubjectDto();
        dto.setId(id);
        dto.setName(name);
        dto.setProfile(profile);
        return dto;
    }

    public static Subject createSubject(Long id, String name, boolean profile) {
        Subject subject = new Subject();
        subject.setId(id);
        subject.setName(name);
        subject.setProfile(profile);
        return subject;
    }

    public static TeacherDto createTeacherDTO(Long id, String fullName, boolean classTeacher, Set<Long> subjectIds) {
        TeacherDto dto = new TeacherDto();
        dto.setId(id);
        dto.setFullName(fullName);
        dto.setClassTeacher(classTeacher);
        dto.setSubjectIds(subjectIds);
        return dto;
    }

    public static Teacher createTeacher(Long id, String fullName, boolean classTeacher, Set<Subject> subjects) {
        Teacher teacher = new Teacher();
        teacher.setId(id);
        teacher.setFullName(fullName);
        teacher.setClassTeacher(classTeacher);
        teacher.setSubjects(subjects);
        return teacher;
    }

    public static StudentDto createStudentDTO(Long id, Long classId, int orderNumber, String fullName) {
        StudentDto dto = new StudentDto();
        dto.setId(id);
        dto.setClassId(classId);
        dto.setOrderNumber(orderNumber);
        dto.setFullName(fullName);
        return dto;
    }

    public static Student createStudent(Long id, ClassEntity classEntity, int orderNumber, String fullName) {
        Student student = new Student();
        student.setId(id);
        student.setClassEntity(classEntity);
        student.setOrderNumber(orderNumber);
        student.setFullName(fullName);
        return student;
    }
}