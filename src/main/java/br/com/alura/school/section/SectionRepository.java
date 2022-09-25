package br.com.alura.school.section;

import br.com.alura.school.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SectionRepository extends JpaRepository<Section, Long> {
    Optional<Section> findByCourseAndCode(Course course, String code);

    Optional<List<Section>> findAllByCourse(Course course);
}
