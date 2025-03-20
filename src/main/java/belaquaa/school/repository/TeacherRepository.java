package belaquaa.school.repository;

import belaquaa.school.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    List<Teacher> findByIsClassTeacher(boolean isClassTeacher);
}