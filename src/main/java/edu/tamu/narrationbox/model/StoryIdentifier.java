package edu.tamu.narrationbox.model;

import lombok.Data;

@Data
public class StoryIdentifier {
    public StoryIdentifier(String id, String title){
        this.id = id;
        this.title = title;
    }
    private String id;
    private String title;
}
