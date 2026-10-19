package com.mapersive.interview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LayerController {
    @Autowired
    private LayerRepository repository;

    @PostMapping("/api/layers")
    public Object create(@RequestBody Map<String, Object> body) {
        try {
            Long projectId = Long.valueOf(body.get("projectId").toString());
            String name = body.get("name").toString();
            String type = body.get("type").toString();
            if (name.length() == 0) {
                return "Name is required";
            }
            MapLayer layer = new MapLayer();
            layer.setProjectId(projectId);
            layer.setName(name);
            layer.setType(type);
            return repository.save(layer);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return error;
        }
    }

    @GetMapping("/api/projects/{projectId}/layers")
    public List<MapLayer> list(@PathVariable Long projectId) {
        List<MapLayer> result = new ArrayList<>();
        for (MapLayer layer : repository.findAll()) {
            if (layer.getProjectId() == projectId) {
                result.add(layer);
            }
        }
        return result;
    }
}
