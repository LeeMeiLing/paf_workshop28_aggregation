package sg.edu.nus.iss.paf_workshop28_aggregate.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.paf_workshop28_aggregate.models.Game;
import sg.edu.nus.iss.paf_workshop28_aggregate.models.GameReview;
import sg.edu.nus.iss.paf_workshop28_aggregate.models.Response;
import sg.edu.nus.iss.paf_workshop28_aggregate.repositories.GameRepository;

@Service
public class GameService {

    @Autowired
    GameRepository gameRepo;

    public Optional<GameReview> findGameReviewById(String game_id){

        
        Optional<Document> result = gameRepo.findGameReviewById(game_id);

        if(result.isPresent()){

            System.out.println(" >>>> in service");
            System.out.println(result.get());

            Integer gid = result.get().getInteger("gid");

            // Use aggregate query to get average rating of the game from comments
            Float average = gameRepo.findAverageRating(gid);

            GameReview gameReview = GameReview.createGameReview(result.get().toJson(), average);

            return Optional.of(gameReview);
        }

        return Optional.empty();
        
    }
    
    public Response findGamesSortByRanking(String sortBy){

        List<Document> docs = gameRepo.findGamesSortByRanking(sortBy);

        List<Game> games = docs.stream().map(Game::toGame).collect(Collectors.toList());
        
        Response response = new Response();
        response.setRating(sortBy);
        response.setGames(games);
        response.setTimestamp(new Date());
        
        return response;

    }
}
