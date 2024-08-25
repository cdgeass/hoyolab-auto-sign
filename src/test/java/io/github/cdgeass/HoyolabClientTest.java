package io.github.cdgeass;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@MicronautTest
public class HoyolabClientTest {

    private static final Logger logger = LoggerFactory.getLogger(HoyolabClientTest.class);

    @Inject
    HoyolabClient client;

    private static final String COOKIE = "";

    private static final String YS_ACT_ID = "e202102251931481";
    private static final String SR_ACT_ID = "e202303301540311";
    private static final String ZZZ_ACT_ID = "e202406031448091";

    @Test
    void testGetUserInfo() {
        var userInfoResponse = client.getUserInfo(COOKIE);
        var userInfo = userInfoResponse.body();
        logger.debug("userInfo: {}", userInfo);
    }

    @Test
    void testGetYsSignInfo() {
        var signInfoResponse = client.getYsSignInfo(COOKIE, YS_ACT_ID);
        var signInfo = signInfoResponse.body();
        logger.debug("signInfo: {}", signInfo);
    }

    @Test
    void testSignYs() {
        var signResponse = client.signYs(COOKIE, YS_ACT_ID);
        logger.debug("response: {}", signResponse);
    }

    @Test
    void testGetSrSignInfo() {
        var signInfoResponse = client.getSrSignInfo(COOKIE, SR_ACT_ID);
        var signInfo = signInfoResponse.body();
        logger.debug("signInfo: {}", signInfo);
    }

    @Test
    void testSignSr() {
        var signResponse = client.signSr(COOKIE, SR_ACT_ID);
        logger.debug("response: {}", signResponse);
    }

    @Test
    void testGetZzzSignInfo() {
        var signInfoResponse = client.getZzzSignInfo(COOKIE, ZZZ_ACT_ID);
        var signInfo = signInfoResponse.body();
        logger.debug("signInfo: {}", signInfo);
    }

    @Test
    void testSignZzz() {
        var signResponse = client.signZzz(COOKIE, ZZZ_ACT_ID);
        logger.debug("response: {}", signResponse);
    }
}
