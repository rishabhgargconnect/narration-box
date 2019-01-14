package edu.tamu.narrationbox.controller;

import edu.tamu.narrationbox.engine.MathComponent;
import edu.tamu.narrationbox.model.Character;
import edu.tamu.narrationbox.model.Impact;
import edu.tamu.narrationbox.model.State;
import edu.tamu.narrationbox.model.TransitionMatrix;
import edu.tamu.narrationbox.repository.CharacterRepository;
import edu.tamu.narrationbox.repository.StateRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/characters/")
@Api(description = "The descriptions of the characters in the story")
public class CharacterController{

    @Autowired
    public CharacterRepository characterRepository;

    @Autowired
    public StateRepository stateRepository;


    @Autowired
    public MathComponent mathComponent;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ApiOperation("Get all the characters registered in the system.")
    public Character[] getAllCharacters(){
        return characterRepository.findAll().toArray(new Character[0]);
    }

    @RequestMapping(value = "{id}",method = RequestMethod.GET, produces = "application/json")
    @ApiOperation("Get a character registered in the system.")
    public Character getCharacter(@PathVariable("id") String id){
        return characterRepository.findById(id).orElse(null);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation("Register a character in the system. Will Autogenerate the missing matrices if value not present")
    public String createCharacters(@RequestBody Character character) {
        //TODO: Validate
       for(TransitionMatrix transitionMatrix: character.getPersonality()){
           State s = stateRepository.findById(transitionMatrix.getStateDescriptorId()).get();
           FillMatrixIfEmpty(transitionMatrix, s);
       }

       for(Impact relation: character.getRelations()){
           for(TransitionMatrix transitionMatrix: relation.getImpact()){
               State s = stateRepository.findById(transitionMatrix.getStateDescriptorId()).get();
               FillMatrixIfEmpty(transitionMatrix, s);
           }
       }

       characterRepository.save(character);
       return "Success";
    }

    private void FillMatrixIfEmpty(TransitionMatrix t, State s) {
        if(t.isEmpty()){
            t.setMatrix(mathComponent.generateTransitionMatrix(s.getSizeOfMatrix(), s.getSizeOfMatrix()));
        }
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @ApiOperation("Update a registered character in the system.")
    public String updateCharacter(@PathVariable("id") String id, @RequestBody Character character) {
        if(!character.getId().equals(id))
            return "Error";

        characterRepository.save(character);
        return "Success";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ApiOperation("Delete a character registered in the system.")
    public String deleteCharacter(@PathVariable("id") String id) {
        characterRepository.deleteById(id);
        return "Success";//TODO: Improve return mechanism
    }
}