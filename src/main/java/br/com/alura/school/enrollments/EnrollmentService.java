package br.com.alura.school.enrollments;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class EnrollmentService {

    private final UserRepository userRepository;

    private final CourseRepository courseRepository;

    private final EnrollmentRepository enrollmentRepository;

    public EnrollmentService(UserRepository userRepository, CourseRepository courseRepository, EnrollmentRepository enrollmentRepository) {
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public URI addNewEnrollment(String courseCode, NewEnrollmentRequest newEnrollmentRequest) {
        User user = userRepository.findByUsername(newEnrollmentRequest.getUsername()).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Section with code %s not found", courseCode)));
        Course course = courseRepository.findByCode(courseCode).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Section with code %s not found", courseCode)));
        Enrollment enrollment = NewEnrollmentRequest.toEntity(user, course);
        if (enrollmentRepository.existsById(new EnrollmentPK(user.getId(), course.getId())))
            throw new ResponseStatusException(BAD_REQUEST, format("Section with code %s not found", courseCode));
        enrollmentRepository.save(enrollment);
        return URI.create(format("/courses/%s/enroll/%s", courseCode, user.getUsername()));
    }
}
