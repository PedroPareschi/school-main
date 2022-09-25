package br.com.alura.school.course;

import br.com.alura.school.enrollments.Enrollment;
import br.com.alura.school.enrollments.EnrollmentRepository;
import br.com.alura.school.enrollments.NewEnrollmentRequest;
import br.com.alura.school.section.Section;
import br.com.alura.school.section.SectionRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import br.com.alura.school.user.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CourseControllerTest {

    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Test
    void should_retrieve_course_by_code() throws Exception {
        Course course = new Course("java-1", "Java OO", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism.");
        User user = new User("pedro", "pedro@email.com");
        user.setRole(UserRole.INSTRUCTOR);
        userRepository.save(user);
        courseRepository.save(course);

        Section section1 = new Section("primeira aula", "primeira aula", course, user);
        Section section2 = new Section("segunda aula", "segunda aula", course, user);
        section2.addVideo("primeiro video");
        section2.addVideo("segundo video");

        sectionRepository.saveAll(List.of(section1, section2));

        mockMvc.perform(get("/courses/java-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", is("java-1")))
                .andExpect(jsonPath("$.name", is("Java OO")))
                .andExpect(jsonPath("$.shortDescription", is("Java and O...")))
                .andExpect(jsonPath("$.sections").isArray())
                .andExpect(jsonPath("$.sections[0].title", is("primeira aula")))
                .andExpect(jsonPath("$.sections[0].author", is("pedro")))
                .andExpect(jsonPath("$.sections[0].videos").isEmpty())
                .andExpect(jsonPath("$.sections[1].title", is("segunda aula")))
                .andExpect(jsonPath("$.sections[1].author", is("pedro")))
                .andExpect(jsonPath("$.sections[1].videos[0]", is("primeiro video")))
                .andExpect(jsonPath("$.sections[1].videos[1]", is("segundo video")));
    }

    @Test
    void should_retrieve_all_courses() throws Exception {
        User user = new User("suzana", "suzana@email.com");
        userRepository.save(user);
        Course course = new Course("spring-1", "Spring Basics", "Spring Core and Spring MVC.");
        Course course2 = new Course("spring-2", "Spring Boot", "Spring Boot");
        courseRepository.save(course);
        courseRepository.save(course2);

        Section section1 = new Section("primeira aula", "primeira aula", course, user);
        Section section2 = new Section("segunda aula", "segunda aula", course2, user);
        section1.addVideo("primeiro video");
        section2.addVideo("segundo video");

        sectionRepository.saveAll(List.of(section1, section2));

        mockMvc.perform(get("/courses")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].code", is("spring-1")))
                .andExpect(jsonPath("$[0].name", is("Spring Basics")))
                .andExpect(jsonPath("$[0].shortDescription", is("Spring Cor...")))
                .andExpect(jsonPath("$[0].sections[0].title", is("primeira aula")))
                .andExpect(jsonPath("$[0].sections[0].videos[0]", is("primeiro video")))
                .andExpect(jsonPath("$[1].code", is("spring-2")))
                .andExpect(jsonPath("$[1].name", is("Spring Boot")))
                .andExpect(jsonPath("$[1].shortDescription", is("Spring Boot")))
                .andExpect(jsonPath("$[1].sections[0].title", is("segunda aula")))
                .andExpect(jsonPath("$[1].sections[0].videos[0]", is("segundo video")));
    }

    @Test
    void should_add_new_course() throws Exception {
        NewCourseRequest newCourseRequest = new NewCourseRequest("java-2", "Java Collections", "Java Collections: Lists, Sets, Maps and more.");

        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newCourseRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/courses/java-2"));
    }

    @Test
    void should_get_sections_by_video_report() throws Exception {
        User user = new User("alex", "alex@email.com");
        userRepository.save(user);
        Course course = new Course("spring-3", "Spring Cloud", "Spring Cloud");
        courseRepository.save(course);
        Enrollment enrollment = NewEnrollmentRequest.toEntity(user, course);
        enrollmentRepository.save(enrollment);
        Section section1 = new Section("primeira aula", "primeira aula", course, user);
        Section section2 = new Section("segunda aula", "segunda aula", course, user);
        section2.addVideo("segundo video");
        section2.addVideo("terceiro video");
        sectionRepository.saveAll(List.of(section1, section2));

        mockMvc.perform(get("/courses/spring-3/sectionByVideosReport")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].courseName", is("Spring Cloud")))
                .andExpect(jsonPath("$[0].sectionTitle", is("segunda aula")))
                .andExpect(jsonPath("$[0].authorName", is("alex")))
                .andExpect(jsonPath("$[0].totalVideos", is(2)))
                .andExpect(jsonPath("$[1].courseName", is("Spring Cloud")))
                .andExpect(jsonPath("$[1].sectionTitle", is("primeira aula")))
                .andExpect(jsonPath("$[1].authorName", is("alex")))
                .andExpect(jsonPath("$[1].totalVideos", is(0)));
        ;

    }

}