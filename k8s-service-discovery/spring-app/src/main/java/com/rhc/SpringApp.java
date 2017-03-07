package com.rhc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableAutoConfiguration
@Controller
public class SpringApp {

    private static String HOSTNAME = null;
    private List<String> pods = new ArrayList<>();

    public static void main(String[] args) {
        SpringApplication.run(SpringApp.class, args);
        HOSTNAME = System.getenv("HOSTNAME") == null ? LocalDateTime.now(ZoneId.of(ZoneId.SHORT_IDS.get("PST"))).toString() :System.getenv("HOSTNAME");

    }

    @RequestMapping(value = "/", produces = { MediaType.TEXT_HTML_VALUE })
    @ResponseBody
    String helloWorld() {
        String vertxUrl = System.getenv("VERTX_URL");
        if (vertxUrl == null || vertxUrl.isEmpty()) {
            vertxUrl = "http://localhost:8082";
        }
        RestTemplate rest = new RestTemplate();
        String response = rest.getForObject(vertxUrl, String.class);
        return String.format("<p>Hello World from Spring <strong>%s</strong>.</p> <p>I'm going to call Vert.x at <strong>%s</strong>.</p><p>Here is Vert.x's response: %n%n%s</p>", HOSTNAME, vertxUrl, response);
    }
    
    @RequestMapping(value = "/vertxpods", method = RequestMethod.PUT, consumes = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<Void> createPodList(@RequestBody List<String> pods) {
        this.pods.clear();
        this.pods.addAll(pods);
        
        return new ResponseEntity<Void>(HttpStatus.OK);
    }
    
    @RequestMapping(value = "/vertxpods", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
    ResponseEntity<List<String>> getPodList() {
        return new ResponseEntity<List<String>>(pods, HttpStatus.OK);
    }
}
