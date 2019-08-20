package edu.tamu.narrationbox.engine;

import com.google.gson.Gson;
import edu.tamu.narrationbox.model.Character;
import edu.tamu.narrationbox.model.*;
import edu.tamu.narrationbox.repository.CharacterRepository;
import edu.tamu.narrationbox.repository.StateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;

@Service
public class StateGeneratorImpl implements StateGenerator {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Value("${spring.math.number_of_iterations}")
    private int NUM_OF_ITERATIONS;

    @Autowired
    private Gson gson;

    @Autowired
    private CharacterRepository characterRepository;

    @Autowired
    private TextGenerator textGenerator;

    @Autowired
    private StateRepository stateRepository;

    @Autowired
    private MathComponent mathComponent;

    @Autowired
    private CharacterSelector characterSelector;

    @Override
    public Story generateNewStory(StoryCreationParams storyCreationParams) {

        LOGGER.trace(MessageFormat
                .format("Got request to create story with id:{0}", storyCreationParams.getId()));

        Story story = new Story();
        story.setCharacterIds(storyCreationParams.getCharactersInStory());
        story.setId(storyCreationParams.getId());
        story.setTitle(storyCreationParams.getTitle());
        Panel panel = GeneratePanel(storyCreationParams.getCharactersInStory());
        story.setPanels(Arrays.asList(panel));

        LOGGER.trace("CREATED NEW STORY:\n" + gson.toJson(story));
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
                stateValues.setValue
                        (mathComponent.generateProbabilityVector(state.getSizeOfMatrix()));
                String stateValueAtLargestIndex = state.getIndices()
                        .get(mathComponent.getIndexOfLargestComponent(stateValues.getValue()));
                stateValues.setValueAtIndexOfLargestComponent(stateValueAtLargestIndex);
                stateValues.setStateText(textGenerator.getCausalityText(character.getIdentity().getTitle(),
                        null,stateValueAtLargestIndex, null, character.getGender()));
                listOfStateValues.add(stateValues);
            }

            CharacterState charState = new CharacterState();
            charState.setCharacterId(character.getId());
            charState.setCharacterStates(listOfStateValues);
            characterStates.add(charState);
        }
        panel.setListOfCharacterStates(characterStates);
        LOGGER.trace("Number of character states while creating: " + characterStates.size());

        List<String> listOfOccurringCharacters = getListOfCharactersToDisplay(charactersInStory);
        panel.setCharactersToDisplay(listOfOccurringCharacters);
        return panel;
    }

    @Override
    public Story generateNewStateInStory(Story story) {
        List<CharacterState> newListOfCharacterStates = new ArrayList<>();
        Iterable<Character> listOfCharacters =characterRepository.findAllById(story.getCharacterIds());
        int numberOfPanels = story.getPanels().size();

        LOGGER.trace("==========================================================================");
        LOGGER.trace("Calculation for panel no. {} in story number {}", numberOfPanels+1, story.getId());
        LOGGER.trace("==========================================================================");

        for (CharacterState characterState : (story.getPanels())
                                                .get(numberOfPanels-1)
                                                    .getListOfCharacterStates()) {
            String idOfCharacter = characterState.getCharacterId();

            Character character = getCharacter(listOfCharacters, idOfCharacter);

            // We build a impact weight matrix where each entry
            // has a value which equals to the list of weights
            // For each character c1 we build a matrix
            //Key: State Id
            //Value: List of Pairs where
            //          Item1=>  Character bringing in the influence
            //          Item2=>  The weight of the influence from te character
            Map<String, List<CharacterWeight>> impactWeights = new HashMap<>();
            AddWeightsToImpactMap(character.getPersonality(), idOfCharacter, impactWeights);
            for (Impact characterRelation: character.getRelations()){
                if (!isCharacterInStory(listOfCharacters, characterRelation))
                    continue;

                AddWeightsToImpactMap(characterRelation.getImpact(),
                        characterRelation.getByCharacter(), impactWeights);
            }

            List<StateValues> listOfNewStateValuesForCharacter = new ArrayList<>();
            for (StateValues stateValue : characterState.getCharacterStates()) {
                String idOfState = stateValue.getStateDescriptorId();

                LOGGER.trace("--------------------------------------------------------------------------");
                LOGGER.trace("Calculation of next state: {}, for character {}", idOfState, idOfCharacter);
                LOGGER.trace("--------------------------------------------------------------------------");

                List<String> stateIndices = stateRepository.findById(idOfState).get().getIndices();

                double[] impactVectorOnCharacter
                        = mathComponent.roundOffVector(mathComponent.normalizeVector(
                                impactWeights.get(idOfState).stream()
                                        .mapToDouble(x->x.getImpactWeight()).toArray()), 2);

                int indexOfImpactingCharacter
                        = mathComponent.getIndexOfNextStateFromProbabilityVector(impactVectorOnCharacter);

                CharacterWeight impactingCharacterWeight = impactWeights.get(idOfState).get(indexOfImpactingCharacter);
                String impactingCharacterId = impactingCharacterWeight.getCharacterId();
                
                LOGGER.trace("Markov chain says impacting character is {}", impactingCharacterId);

                double[][] impactMatrix = impactingCharacterId.equals(idOfCharacter)?
                            //If self
                            character.getPersonality()
                                .stream()
                                .filter(x-> (x.getStateDescriptorId().equals(idOfState)))
                                .findFirst().get()
                                .getMatrix():
                            //If someone else is the impacting character
                            Arrays.stream(character.getRelations())
                                .filter(x-> x.getByCharacter().equals(impactingCharacterId))
                                .findFirst().get()
                                .getImpact()
                                .stream()
                                .filter(x->x.getStateDescriptorId().equals(idOfState))
                                .findFirst().get()
                                .getMatrix();

                LOGGER.trace("Number of characters in previous panel: {}", story.getPanels().get(numberOfPanels -1 ).getListOfCharacterStates().size());

                double[] stateValueOfImpactingCharacter = story.getPanels().get(numberOfPanels -1 ) //Get Last Panel
                                    .getListOfCharacterStates()
                                    .stream().filter(x->x.getCharacterId().equals(impactingCharacterId))
                                    .findFirst().get()
                                    .getCharacterStates().stream()
                                    .filter(y->y.getStateDescriptorId().equals(idOfState))
                                    .findFirst().get().getValue();

                String oldStateIndex = stateIndices.get(
                        mathComponent.getIndexOfLargestComponent(stateValueOfImpactingCharacter));

                LOGGER.trace("State value of the impacting character {} is:{} which corresponds to {}",
                        impactingCharacterId ,stateValueOfImpactingCharacter, oldStateIndex);
                LOGGER.trace("Influence matrix of the impacting character is:{}", impactMatrix);

                double[] nextState = mathComponent.getNextStateFromTransitionMatrix(
                                        impactMatrix,
                                        stateValueOfImpactingCharacter,
                                        NUM_OF_ITERATIONS);

                double[] normalizedStateVector = mathComponent.normalizeVector(nextState);
                double[] roundedStateVector = mathComponent.roundOffVector(normalizedStateVector, 2);

                LOGGER.trace("New State of character {} is {}.", character.getId(), roundedStateVector);

                Character impactingCharacter = getCharacter(listOfCharacters, impactingCharacterId);

                StateValues newStateValue =  new StateValues();
                newStateValue.setStateDescriptorId(idOfState);
                newStateValue.setValue(roundedStateVector);
                newStateValue.setCausality(impactingCharacterId);

                String indexOfNewState = stateIndices.get(
                        mathComponent.getIndexOfLargestComponent(roundedStateVector));
                newStateValue.setValueAtIndexOfLargestComponent(indexOfNewState);

                newStateValue.setStateText(textGenerator.getCausalityText(character.getIdentity().getTitle(),
                        impactingCharacter.getIdentity().getTitle(),indexOfNewState, oldStateIndex, character.getGender() ));

                LOGGER.trace("New state value for the state:{} of the character {} is {}",
                        idOfState, idOfCharacter, indexOfNewState);

                listOfNewStateValuesForCharacter.add(newStateValue);
            }
            CharacterState newCharacterState = new CharacterState();
            newCharacterState.setCharacterStates(listOfNewStateValuesForCharacter);
            newCharacterState.setCharacterId(idOfCharacter);
            newListOfCharacterStates.add(newCharacterState);
        }
        Panel p = new Panel();
        p.setListOfCharacterStates(newListOfCharacterStates);


        List<String> listOfOccurringCharacters = getListOfCharactersToDisplay(listOfCharacters);
        p.setCharactersToDisplay(listOfOccurringCharacters);

        story.getPanels().add(p);
        return story;
    }

    private List<String> getListOfCharactersToDisplay(Iterable<Character> listOfCharacters) {
        List<String> listOfOccuringCharacters = characterSelector.getListOfCharactersWhichOccur(listOfCharacters);
        while (listOfOccuringCharacters.isEmpty()) {
            listOfOccuringCharacters = characterSelector.getListOfCharactersWhichOccur(listOfCharacters);
            LOGGER.info("Had to regenerate characters to display because it rendered an empty list");
        }
        return listOfOccuringCharacters;
    }

    private Character getCharacter(Iterable<Character> listOfCharacters, String idOfCharacter) {
        Character character = null;
        for(Character c : listOfCharacters){
            if(c.getId().equals(idOfCharacter)){
                character = c;
            }
        }
        return character;
    }

    private boolean isCharacterInStory(Iterable<Character> listOfCharacters, Impact characterRelation) {
       for(Character c : listOfCharacters){
            if(c.getId().equals(characterRelation.getByCharacter())){
               return true;
            }
        }
        return false;
    }

    private void AddWeightsToImpactMap(Iterable<TransitionMatrix> matrices,
                                       String characterId,
                                       Map<String, List<CharacterWeight>> impactWeights) {
        for(TransitionMatrix transitionMatrix : matrices){
            if(!impactWeights.containsKey(transitionMatrix.getStateDescriptorId())){
                impactWeights.put(transitionMatrix.getStateDescriptorId(), new ArrayList());
            }
            impactWeights.get(transitionMatrix.getStateDescriptorId())
                    .add(new CharacterWeight(characterId, transitionMatrix.getImpactWeight()));
        }
    }
}
