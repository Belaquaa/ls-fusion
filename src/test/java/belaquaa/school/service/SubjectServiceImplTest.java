package belaquaa.school.service;

import belaquaa.school.dto.SubjectDto;
import belaquaa.school.exception.ResourceNotFoundException;
import belaquaa.school.model.Subject;
import belaquaa.school.service.impl.SubjectServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static belaquaa.school.service.ServiceTestDataFactory.createSubject;
import static belaquaa.school.service.ServiceTestDataFactory.createSubjectDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SubjectServiceImplTest extends AbstractServiceTest {
    private SubjectServiceImpl subjectService;

    @BeforeEach
    public void setUp() {
        subjectService = new SubjectServiceImpl(subjectRepository, subjectMapper);
    }

    @Test
    public void testGetAll() {
        List<Subject> subjects = Arrays.asList(new Subject(), new Subject());
        when(subjectRepository.findAll()).thenReturn(subjects);
        when(subjectMapper.toDTO(any(Subject.class))).thenReturn(new SubjectDto());
        List<SubjectDto> result = subjectService.getAll();
        assertEquals(2, result.size());
        verify(subjectRepository).findAll();
        verify(subjectMapper, times(subjects.size())).toDTO(any(Subject.class));
    }

    @Test
    public void testGetByIdFound() {
        Subject subject = createSubject(1L, "Math", true);
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(subject));
        SubjectDto dto = createSubjectDTO(1L, "Math", true);
        when(subjectMapper.toDTO(subject)).thenReturn(dto);
        SubjectDto result = subjectService.getById(1L);
        assertNotNull(result);
        verify(subjectRepository).findById(1L);
        verify(subjectMapper).toDTO(subject);
    }

    @Test
    public void testGetByIdNotFound() {
        when(subjectRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> subjectService.getById(1L));
        verify(subjectRepository).findById(1L);
    }

    @Test
    public void testCreate() {
        SubjectDto dto = createSubjectDTO(null, "History", false);
        Subject subject = createSubject(null, "History", false);
        when(subjectMapper.toEntity(dto)).thenReturn(subject);
        when(subjectRepository.save(subject)).thenReturn(subject);
        when(subjectMapper.toDTO(subject)).thenReturn(dto);
        SubjectDto result = subjectService.create(dto);
        assertNotNull(result);
        verify(subjectMapper).toEntity(dto);
        verify(subjectRepository).save(subject);
        verify(subjectMapper).toDTO(subject);
    }

    @Test
    public void testUpdate() {
        Subject existing = createSubject(1L, "Old Name", false);
        when(subjectRepository.findById(1L)).thenReturn(Optional.of(existing));
        SubjectDto updateDTO = createSubjectDTO(null, "New Name", true);
        when(subjectRepository.save(existing)).thenReturn(existing);
        when(subjectMapper.toDTO(existing)).thenReturn(updateDTO);
        SubjectDto result = subjectService.update(1L, updateDTO);
        assertEquals("New Name", result.getName());
        assertTrue(result.isProfile());
        verify(subjectRepository).findById(1L);
        verify(subjectRepository).save(existing);
        verify(subjectMapper).toDTO(existing);
    }

    @Test
    public void testDelete() {
        subjectService.delete(1L);
        verify(subjectRepository).deleteById(1L);
    }
}
