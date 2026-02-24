package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @PostMapping("/{id}/subscribe")
    public ResponseEntity<Map<String, String>> subscribe(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        subscriptionService.subscribe(user, id);
        return ResponseEntity.ok(Map.of("message", "Abonnement réussi"));
    }

    @DeleteMapping("/{id}/subscribe")
    public ResponseEntity<Map<String, String>> unsubscribe(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        subscriptionService.unsubscribe(user, id);
        return ResponseEntity.ok(Map.of("message", "Désabonnement réussi"));
    }
}
