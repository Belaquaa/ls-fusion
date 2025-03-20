package belaquaa.school.service.impl;

import belaquaa.school.dto.SubjectDto;
import belaquaa.school.exception.ResourceNotFoundException;
import belaquaa.school.mapper.SubjectMapper;
import belaquaa.school.model.Subject;
import belaquaa.school.repository.SubjectRepository;
import belaquaa.school.service.SubjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "subjectList")
    public List<SubjectDto> getAll() {
        return subjectRepository.findAll().stream()
                .map(subjectMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(cacheNames = "subjects", key = "#id")
    public SubjectDto getById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Предмет не найден"));
        return subjectMapper.toDTO(subject);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "subjectList", allEntries = true)
    @CachePut(cacheNames = "subjects", key = "#result.id")
    public SubjectDto create(SubjectDto subjectDTO) {
        Subject subject = subjectMapper.toEntity(subjectDTO);
        subject = subjectRepository.save(subject);
        log.info("Создан предмет с id {}", subject.getId());
        return subjectMapper.toDTO(subject);
    }

    @Override
    @Transactional
    @CacheEvict(cacheNames = "subjectList", allEntries = true)
    @CachePut(cacheNames = "subjects", key = "#result.id")
    public SubjectDto update(Long id, SubjectDto subjectDTO) {
        Subject existing = subjectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Предмет не найден"));
        existing.setName(subjectDTO.getName());
        existing.setProfile(subjectDTO.isProfile());
        existing = subjectRepository.save(existing);
        log.info("Обновлен предмет с id {}", id);
        return subjectMapper.toDTO(existing);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(cacheNames = "subjectList", allEntries = true),
            @CacheEvict(cacheNames = "subjects", key = "#id")
    })
    public void delete(Long id) {
        subjectRepository.deleteById(id);
        log.info("Удален предмет с id {}", id);
    }
}
