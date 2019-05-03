package edu.tamu.narrationbox.model;

import lombok.Data;

import java.util.List;

@Data
public class Impact {
    private String byCharacter;
    private List<TransitionMatrix> impact;
}
