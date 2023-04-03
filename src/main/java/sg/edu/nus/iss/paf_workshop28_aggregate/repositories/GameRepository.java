package sg.edu.nus.iss.paf_workshop28_aggregate.repositories;

import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import com.mongodb.internal.operation.AggregateOperation;

@Repository
public class GameRepository {
    
    @Autowired
    private MongoTemplate mongoTemplate;

    /*
     * db.games.aggregate([
        {
            $match: { _id: ObjectId("6427051274977736e832f5fa")}
        },
        {
            $lookup: {
                from: "comments",
                foreignField: "gid",
                localField: "gid",
                as: "reviews"
            }
        },
        {
            $project: {
                "_id" : 1,
                "gid" : 1,
                "name" : 1,
                "year" : 1,
                "ranking" : 1,
                "users_rated" : 1,
                "url" : 1,
                "image" : 1,
                "reviews": 1,
                "timestamp": new Date()     //In this app, timestamp will be created in java instead of mongo
            }
        }
        
    ])
     */
    public Optional<Document> findGameReviewById(String game_id){

        System.out.println(">>> in repo find GameReview By Id");
        
        try{
            ObjectId id = new ObjectId(game_id); // this will throw error if game_id is invalid format

            // stages
            MatchOperation matchById = Aggregation.match(Criteria.where("_id").is(id));
            LookupOperation lookupComments = Aggregation.lookup("comments", "gid", "gid", "reviews");

            // pipeline
            Aggregation pipeline = Aggregation.newAggregation(matchById,lookupComments);

            // perform query & get result
            AggregationResults<Document> results = mongoTemplate.aggregate(pipeline, "games", Document.class);

           
            // results.getMappedResults();      // this return List<Document>
            // results.getUniqueMappedResult(); // this return Document, throw error if more than one result

            List<Document> doc = results.getMappedResults();
            System.out.println(doc);
            // System.out.println(doc.toJson());
            return Optional.of(doc.get(0));

        }catch(Exception exception){
            return Optional.empty();
        }
    }


    /*
     * db.comments.aggregate([
            { $match: { _id : ObjectId("game_id") } },
            { $group: 
                { 
                    _id: "$gid" ,
                    count : { $sum:1 },
                    average: { $avg: "$rating" }
                }
            }
        ])
     */
    public Float findAverageRating(Integer gid){

        // create operation
        MatchOperation matchByGid = Aggregation.match(Criteria.where("gid").is(gid));
        GroupOperation groupByGid = Aggregation.group("gid")
                                                .count().as("count") // count not needed in this app
                                                .avg("rating").as("average");
                                
        // create pipeline
        Aggregation pipeline = Aggregation.newAggregation(matchByGid, groupByGid);
        
        // perform query: mongoTemplate.aggregate(pipeline, collection_name, return_type)
        AggregationResults<Document> results = mongoTemplate.aggregate(pipeline, "comments",Document.class);
        System.out.println(" >>> find average result");
        System.out.println(results.getMappedResults());

        Float averate_rating = results.getUniqueMappedResult().getDouble("average").floatValue(); // document number to float

        return averate_rating;

    }

    /*
     * db.games.aggregate([
            {
                $lookup: {
                    from: "comments",
                    foreignField: "gid",
                    localField: "gid",
                    as: "reviews"
                }
            },
            {
                $unwind: "$reviews"
            },
            {
                $sort:{"reviews.rating":-1}
            },
            {
                $group:{
                    _id:"$_id",
                    gid:{ $first: "$gid"},
                    name: { $first: "$name"},
                    rating: { $first: "$reviews.rating"},
                    user: { $first: "$reviews.user"},
                    comment:{ $first: "$reviews.c_text"},
                    review_id:{ $first: "$reviews._id"},
                    
                }
            
            }

        ])
     */
    public List<Document> findGamesSortByRanking(String sortBy){

        // stages // add index to gid got "games" & "comments"
        LookupOperation lookupComments = Aggregation.lookup("comments", "gid", "gid", "reviews");
        UnwindOperation unwind = Aggregation.unwind("reviews");
        SortOperation sortByRating = null;

        if(sortBy.equals("highest")){
            sortByRating = Aggregation.sort(Direction.DESC, "reviews.rating");
        }else if(sortBy.equals("lowest")){
            sortByRating = Aggregation.sort(Direction.ASC, "reviews.rating");
        }

        // if(sortByRating == null){
        //     throw new Exception();
        // }
 
        GroupOperation groupByGid = Aggregation.group("_id")
                                                 .first("name").as("name")
                                                 .first("reviews.rating").as("rating")
                                                 .first("reviews.user").as("user")
                                                 .first("reviews.c_text").as("comment")
                                                 .first("reviews._id").as("review_id");

        // pipeline
        Aggregation pipeline = Aggregation.newAggregation(lookupComments,unwind,sortByRating,groupByGid);

        // perform query & get result
        AggregationResults<Document> results = mongoTemplate.aggregate(pipeline, "games", Document.class);

        List<Document> docs = results.getMappedResults();
        
        return docs;


    }

}
