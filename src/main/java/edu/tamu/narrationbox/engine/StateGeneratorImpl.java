package edu.tamu.narrationbox.engine;

import edu.tamu.narrationbox.model.*;
import edu.tamu.narrationbox.model.Character;
import edu.tamu.narrationbox.repository.CharacterRepository;
import edu.tamu.narrationbox.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class StateGeneratorImpl implements StateGenerator {

    @Autowired
    private CharacterRepository characterRespository;

    @Autowired
    private StateRepository stateRepository;

    @Override
    public Story generateNewStory(StoryCreationParams storyCreationParams) {
        Story story = new Story();
        story.setId(storyCreationParams.getId());
        story.setTitle(storyCreationParams.getTitle());
        Panel panel = GeneratePanel(storyCreationParams.getCharactersInStory());
        story.setPanels(Arrays.asList(panel));
        return story;
    }

    private Panel GeneratePanel(Iterable<String> listOfCharacterIds) {
        Panel panel = new Panel();
        ArrayList<CharacterState> characterStates = new ArrayList<>();
        Iterable<Character> charactersInStory = characterRespository.findAllById(listOfCharacterIds);
        for (Character character:charactersInStory){

            List<StateValues> listOfStateValues = new ArrayList<>();
            Iterable<State> statesOfCharacter = stateRepository.findAllById(character.getStateIds());
            for(State state: statesOfCharacter){
                StateValues stateValues = new StateValues();
                stateValues.setStateDescriptorId(state.getId());
                stateValues.setValue(state.generateSample());
                listOfStateValues.add(stateValues);
            }

            CharacterState charState = new CharacterState();
            charState.setCharacterId(character.getId());
            charState.setCharacterStates(listOfStateValues);
            characterStates.add(charState);
        }
        panel.setListOfCharacterStates(characterStates);
        return panel;
    }

    @Override
    public Story generateNewStateInStory(Story story) {
        return null;
    }
}
