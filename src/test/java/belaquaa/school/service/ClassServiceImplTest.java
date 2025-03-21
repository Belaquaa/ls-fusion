package belaquaa.school.service;

import belaquaa.school.dto.ClassDto;
import belaquaa.school.dto.StudentDto;
import belaquaa.school.exception.InvalidOperationException;
import belaquaa.school.exception.ResourceNotFoundException;
import belaquaa.school.model.ClassEntity;
import belaquaa.school.model.Student;
import belaquaa.school.model.Subject;
import belaquaa.school.model.Teacher;
import belaquaa.school.service.impl.ClassServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static belaquaa.school.service.ServiceTestDataFactory.createClassDTO;
import static belaquaa.school.service.ServiceTestDataFactory.createClassEntity;
import static belaquaa.school.service.ServiceTestDataFactory.createStudent;
import static belaquaa.school.service.ServiceTestDataFactory.createStudentDTO;
import static belaquaa.school.service.ServiceTestDataFactory.createSubject;
import static belaquaa.school.service.ServiceTestDataFactory.createSubjectDTO;
import static belaquaa.school.service.ServiceTestDataFactory.createTeacher;
import static belaquaa.school.service.ServiceTestDataFactory.createTeacherDTO;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ClassServiceImplTest extends AbstractServiceTest {
    private ClassServiceImpl classService;
    private SubjectService subjectService;
    private TeacherService teacherService;

    @BeforeEach
    public void setUp() {
        subjectService = mock(SubjectService.class);
        teacherService = mock(TeacherService.class);
        classService = new ClassServiceImpl(classRepository, studentRepository, classMapper, studentMapper, subjectService, teacherService, subjectMapper, teacherMapper);
    }

    @Test
    public void testGetAll() {
        List<ClassEntity> classEntities = Arrays.asList(createClassEntity(1L, 0, null, null, null, new ArrayList<>()),
                createClassEntity(2L, 0, null, null, null, new ArrayList<>()));
        when(classRepository.findAll()).thenReturn(classEntities);
        when(classMapper.toDTO(any())).thenReturn(new ClassDto());
        List<ClassDto> result = classService.getAll();
        assertEquals(2, result.size());
        verify(classRepository).findAll();
        verify(classMapper, times(classEntities.size())).toDTO(any());
    }

    @Test
    public void testGetByIdFound() {
        ClassEntity classEntity = createClassEntity(1L, 9, "A", null, null, new ArrayList<>());
        when(classRepository.findById(1L)).thenReturn(Optional.of(classEntity));
        when(classMapper.toDTO(classEntity)).thenReturn(new ClassDto());
        ClassDto result = classService.getById(1L);
        assertNotNull(result);
        verify(classRepository).findById(1L);
        verify(classMapper).toDTO(classEntity);
    }

    @Test
    public void testGetByIdNotFound() {
        when(classRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> classService.getById(1L));
        verify(classRepository).findById(1L);
    }

    @Test
    public void testCreate() {
        ClassDto classDTO = createClassDTO(null, 9, "A", 10L, 20L);
        ClassEntity classEntity = createClassEntity(null, 9, "A", null, null, new ArrayList<>());
        Subject subject = createSubject(10L, "Math", true);
        Teacher teacher = createTeacher(20L, "Ivanov", true, null);
        when(classMapper.toEntity(classDTO)).thenReturn(classEntity);
        when(subjectService.getById(10L)).thenReturn(createSubjectDTO(10L, "Math", true));
        when(subjectMapper.toEntity(any())).thenReturn(subject);
        when(teacherService.getById(20L)).thenReturn(createTeacherDTO(20L, "Ivanov", true, null));
        when(teacherMapper.toEntity(any())).thenReturn(teacher);
        when(classRepository.save(classEntity)).thenReturn(classEntity);
        when(classMapper.toDTO(classEntity)).thenReturn(classDTO);
        ClassDto result = classService.create(classDTO);
        assertNotNull(result);
        verify(classMapper).toEntity(classDTO);
        verify(subjectService).getById(10L);
        verify(teacherService).getById(20L);
        verify(classRepository).save(classEntity);
        verify(classMapper).toDTO(classEntity);
    }

    @Test
    public void testUpdate() {
        ClassEntity existing = createClassEntity(1L, 1, "A", createSubject(10L, "Math", true), createTeacher(20L, "Ivanov", true, null), new ArrayList<>());
        when(classRepository.findById(1L)).thenReturn(Optional.of(existing));
        ClassDto updateDTO = createClassDTO(null, 2, "B", 11L, 20L);
        Subject newSubject = createSubject(11L, "Physics", true);
        when(subjectService.getById(11L)).thenReturn(createSubjectDTO(11L, "Physics", true));
        when(subjectMapper.toEntity(any())).thenReturn(newSubject);
        when(teacherService.getById(20L)).thenReturn(createTeacherDTO(20L, "Ivanov", true, null));
        when(teacherMapper.toEntity(any())).thenReturn(existing.getClassTeacher());
        when(classRepository.save(existing)).thenReturn(existing);
        when(classMapper.toDTO(existing)).thenReturn(updateDTO);
        ClassDto result = classService.update(1L, updateDTO);
        assertEquals(2, result.getNumber());
        assertEquals("B", result.getLetter());
        verify(classRepository).findById(1L);
        verify(subjectService).getById(11L);
        verify(teacherService).getById(20L);
        verify(classRepository).save(existing);
        verify(classMapper).toDTO(existing);
    }

    @Test
    public void testDelete() {
        classService.delete(1L);
        verify(classRepository).deleteById(1L);
    }

    @Test
    public void testAddStudentToClass() {
        ClassEntity classEntity = createClassEntity(1L, 9, "A", null, null, new ArrayList<>());
        when(classRepository.findById(1L)).thenReturn(Optional.of(classEntity));
        StudentDto studentDTO = createStudentDTO(null, 1L, 0, "Student");
        Student student = createStudent(100L, classEntity, 0, "Student");
        when(studentMapper.toEntity(studentDTO)).thenReturn(student);
        when(studentRepository.save(student)).thenReturn(student);
        when(studentMapper.toDTO(student)).thenReturn(studentDTO);
        StudentDto result = classService.addStudentToClass(1L, studentDTO);
        assertNotNull(result);
        verify(classRepository).findById(1L);
        verify(studentMapper).toEntity(studentDTO);
        verify(studentRepository).save(student);
        verify(studentMapper).toDTO(student);
    }

    @Test
    public void testRemoveStudentFromClass() {
        ClassEntity classEntity = createClassEntity(1L, 9, "A", null, null, new ArrayList<>());
        Student student = createStudent(100L, classEntity, 1, "Student");
        when(classRepository.findById(1L)).thenReturn(Optional.of(classEntity));
        when(studentRepository.findById(100L)).thenReturn(Optional.of(student));
        classService.removeStudentFromClass(1L, 100L);
        verify(classRepository).findById(1L);
        verify(studentRepository).findById(100L);
        verify(studentRepository).delete(student);
    }

    @Test
    public void testRemoveStudentFromClassInvalid() {
        ClassEntity classEntity = createClassEntity(1L, 9, "A", null, null, new ArrayList<>());
        ClassEntity anotherClass = createClassEntity(2L, 9, "B", null, null, new ArrayList<>());
        Student student = createStudent(100L, anotherClass, 1, "Student");
        when(classRepository.findById(1L)).thenReturn(Optional.of(classEntity));
        when(studentRepository.findById(100L)).thenReturn(Optional.of(student));
        assertThrows(InvalidOperationException.class, () -> classService.removeStudentFromClass(1L, 100L));
        verify(classRepository).findById(1L);
        verify(studentRepository).findById(100L);
    }
}
