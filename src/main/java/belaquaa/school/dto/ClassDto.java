package belaquaa.school.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ClassDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    private Integer number;

    private String letter;

    private Long profileSubjectId;

    private Long classTeacherId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Long> studentIds;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int studentsCount;
}
