package belaquaa.school.service;

import belaquaa.school.dto.SubjectDto;

import java.util.List;

public interface SubjectService {
    List<SubjectDto> getAll();

    SubjectDto getById(Long id);

    SubjectDto create(SubjectDto subjectDTO);

    SubjectDto update(Long id, SubjectDto subjectDTO);

    void delete(Long id);
}
