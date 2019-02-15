package edu.tamu.narrationbox.controller;

import edu.tamu.narrationbox.engine.CharacterSelector;
import edu.tamu.narrationbox.engine.StateGenerator;
import edu.tamu.narrationbox.model.Character;
import edu.tamu.narrationbox.model.*;
import edu.tamu.narrationbox.repository.CharacterRepository;
import edu.tamu.narrationbox.repository.StoryRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/stories/")
@Api(description = "Controller to generate panels and create a story")
public class StoryController{

    @Autowired
    private StateGenerator stateGenerator;

    @Autowired
    private StoryRepository storyRepository;

    @Autowired
    private CharacterRepository characterRepository;


    @Autowired
    private CharacterSelector characterSelector;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    @ApiOperation(value = "Create a Story using the defined params")
    public StoryViewModel createStory(@RequestBody StoryCreationParams storyCreationParams){
        Iterable<Character> characters = characterRepository.findAllById(
                storyCreationParams.getCharactersInStory());
        Story newStory =  stateGenerator.generateNewStory(storyCreationParams);
        storyRepository.insert(newStory);
        return new StoryViewModel(newStory);
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Get all the stories and their titles")
    public StoryIdentifier[] getAllStories(){
        return storyRepository.findAll().stream().map(story ->
                new StoryIdentifier(story.getId(), story.getTitle()))
                                                .toArray(StoryIdentifier[]::new);
    }

    @RequestMapping(path = "{id}", method = RequestMethod.GET, produces = "application/json")
    @ApiOperation(value = "Get all the stories and their titles")
    public StoryViewModel getStory(@RequestParam String id ){
        return new StoryViewModel(storyRepository.findById(id).get());
    }

    @RequestMapping(path = "addPanel",method = RequestMethod.POST, produces = "application/json")
    @ApiOperation(value = "Create the new states of the characters in the story")
    public StoryViewModel generateNewStateInStory(@RequestParam String storyId){
        Story storyTillNow = storyRepository.findById(storyId).orElse(null);
        if(storyTillNow == null){
            return null;//TODO:Return 404
        }
        Story newStory = stateGenerator.generateNewStateInStory(storyTillNow);
        storyRepository.save(newStory);
        return new StoryViewModel(newStory);
    }



}