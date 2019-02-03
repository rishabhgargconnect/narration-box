package edu.tamu.narrationbox.controller;

import edu.tamu.narrationbox.engine.StateGenerator;
import edu.tamu.narrationbox.model.Story;
import edu.tamu.narrationbox.model.StoryCreationParams;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/stories/")
@Api(description = "Controller to generate panels and create a story")
public class StoryController{

    @Autowired
    private StateGenerator stateGenerator;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    @ApiOperation(value = "Create a Story using the defined params")
    public Story createStory(@RequestBody StoryCreationParams storyCreationParams){

        return stateGenerator.generateNewStory(storyCreationParams);

    }

    @RequestMapping(path = "addState",method = RequestMethod.POST, produces = "application/json")
    @ApiOperation(value = "Create the new states of the characters in the story")
    public Story generateNewStateInStory(@RequestBody Story story){
        return stateGenerator.generateNewStateInStory(story);
    }
}