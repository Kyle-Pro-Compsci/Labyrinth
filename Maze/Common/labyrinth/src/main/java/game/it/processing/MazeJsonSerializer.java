package game.it.processing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import game.model.Position;

import java.util.List;

/**
 * Serialization methods for Labyrinth models.
 */
public class MazeJsonSerializer {
    ObjectMapper mapper;

    public MazeJsonSerializer() {
        this.mapper = new ObjectMapper();
    }

    public String reachablePositionsToJson(List<Position> reachablePositionsList) throws JsonProcessingException {
        return this.mapper.writeValueAsString(reachablePositionsList);
    }
}
