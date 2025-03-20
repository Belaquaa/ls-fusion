package belaquaa.school.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class TeacherDTO {
    private Long id;

    @NotBlank(message = "ФИО учителя обязательно")
    @Size(max = 50, message = "ФИО не должно превышать 50 символов")
    private String fullName;

    private boolean isClassTeacher;

    private Set<SubjectDTO> subjects;
}
