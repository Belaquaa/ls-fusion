package belaquaa.school.service;

import belaquaa.school.dto.ClassDTO;
import belaquaa.school.dto.StudentDTO;
import belaquaa.school.dto.SubjectDTO;
import belaquaa.school.dto.TeacherDTO;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ClassServiceImplTest extends AbstractServiceTest {

    private ClassServiceImpl classService;

    @BeforeEach
    public void setUp() {
        classService = new ClassServiceImpl(classRepository, subjectRepository, teacherRepository, studentRepository, classMapper, studentMapper);
    }

    @Test
    public void testGetAll() {
        List<ClassEntity> classEntities = Arrays.asList(new ClassEntity(), new ClassEntity());
        when(classRepository.findAll()).thenReturn(classEntities);
        when(classMapper.toDTO(any())).thenReturn(new ClassDTO());

        List<ClassDTO> result = classService.getAll();
        assertEquals(2, result.size());
        verify(classRepository).findAll();
        verify(classMapper, times(classEntities.size())).toDTO(any());
    }

    @Test
    public void testGetByIdFound() {
        ClassEntity classEntity = new ClassEntity();
        classEntity.setId(1L);
        when(classRepository.findById(1L)).thenReturn(Optional.of(classEntity));
        when(classMapper.toDTO(classEntity)).thenReturn(new ClassDTO());

        ClassDTO result = classService.getById(1L);
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
        ClassDTO classDTO = new ClassDTO();
        // Задаем профильный предмет в DTO
        SubjectDTO subjectDTO = new SubjectDTO();
        subjectDTO.setId(10L);
        classDTO.setProfileSubject(subjectDTO);
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setId(20L);
        classDTO.setClassTeacher(teacherDTO);

        ClassEntity classEntity = new ClassEntity();
        Subject profileSubject = new Subject();
        profileSubject.setId(10L);
        profileSubject.setProfile(true);
        classEntity.setProfileSubject(profileSubject);

        when(classMapper.toEntity(classDTO)).thenReturn(classEntity);
        when(subjectRepository.findById(10L)).thenReturn(Optional.of(profileSubject));

        Teacher teacher = new Teacher();
        teacher.setId(20L);
        teacher.setClassTeacher(true);
        when(teacherRepository.findById(20L)).thenReturn(Optional.of(teacher));

        when(classRepository.save(classEntity)).thenReturn(classEntity);
        when(classMapper.toDTO(classEntity)).thenReturn(classDTO);

        ClassDTO result = classService.create(classDTO);
        assertNotNull(result);
        verify(classMapper).toEntity(classDTO);
        verify(subjectRepository).findById(10L);
        verify(teacherRepository).findById(20L);
        verify(classRepository).save(classEntity);
        verify(classMapper).toDTO(classEntity);
    }

    @Test
    public void testUpdate() {
        ClassEntity existing = new ClassEntity();
        existing.setId(1L);
        existing.setNumber(1);
        existing.setLetter("A");
        Subject oldSubject = new Subject();
        oldSubject.setId(10L);
        oldSubject.setProfile(true);
        existing.setProfileSubject(oldSubject);
        Teacher teacher = new Teacher();
        teacher.setId(20L);
        teacher.setClassTeacher(true);
        existing.setClassTeacher(teacher);

        when(classRepository.findById(1L)).thenReturn(Optional.of(existing));

        ClassDTO updateDTO = new ClassDTO();
        updateDTO.setNumber(2);
        updateDTO.setLetter("B");
        SubjectDTO newSubjectDTO = new SubjectDTO();
        newSubjectDTO.setId(11L);
        updateDTO.setProfileSubject(newSubjectDTO);
        TeacherDTO teacherDTO = new TeacherDTO();
        teacherDTO.setId(20L);
        updateDTO.setClassTeacher(teacherDTO);

        Subject newSubject = new Subject();
        newSubject.setId(11L);
        newSubject.setProfile(true);
        when(subjectRepository.findById(11L)).thenReturn(Optional.of(newSubject));
        when(teacherRepository.findById(20L)).thenReturn(Optional.of(teacher));
        when(classRepository.save(existing)).thenReturn(existing);
        when(classMapper.toDTO(existing)).thenReturn(updateDTO);

        ClassDTO result = classService.update(1L, updateDTO);
        assertEquals(2, result.getNumber());
        assertEquals("B", result.getLetter());
        verify(classRepository).findById(1L);
        verify(subjectRepository).findById(11L);
        verify(teacherRepository).findById(20L);
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
        ClassEntity classEntity = new ClassEntity();
        classEntity.setId(1L);
        classEntity.setStudents(new ArrayList<>());
        when(classRepository.findById(1L)).thenReturn(Optional.of(classEntity));

        StudentDTO studentDTO = new StudentDTO();
        Student student = new Student();
        student.setId(100L);
        student.setOrderNumber(0);
        when(studentMapper.toEntity(studentDTO)).thenReturn(student);
        when(studentRepository.save(student)).thenReturn(student);
        when(studentMapper.toDTO(student)).thenReturn(studentDTO);

        StudentDTO result = classService.addStudentToClass(1L, studentDTO);
        assertNotNull(result);
        verify(classRepository).findById(1L);
        verify(studentMapper).toEntity(studentDTO);
        verify(studentRepository).save(student);
        verify(studentMapper).toDTO(student);
    }

    @Test
    public void testRemoveStudentFromClass() {
        ClassEntity classEntity = new ClassEntity();
        classEntity.setId(1L);
        Student student = new Student();
        student.setId(100L);
        student.setClassEntity(classEntity);

        when(classRepository.findById(1L)).thenReturn(Optional.of(classEntity));
        when(studentRepository.findById(100L)).thenReturn(Optional.of(student));

        classService.removeStudentFromClass(1L, 100L);
        verify(classRepository).findById(1L);
        verify(studentRepository).findById(100L);
        verify(studentRepository).delete(student);
    }

    @Test
    public void testRemoveStudentFromClassInvalid() {
        ClassEntity classEntity = new ClassEntity();
        classEntity.setId(1L);
        ClassEntity anotherClass = new ClassEntity();
        anotherClass.setId(2L);
        Student student = new Student();
        student.setId(100L);
        student.setClassEntity(anotherClass);

        when(classRepository.findById(1L)).thenReturn(Optional.of(classEntity));
        when(studentRepository.findById(100L)).thenReturn(Optional.of(student));

        assertThrows(InvalidOperationException.class, () -> classService.removeStudentFromClass(1L, 100L));
        verify(classRepository).findById(1L);
        verify(studentRepository).findById(100L);
    }
}
