package br.com.alura.school.section;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;

class NewVideoRequest {

    @NotBlank
    @JsonProperty
    private String video;

    public NewVideoRequest() {
    }

    public NewVideoRequest(String video) {
        this.video = video;
    }

    public String getVideo() {
        return video;
    }
}
