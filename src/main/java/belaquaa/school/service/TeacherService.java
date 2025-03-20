package belaquaa.school.service;

import belaquaa.school.dto.TeacherDTO;

import java.util.List;

public interface TeacherService {
    List<TeacherDTO> getAll();

    TeacherDTO getById(Long id);

    TeacherDTO create(TeacherDTO teacherDTO);

    TeacherDTO update(Long id, TeacherDTO teacherDTO);

    void delete(Long id);

    List<TeacherDTO> getClassTeachers();
}
