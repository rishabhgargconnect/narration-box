package edu.tamu.narrationbox.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


/*
* The character taking part in the narration. The entity
* has been defined as a composition of an Identity and a Personality.
*/
@Data
@Document(collection = "character")
public class Character {
    @Id
    private String id;
    private double probabilityOfOccurence;
    private List<TransitionMatrix> personality;
    private Impact[] relations;
    private Identity identity;
    private List<String> stateIds;
}
