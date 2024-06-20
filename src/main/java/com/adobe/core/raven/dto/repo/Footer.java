package com.adobe.core.raven.dto.repo;

import lombok.Data;

import java.util.List;

public @Data class Footer {

    private String language;

    private String optOutLinkLabel;

    private String optoutLocale;

    private String footerHtml;

    private List<Paragraphs> paragraphs;

}
