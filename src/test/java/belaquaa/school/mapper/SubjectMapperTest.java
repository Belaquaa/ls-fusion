package belaquaa.school.mapper;

import belaquaa.school.dto.SubjectDTO;
import belaquaa.school.model.Subject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubjectMapperTest extends AbstractMapperTest {

    @Test
    public void testToDTO() {
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Математика");
        subject.setProfile(true);

        SubjectDTO dto = subjectMapper.toDTO(subject);
        assertNotNull(dto);
        assertEquals(subject.getId(), dto.getId());
        assertEquals(subject.getName(), dto.getName());
        assertEquals(subject.isProfile(), dto.isProfile());
    }

    @Test
    public void testToEntity() {
        SubjectDTO dto = new SubjectDTO();
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
