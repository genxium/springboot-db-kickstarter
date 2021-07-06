package com.mytrial.auth;

import com.mytrial.app.security.DatabasePasswordEncoder;
import com.mytrial.app.security.GrantedRolesMapper;
import com.mytrial.app.shardingsphereds.BeanExporter;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit test for simple App.
 */
@RunWith(SpringRunner.class)
@SpringBootTest // Includes "@ExtendsWith(SpringExtension.class)"
public class AppTest {
    /**
     * SpringContext and autowiring references
     * - https://www.baeldung.com/spring-boot-testing.
     * */

    private static final Logger logger = LoggerFactory.getLogger(AppTest.class);

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private BeanExporter beanExporter;

    @Autowired
    private DatabasePasswordEncoder passwordEncoder;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        final String principal = "player_1";
        final String rawPassword = "player_1";
        final String displayName = "player_1";

        final byte[] passwordUtf8Bytes = StringUtils.getBytesUtf8(rawPassword);
        final String sha1edPasswordUtf8Hex = DigestUtils.sha1Hex(passwordUtf8Bytes);
        final String saltedPassword = passwordEncoder.encode(sha1edPasswordUtf8Hex);
        final int roles = GrantedRolesMapper.ROLE_LEARNER;
        final long createdAt = System.currentTimeMillis();
        final long updatedAt = createdAt;

        try {
            final String insertQ = "INSERT INTO player(principal, salted_password, display_name, roles, created_at, updated_at) VALUES(?, ?, ?, ?, ?, ?)";
            int rowsInserted = beanExporter.getJdbcTemplate().update(insertQ, principal, saltedPassword, displayName, roles, createdAt, updatedAt);
            if (1 == rowsInserted) {
                logger.info("Player1 for testing is created.");
            }
        } catch (Exception ex) {
            logger.warn("Player1 for testing is not created, maybe it's already existent.", ex);
        }
    }

    @Test
    public void loginPlayer1() throws Exception {
        /**
         * Or by curl
         * ```
         * curl -i -v -X POST -d 'principal=player_1' -d 'password=d2fb021cf736725ade06783d96377a818d0cfe2b' -d 'remember-me=true' http://localhost:10570/login
         * ```
         * */
        final String rawPassword = "player_1";
        final byte[] passwordUtf8Bytes = StringUtils.getBytesUtf8(rawPassword);
        final String sha1edPasswordUtf8Hex = DigestUtils.sha1Hex(passwordUtf8Bytes);

        final String principal = "player_1";
        RequestBuilder requestBuilder = post("/login")
                .param("principal", principal)
                .param("password", sha1edPasswordUtf8Hex)
                .param("remember-me", "true");

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void recoverWalledReqByPlayer1() throws Exception {
        /**
         * Or by curl approach#1 ("JSESSIONID" approach)
         * ```
         * Step1> curl -i -v -X GET http://localhost:10570/Walled
         * Step2> curl -i --cookie JSESSIONID=XXXXXXXXXX -d 'principal=player_1' -d 'password=d2fb021cf736725ade06783d96377a818d0cfe2b' -d 'remember-me=true' -v -X POST http://localhost:10570/login
         * ```
         *
         * * Or by curl approach#2 ("targetUrlParameter" approach)
         * ```
         * Step1> curl -i -v -X GET http://localhost:10570/Walled
         * Step2> curl -i -d 'principal=player_1' -d 'password=d2fb021cf736725ade06783d96377a818d0cfe2b' -d 'remember-me=true' -v -X POST http://localhost:10570/login?targetUrl=http%3A%2F%2Flocalhost%3A10570%2FWalled
         * ```
         *
         * Both "Step2" calls will return an "HttpResponseHeader{Status: 302, Set-Cookie: remember-me=Yyyyyyyyyyy, Location: http://localhost:10570/Walled}" with no "HttpResponseBody".
         * */
    }
}
