package br.com.alura.school.section;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;
import br.com.alura.school.user.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Objects;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;

    private final CourseRepository courseRepository;

    private final UserRepository userRepository;

    public SectionService(SectionRepository sectionRepository, CourseRepository courseRepository, UserRepository userRepository) {
        this.sectionRepository = sectionRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    public URI addNewSection(String code, NewSectionRequest newSectionRequest){
        Course course = courseRepository.findByCode(code).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Course with code %s not found", code)));
        User author = userRepository.findByUsername(newSectionRequest.getAuthorUsername()).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("User with username %s not found", newSectionRequest.getAuthorUsername())));
        if (author.getRole() != UserRole.INSTRUCTOR) throw new ResponseStatusException(BAD_REQUEST, format("User %s is not a instructor", author.getUsername()));
        Section section = newSectionRequest.toEntity(course, author);
        for(Section courseSections: course.getSections()){
            if(Objects.equals(courseSections.getCode(), section.getCode())){
                throw new ResponseStatusException(BAD_REQUEST, format("Section code %s already in use", section.getCode()));
            }
        }
        sectionRepository.save(newSectionRequest.toEntity(course, author));
        return URI.create(format("/courses/%s/sections/%s", code, newSectionRequest.getCode()));
    }

    public void addNewVideo(String code, String sectionCode, NewVideoRequest newVideoRequest){
        Course course = courseRepository.findByCode(code).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Section with code %s not found", code)));
        Section section = sectionRepository.findByCourseAndCode(course, sectionCode).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Section with code %s not found", code)));
        String video = newVideoRequest.getVideo();
        if(section.getVideos().contains(video)) throw new ResponseStatusException(BAD_REQUEST, format("Video %s already on class", video));
        section.addVideo(video);
        sectionRepository.save(section);
    }

}
