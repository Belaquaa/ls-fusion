package belaquaa.school.mapper;

import belaquaa.school.dto.TeacherDTO;
import belaquaa.school.model.Teacher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = SubjectMapper.class)
public interface TeacherMapper {
    TeacherDTO toDTO(Teacher teacher);
    Teacher toEntity(TeacherDTO teacherDTO);
}
