package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/{id}/subscribe")
    public ResponseEntity<Void> subscribe(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        subscriptionService.subscribe(user, id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/subscribe")
    public ResponseEntity<Void> unsubscribe(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        subscriptionService.unsubscribe(user, id);
        return ResponseEntity.ok().build();
    }
}
