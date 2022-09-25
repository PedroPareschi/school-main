package br.com.alura.school.course;

import br.com.alura.school.section.Section;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CourseSectionByVideosResponse {

    @JsonProperty
    private final String courseName;
    @JsonProperty
    private final String sectionTitle;
    @JsonProperty
    private final String authorName;
    @JsonProperty
    private final int totalVideos;

    public CourseSectionByVideosResponse(Section section) {
        this.courseName = section.getCourse().getName();
        this.sectionTitle = section.getTitle();
        this.authorName = section.getAuthor().getUsername();
        this.totalVideos = section.getVideos().size();
    }

    public String getCourseName() {
        return courseName;
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public String getAuthorName() {
        return authorName;
    }

    public int getTotalVideos() {
        return totalVideos;
    }
}
