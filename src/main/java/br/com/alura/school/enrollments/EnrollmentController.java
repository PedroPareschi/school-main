package br.com.alura.school.enrollments;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import java.net.URI;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
public class EnrollmentController {

    private final UserRepository userRepository;

    private final CourseRepository courseRepository;

    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentController(UserRepository userRepository, CourseRepository courseRepository, EnrollmentRepository enrollmentRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @PostMapping("/courses/{courseCode}/enroll")
    ResponseEntity<Void> newEnrollment(@PathVariable("courseCode") String courseCode, @RequestBody @Valid NewEnrollmentRequest newEnrollmentRequest) {
        User user = userRepository.findByUsername(newEnrollmentRequest.getUsername()).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Section with code %s not found", courseCode)));
        Course course = courseRepository.findByCode(courseCode).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Section with code %s not found", courseCode)));
        Enrollment enrollment = NewEnrollmentRequest.toEntity(user, course);
        if (enrollmentRepository.existsById(new EnrollmentPK(user.getId(), course.getId()))) throw new ResponseStatusException(BAD_REQUEST, format("Section with code %s not found", courseCode));
        enrollmentRepository.save(enrollment);
        URI location = URI.create(format("/courses/%s/enroll/%s", courseCode, user.getUsername()));
        return ResponseEntity.created(location).build();
    }
}
