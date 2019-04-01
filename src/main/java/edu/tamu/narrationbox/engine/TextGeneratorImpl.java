package edu.tamu.narrationbox.engine;

import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public class TextGeneratorImpl implements TextGenerator {
    Map<String, String> pastParticiple ;
    public TextGeneratorImpl(){
        pastParticiple = new HashMap<>();
        pastParticiple.put("laugh", "laughing");
        pastParticiple.put("shame", "ashamed");
        pastParticiple.put("happy", "happy");
        pastParticiple.put("terror", "terrified");

    }

    @Override
    public String getCausalityText(String characterId, String impactingCharacterId, String state, String impactingCharacterState) {
        if(impactingCharacterId!= null && impactingCharacterState!= null
                && !impactingCharacterId.isEmpty() && !impactingCharacterState.isEmpty()){
            return MessageFormat.format("{0} is {1} because {2} is {3}.", characterId, pastParticiple.get(state), impactingCharacterId, pastParticiple.get(impactingCharacterState));
        }
        else {
            return MessageFormat.format("{0} is {1}.", characterId, pastParticiple.get(state));
        }
    }


}
