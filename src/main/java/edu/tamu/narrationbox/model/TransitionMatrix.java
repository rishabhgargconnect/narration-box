package edu.tamu.narrationbox.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class TransitionMatrix {
    private String stateDescriptorId;
    private double impactWeight;
    private double[][] matrix;

    @JsonIgnore
    public boolean isEmpty(){
        return (matrix.length == 1) && (matrix[0].length == 1) && (matrix[0][0] == 0.0d);
    }
}
