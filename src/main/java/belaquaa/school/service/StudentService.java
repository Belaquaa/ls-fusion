package belaquaa.school.service;

import belaquaa.school.dto.StudentDTO;

import java.util.List;

public interface StudentService {
    List<StudentDTO> getAll();

    StudentDTO getById(Long id);

    StudentDTO create(StudentDTO studentDTO);

    StudentDTO update(Long id, StudentDTO studentDTO);

    void delete(Long id);
}
