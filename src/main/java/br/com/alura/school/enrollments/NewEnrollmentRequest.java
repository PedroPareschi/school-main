package br.com.alura.school.enrollments;


import br.com.alura.school.course.Course;
import br.com.alura.school.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;

public class NewEnrollmentRequest {

    @NotBlank
    @JsonProperty
    private String username;

    public NewEnrollmentRequest() {
    }

    public NewEnrollmentRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    static Enrollment toEntity(User student, Course course) {
        return new Enrollment(new EnrollmentPK(student.getId(), course.getId()), student, course, Timestamp.from(Instant.now()));
    }
}
