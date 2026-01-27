package com.fitness.activityservice.service;

import com.fitness.activityservice.ActivityRepository;
import com.fitness.activityservice.model.Activity;
import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class Activityservice {
    private final ActivityRepository repo;
    private final UserValidate userValidate;
    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    public ActivityResponse trackActivity(ActivityRequest request) {
    boolean isValidUser= userValidate.validateUser(request.getUserId());
    if(!isValidUser){
        throw new RuntimeException("Invalid user :"+request.getUserId());
    }
            Activity activity= Activity.builder().userId(request.getUserId())
                    .type(request.getType())
                    .duration(request.getDuration())
                    .caloriesBurnt(request.getCaloriesBurnt())
                    .startTime(request.getStartTime())
                    .additionalMetrics(request.getAdditionalMetrics())
                    .build();

            Activity saved =repo.save(activity);

            //publish to RabbitMQ for AI processing
        try{
            rabbitTemplate.convertAndSend(exchange, routingKey, saved );
        }catch(Exception e){
            log.error("Failer to publish activity to RabbitMQ :",e);
        }
            return mapToResponse(saved);

    }
    private ActivityResponse mapToResponse(Activity activity){
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUserId());
        response.setType(activity.getType());
        response.setDuration(activity.getDuration());
        response.setCaloriesBurnt(activity.getCaloriesBurnt());
        response.setStartTime(activity.getStartTime());
        response.setAdditionalMetrics(activity.getAdditionalMetrics());
        response.setUpdatedAt(activity.getUpdatedAt());
        response.setCreatedAt(activity.getCreatedAt());
        return response;
    }

    public List<ActivityResponse> getUserActivity(String userId) {
      List<Activity> activities=repo.findByUserId(userId);
      return activities.stream()
              .map(this::mapToResponse)
              .collect(Collectors.toList());


    }

    public ActivityResponse getActivityById(String activityId) {
        return repo.findById(activityId).map(this::mapToResponse).orElseThrow(()->new RuntimeException("Activity not found with id "+activityId));
    }
}
