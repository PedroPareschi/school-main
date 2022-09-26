package br.com.alura.school.enrollments;

import br.com.alura.school.course.CourseException;
import br.com.alura.school.user.UserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;

@RestController
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/courses/{courseCode}/enroll")
    ResponseEntity<Void> newEnrollment(@PathVariable("courseCode") String courseCode, @RequestBody @Valid NewEnrollmentRequest newEnrollmentRequest) {
        URI location;
        try {
            location = enrollmentService.addNewEnrollment(courseCode, newEnrollmentRequest);
        } catch (EnrollmentException | CourseException | UserException e) {
            throw new ResponseStatusException(e.getHttpStatus(), e.getMessage());
        }
        return ResponseEntity.created(location).build();
    }
}
