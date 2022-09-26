package br.com.alura.school.course;

import br.com.alura.school.section.SectionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
class CourseController {

    private final CourseService courseService;

    CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/courses")
    ResponseEntity<List<CourseResponse>> allCourses() {
        List<CourseResponse> responses = courseService.getAllCourses();
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/courses/{code}")
    ResponseEntity<CourseResponse> courseByCode(@PathVariable("code") String code) {
        Course course;
        try {
            course = courseService.getCourseByCode(code);
        } catch (CourseException e) {
            throw new ResponseStatusException(e.getHttpStatus(), e.getMessage());
        }
        return ResponseEntity.ok(new CourseResponse(course));
    }

    @PostMapping("/courses")
    ResponseEntity<Void> newCourse(@RequestBody @Valid NewCourseRequest newCourseRequest) {
        URI location = courseService.addCourse(newCourseRequest);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/courses/{code}/sectionByVideosReport")
    ResponseEntity<List<CourseSectionByVideosResponse>> getSectionsByVideosReport(@PathVariable("code") String code) {
        List<CourseSectionByVideosResponse> responses = null;
        try {
            responses = courseService.getCourseSectionByVideos(code);
        } catch (CourseException | SectionException e) {
            throw new ResponseStatusException(e.getHttpStatus(), e.getMessage());
        }
        return ResponseEntity.ok().body(responses);
    }
}


