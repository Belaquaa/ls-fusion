package belaquaa.school.service.impl;

import belaquaa.school.dto.SubjectDTO;
import belaquaa.school.dto.TeacherDTO;
import belaquaa.school.exception.ResourceNotFoundException;
import belaquaa.school.mapper.TeacherMapper;
import belaquaa.school.model.Subject;
import belaquaa.school.model.Teacher;
import belaquaa.school.repository.SubjectRepository;
import belaquaa.school.repository.TeacherRepository;
import belaquaa.school.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final TeacherMapper teacherMapper;

    @Override
    @Cacheable(cacheNames = "teachers")
    public List<TeacherDTO> getAll() {
        return teacherRepository.findAll().stream()
                .map(teacherMapper::toDTO)
                .toList();
    }

    @Override
    @Cacheable(cacheNames = "teachers", key = "#id")
    public TeacherDTO getById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Учитель не найден"));
        return teacherMapper.toDTO(teacher);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "teachers", allEntries = true)
    public TeacherDTO create(@Valid TeacherDTO teacherDTO) {
        Teacher teacher = teacherMapper.toEntity(teacherDTO);
        teacher.setSubjects(loadSubjects(teacherDTO.getSubjects()));
        teacher = teacherRepository.save(teacher);
        log.info("Создан учитель с id {}", teacher.getId());
        return teacherMapper.toDTO(teacher);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "teachers", allEntries = true)
    public TeacherDTO update(Long id, @Valid TeacherDTO teacherDTO) {
        Teacher existing = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Учитель не найден"));
        existing.setFullName(teacherDTO.getFullName());
        existing.setClassTeacher(teacherDTO.isClassTeacher());
        existing.setSubjects(loadSubjects(teacherDTO.getSubjects()));
        existing = teacherRepository.save(existing);
        log.info("Обновлен учитель с id {}", id);
        return teacherMapper.toDTO(existing);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "teachers", allEntries = true)
    public void delete(Long id) {
        teacherRepository.deleteById(id);
        log.info("Удален учитель с id {}", id);
    }

    @Override
    @Cacheable(cacheNames = "teachers", key = "'classTeachers'")
    public List<TeacherDTO> getClassTeachers() {
        return teacherRepository.findByIsClassTeacherTrue().stream()
                .map(teacherMapper::toDTO)
                .toList();
    }

    private Set<Subject> loadSubjects(Set<SubjectDTO> subjectDTOs) {
        Set<Subject> subjects = new HashSet<>();
        if (subjectDTOs != null) {
            subjectDTOs.forEach(dto -> {
                Subject subject = subjectRepository.findById(dto.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Предмет не найден"));
                subjects.add(subject);
            });
        }
        return subjects;
    }
}
