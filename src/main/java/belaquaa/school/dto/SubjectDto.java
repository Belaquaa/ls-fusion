package belaquaa.school.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SubjectDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank(message = "Наименование предмета обязательно")
    @Size(max = 25, message = "Наименование не должно превышать 25 символов")
    private String name;

    private boolean isProfile;
}
