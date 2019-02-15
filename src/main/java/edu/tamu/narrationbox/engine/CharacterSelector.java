package edu.tamu.narrationbox.engine;

import edu.tamu.narrationbox.model.Character;

import java.util.List;

public interface CharacterSelector {
    Boolean isCharacterOccurs(Character character);
    List<String> getListOfCharactersWhichOccur(Iterable<Character> characterList);
}