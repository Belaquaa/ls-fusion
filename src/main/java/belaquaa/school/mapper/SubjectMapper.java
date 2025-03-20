package belaquaa.school.mapper;

import belaquaa.school.dto.SubjectDTO;
import belaquaa.school.model.Subject;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubjectMapper {
    SubjectDTO toDTO(Subject subject);
    Subject toEntity(SubjectDTO subjectDTO);
}
