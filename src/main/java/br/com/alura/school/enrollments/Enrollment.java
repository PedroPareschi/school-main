package br.com.alura.school.enrollments;

import br.com.alura.school.course.Course;
import br.com.alura.school.user.User;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
public class Enrollment {

    @EmbeddedId
    private EnrollmentPK id;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    private Timestamp dateTime;

    public Enrollment() {
    }

    public Enrollment(EnrollmentPK id, User student, Course course, Timestamp dateTime) {
        this.id = id;
        this.student = student;
        this.course = course;
        this.dateTime = dateTime;
    }

    public EnrollmentPK getId() {
        return id;
    }

    public User getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public Date getDateTime() {
        return dateTime;
    }
}
