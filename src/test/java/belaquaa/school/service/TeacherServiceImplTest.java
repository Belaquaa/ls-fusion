package belaquaa.school.service;

import belaquaa.school.dto.SubjectDTO;
import belaquaa.school.dto.TeacherDTO;
import belaquaa.school.exception.ResourceNotFoundException;
import belaquaa.school.model.Subject;
import belaquaa.school.model.Teacher;
import belaquaa.school.service.impl.TeacherServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TeacherServiceImplTest extends AbstractServiceTest {

    private TeacherServiceImpl teacherService;

    @BeforeEach
    public void setUp() {
        teacherService = new TeacherServiceImpl(teacherRepository, subjectRepository, teacherMapper);
    }

    @Test
    public void testGetAll() {
        List<Teacher> teachers = Arrays.asList(new Teacher(), new Teacher());
        when(teacherRepository.findAll()).thenReturn(teachers);
        when(teacherMapper.toDTO(any())).thenReturn(new TeacherDTO());

        List<TeacherDTO> result = teacherService.getAll();
        assertEquals(2, result.size());
        verify(teacherRepository).findAll();
        verify(teacherMapper, times(2)).toDTO(any());
    }

    @Test
    public void testGetByIdFound() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(teacherMapper.toDTO(teacher)).thenReturn(new TeacherDTO());

        TeacherDTO result = teacherService.getById(1L);
        assertNotNull(result);
        verify(teacherRepository).findById(1L);
        verify(teacherMapper).toDTO(teacher);
    }

    @Test
    public void testGetByIdNotFound() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> teacherService.getById(1L));
        verify(teacherRepository).findById(1L);
    }

    @Test
    public void testCreate() {
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setId(1L);
        // Устанавливаем предметы в teacherDTO
        SubjectDTO subjectDTO = new SubjectDTO();
        subjectDTO.setId(100L);
        teacherDTO.setSubjects(new HashSet<>(List.of(subjectDTO)));

        Teacher teacher = new Teacher();
        teacher.setId(1L);
        when(teacherMapper.toEntity(teacherDTO)).thenReturn(teacher);

        Subject subject = new Subject();
        when(subjectRepository.findById(100L)).thenReturn(Optional.of(subject));

        teacher.setSubjects(new HashSet<>(List.of(subject)));
        when(teacherRepository.save(teacher)).thenReturn(teacher);
        when(teacherMapper.toDTO(teacher)).thenReturn(teacherDTO);

        TeacherDTO result = teacherService.create(teacherDTO);
        assertNotNull(result);
        verify(subjectRepository).findById(100L);
        verify(teacherRepository).save(teacher);
        verify(teacherMapper).toDTO(teacher);
    }

    @Test
    public void testUpdate() {
        Teacher existing = new Teacher();
        existing.setId(1L);
        existing.setFullName("Old Name");
        existing.setClassTeacher(false);
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(existing));

        TeacherDTO updateDTO = new TeacherDTO();
        updateDTO.setFullName("New Name");
        updateDTO.setClassTeacher(true);
        SubjectDTO subjectDTO = new SubjectDTO();
        subjectDTO.setId(100L);
        updateDTO.setSubjects(new HashSet<>(List.of(subjectDTO)));

        Subject subject = new Subject();
        when(subjectRepository.findById(100L)).thenReturn(Optional.of(subject));
        existing.setSubjects(new HashSet<>(List.of(subject)));
        when(teacherRepository.save(existing)).thenReturn(existing);
        when(teacherMapper.toDTO(existing)).thenReturn(updateDTO);

        TeacherDTO result = teacherService.update(1L, updateDTO);
        assertEquals("New Name", result.getFullName());
        assertTrue(result.isClassTeacher());
        verify(teacherRepository).findById(1L);
        verify(subjectRepository).findById(100L);
        verify(teacherRepository).save(existing);
        verify(teacherMapper).toDTO(existing);
    }

    @Test
    public void testDelete() {
        teacherService.delete(1L);
        verify(teacherRepository).deleteById(1L);
    }

    @Test
    public void testGetClassTeachers() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setClassTeacher(true);
        when(teacherRepository.findByIsClassTeacherTrue()).thenReturn(List.of(teacher));

        TeacherDTO teacherDTO = new TeacherDTO();
        when(teacherMapper.toDTO(teacher)).thenReturn(teacherDTO);

        List<TeacherDTO> result = teacherService.getClassTeachers();
        assertEquals(1, result.size());
        verify(teacherRepository).findByIsClassTeacherTrue();
        verify(teacherMapper).toDTO(teacher);
    }
}
