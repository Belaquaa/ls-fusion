package belaquaa.school.service;

import belaquaa.school.dto.StudentDTO;
import belaquaa.school.exception.ResourceNotFoundException;
import belaquaa.school.model.ClassEntity;
import belaquaa.school.model.Student;
import belaquaa.school.service.impl.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class StudentServiceImplTest extends AbstractServiceTest {

    private StudentServiceImpl studentService;

    @BeforeEach
    public void setUp() {
        studentService = new StudentServiceImpl(studentRepository, classRepository, studentMapper);
    }

    @Test
    public void testGetAll() {
        List<Student> students = Arrays.asList(new Student(), new Student());
        when(studentRepository.findAll()).thenReturn(students);
        when(studentMapper.toDTO(any())).thenReturn(new StudentDTO());

        List<StudentDTO> result = studentService.getAll();
        assertEquals(2, result.size());
        verify(studentRepository).findAll();
        verify(studentMapper, times(2)).toDTO(any());
    }

    @Test
    public void testGetByIdFound() {
        Student student = new Student();
        student.setId(1L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentMapper.toDTO(student)).thenReturn(new StudentDTO());

        StudentDTO result = studentService.getById(1L);
        assertNotNull(result);
        verify(studentRepository).findById(1L);
        verify(studentMapper).toDTO(student);
    }

    @Test
    public void testGetByIdNotFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> studentService.getById(1L));
        verify(studentRepository).findById(1L);
    }

    @Test
    public void testCreate() {
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setClassId(1L);
        Student student = new Student();
        when(studentMapper.toEntity(studentDTO)).thenReturn(student);

        ClassEntity classEntity = new ClassEntity();
        when(classRepository.findById(1L)).thenReturn(Optional.of(classEntity));
        student.setClassEntity(classEntity);

        when(studentRepository.save(student)).thenReturn(student);
        when(studentMapper.toDTO(student)).thenReturn(studentDTO);

        StudentDTO result = studentService.create(studentDTO);
        assertNotNull(result);
        verify(studentMapper).toEntity(studentDTO);
        verify(classRepository).findById(1L);
        verify(studentRepository).save(student);
        verify(studentMapper).toDTO(student);
    }

    @Test
    public void testUpdate() {
        Student existing = new Student();
        existing.setId(1L);
        existing.setFullName("Old Name");
        existing.setOrderNumber(1);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(existing));

        StudentDTO updateDTO = new StudentDTO();
        updateDTO.setFullName("New Name");
        updateDTO.setOrderNumber(2);
        updateDTO.setClassId(1L);

        ClassEntity classEntity = new ClassEntity();
        when(classRepository.findById(1L)).thenReturn(Optional.of(classEntity));
        existing.setClassEntity(classEntity);

        when(studentRepository.save(existing)).thenReturn(existing);
        when(studentMapper.toDTO(existing)).thenReturn(updateDTO);

        StudentDTO result = studentService.update(1L, updateDTO);
        assertEquals("New Name", result.getFullName());
        assertEquals(2, result.getOrderNumber());
        verify(studentRepository).findById(1L);
        verify(classRepository).findById(1L);
        verify(studentRepository).save(existing);
        verify(studentMapper).toDTO(existing);
    }

    @Test
    public void testDelete() {
        studentService.delete(1L);
        verify(studentRepository).deleteById(1L);
    }
}
