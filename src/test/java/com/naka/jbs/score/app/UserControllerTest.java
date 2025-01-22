package com.naka.jbs.score.app;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
import java.util.Objects;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;

import com.naka.jbs.score.RedisProperties;

import redis.embedded.RedisServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ContextConfiguration
@ActiveProfiles("test")
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class })
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RedisProperties redisProperties;
    private RedisServer redisServer;
    private static LettuceConnectionFactory lettuceConnectionFactory;

    public UserControllerTest(@Autowired LettuceConnectionFactory lettuceConnectionFactory) {
        UserControllerTest.lettuceConnectionFactory = lettuceConnectionFactory;
    }

    @BeforeEach
    public void setup() throws IOException {
        if (Objects.isNull(redisServer)) {
            redisServer = new RedisServer(redisProperties.getRedisPort());
            redisServer.start();
        }
    }

    @AfterAll
    public static void tearDown() {
        if (Objects.nonNull(lettuceConnectionFactory)) {
            lettuceConnectionFactory.destroy();
        }
    }

    @Test
    void testRedis() throws Exception {
        mockMvc.perform(post("/user")).andDo(print()).andExpect(status().isOk());
        mockMvc.perform(get("/user")).andDo(print()).andExpect(status().isOk());
    }

}
