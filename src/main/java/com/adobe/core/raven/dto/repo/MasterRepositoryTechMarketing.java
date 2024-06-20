package com.adobe.core.raven.dto.repo;

import com.adobe.core.raven.dto.qa.Metadata;
import lombok.Data;

import javax.persistence.Id;
import java.util.List;

public @Data  class MasterRepositoryTechMarketing {

    @Id
    private String id;

    private String countryCode;

    private String acomLocaleCode;

    private String mirrorText;

    private List<SocialLinks> socialLinks;

    private List<Footer> footer;

    private Metadata metadata;

}
