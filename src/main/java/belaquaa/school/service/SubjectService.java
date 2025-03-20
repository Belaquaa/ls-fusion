package belaquaa.school.service;

import belaquaa.school.dto.SubjectDTO;

import java.util.List;

public interface SubjectService {
    List<SubjectDTO> getAll();

    SubjectDTO getById(Long id);

    SubjectDTO create(SubjectDTO subjectDTO);

    SubjectDTO update(Long id, SubjectDTO subjectDTO);

    void delete(Long id);
}
