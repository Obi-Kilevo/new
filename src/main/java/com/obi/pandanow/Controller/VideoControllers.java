package com.obi.pandanow.Controller;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;


import java.util.ArrayList;
import java.util.List;
//@Controller
//@RequestMapping("/panda")
//public class VideoControllers {
//
//    @RequestMapping("/kid")
//    public String showmkiid(Model model) {
//        final String PEXELS_KEY = "api";
//        String word = "dog is chasing a RAT";
//        String url = "https://api.pexels.com/videos/videos/30962956";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", PEXELS_KEY);
//        HttpEntity<String> entity = new HttpEntity<>(headers);
//
//        RestTemplate restTemplate = new RestTemplate();
//        ResponseEntity<String> response = restTemplate.exchange(
//                url, HttpMethod.GET, entity, String.class
//        );
//
//        List<String> videoUrls = new ArrayList<>();
//
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            JsonNode root = mapper.readTree(response.getBody());
//            JsonNode videoFiles = root.path("video_files");
//
//            if (videoFiles.isArray() && videoFiles.size() > 0) {
//                for (JsonNode file : videoFiles) {
//                    String videoUrl = file.path("link").asText();
//                    if (file.path("quality").asText().equals("hd")) {
//                        videoUrls.add(videoUrl);
//                    }
//                }
//            }
//
//            if (videoUrls.isEmpty() && videoFiles.size() > 0) {
//                String videoUrl = videoFiles.get(0).path("link").asText();
//                videoUrls.add(videoUrl);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            videoUrls.add("https://player.vimeo.com/external/577442552.hd.mp4?s=854da78e77f42d95a2cda289c1b0b4bda9e8a325&profile_id=175");
//        }
//
//        model.addAttribute("word", word);
//        model.addAttribute("videos", videoUrls);
//
//        return "child/kid";
//    }
//}

@Controller
@RequestMapping("/panda")
public class VideoControllers {
    @RequestMapping("/kid")
    public String showKid(Model model) {
        final String PEXELS_KEY = "UNd8P2jHZDckiFIHRvNgLJlaph07mkMPa961HyBYpdJOERuGZDObDvFm";
        String word = "dog is chasing a RAT";

        // Video API URLs
        List<String> videoApiUrls = List.of(
                "https://api.pexels.com/videos/videos/30962956", // first video
                "https://api.pexels.com/videos/videos/15529198"  // second video
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", PEXELS_KEY);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        List<String> videoUrls = new ArrayList<>();

        // Loop over each API URL to fetch video
        for (String url : videoApiUrls) {
            try {
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());
                JsonNode videoFiles = root.path("video_files");

                boolean addedHd = false;
                if (videoFiles.isArray() && videoFiles.size() > 0) {
                    for (JsonNode file : videoFiles) {
                        if (file.path("quality").asText().equals("hd")) {
                            videoUrls.add(file.path("link").asText());
                            addedHd = true;
                            break; // take only one HD per video
                        }
                    }
                    // fallback to first if no HD
                    if (!addedHd) {
                        videoUrls.add(videoFiles.get(0).path("link").asText());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                // fallback video for this API
                videoUrls.add("https://player.vimeo.com/external/577442552.hd.mp4?s=854da78e77f42d95a2cda289c1b0b4bda9e8a325&profile_id=175");
            }
        }

        model.addAttribute("word", word);
        model.addAttribute("videos", videoUrls); // both videos added

        return "child/kid";
    }
}

//UNd8P2jHZDckiFIHRvNgLJlaph07mkMPa961HyBYpdJOERuGZDObDvFm