package edu.tamu.narrationbox.repository;

import edu.tamu.narrationbox.model.Character;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CharacterRepository extends MongoRepository<Character, String> {
}
