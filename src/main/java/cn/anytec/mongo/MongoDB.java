package cn.anytec.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.mongodb.client.model.Filters.*;

@Service
public class MongoDB {

    private static final Logger logger = LoggerFactory.getLogger(MongoDB.class);

    private final MongoClient mongoClient = new MongoClient("127.0.0.1");
    private final MongoDatabase database = mongoClient.getDatabase("nantou");
    private final MongoCollection<Document> faceCollection = database.getCollection("face");
    // private final MongoCollection<Document> photoCollection = database.getCollection("photo");
    private JSONParser jsonParser = new JSONParser();

    public MongoDB() {
        logger.info("======= 初始化MongoDB =======");
    }


    public boolean insertFace(JSONObject match) {
        //String photoId = (String)match.get("photoId");
        String matchFace = (String) match.get("matchFace");
        String photo = (String) match.get("photo");
        String base64Face = (String) match.get("face");
        String meta = (String) match.get("meta");
        String camera = (String) match.get("camera");
        String coordinate = (String) match.get("coordinate");
        String confidence = match.get("confidence").toString();
        boolean friend = (boolean) match.get("friend");
        long matchId = (long) match.get("id");
        String timestamp = match.get("timestamp").toString();
//        Date date = parseTime(timestamp);
        faceCollection.insertOne(new Document("id", System.currentTimeMillis())
                //      .append("photoId",photoId)
                .append("matchId", matchId)
                .append("photo", photo)
                .append("matchFace", matchFace)
                .append("meta", meta)
                .append("camera", camera)
                .append("face", base64Face)
                .append("confidence", confidence)
                .append("coordinate", coordinate)
                .append("friend", friend)
                .append("timestamp", timestamp)
        );
        return true;
    }
   /* public boolean insertPhoto(String photoId,String base64Photo){
        photoCollection.insertOne(new Document("ID",photoId)
                .append("photo",base64Photo));
        return true;
    }*/

  /*  public String getPhoto(String ID){
        FindIterable<Document> documents= photoCollection.find(eq("ID",ID));
        if(null == documents.first()){
            logger.warn("图片不存在");
            return null;
        }
        return (String)documents.first().get("photo");
    }*/

    public List<JSONObject> getFaceByCondition(Map<String, String[]> params) {
        Integer offset = 0;
        Integer limit = 10;
        String meta = "";
        String camera = "";
        if (params.containsKey("offset")) {
            offset = Integer.parseInt(params.get("offset")[0]);
        }

        if (params.containsKey("limit")) {
            limit = Integer.parseInt(params.get("limit")[0]);
        }

        if (params.containsKey("meta")) {
            meta = params.get("meta")[0];
        }

        if (params.containsKey("camera")) {
            camera = params.get("camera")[0];
        }

        if (!meta.equals("") && !camera.equals("")) {
            BasicDBObject dbObject = new BasicDBObject();
            dbObject.put("meta", meta);
            dbObject.put("camera", camera);
            FindIterable<Document> documents = faceCollection.find(dbObject).skip(limit * offset).limit(limit).sort(new BasicDBObject("timestamp",-1));
            if (null == documents) {
                return getResultJson(faceCollection.find(dbObject).limit(limit).sort(new BasicDBObject("timestamp",-1)));
            }
            return getResultJson(documents);
        } else if (!meta.equals("")) {
            FindIterable<Document> documents = faceCollection.find(eq("meta", meta)).skip(limit * offset).limit(limit).sort(new BasicDBObject("timestamp",-1));
            if (null == documents.first())
                documents = faceCollection.find(eq("meta", meta)).limit(limit).sort(new BasicDBObject("timestamp",-1));
            return getResultJson(documents);
        } else if (!camera.equals("")) {
            FindIterable<Document> documents = faceCollection.find(eq("camera", camera)).skip(limit * offset).limit(limit);
            if (null == documents.first())
                documents = faceCollection.find(eq("camera", camera)).limit(limit).sort(new BasicDBObject("timestamp",-1));
            return getResultJson(documents);
        }
        return getResultJson(faceCollection.find().skip(limit * offset).limit(limit).sort(new BasicDBObject("timestamp",-1)));
    }
//        if(params.containsKey("start")&&!params.containsKey("end")){
//            String time1 = params.get("start")[0];
//            Date start = parseTime(time1);
//            FindIterable<Document> documents = faceCollection.find(gte("timestamp",start));
//            if(null == documents.first())
//                return null;
//            return getResultJson(documents);
//        }
//        if(params.containsKey("end")&&!params.containsKey("start")){
//            String time2 = params.get("end")[0];
//            Date end = parseTime(time2);
//            FindIterable<Document> documents = faceCollection.find(lte("timestamp",end));
//            if(null == documents.first())
//                return null;
//            return getResultJson(documents);
//        }


    public int getFaceNumByCondition(Map<String, String[]> params) {
        String meta = "";
        String camera = "";
        if (params.containsKey("meta")) {
            meta = params.get("meta")[0];
        }

        if (params.containsKey("camera")) {
            camera = params.get("camera")[0];
        }

        if (null != meta && !meta.equals("") && null != camera && !camera.equals("")) {
            BasicDBObject dbObject = new BasicDBObject();
            dbObject.put("meta", meta);
            dbObject.put("camera", camera);
//            FindIterable<Document> documents= faceCollection.find(dbObject);
            return Integer.parseInt(faceCollection.count(dbObject) + "");
//            return getResultJson(documents).size();
//            return faceCollection.count(dbObject);
        } else if (null != meta && !meta.equals("")) {
//            FindIterable<Document> documents= faceCollection.find(eq("meta",meta));
//            if(null == documents.first())
//                return null;
            return Integer.parseInt(faceCollection.count(eq("meta", meta)) + "");
//            return getResultJson(documents).size();
        } else if (null != camera && !camera.equals("")) {
            return Integer.parseInt(faceCollection.count(eq("camera", camera)) + "");
//            FindIterable<Document> documents= faceCollection.find(eq("camera",camera));
//            if(null == documents.first())
//                return null;
//            return getResultJson(documents).size();
        }
        int faceNum=Integer.parseInt(faceCollection.count() + "");
//        logger.info("num"+faceNum);
        return Integer.parseInt(faceCollection.count() + "");
    }

    private List<JSONObject> getResultJson(FindIterable<Document> documents) {
        List<JSONObject> result = new ArrayList<>();
        Iterator<Document> iterator = documents.iterator();
        while (iterator.hasNext()) {
            Document document = iterator.next();
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject) jsonParser.parse(document.toJson());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            result.add(jsonObject);
        }
        return result;
    }

    private JSONArray getResultJsonJson(FindIterable<Document> documents) {
        JSONArray result = new JSONArray();
        Iterator<Document> iterator = documents.iterator();
        while (iterator.hasNext()) {
            Document document = iterator.next();
            JSONObject jsonObject = null;
            try {
                jsonObject = (JSONObject) jsonParser.parse(document.toJson());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            result.add(jsonObject);
        }
        return result;
    }

    private Date parseTime(String time) {
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(time);
            // localDateTime = localDateTime.plusHours(Long.parseLong(Constant.TIME_PLUS_HOURS));
            int year = localDateTime.getYear();
            int mouth = localDateTime.getMonthValue();
            int day = localDateTime.getDayOfMonth();
            int hour = localDateTime.getHour();
            int minute = localDateTime.getMinute();
            int second = localDateTime.getSecond();
            long timestamp = LocalDateTime.of(year, mouth, day, hour, minute, second).atZone(ZoneId.systemDefault()).
                    toInstant().toEpochMilli();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String d = format.format(timestamp);
            return format.parse(d);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        logger.error("时间转换失败");
        return null;
    }


}
