package edu.tamu.narrationbox.engine;

import edu.tamu.narrationbox.model.Character;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CharacterSelectorImpl implements CharacterSelector {

    @Autowired
    MathComponent mathComponent;

    @Override
    public Boolean isCharacterOccurs(Character character) {
        return mathComponent
                .getIndexOfNextStateFromProbabilityVector
                        (new double[]{character.getProbabilityOfOccurence(), 1.0}) == 0;
    }

    @Override
    public List<String> getListOfCharactersWhichOccur(Iterable<Character> characterList) {
        List<String> result = new ArrayList<>();
        for(Character character: characterList){
            if( isCharacterOccurs(character) ){
                result.add(character.getId());
            }
        }
        return result;
    }
}
