package edu.tamu.narrationbox.controller;

import edu.tamu.narrationbox.model.States.State;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@RestController
@RequestMapping(value = "/states/")
@Api(description = "The possible states of the characters which can be of types: emotional, physical, existential etc.")
public class StateController{

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ApiOperation("Get all the states registered in the system.")
    public State[] getAllStates(){
        return new State[10];
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation("Register a state in the system.")
    public String createStates(@RequestBody State state) {
        return "";

    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @ApiOperation("Update a registered state in the system.")
    public String updateState(@PathVariable("id") String id, @RequestBody State state) {
        return "";

    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ApiOperation("Delete a state registered in the system.")
    public String deleteState(@PathVariable("id") String id) {
        return "";
    }
}