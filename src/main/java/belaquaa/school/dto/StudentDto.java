package belaquaa.school.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StudentDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private Long classId;

    private int orderNumber;

    @NotBlank(message = "ФИО ученика обязательно")
    @Size(max = 50, message = "ФИО ученика не должно превышать 50 символов")
    private String fullName;
}
