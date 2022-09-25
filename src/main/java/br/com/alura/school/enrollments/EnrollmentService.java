package br.com.alura.school.enrollments;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseService;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class EnrollmentService {

    @Autowired
    private  UserService userService;

    @Autowired
    private  CourseService courseService;

    @Autowired
    private  EnrollmentRepository enrollmentRepository;


    public URI addNewEnrollment(String courseCode, NewEnrollmentRequest newEnrollmentRequest) {
        User user = userService.findByUsername(newEnrollmentRequest.getUsername());
        Course course = courseService.getCourseByCode(courseCode);
        Enrollment enrollment = NewEnrollmentRequest.toEntity(user, course);
        if (enrollmentRepository.existsById(new EnrollmentPK(user.getId(), course.getId())))
            throw new ResponseStatusException(BAD_REQUEST, format("Section with code %s not found", courseCode));
        enrollmentRepository.save(enrollment);
        return URI.create(format("/courses/%s/enroll/%s", courseCode, user.getUsername()));
    }
}
