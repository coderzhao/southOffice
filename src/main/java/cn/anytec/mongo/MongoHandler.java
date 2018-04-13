package cn.anytec.mongo;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class MongoHandler {

    private ExecutorService dataSaveThreadPool = Executors.newFixedThreadPool(3);

    @Autowired
    DataSaveRunnable dataSaveRunnable;

    public void notifyMongo(JSONObject json){
        dataSaveRunnable.setJsonData(json);
        dataSaveThreadPool.execute(dataSaveRunnable);
    }
}
