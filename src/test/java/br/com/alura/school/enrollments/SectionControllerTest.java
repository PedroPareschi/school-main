package br.com.alura.school.enrollments;


import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.Instant;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SectionControllerTest {

    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_add_new_section() throws Exception {
        User user = new User("alex", "alex@email.com");
        userRepository.save(user);
        courseRepository.save(new Course("java-1", "Java OO", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism."));

        NewEnrollmentRequest enrollmentRequest = new NewEnrollmentRequest("alex");

        mockMvc.perform(post("/courses/java-1/enroll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(enrollmentRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/courses/java-1/enroll/alex"));
    }

    @Test
    void should_not_add_no_username() throws Exception {
        User user = new User("alex", "alex@email.com");
        userRepository.save(user);
        courseRepository.save(new Course("java-1", "Java OO", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism."));

        NewEnrollmentRequest enrollmentRequest = new NewEnrollmentRequest("");

        mockMvc.perform(post("/courses/java-1/enroll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(enrollmentRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_acept_unexistent_course() throws Exception {
        User user = new User("alex", "alex@email.com");
        userRepository.save(user);

        NewEnrollmentRequest enrollmentRequest = new NewEnrollmentRequest("alex");

        mockMvc.perform(post("/courses/java-1/enroll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(enrollmentRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void should_not_accept_duplicate_user() throws Exception {
        User user = new User("alex", "alex@email.com");
        userRepository.save(user);
        Course course = new Course("java-1", "Java OO", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism.");
        courseRepository.save(course);

        enrollmentRepository.save(new Enrollment(new EnrollmentPK(1L, 1L), user, course, Timestamp.from(Instant.now())));

        NewEnrollmentRequest enrollmentRequest = new NewEnrollmentRequest("alex");

        mockMvc.perform(post("/courses/java-1/enroll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(enrollmentRequest)))
                .andExpect(status().isBadRequest());
    }
}


