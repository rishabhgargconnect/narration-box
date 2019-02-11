package edu.tamu.narrationbox.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class PanelViewModel {
    private List<Map<String, String>> characters;

    public PanelViewModel(Panel panel){
        characters= new ArrayList<>();
        for(CharacterState charState : panel.getListOfCharacterStates()){
            Map<String, String> attributes = new HashMap<>();
            attributes.put("character-name", charState.getCharacterId());
            for (StateValues sv: charState.getCharacterStates() ){
                attributes.put(sv.getStateDescriptorId(),
                        sv.getValueAtIndexOfLargestComponent());
            }
            characters.add(attributes);
        }
    }
}
