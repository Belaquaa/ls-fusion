package belaquaa.school.mapper;

import belaquaa.school.dto.ClassDto;
import belaquaa.school.model.ClassEntity;
import belaquaa.school.model.Student;
import belaquaa.school.model.Subject;
import belaquaa.school.model.Teacher;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;


public class ClassMapperTest extends AbstractMapperTest {
    @Test
    public void testToDTO() {
        Student student1 = MapperTestDataFactory.createStudent(100L, "Student1", 1, null);
        Student student2 = MapperTestDataFactory.createStudent(101L, "Student2", 2, null);
        List<Student> students = Arrays.asList(student1, student2);
        Subject subject = MapperTestDataFactory.createSubject(5L, "Математика", true);
        Teacher teacher = MapperTestDataFactory.createTeacher(7L, "Иванов Иван", true, null);
        ClassEntity classEntity = MapperTestDataFactory.createClassEntity(1L, 9, "А", subject, teacher, students);
        ClassDto dto = classMapper.toDTO(classEntity);
        assertNotNull(dto);
        assertEquals(classEntity.getId(), dto.getId());
        assertEquals(classEntity.getNumber(), dto.getNumber());
        assertEquals(classEntity.getLetter(), dto.getLetter());
        assertEquals(classEntity.getStudents().size(), dto.getStudentsCount());
        assertEquals(subject.getId(), dto.getProfileSubjectId());
        assertEquals(teacher.getId(), dto.getClassTeacherId());
        List<Long> expectedStudentIds = Arrays.asList(student1.getId(), student2.getId());
        assertEquals(expectedStudentIds, dto.getStudentIds());
    }

    @Test
    public void testToEntity() {
        ClassDto dto = new ClassDto();
        dto.setId(2L);
        dto.setNumber(10);
        dto.setLetter("Б");
        dto.setProfileSubjectId(null);
        dto.setClassTeacherId(null);
        dto.setStudentIds(Collections.emptyList());
        dto.setStudentsCount(0);
        ClassEntity entity = classMapper.toEntity(dto);
        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getNumber(), entity.getNumber());
        assertEquals(dto.getLetter(), entity.getLetter());
        assertNull(entity.getProfileSubject());
        assertNull(entity.getClassTeacher());
    }
}
