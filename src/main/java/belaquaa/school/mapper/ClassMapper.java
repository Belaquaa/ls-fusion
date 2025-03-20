package belaquaa.school.mapper;

import belaquaa.school.dto.ClassDto;
import belaquaa.school.model.ClassEntity;
import belaquaa.school.model.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring", uses = {SubjectMapper.class, TeacherMapper.class, StudentMapper.class})
public interface ClassMapper {

    @Mapping(target = "studentsCount", expression = "java(entity.getStudents() != null ? entity.getStudents().size() : 0)")
    @Mapping(target = "profileSubjectId", source = "profileSubject.id")
    @Mapping(target = "classTeacherId", source = "classTeacher.id")
    @Mapping(target = "studentIds", expression = "java(getStudentIds(entity))")
    ClassDto toDTO(ClassEntity entity);

    default List<Long> getStudentIds(ClassEntity entity) {
        if (entity.getStudents() == null) {
            return Collections.emptyList();
        }
        return entity.getStudents().stream()
                .map(Student::getId)
                .toList();
    }

    @Mapping(target = "profileSubject", ignore = true)
    @Mapping(target = "classTeacher", ignore = true)
    @Mapping(target = "students", ignore = true)
    ClassEntity toEntity(ClassDto dto);
}