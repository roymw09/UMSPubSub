package org.ac.cst8277.williams.roy;

import org.ac.cst8277.williams.roy.model.Publisher;
import org.ac.cst8277.williams.roy.model.Subscriber;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
public class UserManagementServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserManagementServiceApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ReactiveRedisOperations<String, Publisher> publisherTokenTemplate(LettuceConnectionFactory lettuceConnectionFactory){
        RedisSerializer<Publisher> valueSerializer = new Jackson2JsonRedisSerializer<>(Publisher.class);
        RedisSerializationContext<String, Publisher> serializationContext = RedisSerializationContext.<String, Publisher>newSerializationContext(RedisSerializer.string())
                .value(valueSerializer)
                .build();
        return new ReactiveRedisTemplate<>(lettuceConnectionFactory, serializationContext);
    }

    @Bean
    public ReactiveRedisOperations<String, Subscriber> subscriberTokenTemplate(LettuceConnectionFactory lettuceConnectionFactory){
        RedisSerializer<Subscriber> valueSerializer = new Jackson2JsonRedisSerializer<>(Subscriber.class);
        RedisSerializationContext<String, Subscriber> serializationContext = RedisSerializationContext.<String, Subscriber>newSerializationContext(RedisSerializer.string())
                .value(valueSerializer)
                .build();
        return new ReactiveRedisTemplate<>(lettuceConnectionFactory, serializationContext);
    }

    @Bean
    LettuceConnectionFactory lettuceConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName("redis-cache-server"); // redis-cache-server
        redisStandaloneConfiguration.setPort(6379);

        LettuceClientConfiguration.LettuceClientConfigurationBuilder lettuceClientConfigurationBuilder = LettuceClientConfiguration.builder();

        return new LettuceConnectionFactory(redisStandaloneConfiguration, lettuceClientConfigurationBuilder.build());
    }
}
