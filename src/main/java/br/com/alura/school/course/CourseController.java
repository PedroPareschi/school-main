package br.com.alura.school.course;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        Course course = courseService.getCourseByCode(code);
        return ResponseEntity.ok(new CourseResponse(course));
    }

    @PostMapping("/courses")
    ResponseEntity<Void> newCourse(@RequestBody @Valid NewCourseRequest newCourseRequest) {
        URI location = courseService.addCourse(newCourseRequest);
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/courses/{code}/sectionByVideosReport")
    ResponseEntity<List<CourseSectionByVideosResponse>> getSectionsByVideosReport(@PathVariable("code") String code) {
        List<CourseSectionByVideosResponse> responses = courseService.getCourseSectionByVideos(code);
        return ResponseEntity.ok().body(responses);
    }
}


