package br.com.alura.school.section;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        URI location = sectionService.addNewSection(code, newSectionRequest);
        return ResponseEntity.created(location).build();
    }


    @PostMapping("/courses/{code}/sections/{sectionCode}")
    ResponseEntity<Void> newVideo(@PathVariable("code") String code, @PathVariable("sectionCode") String sectionCode, @RequestBody @Valid NewVideoRequest newVideoRequest) {
        sectionService.addNewVideo(code, sectionCode, newVideoRequest);
        return ResponseEntity.ok().build();
    }
}
