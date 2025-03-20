package belaquaa.school.service.impl;

import belaquaa.school.dto.TeacherDto;
import belaquaa.school.exception.ResourceNotFoundException;
import belaquaa.school.mapper.SubjectMapper;
import belaquaa.school.mapper.TeacherMapper;
import belaquaa.school.model.Subject;
import belaquaa.school.model.Teacher;
import belaquaa.school.repository.TeacherRepository;
import belaquaa.school.service.SubjectService;
import belaquaa.school.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;
    private final SubjectService subjectService;
    private final TeacherMapper teacherMapper;
    private final SubjectMapper subjectMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TeacherDto> getAll() {
        return teacherRepository.findAll().stream()
                .map(teacherMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "teachers", key = "#id")
    public TeacherDto getById(Long id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Учитель не найден"));
        return teacherMapper.toDTO(teacher);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherDto> getTeachersByIsClassTeacher(Boolean isClassTeacher) {
        List<Teacher> teachers;
        if (isClassTeacher == null) {
            teachers = teacherRepository.findAll();
        } else {
            teachers = teacherRepository.findByIsClassTeacher(isClassTeacher);
        }
        return teachers.stream()
                .map(teacherMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    @CachePut(cacheNames = "teachers", key = "#result.id")
    public TeacherDto create(TeacherDto teacherDTO) {
        Teacher teacher = teacherMapper.toEntity(teacherDTO);
        teacher.setSubjects(loadSubjects(teacherDTO.getSubjectIds()));
        teacher = teacherRepository.save(teacher);
        log.info("Создан учитель с id {}", teacher.getId());
        return teacherMapper.toDTO(teacher);
    }

    @Override
    @Transactional
    @CachePut(cacheNames = "teachers", key = "#result.id")
    public TeacherDto update(Long id, TeacherDto teacherDTO) {
        Teacher existing = teacherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Учитель не найден"));
        existing.setFullName(teacherDTO.getFullName());
        existing.setClassTeacher(teacherDTO.isClassTeacher());
        existing.setSubjects(loadSubjects(teacherDTO.getSubjectIds()));
        existing = teacherRepository.save(existing);
        log.info("Обновлен учитель с id {}", id);
        return teacherMapper.toDTO(existing);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "teachers", key = "#id")
    public void delete(Long id) {
        teacherRepository.deleteById(id);
        log.info("Удален учитель с id {}", id);
    }

    private Set<Subject> loadSubjects(Set<Long> subjectIds) {
        if (subjectIds == null || subjectIds.isEmpty()) {
            return new HashSet<>();
        }
        return subjectService.getAll().stream()
                .filter(subjectDto -> subjectIds.contains(subjectDto.getId()))
                .map(subjectMapper::toEntity)
                .collect(Collectors.toSet());
    }
}
