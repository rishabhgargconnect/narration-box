package edu.tamu.narrationbox.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class StoryCreationParams {
    @Id
    private String id;
    private String title;
    private List<String> charactersInStory;
}
