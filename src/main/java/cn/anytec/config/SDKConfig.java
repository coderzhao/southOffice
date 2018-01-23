package cn.anytec.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sdk")
public class SDKConfig {

    private String TOKEN;
    private String URI;
    private String STOAGE;
    private String HOST;

    public String getSTOAGE() {
        return STOAGE;
    }

    public void setSTOAGE(String STOAGE) {
        this.STOAGE = STOAGE;
    }

    public String getHOST() {
        return HOST;
    }

    public void setHOST(String HOST) {
        this.HOST = HOST;
    }


    public void setTOKEN(String TOKEN) {
        this.TOKEN = TOKEN;
    }

    public void setURI(String URI) {
        this.URI = URI;
    }

    public String getTOKEN() {
        return TOKEN;
    }

    public String getURI() {
        return URI;
    }


}
