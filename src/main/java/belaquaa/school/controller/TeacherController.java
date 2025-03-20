package belaquaa.school.controller;

import belaquaa.school.dto.TeacherDTO;
import belaquaa.school.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/teachers")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping
    public List<TeacherDTO> getAllTeachers(@RequestParam(value = "isClassTeacher", required = false) Boolean isClassTeacher) {
        if (isClassTeacher != null && isClassTeacher) {
            return teacherService.getClassTeachers();
        }
        return teacherService.getAll();
    }

    @GetMapping("/{id}")
    public TeacherDTO getTeacherById(@PathVariable Long id) {
        return teacherService.getById(id);
    }

    @PostMapping
    public TeacherDTO createTeacher(@Valid @RequestBody TeacherDTO teacherDTO) {
        return teacherService.create(teacherDTO);
    }

    @PutMapping("/{id}")
    public TeacherDTO updateTeacher(@PathVariable Long id, @Valid @RequestBody TeacherDTO teacherDTO) {
        return teacherService.update(id, teacherDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteTeacher(@PathVariable Long id) {
        teacherService.delete(id);
    }
}
