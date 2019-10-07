package edu.tamu.narrationbox.model;

import lombok.Data;

import java.util.List;

@Data
public class Impact {
    private String byCharacter;
    private Double closeness = 0.5;
    private List<TransitionMatrix> impact;
}
