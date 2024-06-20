package com.adobe.core.raven.service.interfaces;


import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public interface ValidateService {

       Boolean validateImageSource(Element image);

       Boolean validateImageAltText(Element image);


       Boolean compare(String val1, String val2);


       Boolean validateFromAddress(String fromAddressFromMail, String workfrontId, String locale, String stepId, String activityId);


}
