package edu.tamu.narrationbox.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/*The story class*/
@Data
@Document(collection = "story")
public class Story {
    private String Id;
    private String Title;
    private List<Panel> panels;
}