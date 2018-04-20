package cn.anytec.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
//@ConfigurationProperties(prefix = "sdk")
public class AppConfig {

    @Value("${app.sdk.token}")
    private String SDKToken;

    @Value("${app.sdk.hostIpPort}")
    private String SDKHostIpPort;

    @Value("${app.hostIpPort}")
    private String hostIpPort;

    @Value("${app.storage.drawFace}")
    private String drawFace;

    @Value("${app.storage.cutFace}")
    private String cutFace;

    @Value("${app.register.url}")
    private String registerUrl;

    public String getSDKToken() {
        return SDKToken;
    }

    public String getSDKHostIpPort() {
        return SDKHostIpPort;
    }

    public String getHostIpPort() {
        return hostIpPort;
    }

    public String getDrawFace() {
        return drawFace;
    }

    public String getCutFace() {
        return cutFace;
    }

    public String getRegisterUrl() {
        return registerUrl;
    }
}
