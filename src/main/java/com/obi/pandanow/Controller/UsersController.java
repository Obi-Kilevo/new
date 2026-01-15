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
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
@Controller
@RequestMapping("/masai")
public class UsersController {



//    UNd8P2jHZDckiFIHRvNgLJlaph07mkMPa961HyBYpdJOERuGZDObDvFm

//    @GetMapping()
//    public String getSmallBoxVideo(Model model) {
//        HttpHeaders headers = new HttpHeaders();
//        final String PEXELS_KEY = " UNd8P2jHZDckiFIHRvNgLJlaph07mkMPa961HyBYpdJOERuGZDObDvFm";
//        headers.set("Authorization", PEXELS_KEY);
//
//        // Each video ID is unique for its section - ADDED YOUR 35653242 AS FOURTH VIDEO
//        String[] videoIds = {
//                "30917411",    // First video - for small boxes section
//                "18271055",    // Second video - for background section
//                "34776281",    // Third video - for another section
//                "35653242"     // Fourth video - YOUR NEW SECTION (prevent duplication)
//        };
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        // Variables for ALL FOUR videos - ADDED FOURTH VARIABLE
//        String firstVideoUrl = "";      // For small boxes
//        String secondVideoUrl = "";     // For background
//        String thirdVideoUrl = "";      // For third section
//        String fourthVideoUrl = "";     // For YOUR NEW SECTION
//
//        for (int i = 0; i < videoIds.length; i++) {
//            String url = "https://api.pexels.com/videos/videos/" + videoIds[i];
//            HttpEntity<String> entity = new HttpEntity<>(headers);
//
//            try {
//                ResponseEntity<String> response = restTemplate.exchange(
//                        url, HttpMethod.GET, entity, String.class
//                );
//
//                ObjectMapper mapper = new ObjectMapper();
//                JsonNode root = mapper.readTree(response.getBody());
//                JsonNode videoFiles = root.path("video_files");
//
//                String fetchedUrl = "";
//                if (videoFiles.isArray() && videoFiles.size() > 0) {
//                    for (JsonNode file : videoFiles) {
//                        if (file.path("quality").asText().equals("hd")) {
//                            fetchedUrl = file.path("link").asText();
//                            break;
//                        }
//                    }
//                    if (fetchedUrl.isEmpty()) {
//                        fetchedUrl = videoFiles.get(0).path("link").asText();
//                    }
//
//                    // Assign to correct variable
//                    switch(i) {
//                        case 0: firstVideoUrl = fetchedUrl; break;    // Small boxes
//                        case 1: secondVideoUrl = fetchedUrl; break;   // Background
//                        case 2: thirdVideoUrl = fetchedUrl; break;    // Third section
//                        case 3: fourthVideoUrl = fetchedUrl; break;   // YOUR NEW SECTION
//                    }
//                }
//            } catch (Exception e) {
//                String fallback = "https://player.vimeo.com/external/577442552.hd.mp4?s=854da78e77f42d95a2cda289c1b0b4bda9e8a325&profile_id=175";
//                switch(i) {
//                    case 0: firstVideoUrl = fallback; break;
//                    case 1: secondVideoUrl = fallback; break;
//                    case 2: thirdVideoUrl = fallback; break;
//                    case 3: fourthVideoUrl = fallback; break;
//                }
//            }
//        }
//
//        // Fallback for any empty URLs
//        String defaultFallback = "https://player.vimeo.com/external/577442552.hd.mp4?s=854da78e77f42d95a2cda289c1b0b4bda9e8a325&profile_id=175";
//
//        if (firstVideoUrl.isEmpty()) firstVideoUrl = defaultFallback;
//        if (secondVideoUrl.isEmpty()) secondVideoUrl = defaultFallback;
//        if (thirdVideoUrl.isEmpty()) thirdVideoUrl = defaultFallback;
//        if (fourthVideoUrl.isEmpty()) fourthVideoUrl = defaultFallback;
//
//        // Add ALL attributes - each video unique to its section
//        model.addAttribute("videoUrl", firstVideoUrl);           // Small boxes section
//        model.addAttribute("backgroundVideoUrl", secondVideoUrl); // Background section
//        model.addAttribute("videos", Arrays.asList(thirdVideoUrl)); // Third section
//        model.addAttribute("thirdVideoUrl", thirdVideoUrl);      // Third section single
//        model.addAttribute("headerIntroVideoUrl", fourthVideoUrl);
//
//        return "me/index";
//    }


@GetMapping()
public String getSmallBoxVideo(Model model) {
    HttpHeaders headers = new HttpHeaders();
    final String PEXELS_KEY = " UNd8P2jHZDckiFIHRvNgLJlaph07mkMPa961HyBYpdJOERuGZDObDvFm";
    headers.set("Authorization", PEXELS_KEY);

    // Fetch 3 different videos - ADDED THE THIRD VIDEO (15529198)
    String[] videoIds = {
            "30917411",    // First video - for small boxes
            "18271055",    // Second video - for background
            "34776281"// Third video - ADDED THIS FROM YOUR OLD CONTROLLER
//            "15529198",

    };

    RestTemplate restTemplate = new RestTemplate();

    // Variables for all three videos
    String firstVideoUrl = "";      // For small boxes
    String secondVideoUrl = "";     // For background
    String thirdVideoUrl = "";      // For the third video (from old controller)

    for (int i = 0; i < videoIds.length; i++) {
        String url = "https://api.pexels.com/videos/videos/" + videoIds[i];
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url, HttpMethod.GET, entity, String.class
            );

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());
            JsonNode videoFiles = root.path("video_files");

            String fetchedUrl = "";
            if (videoFiles.isArray() && videoFiles.size() > 0) {
                // Get HD quality first, fallback to first available
                for (JsonNode file : videoFiles) {
                    if (file.path("quality").asText().equals("hd")) {
                        fetchedUrl = file.path("link").asText();
                        break;
                    }
                }
                if (fetchedUrl.isEmpty()) {
                    fetchedUrl = videoFiles.get(0).path("link").asText();
                }

                // Assign to appropriate variable based on index
                if (i == 0) {
                    firstVideoUrl = fetchedUrl;      // First video for small boxes
                } else if (i == 1) {
                    secondVideoUrl = fetchedUrl;     // Second video for background
                } else if (i == 2) {
                    thirdVideoUrl = fetchedUrl;      // Third video (from old controller)
                }
            }
        } catch (Exception e) {
            // Fallback video
            String fallback = "https://player.vimeo.com/external/577442552.hd.mp4?s=854da78e77f42d95a2cda289c1b0b4bda9e8a325&profile_id=175";
            if (i == 0) {
                firstVideoUrl = fallback;
            } else if (i == 1) {
                secondVideoUrl = fallback;
            } else if (i == 2) {
                thirdVideoUrl = fallback;
            }
        }
    }

    // Ensure all videos are set
    String defaultFallback = "https://player.vimeo.com/external/577442552.hd.mp4?s=854da78e77f42d95a2cda289c1b0b4bda9e8a325&profile_id=175";

    if (firstVideoUrl.isEmpty()) {
        firstVideoUrl = defaultFallback;
    }
    if (secondVideoUrl.isEmpty()) {
        secondVideoUrl = defaultFallback;
    }
    if (thirdVideoUrl.isEmpty()) {
        thirdVideoUrl = defaultFallback;
    }

    // Add attributes - ALL THREE VIDEOS
    model.addAttribute("videoUrl", firstVideoUrl);           // For small boxes
    model.addAttribute("backgroundVideoUrl", secondVideoUrl); // For background video
    model.addAttribute("videos", Arrays.asList(thirdVideoUrl)); // For last page (from old controller)
    // If you need the third video as a single string instead of list:
    model.addAttribute("thirdVideoUrl", thirdVideoUrl); // Additional attribute if needed

    return "me/index";
}





    @GetMapping("/map")
    public String getters() {
        return "map/africa";
    }

    @GetMapping("/africaMap")
    public String getter() {
        return "map/realkafrica";
    }
}