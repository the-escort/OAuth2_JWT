package com.escort.oauth2_jwt.controller;

import com.escort.oauth2_jwt.controller.request.ArticleRequest;
import com.escort.oauth2_jwt.controller.response.ArticleResponse;
import com.escort.oauth2_jwt.domain.entity.Article;
import com.escort.oauth2_jwt.service.ArticleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api")
@Controller
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping("/article")
    public ResponseEntity<ArticleResponse> addArticle(@RequestBody ArticleRequest articleRequest) {
        Article article = articleService.save(articleRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ArticleResponse.fromEntity(article));
    }

    @GetMapping("/article")
    public ResponseEntity<List<ArticleResponse>> findAllArticles() {
        List<ArticleResponse> articles = articleService.findAll().stream()
                .map(ArticleResponse::fromEntity)
                .toList();

        return ResponseEntity.ok()
                .body(articles);
    }

    @GetMapping("/article/{id}")
    public ResponseEntity<ArticleResponse> findArticle(@PathVariable Long id) {
        Article article = articleService.findById(id);

        return ResponseEntity.ok()
                .body(ArticleResponse.fromEntity(article)) ;
    }

    @DeleteMapping("/article/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.delete(id);

        return ResponseEntity.ok()
                .build();
    }

    @PutMapping("/article/{id}")
    public ResponseEntity<ArticleResponse> updateArticle(@PathVariable Long id, @RequestBody ArticleRequest articleRequest) {
        Article article = articleService.update(id, articleRequest);

        return ResponseEntity.ok()
                .body(ArticleResponse.fromEntity(article));
    }

}
