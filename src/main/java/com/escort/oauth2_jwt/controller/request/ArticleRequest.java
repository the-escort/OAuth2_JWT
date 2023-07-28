package com.escort.oauth2_jwt.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ArticleRequest {

    private String title;

    private String content;

}
