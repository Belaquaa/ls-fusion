package belaquaa.school.service;

import belaquaa.school.dto.SubjectDto;
import belaquaa.school.dto.TeacherDto;
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

import static belaquaa.school.service.ServiceTestDataFactory.createSubject;
import static belaquaa.school.service.ServiceTestDataFactory.createSubjectDTO;
import static belaquaa.school.service.ServiceTestDataFactory.createTeacher;
import static belaquaa.school.service.ServiceTestDataFactory.createTeacherDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TeacherServiceImplTest extends AbstractServiceTest {
    private TeacherServiceImpl teacherService;
    private SubjectService subjectService;

    @BeforeEach
    public void setUp() {
        subjectService = mock(SubjectService.class);
        teacherService = new TeacherServiceImpl(teacherRepository, subjectService, teacherMapper, subjectMapper);
    }

    @Test
    public void testGetAll() {
        List<Teacher> teachers = Arrays.asList(new Teacher(), new Teacher());
        when(teacherRepository.findAll()).thenReturn(teachers);
        when(teacherMapper.toDTO(any())).thenReturn(new TeacherDto());
        List<TeacherDto> result = teacherService.getAll();
        assertEquals(2, result.size());
        verify(teacherRepository).findAll();
        verify(teacherMapper, times(2)).toDTO(any());
    }

    @Test
    public void testGetByIdFound() {
        Teacher teacher = createTeacher(1L, "Teacher", true, null);
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));
        when(teacherMapper.toDTO(teacher)).thenReturn(new TeacherDto());
        TeacherDto result = teacherService.getById(1L);
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
        TeacherDto teacherDTO = createTeacherDTO(null, "Teacher", true, new HashSet<>(List.of(100L)));
        Teacher teacher = createTeacher(null, "Teacher", true, new HashSet<>());
        when(teacherMapper.toEntity(teacherDTO)).thenReturn(teacher);
        SubjectDto subjectDTO = createSubjectDTO(100L, "History", false);
        when(subjectService.getAll()).thenReturn(List.of(subjectDTO));
        Subject subject = createSubject(100L, "History", false);
        when(subjectMapper.toEntity(any())).thenReturn(subject);
        teacher.setSubjects(new HashSet<>(List.of(subject)));
        when(teacherRepository.save(teacher)).thenReturn(teacher);
        when(teacherMapper.toDTO(teacher)).thenReturn(teacherDTO);
        TeacherDto result = teacherService.create(teacherDTO);
        assertNotNull(result);
        verify(subjectService).getAll();
        verify(teacherRepository).save(teacher);
        verify(teacherMapper).toDTO(teacher);
    }

    @Test
    public void testUpdate() {
        Teacher existing = createTeacher(1L, "Old Name", false, new HashSet<>());
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(existing));
        TeacherDto updateDTO = createTeacherDTO(null, "New Name", true, new HashSet<>(List.of(100L)));
        SubjectDto subjectDTO = createSubjectDTO(100L, "History", false);
        when(subjectService.getAll()).thenReturn(List.of(subjectDTO));
        Subject subject = createSubject(100L, "History", false);
        when(subjectMapper.toEntity(any())).thenReturn(subject);
        existing.setSubjects(new HashSet<>(List.of(subject)));
        when(teacherRepository.save(existing)).thenReturn(existing);
        when(teacherMapper.toDTO(existing)).thenReturn(updateDTO);
        TeacherDto result = teacherService.update(1L, updateDTO);
        assertEquals("New Name", result.getFullName());
        assertTrue(result.isClassTeacher());
        verify(teacherRepository).findById(1L);
        verify(subjectService).getAll();
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
        Teacher teacher = createTeacher(1L, "Teacher", true, null);
        when(teacherRepository.findByIsClassTeacher(true)).thenReturn(List.of(teacher));
        when(teacherMapper.toDTO(teacher)).thenReturn(new TeacherDto());
        List<TeacherDto> result = teacherService.getTeachersByIsClassTeacher(true);
        assertEquals(1, result.size());
        verify(teacherRepository).findByIsClassTeacher(true);
        verify(teacherMapper).toDTO(teacher);
    }
}
