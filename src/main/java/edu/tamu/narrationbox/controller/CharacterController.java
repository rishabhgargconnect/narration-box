package edu.tamu.narrationbox.controller;

import edu.tamu.narrationbox.model.Character;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/characters/")
@Api(description = "The possible characters of the characters which can be of types: emotional, physical, existential etc.")
public class CharacterController {

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ApiOperation("Get all the characters registered in the system.")
    public Character[] getAllCharacters(){
        return new Character[10];
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation("Register a character in the system.")
    public String createCharacters(@RequestBody Character character) {
        return "";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @ApiOperation("Update a registered character in the system.")
    public String updateCharacter(@PathVariable("id") String id, @RequestBody Character character) {
        return "";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ApiOperation("Delete a character registered in the system.")
    public String deleteCharacter(@PathVariable("id") String id) {
        return "";
    }
}