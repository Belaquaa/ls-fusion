package belaquaa.school.service.impl;

import belaquaa.school.dto.ClassDto;
import belaquaa.school.dto.StudentDto;
import belaquaa.school.dto.SubjectDto;
import belaquaa.school.dto.TeacherDto;
import belaquaa.school.exception.InvalidOperationException;
import belaquaa.school.exception.ResourceNotFoundException;
import belaquaa.school.mapper.ClassMapper;
import belaquaa.school.mapper.StudentMapper;
import belaquaa.school.mapper.SubjectMapper;
import belaquaa.school.mapper.TeacherMapper;
import belaquaa.school.model.ClassEntity;
import belaquaa.school.model.Student;
import belaquaa.school.model.Subject;
import belaquaa.school.model.Teacher;
import belaquaa.school.repository.ClassRepository;
import belaquaa.school.repository.StudentRepository;
import belaquaa.school.service.ClassService;
import belaquaa.school.service.SubjectService;
import belaquaa.school.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassServiceImpl implements ClassService {
    private final ClassRepository classRepository;
    private final StudentRepository studentRepository;
    private final ClassMapper classMapper;
    private final StudentMapper studentMapper;
    private final SubjectService subjectService;
    private final TeacherService teacherService;
    private final SubjectMapper subjectMapper;
    private final TeacherMapper teacherMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ClassDto> getAll() {
        return classRepository.findAll().stream()
                .map(classMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClassDto getById(Long id) {
        ClassEntity classEntity = classRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Класс не найден"));
        return classMapper.toDTO(classEntity);
    }

    @Override
    @Transactional
    public ClassDto create(ClassDto classDTO) {
        ClassEntity classEntity = classMapper.toEntity(classDTO);
        setProfileSubject(classDTO, classEntity);
        setClassTeacher(classDTO, classEntity);
        classEntity = classRepository.save(classEntity);
        log.info("Создан класс с id {}", classEntity.getId());
        return classMapper.toDTO(classEntity);
    }

    @Override
    @Transactional
    public ClassDto update(Long id, ClassDto classDTO) {
        ClassEntity existing = classRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Класс не найден"));
        existing.setNumber(classDTO.getNumber());
        existing.setLetter(classDTO.getLetter());
        setProfileSubject(classDTO, existing);
        setClassTeacher(classDTO, existing);
        existing = classRepository.save(existing);
        log.info("Обновлен класс с id {}", id);
        return classMapper.toDTO(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        classRepository.deleteById(id);
        log.info("Удален класс с id {}", id);
    }

    @Override
    @Transactional
    public StudentDto addStudentToClass(Long classId, StudentDto studentDTO) {
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
    public void removeStudentFromClass(Long classId, Long studentId) {
        ClassEntity classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new ResourceNotFoundException("Класс не найден"));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Ученик не найден"));
        if (student.getClassEntity() == null || !student.getClassEntity().getId().equals(classEntity.getId())) {
            throw new InvalidOperationException("Ученик не принадлежит классу id=" + classId);
        }
        studentRepository.delete(student);
        log.info("Удален ученик с id {} из класса id {}", studentId, classId);
    }

    private void setProfileSubject(ClassDto classDTO, ClassEntity classEntity) {
        if (classDTO.getProfileSubjectId() != null) {
            SubjectDto subjDTO = subjectService.getById(classDTO.getProfileSubjectId());
            Subject subject = subjectMapper.toEntity(subjDTO);
            if (!subject.isProfile()) {
                throw new InvalidOperationException("Предмет " + subject.getName() + " не может быть профильным");
            }
            classEntity.setProfileSubject(subject);
        } else {
            classEntity.setProfileSubject(null);
        }
    }

    private void setClassTeacher(ClassDto classDTO, ClassEntity classEntity) {
        if (classDTO.getClassTeacherId() != null) {
            TeacherDto teacherDTO = teacherService.getById(classDTO.getClassTeacherId());
            Teacher teacher = teacherMapper.toEntity(teacherDTO);
            if (!teacher.isClassTeacher()) {
                throw new InvalidOperationException("Учитель " + teacher.getFullName() + " не является классным руководителем");
            }
            classEntity.setClassTeacher(teacher);
        } else {
            classEntity.setClassTeacher(null);
        }
    }
}
