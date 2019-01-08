package edu.tamu.narrationbox.repository;

import edu.tamu.narrationbox.model.Story;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StoryRepository extends MongoRepository<Story, String> {
}
