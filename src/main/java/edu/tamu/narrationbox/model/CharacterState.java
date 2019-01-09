package edu.tamu.narrationbox.model;

import lombok.Data;

import java.util.List;

/*class to display the state values of a character ina a story*/
@Data
public class CharacterState {
    private String characterId;
    private List<StateValues> characterStates;
}
