package cn.anytec.mongo;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataSaveRunnable implements Runnable {

    private JSONObject data;
    private ThreadLocal<JSONObject> dataThreadLocal = new ThreadLocal<>();

    @Autowired
    MongoDB mongoDB;

    @Override
    public void run() {
        dataThreadLocal.set(data);
        JSONObject jsonObject = dataThreadLocal.get();
        try {
            //  String base64Photo = (String)jsonObject.get("photo");
            // String photoId = (String)jsonObject.get("photoId");
            //   mongoDB.insertPhoto(photoId,base64Photo);

            JSONArray results = (JSONArray) jsonObject.get("results");
            for (int i = 0; i < results.size(); i++) {
                JSONObject match = (JSONObject) results.get(i);
                Set<String> keySet = jsonObject.keySet();
                keySet.stream().forEach((key)->{
                    if(!key.equals("results")){
                        match.put(key,jsonObject.get(key));
                    }
                });
                mongoDB.insertFace(match);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        dataThreadLocal.remove();
    }

    public void setJsonData(JSONObject json) {
        data = json;
    }
}
