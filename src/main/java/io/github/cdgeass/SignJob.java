package io.github.cdgeass;

import java.util.Optional;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.context.ApplicationContext;
import io.micronaut.scheduling.annotation.Scheduled;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class SignJob {

    private static final Logger logger = LoggerFactory.getLogger(SignJob.class);

    private static final String YS_ACT_ID = "e202102251931481";
    private static final String SR_ACT_ID = "e202303301540311";
    private static final String ZZZ_ACT_ID = "e202406031448091";

    @Inject
    HoyolabClient client;

    @Inject
    ApplicationContext applicationContext;

    @PostConstruct
    void postConstruct() {
        executeSign();
    }
    
    @SuppressWarnings("unchecked")
    @Scheduled(cron = "0 0 8 * * ?")
    void executeSign() {
        logger.info("sign start");

        String cookie = getCookie().orElseThrow(() -> new RuntimeException("Please set you cookie"));

        var userInfoResponse = client.getUserInfo(cookie).body();
        var nickname = MapUtils.getString(MapUtils.getMap(MapUtils.getMap(userInfoResponse, "data"), "user_info"), "nickname");

        client.signYs(cookie, YS_ACT_ID);
        client.signSr(cookie, SR_ACT_ID);
        client.signZzz(cookie, ZZZ_ACT_ID);

        var ysSignInfoResponse = client.getYsSignInfo(cookie, YS_ACT_ID).body();
        var ysTotalSignDay = MapUtils.getString(MapUtils.getMap(ysSignInfoResponse, "data"), "total_sign_day");
        logger.info("已为 {} 签到原神，共签到 {} 天", nickname, ysTotalSignDay);

        var srSignInfoResponse = client.getSrSignInfo(cookie, YS_ACT_ID).body();
        var srTotalSignDay = MapUtils.getString(MapUtils.getMap(srSignInfoResponse, "data"), "total_sign_day");
        logger.info("已为 {} 签到星穹铁道，共签到 {} 天", nickname, srTotalSignDay);

        var zzzSignInfoResponse = client.getZzzSignInfo(cookie, YS_ACT_ID).body();
        var zzzTotalSignDay = MapUtils.getString(MapUtils.getMap(zzzSignInfoResponse, "data"), "total_sign_day");
        logger.info("已为 {} 签到绝区零，共签到 {} 天", nickname, zzzTotalSignDay);
    }

    public Optional<String> getCookie() {
        return applicationContext.getEnvironment().getProperty("cookie", String.class);
    }
}
