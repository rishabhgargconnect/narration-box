package edu.tamu.narrationbox.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/* The base class for the state of a character. It can be
emotional,physical, existential etc.*/
@Data
@Document(collection = "state")
public class State {
    @Id
    private String id;
    private List<String> indices;
    private double[][] default_min;
    private double[][] default_max;
    public int getSizeOfMatrix() {
        return indices.size();
    }
}
