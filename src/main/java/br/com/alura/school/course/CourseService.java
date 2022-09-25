package br.com.alura.school.course;

import br.com.alura.school.section.Section;
import br.com.alura.school.section.SectionComparator;
import br.com.alura.school.section.SectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;

    public CourseService(CourseRepository courseRepository, SectionRepository sectionRepository) {
        this.courseRepository = courseRepository;
        this.sectionRepository = sectionRepository;
    }

    public List<CourseResponse> getAllCourses(){
        List<Course> courses = courseRepository.findAll();
        return courses.stream().map(CourseResponse::new).toList();
    }

    public Course getCourseByCode(String code){
        return courseRepository.findByCode(code).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Course with code %s not found", code)));
    }

    public URI addCourse(NewCourseRequest newCourseRequest){
        courseRepository.save(newCourseRequest.toEntity());
        return URI.create(format("/courses/%s", newCourseRequest.getCode()));
    }

    public List<CourseSectionByVideosResponse> getCourseSectionByVideos(String code){
        Course course = courseRepository.findByCode(code).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Course with code %s not found", code)));
        if (course.getStudents().isEmpty())
            throw new ResponseStatusException(BAD_REQUEST, format("Course %s has no enrollments", code));
        List<Section> sections = sectionRepository.findAllByCourse(course).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Course with code %s has no classes yet", code)));
        sections.sort(new SectionComparator());
        return sections.stream().map(CourseSectionByVideosResponse::new).toList();
    }
}
