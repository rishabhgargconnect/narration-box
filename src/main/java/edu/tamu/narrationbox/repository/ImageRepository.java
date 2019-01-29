package edu.tamu.narrationbox.repository;

import edu.tamu.narrationbox.model.Image;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ImageRepository extends MongoRepository<Image, String> {

    @Query("{'identity' :?0, 'emotion': ?1}")
    List<Image> findByImageMatchingAttributes(String characterIdentity, String emotionalState);
}
