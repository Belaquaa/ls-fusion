package belaquaa.school.controller;

import belaquaa.school.dto.ClassDto;
import belaquaa.school.dto.StudentDto;
import belaquaa.school.service.ClassService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/api/classes")
@RequiredArgsConstructor
public class ClassController {
    private final ClassService classService;

    @GetMapping
    public List<ClassDto> getAllClasses() {
        return classService.getAll();
    }

    @GetMapping("/{id}")
    public ClassDto getClassById(@PathVariable Long id) {
        return classService.getById(id);
    }

    @PostMapping
    public ClassDto createClass(@Valid @RequestBody ClassDto classDTO) {
        return classService.create(classDTO);
    }

    @PutMapping("/{id}")
    public ClassDto updateClass(@PathVariable Long id, @Valid @RequestBody ClassDto classDTO) {
        return classService.update(id, classDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteClass(@PathVariable Long id) {
        classService.delete(id);
    }

    @PostMapping("/{id}/students")
    public StudentDto addStudent(@PathVariable Long id, @Valid @RequestBody StudentDto studentDTO) {
        return classService.addStudentToClass(id, studentDTO);
    }

    @DeleteMapping("/{id}/students/{studentId}")
    public void removeStudent(@PathVariable Long id, @PathVariable Long studentId) {
        classService.removeStudentFromClass(id, studentId);
    }
}
