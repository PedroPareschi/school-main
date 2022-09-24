package br.com.alura.school.section;


import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import br.com.alura.school.user.UserRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SectionControllerTest {

    private final ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void should_add_new_section() throws Exception {
        User user = new User("alex", "alex@email.com");
        user.setRole(UserRole.INSTRUCTOR);
        userRepository.save(user);
        courseRepository.save(new Course("java-1", "Java OO", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism."));

        NewSectionRequest newSectionRequest = new NewSectionRequest("flutter-cores-dinamicas", "Flutter: Configurando cores dinâmicas", "alex");

        mockMvc.perform(post("/courses/java-1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newSectionRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/courses/java-1/sections/flutter-cores-dinamicas"));
    }


    @ParameterizedTest
    @CsvSource({
            ",Flutter: Configurando cores dinâmicas,alex",
            "flutter-cores-dinamicas,,alex",
            "flutter-cores-dinamicas,Flutter: Configurando cores dinâmicas,",
            "flutter-cores-dinamicas,test,alex",

    })
    void should_validate_bad_section_requests(String code, String title, String authorUsername) throws Exception {
        if (!courseRepository.existsById(1L)) {
            courseRepository.save(new Course("java-1", "Java OO", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism."));
        }
        if (!userRepository.existsById(1L)) {
            User user = new User("alex", "alex@email.com");
            user.setRole(UserRole.INSTRUCTOR);
            userRepository.save(user);
        }
        NewSectionRequest newSection = new NewSectionRequest(code, title, authorUsername);
        mockMvc.perform(post("/courses/java-1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newSection)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_allow_duplication_of_code() throws Exception {
        User user = new User("alex", "alex@email.com");
        user.setRole(UserRole.INSTRUCTOR);
        userRepository.save(user);
        Course course = new Course("java-1", "Java OO", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism.");
        courseRepository.save(course);

        sectionRepository.save(new Section("flutter-cores-dinamicas", "Flutter: Configurando cores dinâmicas", course, null));

        NewSectionRequest newSectionRequest = new NewSectionRequest("flutter-cores-dinamicas", "Flutter: Configurando cores dinâmicas", "alex");

        mockMvc.perform(post("/courses/java-1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newSectionRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_if_course_does_not_exist() throws Exception {
        User user = new User("alex", "alex@email.com");
        user.setRole(UserRole.INSTRUCTOR);
        userRepository.save(user);

        NewSectionRequest newSectionRequest = new NewSectionRequest("flutter-cores-dinamicas", "Flutter: Configurando cores dinâmicas", "alex");

        mockMvc.perform(post("/courses/java-1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newSectionRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void should_not_add_if_username_does_not_exist() throws Exception {
        Course course = new Course("java-1", "Java OO", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism.");
        courseRepository.save(course);

        NewSectionRequest newSectionRequest = new NewSectionRequest("flutter-cores-dinamicas", "Flutter: Configurando cores dinâmicas", "alex");

        mockMvc.perform(post("/courses/java-1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newSectionRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void should_not_allow_non_instructor() throws Exception {
        User user = new User("alex", "alex@email.com");
        userRepository.save(user);
        Course course = new Course("java-1", "Java OO", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism.");
        courseRepository.save(course);

        NewSectionRequest newSectionRequest = new NewSectionRequest("flutter-cores-dinamicas", "Flutter: Configurando cores dinâmicas", "alex");

        mockMvc.perform(post("/courses/java-1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(newSectionRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_add_new_video() throws Exception {
        User user = new User("alex", "alex@email.com");
        user.setRole(UserRole.INSTRUCTOR);
        userRepository.save(user);
        Course course = new Course("java-1", "Java OO", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism.");
        courseRepository.save(course);

        sectionRepository.save(new Section("flutter-cores-dinamicas", "Flutter: Configurando cores dinâmicas", course, user));

        NewVideoRequest videoRequest = new NewVideoRequest("https://www.youtube.com/watch?v=gI4-vj0WpKM");

        mockMvc.perform(post("/courses/java-1/sections/flutter-cores-dinamicas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(videoRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void should_validate_bad_video_request() throws Exception {
        User user = new User("alex", "alex@email.com");
        user.setRole(UserRole.INSTRUCTOR);
        userRepository.save(user);
        Course course = new Course("java-1", "Java OO", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism.");
        courseRepository.save(course);

        sectionRepository.save(new Section("flutter-cores-dinamicas", "Flutter: Configurando cores dinâmicas", course, user));

        NewVideoRequest videoRequest = new NewVideoRequest("");

        mockMvc.perform(post("/courses/java-1/sections/flutter-cores-dinamicas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(videoRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_duplicate_video() throws Exception {
        User user = new User("alex", "alex@email.com");
        user.setRole(UserRole.INSTRUCTOR);
        userRepository.save(user);
        Course course = new Course("java-1", "Java OO", "Java and Object Orientation: Encapsulation, Inheritance and Polymorphism.");
        courseRepository.save(course);
        Section section = new Section("flutter-cores-dinamicas", "Flutter: Configurando cores dinâmicas", course, user);
        section.addVideo("https://www.youtube.com/watch?v=gI4-vj0WpKM");
        sectionRepository.save(section);

        NewVideoRequest videoRequest = new NewVideoRequest("https://www.youtube.com/watch?v=gI4-vj0WpKM");

        mockMvc.perform(post("/courses/java-1/sections/flutter-cores-dinamicas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonMapper.writeValueAsString(videoRequest)))
                .andExpect(status().isBadRequest());
    }

}


