package edu.tamu.narrationbox.model;

import lombok.Data;

import java.util.List;
import java.util.UUID;

/*The story class*/
@Data
public class Story {
    public UUID Id;
    public String Title;
    public List<Panel> panels;
}