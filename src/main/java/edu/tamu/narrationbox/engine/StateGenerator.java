package edu.tamu.narrationbox.engine;

import edu.tamu.narrationbox.model.Story;
import edu.tamu.narrationbox.model.StoryCreationParams;

public interface StateGenerator {
    Story generateNewStory(StoryCreationParams storyCreationParams);

    Story generateNewStateInStory(Story story);
}
