package belaquaa.school.controller;

import belaquaa.school.dto.ClassDTO;
import belaquaa.school.dto.StudentDTO;
import belaquaa.school.service.ClassService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/classes")
@RequiredArgsConstructor
public class ClassController {
    private final ClassService classService;

    @GetMapping
    public List<ClassDTO> getAllClasses() {
        return classService.getAll();
    }

    @GetMapping("/{id}")
    public ClassDTO getClassById(@PathVariable Long id) {
        return classService.getById(id);
    }

    @PostMapping
    public ClassDTO createClass(@Valid @RequestBody ClassDTO classDTO) {
        return classService.create(classDTO);
    }

    @PutMapping("/{id}")
    public ClassDTO updateClass(@PathVariable Long id, @Valid @RequestBody ClassDTO classDTO) {
        return classService.update(id, classDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteClass(@PathVariable Long id) {
        classService.delete(id);
    }

    @PostMapping("/{id}/students")
    public StudentDTO addStudent(@PathVariable Long id, @Valid @RequestBody StudentDTO studentDTO) {
        return classService.addStudentToClass(id, studentDTO);
    }

    @DeleteMapping("/{id}/students/{studentId}")
    public void removeStudent(@PathVariable Long id, @PathVariable Long studentId) {
        classService.removeStudentFromClass(id, studentId);
    }
}
