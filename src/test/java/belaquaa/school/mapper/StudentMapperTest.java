package belaquaa.school.mapper;

import belaquaa.school.dto.StudentDto;
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
        Student student = MapperTestDataFactory.createStudent(1L, "Иванов Иван", 5, classEntity);
        StudentDto dto = studentMapper.toDTO(student);
        assertNotNull(dto);
        assertEquals(student.getId(), dto.getId());
        assertEquals(classEntity.getId(), dto.getClassId());
        assertEquals(student.getFullName(), dto.getFullName());
        assertEquals(student.getOrderNumber(), dto.getOrderNumber());
    }

    @Test
    public void testToEntity() {
        StudentDto dto = new StudentDto();
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