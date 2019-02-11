package edu.tamu.narrationbox.engine;

import edu.tamu.narrationbox.model.Character;
import edu.tamu.narrationbox.model.*;
import edu.tamu.narrationbox.repository.CharacterRepository;
import edu.tamu.narrationbox.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class StateGeneratorImpl implements StateGenerator {

    //TODO:@Value("${spring.math.num_of_iterations}")
    private int NUM_OF_ITERATIONS=10;

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private MathComponent mathComponent;

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
        Iterable<Character> charactersInStory = characterRepository.findAllById(listOfCharacterIds);
        for (Character character : charactersInStory) {

            List<StateValues> listOfStateValues = new ArrayList<>();
            Iterable<State> statesOfCharacter = stateRepository.findAllById(character.getStateIds());
            for (State state : statesOfCharacter) {
                StateValues stateValues = new StateValues();
                stateValues.setStateDescriptorId(state.getId());
                stateValues.setValue(mathComponent.generateProbabilityVector(state.getSizeOfMatrix()));

                stateValues.setValueAtIndexOfLargestComponent(state.getIndices()
                        .get(mathComponent.getIndexOfNextStateFromProbabilityVector(stateValues.getValue())));
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
        List<CharacterState> newListOfCharacterStates = new ArrayList<CharacterState>();
        for (CharacterState characterState : (story.getPanels()).get(story.getPanels().size()-1).getListOfCharacterStates()) {
            String idOfCharacter = characterState.getCharacterId();
            List<StateValues> listOfNewStateValuesForCharacter = new ArrayList<>();
            Character character = characterRepository.findById(idOfCharacter).get();
            for (StateValues stateValue : characterState.getCharacterStates()) {
                String idOfState = stateValue.getStateDescriptorId();
                int lengthOfStateVector = stateValue.getValue().length;
                double[] nextState = new double[lengthOfStateVector];
                for(TransitionMatrix relatedPersonalityVector : character.getPersonality()){
                    if(relatedPersonalityVector.getStateDescriptorId().equals(idOfState)){
                        nextState = mathComponent.getNextStateFromTransitionMatrix(
                                relatedPersonalityVector.getMatrix(),
                                stateValue.getValue(),
                                NUM_OF_ITERATIONS);
                    }
                }

                for (Impact relation : character.getRelations()) {
                    for (TransitionMatrix transitionMatrix : relation.getImpact()) {
                        if (transitionMatrix.getStateDescriptorId().equals(idOfState)) {
                            double impactWeight = transitionMatrix.getImpactWeight();
                            nextState = mathComponent.addStateComponent
                                    (nextState, impactWeight,
                                            mathComponent.getNextStateFromTransitionMatrix
                                                    (transitionMatrix.getMatrix(),
                                                            stateValue.getValue(),
                                                            NUM_OF_ITERATIONS));
                        }
                    }
                }

                double[] normalizedStateVector = mathComponent.normalizeVector(nextState);
                double[] roundedStateVector = mathComponent.roundOffVector(normalizedStateVector, 2);//TODO: Recheck
                StateValues newStateValue =  new StateValues();
                newStateValue.setStateDescriptorId(idOfState);
                newStateValue.setValue(roundedStateVector);
                newStateValue.setValueAtIndexOfLargestComponent(
                        stateRepository.findById(idOfState).get().getIndices()
                        .get(mathComponent.getIndexOfNextStateFromProbabilityVector(roundedStateVector)));
                listOfNewStateValuesForCharacter.add(newStateValue);
            }
            CharacterState newCharacterState = new CharacterState();
            newCharacterState.setCharacterStates(listOfNewStateValuesForCharacter);
            newCharacterState.setCharacterId(idOfCharacter);
            newListOfCharacterStates.add(newCharacterState);
        }
        Panel p = new Panel();
        p.setListOfCharacterStates(newListOfCharacterStates);
        story.getPanels().add(p);
        return story;
    }
}
