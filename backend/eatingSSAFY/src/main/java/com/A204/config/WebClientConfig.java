package com.A204.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@PropertySource("classpath:application.yaml")
public class WebClientConfig {

    @Value("${ocr.request_url}")
    private String OCR_REQUEST_URL;

    @Value("${ocr.secret.property_name}")
    private String OCR_PROPERTY_NAME;

    @Value("${ocr.secret.key}")
    private String OCR_SECRET_KEY;

    /**
     * OCR 서비스를 이용하기 위한 WebClient Bean 등록
     */
    @Bean(name = "ocrWebClient")
    public WebClient ocrWebClient() {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));

        ObjectMapper objectMapper = new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL) // Null 필드 무시
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // DTO에 정의되지 않은 필드 무시
        ExchangeStrategies exchangeStrategies = ExchangeStrategies
                .builder()
                .codecs(clientCodecConfigurer -> {
                    clientCodecConfigurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper, MediaType.APPLICATION_JSON));
                    clientCodecConfigurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper, MediaType.APPLICATION_JSON));
                })
                .build();

        return WebClient.builder()
                .exchangeStrategies(exchangeStrategies)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                    httpHeaders.add(OCR_PROPERTY_NAME, OCR_SECRET_KEY);
                })
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(OCR_REQUEST_URL)
                .build();
    }
}
