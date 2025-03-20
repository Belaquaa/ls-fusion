package belaquaa.school.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class TeacherDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "ФИО учителя обязательно")
    @Size(max = 50, message = "ФИО не должно превышать 50 символов")
    private String fullName;

    private boolean isClassTeacher;

    private Set<Long> subjectIds;
}