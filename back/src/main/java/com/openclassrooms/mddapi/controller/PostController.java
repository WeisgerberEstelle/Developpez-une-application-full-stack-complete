package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.CreatePostRequest;
import com.openclassrooms.mddapi.dto.PostResponse;
import com.openclassrooms.mddapi.entity.User;
import com.openclassrooms.mddapi.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/feed")
    public ResponseEntity<List<PostResponse>> getFeed(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(postService.getFeed(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getById(id));
    }

    @PostMapping
    public ResponseEntity<PostResponse> create(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody CreatePostRequest request) {
        return ResponseEntity.ok(postService.create(user, request));
    }
}
