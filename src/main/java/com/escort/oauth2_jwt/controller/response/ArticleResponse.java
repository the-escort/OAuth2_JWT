package com.escort.oauth2_jwt.controller.response;

import com.escort.oauth2_jwt.domain.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ArticleResponse {

    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String createdBy;
    private String modifiedBy;

    public static ArticleResponse fromEntity(Article article) {
        return new ArticleResponse(
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
