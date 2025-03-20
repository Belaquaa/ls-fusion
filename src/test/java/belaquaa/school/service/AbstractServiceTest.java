package belaquaa.school.service;

import belaquaa.school.mapper.ClassMapper;
import belaquaa.school.mapper.StudentMapper;
import belaquaa.school.mapper.SubjectMapper;
import belaquaa.school.mapper.TeacherMapper;
import belaquaa.school.repository.ClassRepository;
import belaquaa.school.repository.StudentRepository;
import belaquaa.school.repository.SubjectRepository;
import belaquaa.school.repository.TeacherRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class AbstractServiceTest {
    @Mock
    protected ClassRepository classRepository;
    @Mock
    protected SubjectRepository subjectRepository;
    @Mock
    protected TeacherRepository teacherRepository;
    @Mock
    protected StudentRepository studentRepository;
    @Mock
    protected ClassMapper classMapper;
    @Mock
    protected StudentMapper studentMapper;
    @Mock
    protected SubjectMapper subjectMapper;
    @Mock
    protected TeacherMapper teacherMapper;
}
