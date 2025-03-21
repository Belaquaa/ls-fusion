package belaquaa.school.mapper;

import belaquaa.school.model.ClassEntity;
import belaquaa.school.model.Student;
import belaquaa.school.model.Subject;
import belaquaa.school.model.Teacher;

import java.util.List;
import java.util.Set;

public class MapperTestDataFactory {
    public static Subject createSubject(Long id, String name, boolean isProfile) {
        Subject subject = new Subject();
        subject.setId(id);
        subject.setName(name);
        subject.setProfile(isProfile);
        return subject;
    }

    public static Teacher createTeacher(Long id, String fullName, boolean isClassTeacher, Set<Subject> subjects) {
        Teacher teacher = new Teacher();
        teacher.setId(id);
        teacher.setFullName(fullName);
        teacher.setClassTeacher(isClassTeacher);
        teacher.setSubjects(subjects);
        return teacher;
    }

    public static Student createStudent(Long id, String fullName, int orderNumber, ClassEntity classEntity) {
        Student student = new Student();
        student.setId(id);
        student.setFullName(fullName);
        student.setOrderNumber(orderNumber);
        student.setClassEntity(classEntity);
        return student;
    }

    public static ClassEntity createClassEntity(Long id, int number, String letter, Subject profileSubject, Teacher classTeacher, List<Student> students) {
        ClassEntity classEntity = new ClassEntity();
        classEntity.setId(id);
        classEntity.setNumber(number);
        classEntity.setLetter(letter);
        classEntity.setProfileSubject(profileSubject);
        classEntity.setClassTeacher(classTeacher);
        classEntity.setStudents(students);
        return classEntity;
    }
}
