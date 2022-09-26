package br.com.alura.school.section;

import br.com.alura.school.course.CourseException;
import br.com.alura.school.user.UserException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;

@RestController
class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping("/courses/{code}/sections")
    ResponseEntity<Void> newSection(@PathVariable("code") String code, @RequestBody @Valid NewSectionRequest newSectionRequest) {
        URI location;
        try {
            location = sectionService.addNewSection(code, newSectionRequest);
        } catch (SectionException | CourseException | UserException e) {
            throw new ResponseStatusException(e.getHttpStatus(), e.getMessage());
        }
        return ResponseEntity.created(location).build();
    }


    @PostMapping("/courses/{code}/sections/{sectionCode}")
    ResponseEntity<Void> newVideo(@PathVariable("code") String code, @PathVariable("sectionCode") String sectionCode, @RequestBody @Valid NewVideoRequest newVideoRequest) {
        try {
            sectionService.addNewVideo(code, sectionCode, newVideoRequest);
        } catch (SectionException | CourseException e) {
            throw new ResponseStatusException(e.getHttpStatus(), e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}
