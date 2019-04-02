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
        pastParticiple.put("fear", "afraid");
        pastParticiple.put("shame", "ashamed");
        pastParticiple.put("happy", "happy");
        pastParticiple.put("terror", "terrified");

    }

    @Override
    public String getCausalityText(String characterId, String impactingCharacterId, String state, String impactingCharacterState, String gender) {
        String pronoun = gender.equals("Male") ? "He" : "She";
        if (impactingCharacterId != null && impactingCharacterState != null
                && !impactingCharacterId.isEmpty() && !impactingCharacterState.isEmpty()) {
            if(impactingCharacterId.equals(characterId) && impactingCharacterState.equals(state)){
                return MessageFormat.format("{0} stays {1}. {2} says, ", characterId, pastParticiple.get(state), pronoun);
            }
            else if(impactingCharacterId.equals(characterId) && !impactingCharacterState.equals(state)){
                return MessageFormat.format("{0} goes from being {1} to {2}. {3} says, ", characterId, pastParticiple.get(impactingCharacterState), pastParticiple.get(state), pronoun);
            }
            else{
                return MessageFormat.format("{0} is {1} because {2} is {3}. {4} says, ", characterId, pastParticiple.get(state), impactingCharacterId, pastParticiple.get(impactingCharacterState), pronoun);
            }
        }
        else {
            return MessageFormat.format("{0} is {1}. {2} says, ", characterId, pastParticiple.get(state), pronoun);
        }
    }
}
