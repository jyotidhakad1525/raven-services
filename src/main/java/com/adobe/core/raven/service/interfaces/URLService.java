package com.adobe.core.raven.service.interfaces;


import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;

public interface URLService {

       String validate(String url);

       HashMap<String,Integer> getRedirectedUrl(String url);

       MultiValueMap<String, String> getQueryParameters(String url);

       HashMap<Integer, String> callGetAPI(String url);

       String callPostAPI(String input, String url);

}
