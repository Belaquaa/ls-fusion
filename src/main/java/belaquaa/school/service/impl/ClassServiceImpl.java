package belaquaa.school.service.impl;

import belaquaa.school.dto.ClassDTO;
import belaquaa.school.dto.StudentDTO;
import belaquaa.school.exception.InvalidOperationException;
import belaquaa.school.exception.ResourceNotFoundException;
import belaquaa.school.mapper.ClassMapper;
import belaquaa.school.mapper.StudentMapper;
import belaquaa.school.model.ClassEntity;
import belaquaa.school.model.Student;
import belaquaa.school.model.Subject;
import belaquaa.school.model.Teacher;
import belaquaa.school.repository.ClassRepository;
import belaquaa.school.repository.StudentRepository;
import belaquaa.school.repository.SubjectRepository;
import belaquaa.school.repository.TeacherRepository;
import belaquaa.school.service.ClassService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassServiceImpl implements ClassService {
    private final ClassRepository classRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final ClassMapper classMapper;
    private final StudentMapper studentMapper;

    @Override
    @Cacheable(cacheNames = "classes")
    public List<ClassDTO> getAll() {
        return classRepository.findAll().stream()
                .map(classMapper::toDTO)
                .toList();
    }

    @Override
    @Cacheable(cacheNames = "classes", key = "#id")
    public ClassDTO getById(Long id) {
        ClassEntity classEntity = classRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Класс не найден"));
        return classMapper.toDTO(classEntity);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "classes", allEntries = true)
    public ClassDTO create(@Valid ClassDTO classDTO) {
        ClassEntity classEntity = classMapper.toEntity(classDTO);
        if (classEntity.getProfileSubject() != null) {
            Subject subj = subjectRepository.findById(classEntity.getProfileSubject().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Профильный предмет не найден"));
            if (!subj.isProfile()) {
                throw new InvalidOperationException("Предмет " + subj.getName() + " не может быть профильным");
            }
            classEntity.setProfileSubject(subj);
        }
        classEntity = processClassTeacher(classEntity, classDTO);
        classEntity = classRepository.save(classEntity);
        log.info("Создан класс с id {}", classEntity.getId());
        return classMapper.toDTO(classEntity);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "classes", allEntries = true)
    public ClassDTO update(Long id, @Valid ClassDTO classDTO) {
        ClassEntity existing = classRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Класс не найден"));
        existing.setNumber(classDTO.getNumber());
        existing.setLetter(classDTO.getLetter());
        if (classDTO.getProfileSubject() != null) {
            Subject subj = subjectRepository.findById(classDTO.getProfileSubject().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Предмет не найден"));
            if (!subj.isProfile()) {
                throw new InvalidOperationException("Предмет " + subj.getName() + " не может быть профильным");
            }
            existing.setProfileSubject(subj);
        }
        existing = processClassTeacher(existing, classDTO);
        existing = classRepository.save(existing);
        log.info("Обновлен класс с id {}", id);
        return classMapper.toDTO(existing);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "classes", allEntries = true)
    public void delete(Long id) {
        classRepository.deleteById(id);
        log.info("Удален класс с id {}", id);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "classes", allEntries = true)
    public StudentDTO addStudentToClass(Long classId, @Valid StudentDTO studentDTO) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Класс не найден"));
        Student student = studentMapper.toEntity(studentDTO);
        student.setClassEntity(classEntity);
        if (student.getOrderNumber() == 0) {
            student.setOrderNumber(classEntity.getStudents().size() + 1);
        }
        student = studentRepository.save(student);
        log.info("Добавлен ученик с id {} в класс id {}", student.getId(), classId);
        return studentMapper.toDTO(student);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "classes", allEntries = true)
    public void removeStudentFromClass(Long classId, Long studentId) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Класс не найден"));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Ученик не найден"));
        if (!student.getClassEntity().getId().equals(classEntity.getId())) {
            throw new InvalidOperationException("Ученик не принадлежит классу id=" + classId);
        }
        studentRepository.delete(student);
        log.info("Удален ученик с id {} из класса id {}", studentId, classId);
    }

    private ClassEntity processClassTeacher(ClassEntity classEntity, ClassDTO dto) {
        if (dto.getClassTeacher() != null) {
            Teacher teacher = teacherRepository.findById(dto.getClassTeacher().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Учитель не найден"));
            if (!teacher.isClassTeacher()) {
                throw new InvalidOperationException("Учитель " + teacher.getFullName() + " не является классным руководителем");
            }
            classEntity.setClassTeacher(teacher);
        }
        return classEntity;
    }
}
