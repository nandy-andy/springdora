package com.wikia.service;

import ch.qos.logback.classic.Level;
import com.wikia.Config;
import com.wikia.api.resource.ArticleResource;
import com.wikia.gateway.Mercury.MercuryResponse;
import com.wikia.gateway.MercuryGateway;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class ArticleService {

    private MercuryGateway mercury;

    public ArticleService() {
    }

    public ArticleService(MercuryGateway mercury) {
        this.mercury = mercury;
    }

    public ArticleResource getArticle(String title) throws java.io.IOException {
        Logger.getLogger(ArticleService.class.toString())
                .log(java.util.logging.Level.INFO, String.format("getting title '%s'", title));
        MercuryResponse response = this.getMercuryGatway().get(title);

        return new ArticleResource(
                response.article.path("content").asText(),
                response.details.path("id").asInt(),
                response.details.path("title").asText(),
                response.details.path("thumbnail").asText()
        );
    }

    public ArticleResource getDefaultArticle() throws java.io.IOException {
        MercuryResponse response = this.getMercuryGatway().getDefaultMercuryAPIResponse();

        return new ArticleResource(
                response.article.path("content").asText(),
                response.details.path("id").asInt(),
                response.details.path("title").asText(),
                response.details.path("thumbnail").asText()
        );
    }

    public ArticleService setMercuryGateway(MercuryGateway mercury) {
        this.mercury = mercury;
        return this;
    }

    public MercuryGateway getMercuryGatway() {
        if (this.mercury == null) {
            this.mercury = new MercuryGateway(Config.getInstance().mercuryBaseURL);
        }

        return this.mercury;
    }
}
