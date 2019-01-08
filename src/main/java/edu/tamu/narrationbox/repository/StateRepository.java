package edu.tamu.narrationbox.repository;

import edu.tamu.narrationbox.model.State;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StateRepository extends MongoRepository<State,String> {
}
