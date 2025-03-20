package belaquaa.school.repository;

import belaquaa.school.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    long countByClassEntityId(Long classId);
}