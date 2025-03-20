package belaquaa.school.mapper;

import belaquaa.school.dto.SubjectDTO;
import belaquaa.school.dto.TeacherDTO;
import belaquaa.school.model.Subject;
import belaquaa.school.model.Teacher;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TeacherMapperTest extends AbstractMapperTest {

    @Test
    public void testToDTO() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFullName("Сидоров Сидор");
        teacher.setClassTeacher(true);

        Subject math = new Subject();
        math.setId(10L);
        math.setName("Математика");
        math.setProfile(true);
        Subject physics = new Subject();
        physics.setId(11L);
        physics.setName("Физика");
        physics.setProfile(false);
        Set<Subject> subjects = new HashSet<>();
        subjects.add(math);
        subjects.add(physics);
        teacher.setSubjects(subjects);

        TeacherDTO dto = teacherMapper.toDTO(teacher);
        assertNotNull(dto);
        assertEquals(teacher.getId(), dto.getId());
        assertEquals(teacher.getFullName(), dto.getFullName());
        assertEquals(teacher.isClassTeacher(), dto.isClassTeacher());
        assertNotNull(dto.getSubjects());
        assertEquals(teacher.getSubjects().size(), dto.getSubjects().size());
    }

    @Test
    public void testToEntity() {
        TeacherDTO dto = new TeacherDTO();
        dto.setId(2L);
        dto.setFullName("Кузнецов Кузьма");
        dto.setClassTeacher(false);

        SubjectDTO subjectDTO = new SubjectDTO();
        subjectDTO.setId(20L);
        subjectDTO.setName("История");
        subjectDTO.setProfile(false);
        Set<SubjectDTO> subjectsDTO = new HashSet<>();
        subjectsDTO.add(subjectDTO);
        dto.setSubjects(subjectsDTO);

        Teacher teacher = teacherMapper.toEntity(dto);
        assertNotNull(teacher);
        assertEquals(dto.getId(), teacher.getId());
        assertEquals(dto.getFullName(), teacher.getFullName());
        assertEquals(dto.isClassTeacher(), teacher.isClassTeacher());
        assertNotNull(teacher.getSubjects());
        assertEquals(dto.getSubjects().size(), teacher.getSubjects().size());
    }
}
