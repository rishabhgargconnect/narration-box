package edu.tamu.narrationbox.model;

import lombok.Data;

import java.util.List;

@Data
public class Panel {
    private List<CharacterState> listOfCharacterStates;
    private List<String> charactersToDisplay;
}