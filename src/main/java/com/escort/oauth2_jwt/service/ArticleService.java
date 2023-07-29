package com.escort.oauth2_jwt.service;

import com.escort.oauth2_jwt.controller.request.ArticleRequest;
import com.escort.oauth2_jwt.domain.entity.Article;
import com.escort.oauth2_jwt.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public Article save(ArticleRequest articleRequest, String username) {
        Article article = Article.builder()
                .title(articleRequest.getTitle())
                .content(articleRequest.getContent())
                .author(username)
                .build();
        return articleRepository.save(article);
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected article -> " + id));
    }

    public void delete(Long id) {
        articleRepository.deleteById(id);
    }

    public Article update(Long id, ArticleRequest articleRequest) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected article -> " + id));
        article.update(articleRequest.getTitle(), article.getContent());

        return article;
    }

}
