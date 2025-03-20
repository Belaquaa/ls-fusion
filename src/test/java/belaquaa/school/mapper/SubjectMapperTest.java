package belaquaa.school.mapper;

import belaquaa.school.dto.SubjectDto;
import belaquaa.school.model.Subject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubjectMapperTest extends AbstractMapperTest {
    @Test
    public void testToDTO() {
        Subject subject = MapperTestDataFactory.createSubject(1L, "Математика", true);
        SubjectDto dto = subjectMapper.toDTO(subject);
        assertNotNull(dto);
        assertEquals(subject.getId(), dto.getId());
        assertEquals(subject.getName(), dto.getName());
        assertEquals(subject.isProfile(), dto.isProfile());
    }

    @Test
    public void testToEntity() {
        SubjectDto dto = new SubjectDto();
        dto.setId(2L);
        dto.setName("История");
        dto.setProfile(false);
        Subject subject = subjectMapper.toEntity(dto);
        assertNotNull(subject);
        assertEquals(dto.getId(), subject.getId());
        assertEquals(dto.getName(), subject.getName());
        assertEquals(dto.isProfile(), subject.isProfile());
    }
}