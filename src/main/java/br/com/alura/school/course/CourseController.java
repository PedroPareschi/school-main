package br.com.alura.school.course;

import br.com.alura.school.section.Section;
import br.com.alura.school.section.SectionComparator;
import br.com.alura.school.section.SectionRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.*;

@RestController
class CourseController {

    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;

    CourseController(CourseRepository courseRepository, SectionRepository sectionRepository) {
        this.courseRepository = courseRepository;
        this.sectionRepository = sectionRepository;
    }

    @GetMapping("/courses")
    ResponseEntity<List<CourseResponse>> allCourses() {
        List<Course> courses = courseRepository.findAll();
        List<CourseResponse> responses = courses.stream().map(CourseResponse::new).toList();
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/courses/{code}")
    ResponseEntity<CourseResponse> courseByCode(@PathVariable("code") String code) {
        Course course = courseRepository.findByCode(code).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Course with code %s not found", code)));
        return ResponseEntity.ok(new CourseResponse(course));
    }

    @PostMapping("/courses")
    ResponseEntity<Void> newCourse(@RequestBody @Valid NewCourseRequest newCourseRequest) {
        courseRepository.save(newCourseRequest.toEntity());
        URI location = URI.create(format("/courses/%s", newCourseRequest.getCode()));
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/courses/{code}/sectionByVideosReport")
    ResponseEntity<List<CourseSectionByVideosResponse>> getSectionsByVideosReport(@PathVariable("code") String code) {
        Course course = courseRepository.findByCode(code).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Course with code %s not found", code)));
        if (course.getStudents().isEmpty())
            throw new ResponseStatusException(BAD_REQUEST, format("Course %s has no enrollments", code));
        List<Section> sections = sectionRepository.findAllByCourse(course).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Course with code %s has no classes yet", code)));
        sections.sort(new SectionComparator());
        List<CourseSectionByVideosResponse> responses = sections.stream().map(CourseSectionByVideosResponse::new).toList();
        return ResponseEntity.ok().body(responses);
    }
}


