package com.obi.pandanow.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.GetMapping;


import java.util.ArrayList;
import java.util.List;
@Controller
@RequestMapping("/masai")
public class UsersController {


    @GetMapping
    public String shi(Model model) {
        HttpHeaders headers = new HttpHeaders();


        String url = "https://api.pexels.com/videos/videos/15529198";

        // Use your REAL API key with "Bearer " prefix
        final String PEXELS_KEY = "UNd8P2jHZDckiFIHRvNgLJlaph07mkMPa961HyBYpdJOERuGZDObDvFm";

//        String url = "https://api.pexels.com/videos/videos/8334109";
//        final String PEXELS)_KEY = ("API"
        headers.set("Authorization", PEXELS_KEY);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        List<String> videoUrls = new ArrayList<>();

        try {
            tools.jackson.databind.ObjectMapper mapper = new tools.jackson.databind.ObjectMapper();
            tools.jackson.databind.JsonNode root = mapper.readTree(response.getBody());
            tools.jackson.databind.JsonNode videoFiles = root.path("video_files");

            if (videoFiles.isArray() && videoFiles.size() > 0) {
                for (tools.jackson.databind.JsonNode file : videoFiles) {
                    String videoUrl = file.path("link").asText();
                    if (file.path("quality").asText().equals("hd")) {
                        videoUrls.add(videoUrl);
                    }
                }
            }

            if (videoUrls.isEmpty() && videoFiles.size() > 0) {
                String videoUrl = videoFiles.get(0).path("link").asText();
                videoUrls.add(videoUrl);
            }
        } catch (Exception e) {
            e.printStackTrace();
            videoUrls.add("https://player.vimeo.com/external/577442552.hd.mp4?s=854da78e77f42d95a2cda289c1b0b4bda9e8a325&profile_id=175");
        }

        model.addAttribute("videos", videoUrls);
        return "me/index";
    }

}
