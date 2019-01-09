package edu.tamu.narrationbox.controller;

import edu.tamu.narrationbox.model.State;
import edu.tamu.narrationbox.repository.StateRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/states/")
@Api(description = "The possible states of the characters which can be of types: emotional, physical, existential etc.")
public class StateController{

    @Autowired
    public StateRepository stateRepository;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ApiOperation("Get all the states registered in the system.")
    public State[] getAllStates(){
        return stateRepository.findAll().toArray(new State[0]);
    }

    @RequestMapping(value ="{id}", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation("Get a state registered in the system.")
    public State getState(@PathVariable("id") String id){
        return stateRepository.findById(id).orElse(null);
    }

    @RequestMapping(method = RequestMethod.POST)
    @ApiOperation("Register a state in the system.")
    public String createStates(@RequestBody State state) {
        stateRepository.save(state);
        return "Success";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.PUT)
    @ApiOperation("Update a registered state in the system.")
    public String updateState(@PathVariable("id") String id, @RequestBody State state) {
        if(!state.getId().equals(id))
            return "Error";

        stateRepository.save(state);
        return "Success";
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    @ApiOperation("Delete a state registered in the system.")
    public String deleteState(@PathVariable("id") String id) {
        stateRepository.deleteById(id);
        return "Success";//TODO: Improve return mechanism
    }
}