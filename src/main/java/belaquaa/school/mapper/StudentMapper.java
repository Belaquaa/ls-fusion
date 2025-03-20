package belaquaa.school.mapper;

import belaquaa.school.dto.StudentDto;
import belaquaa.school.model.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "classId", expression = "java(getClassId(student))")
    StudentDto toDTO(Student student);

    @Mapping(target = "classEntity", ignore = true)
    Student toEntity(StudentDto studentDto);

    default Long getClassId(Student student) {
        return student.getClassEntity() != null ? student.getClassEntity().getId() : null;
    }
}