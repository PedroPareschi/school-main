package br.com.alura.school.course;

import br.com.alura.school.section.Section;
import br.com.alura.school.section.SectionComparator;
import br.com.alura.school.section.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class CourseService {

    @Autowired
    private  CourseRepository courseRepository;

    @Autowired
    private  SectionService sectionService;

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
        List<Section> sections = sectionService.findAllByCourse(course);
        sections.sort(new SectionComparator());
        return sections.stream().map(CourseSectionByVideosResponse::new).toList();
    }
}
