package edu.tamu.narrationbox.repository;

import edu.tamu.narrationbox.model.Image;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ImageRepository extends MongoRepository<Image, String> {

    @Query("{'identity' :?0, 'emotion': ?1}")
    List<Image> findByImageMatchingAttributes(String characterIdentity, String emotionalState);

    @Query("{'identity' :?0")
    List<Image> findExpressionsForImage(String characterIdentity);

    @Query("{'emotion': 'default'}")
    List<Image> findDefaultImageOfAllCharacters();

    @DeleteQuery("{'identity' :?0, 'emotion': ?1}")
    void deleteImageMatchingAttributes(String characterIdentity, String emotionalState);

    @DeleteQuery("{'identity' :?0}")
    void deleteImageIdentity(String characterIdentity);

    @Query("{ 'path':{$regex:?0}, 'type' : ?1 }")
    List<Image> getImagesOnPath(String pathRegex, String category );
}