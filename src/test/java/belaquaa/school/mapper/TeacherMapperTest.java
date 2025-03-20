package belaquaa.school.mapper;

import belaquaa.school.dto.TeacherDto;
import belaquaa.school.model.Subject;
import belaquaa.school.model.Teacher;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TeacherMapperTest extends AbstractMapperTest {
    @Test
    public void testToDTO() {
        Set<Subject> subjects = new HashSet<>();
        subjects.add(MapperTestDataFactory.createSubject(10L, "Математика", true));
        subjects.add(MapperTestDataFactory.createSubject(11L, "Физика", false));
        Teacher teacher = MapperTestDataFactory.createTeacher(1L, "Сидоров Сидор", true, subjects);
        TeacherDto dto = teacherMapper.toDTO(teacher);
        assertNotNull(dto);
        assertEquals(teacher.getId(), dto.getId());
        assertEquals(teacher.getFullName(), dto.getFullName());
        assertEquals(teacher.isClassTeacher(), dto.isClassTeacher());
        assertNotNull(dto.getSubjectIds());
        assertEquals(teacher.getSubjects().size(), dto.getSubjectIds().size());
        assertTrue(dto.getSubjectIds().contains(10L));
        assertTrue(dto.getSubjectIds().contains(11L));
    }

    @Test
    public void testToEntity() {
        TeacherDto dto = new TeacherDto();
        dto.setId(2L);
        dto.setFullName("Кузнецов Кузьма");
        dto.setClassTeacher(false);
        Set<Long> subjectIds = new HashSet<>();
        subjectIds.add(20L);
        subjectIds.add(21L);
        dto.setSubjectIds(subjectIds);
        Teacher teacher = teacherMapper.toEntity(dto);
        assertNotNull(teacher);
        assertEquals(dto.getId(), teacher.getId());
        assertEquals(dto.getFullName(), teacher.getFullName());
        assertEquals(dto.isClassTeacher(), teacher.isClassTeacher());
        assertTrue(teacher.getSubjects() == null || teacher.getSubjects().isEmpty());
    }
}