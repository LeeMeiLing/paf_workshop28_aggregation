package sg.edu.nus.iss.paf_workshop28_aggregate.models;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class GameReview {
    /*
     * {
            game_id: <ID field>,
            name: <Name field>,
            year: <Year field>,
            rank: <Rank field>,
            average: <Average field>,
            users_rated: <Users rated field>,
            url: <URL field>,
            thumbnail: <Thumbnail field>,
            reviews: [
            “/review/<review_id>”,
            “/review/<review_id>”
            ...
            ]
            timestamp: <result timestamp>
        }
     */

    private String game_id;
    private Integer gid;
    private String name;
    private Integer year;
    private Integer rank;
    private Float average;
    private Integer users_rated;
    private String url;
    private String thumbnail;
    private List<String> reviews = new ArrayList<>();
    private Date timestamp;

    public String getGame_id() {
        return game_id;
    }
    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }
    public Integer getGid() {
        return gid;
    }
    public void setGid(Integer gid) {
        this.gid = gid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getYear() {
        return year;
    }
    public void setYear(Integer year) {
        this.year = year;
    }
    public Integer getRank() {
        return rank;
    }
    public void setRank(Integer rank) {
        this.rank = rank;
    }
    public Float getAverage() {
        return average;
    }
    public void setAverage(Float average) {
        this.average = average;
    }
    public Integer getUsers_rated() {
        return users_rated;
    }
    public void setUsers_rated(Integer users_rated) {
        this.users_rated = users_rated;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getThumbnail() {
        return thumbnail;
    }
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    public List<String> getReviews() {
        return reviews;
    }
    public void setReviews(List<String> reviews) {
        this.reviews = reviews;
    }
    public Date getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
    @Override
    public String toString() {
        return "GameReview [game_id=" + game_id + ", gid=" + gid + ", name=" + name + ", year=" + year + ", rank="
                + rank + ", average=" + average + ", users_rated=" + users_rated + ", url=" + url + ", thumbnail="
                + thumbnail + ", reviews=" + reviews + ", timestamp=" + timestamp + "]";
    }

    public static GameReview createGameReview(String jsonStr, Float average_rating){

        GameReview gameReview = new GameReview();

        JsonReader reader = Json.createReader(new StringReader(jsonStr));
        JsonObject jo = reader.readObject();

        JsonObject oid = jo.getJsonObject("_id");
        gameReview.setGame_id(oid.getString("$oid"));

        gameReview.setGid(jo.getInt("gid"));
        gameReview.setName(jo.getString("name"));
        gameReview.setYear(jo.getInt("year"));
        gameReview.setRank(jo.getInt("ranking"));
        // gameReview.setAverage(jo.getJsonNumber("average").bigDecimalValue().floatValue()); // jsonNumber to float
        gameReview.setAverage(average_rating);
        gameReview.setUsers_rated(jo.getInt("users_rated"));
        gameReview.setUrl(jo.getString("url"));
        gameReview.setThumbnail(jo.getString("image"));

        // retrieve review id from reviews[]
        /*
            * reviews": [{" id": {"$oid": "6427052aoab15f0be75febd6"}, "c id": "Ofbb7913",
            "user": "gobbeg", "rating": 7, "c_text": "Very nicely produced." , "gid": 6}]} 

         */
        JsonArray arr = jo.getJsonArray("reviews");

        for(int i =0; i<arr.size(); i++){
            JsonObject o = arr.getJsonObject(i);
            JsonObject r_oid = o.getJsonObject("_id");
            String r_id = r_oid.getString("$oid");
            String temp = "/review/" + r_id;
            gameReview.getReviews().add(temp); // check if successfully appended
        }

        gameReview.setTimestamp(new Date());

        return gameReview; 
    }

    public JsonObject toJson(){

        /*
         * game_id: <ID field>,
            name: <Name field>,
            year: <Year field>,
            rank: <Rank field>,
            average: <Average field>,
            users_rated: <Users rated field>,
            url: <URL field>,
            thumbnail: <Thumbnail field>,
            reviews: [
            “/review/<review_id>”,
            “/review/<review_id>”
            ...
            ]
            timestamp: <result timestamp>
         */

        JsonArrayBuilder arrB = Json.createArrayBuilder();
        for(String s : reviews){
            arrB.add(s);
        }
        JsonArray ReviewArr = arrB.build();

        JsonObject json = Json.createObjectBuilder()
                                .add("game_id",game_id)
                                .add("name",name)
                                .add("year", year)
                                .add("rank", rank)
                                .add("average",average)
                                .add("users_rated", users_rated)
                                .add("url",url)
                                .add("thumbnail", thumbnail)
                                .add("reviews",ReviewArr)
                                .add("timestamp",timestamp.toString())
                                .build();
        return json;
    }
    
}
