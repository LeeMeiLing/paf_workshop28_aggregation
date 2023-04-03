package sg.edu.nus.iss.paf_workshop28_aggregate.models;

import org.bson.Document;

public class Game {
    /*
     * {
        _id: <game id>,
        name: <board game name>,
        rating: <the highest or lowest rating>,
        user: <the user who gave that rating>,
        comment: <the associated comment>,
        review_id: <the review id>
        }
     */
    private String _id;
    private String name;
    private Integer rating;
    private String user;
    private String comment;
    private String review_id;
    public String get_id() {
        return _id;
    }
    public void set_id(String _id) {
        this._id = _id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getRating() {
        return rating;
    }
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public String getReview_id() {
        return review_id;
    }
    public void setReview_id(String review_id) {
        this.review_id = review_id;
    }
    @Override
    public String toString() {
        return "Game [_id=" + _id + ", name=" + name + ", rating=" + rating + ", user=" + user + ", comment=" + comment
                + ", review_id=" + review_id + "]";
    }

    public static Game toGame(Document d){

        Game game = new Game();
        game.set_id(d.getObjectId("_id").toString());
        game.setName(d.getString("name"));
        game.setRating(d.getInteger("rating"));
        game.setUser(d.getString("user"));
        game.setComment(d.getString("comment"));
        game.setReview_id(d.getObjectId("review_id").toString());
        return game;
    }
    
}
