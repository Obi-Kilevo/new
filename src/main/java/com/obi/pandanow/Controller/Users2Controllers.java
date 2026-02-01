package com.obi.pandanow.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;



//    UNd8P2jHZDckiFIHRvNgLJlaph07mkMPa961HyBYpdJOERuGZDObDvFm


    @Controller

    @RequestMapping("/masai2")
    public class Users2Controllers {

            @GetMapping
            public String home(Model model) {
                HttpHeaders headers = new HttpHeaders();
                final String PEXELS_KEY = "UNd8P2jHZDckiFIHRvNgLJlaph07mkMPa961HyBYpdJOERuGZDObDvFm";
                headers.set("Authorization", PEXELS_KEY);
                RestTemplate restTemplate = new RestTemplate();

                String[] thirdIds = {"34776281"};
                List<String> thirdUrls = new ArrayList<>();
                for (String id : thirdIds) {
                    thirdUrls.add(fetchVideo(id, headers, restTemplate));
                }
                model.addAttribute("videos", thirdUrls);
                model.addAttribute("thirdVideoUrl", thirdUrls.get(0));

                return "me2/index";
            }

            private String fetchVideo(String videoId, HttpHeaders headers, RestTemplate restTemplate) {
                String url = "https://api.pexels.com/videos/videos/" + videoId;
                try {
                    ResponseEntity<String> response = restTemplate.exchange(
                            url,
                            HttpMethod.GET,
                            new HttpEntity<>(headers),
                            String.class
                    );

                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(response.getBody());
                    return root.path("video_files").get(0).path("link").asText();

                } catch (Exception e) {
                    return "";
                }
            }
        }


