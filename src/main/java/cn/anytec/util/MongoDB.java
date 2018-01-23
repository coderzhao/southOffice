package cn.anytec.util;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Collections;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.push;

public class MongoDB {
    private static MongoDB instance;
    private  final MongoClient mongoClient = new MongoClient("127.0.0.1");
    private  final MongoDatabase database = mongoClient.getDatabase("southOffice");
    private  final MongoCollection<Document> collection = database.getCollection("reslutRecord");

    public static MongoDB getInstance() {
        if (instance == null){
            synchronized(MongoDB.class){
                if (instance == null)
                    instance = new MongoDB();
            }
        }
        return instance;
    }

    private MongoDB() {
    }


    private int insertIntoRecord(){

        return 1;
    }
    private int addCustomer(String customerId,String personId){
        FindIterable<Document> documents= collection.find(eq("customer_id",customerId));
//        if (null!=documents.first()){
//            //用户已经存在
//            return 0;
//        }
        collection.insertOne(new Document("customer_id",customerId).append("person_id",personId));
        return 1;
    }

    private List<String> getVideosById(String customerId){
        Document document= collection.find(eq("customer_id",customerId)).first();
        if(null==document||!document.containsKey("videos")){
            return Collections.emptyList();
        }
        List<String> videos = (List<String>) document.get("videos");
        return videos;
    }

    private int deleteById(String customerId){
//        if(null==collection.find(eq("customer_id",customerId))){
//            return 1;
//        }
        collection.deleteOne(eq("customer_id",customerId));
        return 0;
    }
    private int count(String customerId){
//        if(null==collection.find(eq("customer_id",customerId))){
//            return 1;
//        }
        BasicDBObject dbObject =new BasicDBObject();
        dbObject.put("customer_id",customerId);
//        System.out.println(collection.find(dbObject).first()+"ffff");
        System.out.println(collection.count());
        return 0;
    }
    private int addVideoToCustomer(String videoUrl,String customerId){
//        List<String> videos = getVideosById(customerId);
        // see what it looks like in JSON (on the first run you will notice that it has got an "_id" but no "Subscribed Topics" array yet)

        Document document = collection.find(eq("customer_id", customerId)).first();
        if(document == null)
        {
            return 1;
        }
        Bson filter = eq("customer_id", customerId);
        Bson change = push("videos", videoUrl);
        collection.updateOne(filter, change);
//        document = collection.find(eq("customer_id", videoUrl)).first();
        return 0;
    }

    public static void main(String[] args) {
        System.out.println(MongoDB.getInstance().addCustomer("abcdfffff","efgsssss"));
//        System.out.println(MongoDB.getInstance().getVideosById("abcd"));
//        MongoDB.getInstance().addVideoToCustomer("abcd","abcd");
//        MongoDB.getInstance().deleteById("abcd");
        MongoDB.getInstance().count("abcdfffff");
//        System.out.println(MongoDB.getInstance().getVideosById("abcd"));

    }
}
