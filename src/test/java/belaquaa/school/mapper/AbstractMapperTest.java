package belaquaa.school.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.mapstruct.factory.Mappers;

public abstract class AbstractMapperTest {

    protected SubjectMapper subjectMapper;
    protected TeacherMapper teacherMapper;
    protected StudentMapper studentMapper;
    protected ClassMapper classMapper;

    @BeforeEach
    public void initMappers() {
        subjectMapper = Mappers.getMapper(SubjectMapper.class);
        teacherMapper = Mappers.getMapper(TeacherMapper.class);
        studentMapper = Mappers.getMapper(StudentMapper.class);
        classMapper = Mappers.getMapper(ClassMapper.class);
    }
}
