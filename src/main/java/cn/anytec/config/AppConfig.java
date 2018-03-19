package cn.anytec.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
//@ConfigurationProperties(prefix = "sdk")
public class AppConfig {

    @Value("${app.sdk.token}")
    private String SDKToken;

    @Value("${app.sdk.uri}")
    private String SDKURI;

    @Value("${app.uri}")
    private String URI;

    @Value("${app.storage.drawFace}")
    private String drawFace;

    @Value("${app.storage.cutFace}")
    private String cutFace;

    public String getSDKToken() {
        return SDKToken;
    }

    public void setSDKToken(String SDKToken) {
        this.SDKToken = SDKToken;
    }

    public String getSDKURI() {
        return SDKURI;
    }

    public void setSDKURI(String SDKURI) {
        this.SDKURI = SDKURI;
    }

    public String getURI() {
        return URI;
    }

    public String getDrawFace() {
        return drawFace;
    }

    public void setDrawFace(String drawFace) {
        this.drawFace = drawFace;
    }

    public String getCutFace() {
        return cutFace;
    }

    public void setCutFace(String cutFace) {
        this.cutFace = cutFace;
    }
}
