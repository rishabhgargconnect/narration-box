package edu.tamu.narrationbox.engine;

import edu.tamu.narrationbox.model.Character;
import edu.tamu.narrationbox.model.*;
import edu.tamu.narrationbox.repository.CharacterRepository;
import edu.tamu.narrationbox.repository.StateRepository;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StateGeneratorImpl implements StateGenerator {

    //@Value("${spring.math.num_of_iterations}")
    private int NUM_OF_ITERATIONS=10;

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private MathComponent mathComponent;

    @Autowired
    private CharacterSelector characterSelector;

    @Override
    public Story generateNewStory(StoryCreationParams storyCreationParams) {
        Story story = new Story();
        story.setCharacterIds(storyCreationParams.getCharactersInStory());
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
        panel.setCharactersToDisplay(characterSelector.getListOfCharactersWhichOccur(charactersInStory));
        return panel;
    }

    @Override
    public Story generateNewStateInStory(Story story) {
        List<CharacterState> newListOfCharacterStates = new ArrayList<CharacterState>();
        Iterable<Character> listOfCharacters =characterRepository.findAllById(story.getCharacterIds());
        for (CharacterState characterState : (story.getPanels())
                                                .get(story.getPanels().size()-1)
                                                    .getListOfCharacterStates()) {
            String idOfCharacter = characterState.getCharacterId();
            Character character = characterRepository.findById(idOfCharacter).get();
            //TODO: Optimize by not going to database again
            //and fetch directly from the list above

            // We build a impact weight matrix where each entry
            // has a value which equals to the list of weights
            Map<String, List<Pair<String, Double>>> impactWeights = new HashMap<>();
            AddWeightsToImpactMap(character.getPersonality(), idOfCharacter, impactWeights);
            for (Impact characterRelation: character.getRelations()){
                AddWeightsToImpactMap(characterRelation.getImpact(), characterRelation.getByCharacter(), impactWeights);
            }
            List<StateValues> listOfNewStateValuesForCharacter = new ArrayList<>();
            for (StateValues stateValue : characterState.getCharacterStates()) {
                String idOfState = stateValue.getStateDescriptorId();
                int lengthOfStateVector = stateValue.getValue().length;
                double[] impactVectorOnCharacter
                        = impactWeights.get(idOfState).stream().mapToDouble(x->x.getValue()).toArray();

                int indexOfImpactingCharacter
                        = mathComponent.getIndexOfNextStateFromProbabilityVector(impactVectorOnCharacter);
                System.out.println(indexOfImpactingCharacter);
                Pair<String, Double> requiredCharacterPair = impactWeights.get(idOfState).get(indexOfImpactingCharacter);
                double[][] impactMatrix = requiredCharacterPair.getKey().equals(idOfCharacter)?
                                    character.getPersonality()
                                            .stream()
                                            .filter(x-> (x.getStateDescriptorId().equals(idOfState)))
                                            .findFirst()
                                            .get()
                                            .getMatrix()
                                            :
                                    Arrays.stream(character.getRelations())
                                            .filter(x-> x.getByCharacter()
                                            .equals(requiredCharacterPair.getKey()))
                                            .findFirst()
                                            .get()
                                            .getImpact()
                                            .stream()
                                            .filter(x->x.getStateDescriptorId().equals(idOfState))
                                            .findFirst()
                                            .get()
                                            .getMatrix();

                double[] nextState = mathComponent.getNextStateFromTransitionMatrix(
                                        impactMatrix,
                                        stateValue.getValue(),
                                        NUM_OF_ITERATIONS);

                double[] normalizedStateVector = mathComponent.normalizeVector(nextState);
                double[] roundedStateVector = mathComponent.roundOffVector(normalizedStateVector, 2);//TODO: Recheck
                StateValues newStateValue =  new StateValues();
                newStateValue.setStateDescriptorId(idOfState);
                newStateValue.setValue(roundedStateVector);
                newStateValue.setCausality(requiredCharacterPair.getKey());
                newStateValue.setValueAtIndexOfLargestComponent(
                        stateRepository.findById(idOfState).get().getIndices()
                        .get(mathComponent.getIndexOfLargestComponent(roundedStateVector)));
                listOfNewStateValuesForCharacter.add(newStateValue);
            }
            CharacterState newCharacterState = new CharacterState();
            newCharacterState.setCharacterStates(listOfNewStateValuesForCharacter);
            newCharacterState.setCharacterId(idOfCharacter);
            newListOfCharacterStates.add(newCharacterState);
        }
        Panel p = new Panel();
        p.setListOfCharacterStates(newListOfCharacterStates);
        p.setCharactersToDisplay(characterSelector.getListOfCharactersWhichOccur(listOfCharacters));
        story.getPanels().add(p);
        return story;
    }

    private void AddWeightsToImpactMap(Iterable<TransitionMatrix> matrices, String characterId,
                                                Map<String, List<Pair<String, Double>>> impactWeights) {
        for(TransitionMatrix transitionMatrix : matrices){
            if(!impactWeights.containsKey(transitionMatrix.getStateDescriptorId())){
                impactWeights.put(transitionMatrix.getStateDescriptorId(), new ArrayList());
            }
            impactWeights.get(transitionMatrix.getStateDescriptorId())
                    .add(new Pair(characterId, transitionMatrix.getImpactWeight()));
        }
    }
}
