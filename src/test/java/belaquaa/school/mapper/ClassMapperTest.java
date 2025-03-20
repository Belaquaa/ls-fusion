package belaquaa.school.mapper;

import belaquaa.school.dto.ClassDTO;
import belaquaa.school.model.ClassEntity;
import belaquaa.school.model.Student;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ClassMapperTest extends AbstractMapperTest {

    @Test
    public void testToDTO() {
        ClassEntity classEntity = new ClassEntity();
        classEntity.setId(1L);
        classEntity.setNumber(9);
        classEntity.setLetter("А");

        Student student1 = new Student();
        student1.setId(100L);
        Student student2 = new Student();
        student2.setId(101L);
        classEntity.setStudents(Arrays.asList(student1, student2));

        classEntity.setProfileSubject(null);
        classEntity.setClassTeacher(null);

        ClassDTO dto = classMapper.toDTO(classEntity);
        assertNotNull(dto);
        assertEquals(classEntity.getId(), dto.getId());
        assertEquals(classEntity.getNumber(), dto.getNumber());
        assertEquals(classEntity.getLetter(), dto.getLetter());
        assertEquals(classEntity.getStudents().size(), dto.getStudentsCount());
    }

    @Test
    public void testToEntity() {
        ClassDTO dto = new ClassDTO();
        dto.setId(2L);
        dto.setNumber(10);
        dto.setLetter("Б");
        dto.setProfileSubject(null);
        dto.setClassTeacher(null);
        dto.setStudents(Collections.emptyList());
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
