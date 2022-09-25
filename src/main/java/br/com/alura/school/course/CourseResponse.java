package br.com.alura.school.course;

import br.com.alura.school.section.SectionResponse;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Optional;

class CourseResponse {

    @JsonProperty
    private final String code;

    @JsonProperty
    private final String name;

    @JsonProperty
    private final String shortDescription;

    @JsonProperty
    private final List<SectionResponse> sections;

    CourseResponse(Course course) {
        this.code = course.getCode();
        this.name = course.getName();
        this.shortDescription = Optional.of(course.getDescription()).map(this::abbreviateDescription).orElse("");
        this.sections = course.getSections().stream().map(SectionResponse::new).toList();;
    }

    private String abbreviateDescription(String description) {
        if (description.length() <= 13) return description;
        return description.substring(0, 10) + "...";
    }


}
