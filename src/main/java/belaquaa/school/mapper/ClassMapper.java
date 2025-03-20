package belaquaa.school.mapper;

import belaquaa.school.dto.ClassDTO;
import belaquaa.school.model.ClassEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {SubjectMapper.class, TeacherMapper.class, StudentMapper.class})
public interface ClassMapper {
    @Mapping(target = "studentsCount", expression = "java(entity.getStudentsCount())")
    ClassDTO toDTO(ClassEntity entity);

    ClassEntity toEntity(ClassDTO dto);
}