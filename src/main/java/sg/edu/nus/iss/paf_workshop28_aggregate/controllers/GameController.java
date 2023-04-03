package sg.edu.nus.iss.paf_workshop28_aggregate.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.nus.iss.paf_workshop28_aggregate.models.GameReview;
import sg.edu.nus.iss.paf_workshop28_aggregate.models.Response;
import sg.edu.nus.iss.paf_workshop28_aggregate.services.GameService;

@RestController
@RequestMapping
public class GameController {

    @Autowired
    GameService gameSvc;
    
    /*
     *  GET /game/<game_id>/reviews
        Accept: application/json
     */
    @GetMapping(path="game/{game_id}/reviews", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getGameWithReview(@PathVariable String game_id){

        Optional<GameReview> result = gameSvc.findGameReviewById(game_id);
        if(result.isPresent()){
            return new ResponseEntity<>(result.get().toJson().toString(),HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Game ID not found",HttpStatus.NOT_FOUND);
        }
        
    }

    /*
     *  GET /games/highest (and lowest)
        Accept: application/json
     */
    @GetMapping(path="games/{sortBy}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response> getGamesByRanking(@PathVariable String sortBy){

        Response response = gameSvc.findGamesSortByRanking(sortBy);
        return ResponseEntity.ok().body(response);
    }

}
