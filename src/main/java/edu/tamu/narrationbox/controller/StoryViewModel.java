package edu.tamu.narrationbox.controller;

import edu.tamu.narrationbox.model.PanelViewModel;
import edu.tamu.narrationbox.model.Story;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class StoryViewModel {
    @Id
    private String id;
    private String title;
    private List<PanelViewModel> panels;

    public StoryViewModel(Story story){
        id = story.getId();
        title = story.getTitle();
        panels = story.getPanels()
                        .stream()
                        .map(panel-> {
                            PanelViewModel panelViewModel =  new PanelViewModel(panel);
                            return panelViewModel;
                        } )
                        .collect(Collectors.toList());
    }
}
