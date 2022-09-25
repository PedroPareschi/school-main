package br.com.alura.school.section;

import br.com.alura.school.course.Course;
import br.com.alura.school.support.validation.Unique;
import br.com.alura.school.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

class NewSectionRequest {

    @NotBlank
    @JsonProperty
    private final String code;

    @Size(min = 5)
    @NotBlank
    @JsonProperty
    private final String title;

    @NotBlank
    @JsonProperty
    private final String authorUsername;

    NewSectionRequest(String code, String title, String authorUsername) {
        this.code = code;
        this.title = title;
        this.authorUsername = authorUsername;
    }

    String getCode() {
        return code;
    }

    String getAuthorUsername() {
        return authorUsername;
    }

    Section toEntity(Course course, User user) {
        return new Section(code, title, course, user);
    }
}
