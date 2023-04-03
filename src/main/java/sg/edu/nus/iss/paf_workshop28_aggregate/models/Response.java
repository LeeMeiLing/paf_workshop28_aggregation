package sg.edu.nus.iss.paf_workshop28_aggregate.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Response {
    /*
     * {
            rating: “highest” (or “lowest”),
            games: [
            <each element is the above document>,
            ]
            timestamp: <result timestamp>
        }
     */
    private String rating; 
    private List<Game> games = new ArrayList<>();
    private Date timestamp;
    public String getRating() {
        return rating;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }
    public List<Game> getGames() {
        return games;
    }
    public void setGames(List<Game> games) {
        this.games = games;
    }
    public Date getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    @Override
    public String toString() {
        return "Response [rating=" + rating + ", games=" + games + ", timestamp=" + timestamp + "]";
    }
    
    
    
}
