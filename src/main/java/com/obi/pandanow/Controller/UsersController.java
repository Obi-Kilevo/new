

//    UNd8P2jHZDckiFIHRvNgLJlaph07mkMPa961HyBYpdJOERuGZDObDvFm



package com.obi.pandanow.Controller;

//import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/masai")
public class UsersController {


    @GetMapping()
    public String getSmallBoxVideo(Model model) {
        HttpHeaders headers = new HttpHeaders();
        final String PEXELS_KEY = "UNd8P2jHZDckiFIHRvNgLJlaph07mkMPa961HyBYpdJOERuGZDObDvFm"; // Replace with your actual Pexels API key
        headers.set("Authorization", PEXELS_KEY);
        RestTemplate restTemplate = new RestTemplate();

        // Section 1 - Centre videos
        String[] centreIds = {"30917411"};
        List<String> centreUrls = new ArrayList<>();
        for (String id : centreIds) {
            centreUrls.add(fetchVideo(id, headers, restTemplate));
        }
        model.addAttribute("videoUrl", centreUrls.get(0));
        model.addAttribute("centreVideos", centreUrls);

        // Section 2 - Forehead videos with speeds
        String[] foreheadIds = {"4628755", "9823494", "17310764", "9210956", "32263333", "33027616"};
        float[] speeds = {2.0f, 6.5f, 8.0f, 5.0f, 3.0f, 2.0f};

        List<Map<String, Object>> foreheadVideosWithSpeed = new ArrayList<>();
        for (int i = 0; i < foreheadIds.length; i++) {
            Map<String, Object> video = new HashMap<>();
            video.put("url", fetchVideo(foreheadIds[i], headers, restTemplate));
            video.put("speed", speeds[i]);
            foreheadVideosWithSpeed.add(video);
        }

        model.addAttribute("foreheadVideos", foreheadVideosWithSpeed);
        model.addAttribute("backgroundVideoUrlforhead", foreheadVideosWithSpeed.get(0).get("url"));

        // Section 3 - Third videos masai
        String[] thirdIds = {"34776281"};
        List<String> thirdUrls = new ArrayList<>();
        for (String id : thirdIds) {
            thirdUrls.add(fetchVideo(id, headers, restTemplate));
        }
        model.addAttribute("videos", thirdUrls);
        model.addAttribute("thirdVideoUrl", thirdUrls.get(0));

        return "me/index";
    }

    private String fetchVideo(String videoId, HttpHeaders headers, RestTemplate restTemplate) {
        String url = "https://api.pexels.com/videos/videos/" + videoId;
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
                for (JsonNode file : videoFiles) {
                    if (file.path("quality").asText().equals("hd")) {
                        fetchedUrl = file.path("link").asText();
                        break;
                    }
                }
                if (fetchedUrl.isEmpty()) {
                    fetchedUrl = videoFiles.get(0).path("link").asText();
                }
            }
            return fetchedUrl;
        } catch (Exception e) {
            // Fallback video URL if fetch fails
            return "https://player.vimeo.com/external/577442552.hd.mp4?s=854da78e77f42d95a2cda289c1b0b4bda9e8a325&profile_id=175";
        }
    }




    @GetMapping("/stream-video")
    public void streamTrimmedVideo(
            @RequestParam String sourceUrl,
            @RequestParam(defaultValue = "10") int trimTo,
            @RequestParam(defaultValue = "1.0") float speed,
            HttpServletResponse response) throws IOException {

        System.err.println("=== STREAM ENDPOINT CALLED ===");
        System.err.println("URL: " + sourceUrl);
        System.err.println("Trim: " + trimTo + "s, Speed: " + speed);

        String ffmpegPath = "C:\\Users\\Administrator\\Downloads\\ffmpeg-2026-01-29-git-c898ddb8fe-essentials_build\\ffmpeg-2026-01-29-git-c898ddb8fe-essentials_build\\bin\\ffmpeg.exe";

        // Test if FFmpeg works
        try {
            String[] testCmd = {ffmpegPath, "-version"};
            Process test = new ProcessBuilder(testCmd).start();
            int testExit = test.waitFor();
            System.err.println("FFmpeg test exit code: " + testExit);
        } catch (Exception e) {
            System.err.println("FFMPEG TEST FAILED: " + e.getMessage());
            response.sendError(500, "FFmpeg failed: " + e.getMessage());
            return;
        }

        String[] cmd = {
                ffmpegPath,
                "-fflags", "flush_packets", // Critical: reduce streaming delay
                "-i", sourceUrl,
                "-t", String.valueOf(trimTo),
                "-c:v", "copy",
                "-c:a", "copy",
                "-f", "matroska",
                "pipe:1"
        };

        System.err.println("Starting FFmpeg process...");

        try {
            Process process = new ProcessBuilder(cmd).start();
            response.setContentType("video/x-matroska");

            // Copy stream
            InputStream in = process.getInputStream();
            OutputStream out = response.getOutputStream();
            byte[] buffer = new byte[8192];
            int bytesRead;
            int totalBytes = 0;

            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
                totalBytes += bytesRead;
            }

            int exitCode = process.waitFor();
            System.err.println("FFmpeg completed. Exit: " + exitCode + ", Bytes: " + totalBytes);

        } catch (Exception e) {
            System.err.println("STREAMING ERROR: " + e.getMessage());
            e.printStackTrace();
            if (!response.isCommitted()) {
                response.sendError(500, "Stream failed: " + e.getMessage());
            }
        }
    }

}

