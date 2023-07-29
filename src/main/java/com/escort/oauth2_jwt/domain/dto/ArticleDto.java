package com.escort.oauth2_jwt.domain.dto;

import com.escort.oauth2_jwt.domain.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ArticleDto {

    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String createdBy;
    private String modifiedBy;

    public static ArticleDto fromEntity(Article article) {
        return new ArticleDto(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                article.getAuthor(),
                article.getCreatedDate(),
                article.getModifiedDate(),
                article.getCreatedBy(),
                article.getModifiedBy()
        );
    }

}
