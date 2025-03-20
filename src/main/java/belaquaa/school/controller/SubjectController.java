package belaquaa.school.controller;

import belaquaa.school.dto.SubjectDTO;
import belaquaa.school.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/subjects")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;

    @GetMapping
    public List<SubjectDTO> getAllSubjects() {
        return subjectService.getAll();
    }

    @GetMapping("/{id}")
    public SubjectDTO getSubjectById(@PathVariable Long id) {
        return subjectService.getById(id);
    }

    @PostMapping
    public SubjectDTO createSubject(@Valid @RequestBody SubjectDTO subjectDTO) {
        return subjectService.create(subjectDTO);
    }

    @PutMapping("/{id}")
    public SubjectDTO updateSubject(@PathVariable Long id, @Valid @RequestBody SubjectDTO subjectDTO) {
        return subjectService.update(id, subjectDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteSubject(@PathVariable Long id) {
        subjectService.delete(id);
    }
}
