package belaquaa.school.controller;

import belaquaa.school.dto.TeacherDto;
import belaquaa.school.service.TeacherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/api/teachers")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;

    @GetMapping
    public List<TeacherDto> getAllTeachers(@RequestParam(value = "isClassTeacher", required = false) Boolean isClassTeacher) {
        return teacherService.getTeachersByIsClassTeacher(isClassTeacher);
    }

    @GetMapping("/{id}")
    public TeacherDto getTeacherById(@PathVariable Long id) {
        return teacherService.getById(id);
    }

    @PostMapping
    public TeacherDto createTeacher(@Valid @RequestBody TeacherDto teacherDTO) {
        return teacherService.create(teacherDTO);
    }

    @PutMapping("/{id}")
    public TeacherDto updateTeacher(@PathVariable Long id, @Valid @RequestBody TeacherDto teacherDTO) {
        return teacherService.update(id, teacherDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteTeacher(@PathVariable Long id) {
        teacherService.delete(id);
    }
}
