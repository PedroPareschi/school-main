package br.com.alura.school.section;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseException;
import br.com.alura.school.course.CourseService;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserException;
import br.com.alura.school.user.UserRole;
import br.com.alura.school.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class SectionService {

    @Autowired
    private SectionRepository sectionRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private UserService userService;


    public URI addNewSection(String code, NewSectionRequest newSectionRequest) throws SectionException, CourseException, UserException {
        Course course = courseService.getCourseByCode(code);
        User author = userService.findByUsername(newSectionRequest.getAuthorUsername());
        if (author.getRole() != UserRole.INSTRUCTOR)
            throw new SectionException(BAD_REQUEST, format("User %s is not a instructor", author.getUsername()));
        Section section = newSectionRequest.toEntity(course, author);
        for (Section courseSections : course.getSections()) {
            if (Objects.equals(courseSections.getCode(), section.getCode())) {
                throw new SectionException(BAD_REQUEST, format("Section code %s already in use", section.getCode()));
            }
        }
        sectionRepository.save(newSectionRequest.toEntity(course, author));
        return URI.create(format("/courses/%s/sections/%s", code, newSectionRequest.getCode()));
    }

    public void addNewVideo(String code, String sectionCode, NewVideoRequest newVideoRequest) throws SectionException, CourseException {
        Course course = courseService.getCourseByCode(code);
        Section section = sectionRepository.findByCourseAndCode(course, sectionCode).orElseThrow(() -> new SectionException(NOT_FOUND, format("Section with code %s not found", code)));
        String video = newVideoRequest.getVideo();
        if (section.getVideos().contains(video))
            throw new SectionException(BAD_REQUEST, format("Video %s already on class", video));
        section.addVideo(video);
        sectionRepository.save(section);
    }

    public List<Section> findAllByCourse(Course course) throws SectionException {
        return sectionRepository.findAllByCourse(course).orElseThrow(() -> new SectionException(NOT_FOUND, format("Course with code %s has no classes yet", course.getCode())));
    }

}
