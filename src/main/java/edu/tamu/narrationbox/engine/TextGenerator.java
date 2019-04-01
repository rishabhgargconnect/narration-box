package edu.tamu.narrationbox.engine;

public interface TextGenerator {
    String getCausalityText(String characterId, String impactingCharacterId, String state, String impactingCharacterState);
}
