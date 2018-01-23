package cn.anytec;

import cn.anytec.util.WsMessStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication {


    public static void main(String[] args) {

        WsMessStore.getInstance().startPushMessThread();
        SpringApplication.run(MainApplication.class, args);
    }
}
