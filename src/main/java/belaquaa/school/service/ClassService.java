package belaquaa.school.service;

import belaquaa.school.dto.ClassDTO;
import belaquaa.school.dto.StudentDTO;

import java.util.List;

public interface ClassService {
    List<ClassDTO> getAll();

    ClassDTO getById(Long id);

    ClassDTO create(ClassDTO classDTO);

    ClassDTO update(Long id, ClassDTO classDTO);

    void delete(Long id);

    StudentDTO addStudentToClass(Long classId, StudentDTO studentDTO);

    void removeStudentFromClass(Long classId, Long studentId);
}
