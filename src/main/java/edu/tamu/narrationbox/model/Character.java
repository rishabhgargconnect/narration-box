package edu.tamu.narrationbox.model;

import lombok.Data;

/*
* The character taking part in the narration. The entity
* has been defined as a composition of an Identity and a Personality.
*/
@Data
public class Character {
    public Personality personality;
    public Identity identity;
}
