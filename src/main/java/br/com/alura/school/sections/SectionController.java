package br.com.alura.school.sections;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import br.com.alura.school.user.UserRole;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
class SectionController {

    private final SectionRepository sectionRepository;

    private final CourseRepository courseRepository;

    private final UserRepository userRepository;

    public SectionController(SectionRepository sectionRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.sectionRepository = sectionRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }


    @PostMapping("/courses/{code}/sections")
    ResponseEntity<Void> newSection(@PathVariable("code") String code, @RequestBody @Valid NewSectionRequest newSectionRequest) {
        Course course = courseRepository.findByCode(code).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Course with code %s not found", code)));
        User author = userRepository.findByUsername(newSectionRequest.getAuthorUsername()).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("User with username %s not found", code)));
        assert author.getRole() == UserRole.INSTRUCTOR: new ResponseStatusException(BAD_REQUEST, format("User %s is not a instructor", author.getUsername()));
        sectionRepository.save(newSectionRequest.toEntity(course, author));
        URI location = URI.create(format("/courses/%s/section/%s", code, newSectionRequest.getCode()));
        return ResponseEntity.created(location).build();
    }
}
