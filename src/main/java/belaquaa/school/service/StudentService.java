package belaquaa.school.service;

import belaquaa.school.dto.StudentDto;

import java.util.List;

public interface StudentService {
    List<StudentDto> getAll();

    StudentDto getById(Long id);

    StudentDto create(StudentDto studentDTO);

    StudentDto update(Long id, StudentDto studentDTO);

    void delete(Long id);
}
