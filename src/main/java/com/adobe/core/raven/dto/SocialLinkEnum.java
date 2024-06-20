package com.adobe.core.raven.dto;

public enum SocialLinkEnum {

//        YOUTUBE("img[alt*=Youtube]", "Youtube"),
//        INSTAGRAM("img[alt*=Instagram]", "Instagram"),
//        TWITTER("img[alt*=Twitter]", "Twitter"),
//        FACEBOOK("img[alt*=Facebook]", "Facebook"),
//        BEHANCE("img[alt*=Behance]", "Behance"),
//        GOOGLEPLUS("img[alt*=Google +]", "Google +"),
//        BLOG("img[alt*=Blog]", "Blog"),
//        PINTREST("img[alt*=Pinterest]", "Pinterest"),
//        LINKEDIN("img[alt*=LinkedIn]", "LinkedIn");

        YOUTUBE("img[alt=Youtube]", "Youtube"),
        INSTAGRAM("img[alt=Instagram]", "Instagram"),
        TWITTER("img[alt=Twitter]", "Twitter"),
        FACEBOOK("img[alt=Facebook]", "Facebook"),
        BEHANCE("img[alt=Behance]", "Behance"),
        GOOGLEPLUS("img[alt=Google +]", "Google +"),
        BLOG("img[alt=Blog]", "Blog"),
        PINTREST("img[alt=Pinterest]", "Pinterest"),
        LINKEDIN("img[alt=LinkedIn]", "LinkedIn");

        // declaring private variable for getting values
        private String linktag;
        private String linkType;

        // getter method
        public String getLinktag()
        {
                return this.linktag;
        }

        public String getLinkType()
        {
                return this.linkType;
        }

        // enum constructor - cannot be public or protected
        private SocialLinkEnum(String linktag, String linkType)
        {

                this.linktag = linktag;
                this.linkType = linkType;
        }
}
