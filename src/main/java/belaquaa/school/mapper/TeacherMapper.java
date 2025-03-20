package belaquaa.school.mapper;

import belaquaa.school.dto.TeacherDto;
import belaquaa.school.model.Subject;
import belaquaa.school.model.Teacher;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = SubjectMapper.class)
public interface TeacherMapper {

    @Mapping(target = "subjectIds", expression = "java(getSubjectIds(teacher))")
    TeacherDto toDTO(Teacher teacher);

    @Mapping(target = "subjects", ignore = true)
    @Mapping(target = "classEntity", ignore = true)
    Teacher toEntity(TeacherDto teacherDto);

    default Set<Long> getSubjectIds(Teacher teacher) {
        if (teacher.getSubjects() == null) {
            return Collections.emptySet();
        }
        return teacher.getSubjects().stream()
                .map(Subject::getId)
                .collect(Collectors.toSet());
    }
}