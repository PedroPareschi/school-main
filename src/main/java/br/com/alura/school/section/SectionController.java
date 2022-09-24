package br.com.alura.school.section;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import br.com.alura.school.user.UserRole;
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
        if (author.getRole() != UserRole.INSTRUCTOR) throw new ResponseStatusException(BAD_REQUEST, format("User %s is not a instructor", author.getUsername()));
        sectionRepository.save(newSectionRequest.toEntity(course, author));
        URI location = URI.create(format("/courses/%s/sections/%s", code, newSectionRequest.getCode()));
        return ResponseEntity.created(location).build();
    }


    @PostMapping("/courses/{code}/sections/{sectionCode}")
    ResponseEntity<Void> newVideo(@PathVariable("code") String code, @PathVariable("sectionCode") String sectionCode, @RequestBody @Valid NewVideoRequest newVideoRequest) {
        Section section = sectionRepository.findByCode(sectionCode).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Section with code %s not found", code)));
        String video = newVideoRequest.getVideo();
        if(section.getVideos().contains(video)) throw new ResponseStatusException(BAD_REQUEST, format("Video %s already on class", video));
        section.addVideo(video);
        sectionRepository.save(section);
        return ResponseEntity.ok().build();
    }
}
