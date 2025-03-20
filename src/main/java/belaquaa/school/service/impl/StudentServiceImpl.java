package belaquaa.school.service.impl;

import belaquaa.school.dto.StudentDTO;
import belaquaa.school.exception.ResourceNotFoundException;
import belaquaa.school.mapper.StudentMapper;
import belaquaa.school.model.ClassEntity;
import belaquaa.school.model.Student;
import belaquaa.school.repository.ClassRepository;
import belaquaa.school.repository.StudentRepository;
import belaquaa.school.service.StudentService;
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
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;
    private final StudentMapper studentMapper;

    @Override
    @Cacheable(cacheNames = "students")
    public List<StudentDTO> getAll() {
        return studentRepository.findAll().stream()
                .map(studentMapper::toDTO)
                .toList();
    }

    @Override
    @Cacheable(cacheNames = "students", key = "#id")
    public StudentDTO getById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ученик не найден"));
        return studentMapper.toDTO(student);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "students", allEntries = true)
    public StudentDTO create(@Valid StudentDTO studentDTO) {
        Student student = studentMapper.toEntity(studentDTO);
        if (studentDTO.getClassId() != null) {
            ClassEntity classEntity = classRepository.findById(studentDTO.getClassId())
                    .orElseThrow(() -> new ResourceNotFoundException("Класс не найден"));
            student.setClassEntity(classEntity);
        }
        student = studentRepository.save(student);
        log.info("Создан ученик с id {}", student.getId());
        return studentMapper.toDTO(student);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "students", allEntries = true)
    public StudentDTO update(Long id, @Valid StudentDTO studentDTO) {
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ученик не найден"));
        existing.setFullName(studentDTO.getFullName());
        existing.setOrderNumber(studentDTO.getOrderNumber());
        if (studentDTO.getClassId() != null) {
            ClassEntity classEntity = classRepository.findById(studentDTO.getClassId())
                    .orElseThrow(() -> new ResourceNotFoundException("Класс не найден"));
            existing.setClassEntity(classEntity);
        }
        existing = studentRepository.save(existing);
        log.info("Обновлен ученик с id {}", id);
        return studentMapper.toDTO(existing);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "students", allEntries = true)
    public void delete(Long id) {
        studentRepository.deleteById(id);
        log.info("Удален ученик с id {}", id);
    }
}
