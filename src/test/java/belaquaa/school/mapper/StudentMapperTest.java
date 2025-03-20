package belaquaa.school.mapper;

import belaquaa.school.dto.StudentDTO;
import belaquaa.school.model.ClassEntity;
import belaquaa.school.model.Student;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class StudentMapperTest extends AbstractMapperTest {

    @Test
    public void testToDTO() {
        ClassEntity classEntity = new ClassEntity();
        classEntity.setId(100L);
        Student student = new Student();
        student.setId(1L);
        student.setFullName("Иванов Иван");
        student.setOrderNumber(5);
        student.setClassEntity(classEntity);

        StudentDTO dto = studentMapper.toDTO(student);
        assertNotNull(dto);
        assertEquals(student.getId(), dto.getId());
        assertEquals(classEntity.getId(), dto.getClassId());
        assertEquals(student.getFullName(), dto.getFullName());
        assertEquals(student.getOrderNumber(), dto.getOrderNumber());
    }

    @Test
    public void testToEntity() {
        StudentDTO dto = new StudentDTO();
        dto.setId(2L);
        dto.setFullName("Петров Пётр");
        dto.setOrderNumber(3);
        dto.setClassId(200L);

        Student student = studentMapper.toEntity(dto);
        assertNotNull(student);
        assertEquals(dto.getId(), student.getId());
        assertEquals(dto.getFullName(), student.getFullName());
        assertEquals(dto.getOrderNumber(), student.getOrderNumber());
        assertNull(student.getClassEntity());
    }
}
