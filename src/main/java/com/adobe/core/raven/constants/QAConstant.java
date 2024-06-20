package com.adobe.core.raven.constants;

public class QAConstant {

    public static final String UTF = "UTF-8";
    public static final String BACKSLASH = "\\.";
    public static final String SPACE = " ";
    public static final String UNDERSCORE = "_";
    public static final String TAG_A = "a";
    public static final String TAG_DIV = "div";
    public static final String TAG_P = "p";
    public static final String TAG_IMG = "img";
    public static final String HREF = "href";
    public static final String START_BRACKET = "\\{\\{";
    public static final String CLOSE_BRACKET = "\\}\\}";
    public static final String VALID = "Valid";
    public static final String NOTVALID = "Not Valid" ;
    public static final String AMPERSAND = "&" ;
    public static final Character TICK = '\u2713' ;
    public static final Character CROSS = '\u274C' ;
    public static final String TRACKINGID = "trackingid";
    public static final String FOOTERID = "reg_foot";
    public static final String OPTOUTLINK = "a[_type$=optout]";
    public static final String MIRRORLINK = "a[_type$=mirrorPage]";
    public static final String OPTOUTLINKLOCALELINK = "<%@ include view='adbeGlobalMarketingComUnsubscriptionLink'%>";
    public static final String OPTOUTLINKLOCALELINK1 = "<%@ include view='adbeGlobalMarketingComUnsubscriptionLink' %>";
    public static final String APAC_OPTOUTLINKLOCALELINK = "<%@ include view='adbeGlobalMarketingComUnsubscriptionLink' %>";
    public static final String APAC_YEAR = "<%= formatDate(new Date(), \"%4Y\") %>";
    public static final String ATAG_FULLSTOP_LINK = "</a>.<br/><br/>";
    public static final String NON_BREAKING_HYPHEN = "&#8209;";
    public static final String SPACE_FULLSTOP_LINK = "&nbsp;.";
    public static final String SPACE_DASH_LINK = "&nbsp;-";
    public static final String Non_breaking_Space = "\u00a0";
    public static final String Non_breaking_Space_Str = "&nbsp;";
    public static final String SingleQuote = "&#39;";



    public static String MessageParserUrl = "http://10.42.69.206:5000/parse";
    public static String MessageFileType = "application/vnd.ms-outlook";

    public static final String WORKFRONT_URL = "http://localhost:8087/v1/job/workfrontInfo";
    public static final String WORKFRONT_PAYLOAD = "{\n" +
            "    \"projectId\": \"";
}
