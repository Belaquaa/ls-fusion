package belaquaa.school.dto;

import lombok.Data;

import java.util.List;

@Data
public class ClassDTO {
    private Long id;

    private Integer number;

    private String letter;

    private SubjectDTO profileSubject;

    private TeacherDTO classTeacher;

    private List<StudentDTO> students;

    private int studentsCount;
}
