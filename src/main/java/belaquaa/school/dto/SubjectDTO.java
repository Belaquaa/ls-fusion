package belaquaa.school.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SubjectDTO {
    private Long id;

    @NotBlank(message = "Наименование предмета обязательно")
    @Size(max = 25, message = "Наименование не должно превышать 25 символов")
    private String name;

    private boolean isProfile;
}
