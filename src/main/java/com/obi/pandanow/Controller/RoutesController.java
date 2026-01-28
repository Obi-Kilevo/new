package com.obi.pandanow.Controller;

import com.obi.pandanow.Entity.RoutesEntity;
import com.obi.pandanow.Repository.RoutesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;  // for passing data to HTML
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("climb")
public class RoutesController {

    @Autowired
    private RoutesRepository routesRepository;

    // =================== JSON API ===================

    @GetMapping("/api")
    @ResponseBody
    public List<RoutesEntity> getAllRoutes() {
        return routesRepository.findAll().stream()
                .filter(r -> "available".equals(r.getStatus()))
                .toList();
    }

    @GetMapping("/api/park/{parkName}")
    @ResponseBody
    public List<RoutesEntity> getRoutesByPark(@PathVariable String parkName) {
        return routesRepository.findAll().stream()
                .filter(r -> r.getParkName().equalsIgnoreCase(parkName) && "available".equals(r.getStatus()))
                .toList();
    }

    @PostMapping("/api")
    @ResponseBody
    public RoutesEntity addRoute(@RequestBody RoutesEntity route) {
        return routesRepository.save(route);
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    public RoutesEntity updateRoute(@PathVariable Long id, @RequestBody RoutesEntity routeDetails) {
        Optional<RoutesEntity> optionalRoute = routesRepository.findById(id);
        if (optionalRoute.isEmpty()) return null;

        RoutesEntity route = optionalRoute.get();
        if (routeDetails.getRouteName() != null) route.setRouteName(routeDetails.getRouteName());
        if (routeDetails.getDescription() != null) route.setDescription(routeDetails.getDescription());
        if (routeDetails.getPrice() != null) route.setPrice(routeDetails.getPrice());
        if (routeDetails.getShowPrice() != null) route.setShowPrice(routeDetails.getShowPrice());
        if (routeDetails.getStatus() != null) route.setStatus(routeDetails.getStatus());
        if (routeDetails.getImageUrl() != null) route.setImageUrl(routeDetails.getImageUrl()); // Add this

        route.setUpdatedAt(LocalDateTime.now()); // Add this

        return routesRepository.save(route);
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public String deleteRoute(@PathVariable Long id) {
        routesRepository.deleteById(id);
        return "Route deleted successfully";
    }

    // =================== HTML PAGES ===================

    // User view
    @GetMapping("/user")
    public String userRoutes(Model model) {
        List<RoutesEntity> routes = routesRepository.findAll().stream()
                .filter(r -> "available".equals(r.getStatus()))
                .toList();
        model.addAttribute("routes", routes);
        return "routes/user";  // templates/routes/user.html
    }

    // Admin view
    @GetMapping("/admin")
    public String adminRoutes(Model model) {
        List<RoutesEntity> routes = routesRepository.findAll();
        model.addAttribute("routes", routes);
        model.addAttribute("newRoute", new RoutesEntity()); // for add form
        return "routes/admin";  // templates/routes/admin.html
    }

    @PostMapping("/admin")
    public String addRouteFromForm(@ModelAttribute RoutesEntity newRoute) {
        routesRepository.save(newRoute);
        return "redirect:/climb/admin"; // go back to admin page
    }


    // Add to your controller for delete functionality
    @PostMapping("/admin/delete/{id}")
    public String deleteRouteFromForm(@PathVariable Long id) {
        routesRepository.deleteById(id);
        return "redirect:/climb/admin";
    }

    // Show edit form
    @GetMapping("/admin/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<RoutesEntity> route = routesRepository.findById(id);
        if (route.isEmpty()) {
            return "redirect:/climb/admin";
        }
        model.addAttribute("route", route.get());
        return "routes/edit";  // templates/routes/edit.html
    }

    @PostMapping("/admin/edit/{id}")
    public String updateRouteFromForm(@PathVariable Long id, @ModelAttribute RoutesEntity updatedRoute) {
        Optional<RoutesEntity> optionalRoute = routesRepository.findById(id);
        if (optionalRoute.isEmpty()) {
            return "redirect:/climb/admin";
        }

        RoutesEntity route = optionalRoute.get();
        route.setParkName(updatedRoute.getParkName());
        route.setRouteName(updatedRoute.getRouteName());
        route.setDescription(updatedRoute.getDescription());
        route.setPrice(updatedRoute.getPrice());
        route.setShowPrice(updatedRoute.getShowPrice());
        route.setStatus(updatedRoute.getStatus());
        route.setImageUrl(updatedRoute.getImageUrl());
        route.setUpdatedAt(LocalDateTime.now());

        // Debug: Print the image URL
        System.out.println("Saving image URL: " + updatedRoute.getImageUrl());

        routesRepository.save(route);
        return "redirect:/climb/admin";
    }

    @GetMapping
    public String climbing() {
        return "routes/test";
    }

    @GetMapping("/t")
    public String climbiing() {
        return "routes/test2";
    }
}