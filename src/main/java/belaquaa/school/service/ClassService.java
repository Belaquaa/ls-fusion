package belaquaa.school.service;

import belaquaa.school.dto.ClassDto;
import belaquaa.school.dto.StudentDto;

import java.util.List;

public interface ClassService {
    List<ClassDto> getAll();

    ClassDto getById(Long id);

    ClassDto create(ClassDto classDTO);

    ClassDto update(Long id, ClassDto classDTO);

    void delete(Long id);

    StudentDto addStudentToClass(Long classId, StudentDto studentDTO);

    void removeStudentFromClass(Long classId, Long studentId);
}
