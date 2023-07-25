package com.escort.oauth2_jwt.repository;

import com.escort.oauth2_jwt.domain.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
