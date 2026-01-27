package com.fitness.activityservice.controller;


import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fitness.activityservice.service.Activityservice;

import java.util.List;

@RestController
@RequestMapping("api/activities")
@AllArgsConstructor
public class ActivityController {
    private Activityservice Service;
    @PostMapping
    public ResponseEntity<ActivityResponse>trackActivity(@RequestBody ActivityRequest request){

    return ResponseEntity.ok(Service.trackActivity(request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ActivityResponse>>getUserActivities(@PathVariable String userId){
        return ResponseEntity.ok(Service.getUserActivity(userId));
    }
    @GetMapping("/activity/{activityId}")
    public ResponseEntity<ActivityResponse>getActivity(@PathVariable String activityId){
        return ResponseEntity.ok(Service.getActivityById(activityId));
    }
}
