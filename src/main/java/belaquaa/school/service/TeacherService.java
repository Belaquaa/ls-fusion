package belaquaa.school.service;

import belaquaa.school.dto.TeacherDto;

import java.util.List;

public interface TeacherService {
    List<TeacherDto> getAll();

    TeacherDto getById(Long id);

    TeacherDto create(TeacherDto teacherDTO);

    TeacherDto update(Long id, TeacherDto teacherDTO);

    void delete(Long id);

    List<TeacherDto> getTeachersByIsClassTeacher(Boolean isClassTeacher);
}
