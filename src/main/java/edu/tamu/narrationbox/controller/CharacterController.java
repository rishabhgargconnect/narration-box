package edu.tamu.narrationbox.controller;

import edu.tamu.narrationbox.model.Character;
import edu.tamu.narrationbox.repository.CharacterRepository;
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
    @ApiOperation("Register a character in the system.")
    public String createCharacters(@RequestBody Character character) {
        characterRepository.save(character);
        return "Success";
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