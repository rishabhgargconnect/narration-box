package edu.tamu.narrationbox.controller;

import edu.tamu.narrationbox.model.States.State;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/states/")
@Api(description = "The possible states of the characters which can be of types: emotional, physical, existential etc.")
public class StateController{

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ApiOperation("Get all the states registered in the system.")
    public State[] getAllStates(){
        return new State[10];
    }
}
