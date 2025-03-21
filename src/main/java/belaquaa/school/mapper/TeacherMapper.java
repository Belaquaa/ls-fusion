package belaquaa.school.mapper;

import belaquaa.school.dto.TeacherDTO;
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
    TeacherDTO toDTO(Teacher teacher);

    Teacher toEntity(TeacherDTO teacherDTO);

    default Set<Long> getSubjectIds(Teacher teacher) {
        if (teacher.getSubjects() == null) {
            return Collections.emptySet();
        }
        return teacher.getSubjects().stream()
                .map(Subject::getId)
                .collect(Collectors.toSet());
    }
}