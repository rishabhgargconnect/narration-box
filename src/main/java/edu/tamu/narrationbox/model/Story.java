package edu.tamu.narrationbox.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/*The story class*/

@Data
@Document(collection = "story")
public class Story {
    @Id
    private String  id;
    private String title;
    private List<Panel> panels;
    private List<String> characterIds;
}