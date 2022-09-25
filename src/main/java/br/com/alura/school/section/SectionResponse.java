package br.com.alura.school.section;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class SectionResponse {

    @JsonProperty
    private final String title;

    @JsonProperty
    private final String author;

    @JsonProperty
    private final List<String> videos;

    public SectionResponse(Section section) {
        this.title = section.getTitle();
        this.author = section.getAuthor().getUsername();
        this.videos = section.getVideos();
    }
}
