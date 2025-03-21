package belaquaa.school.mapper;

import belaquaa.school.dto.SubjectDto;
import belaquaa.school.model.Subject;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubjectMapper {
    SubjectDto toDTO(Subject subject);

    Subject toEntity(SubjectDto subjectDto);
}