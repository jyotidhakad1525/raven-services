package com.adobe.core.raven.service.implementation;

import com.adobe.core.raven.constants.QAConstant;
import com.adobe.core.raven.dto.SocialLinkEnum;
import com.adobe.core.raven.service.interfaces.URLService;
import okhttp3.*;
import okio.Buffer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.rmi.RemoteException;
import java.util.HashMap;

public class URLServiceImpl implements URLService {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass().getName());

    @Override
    public String validate(String url) {
        HttpURLConnection connection = null;
        int respCode = 200;
        if (url == null || url.isEmpty()) {
            System.out.println("URL is either not configured for anchor tag or it is empty");
        }
        try {
            connection = (HttpURLConnection) (new URL(url).openConnection());
            connection.setRequestMethod("GET");
            connection.connect();
            respCode = connection.getResponseCode();

            if (respCode >= 400) {
                return QAConstant.NOTVALID + QAConstant.UNDERSCORE + respCode;
            } else {
                return QAConstant.VALID + QAConstant.UNDERSCORE + respCode;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return QAConstant.NOTVALID;
    }

    @Override
    public HashMap<String, Integer> getRedirectedUrl(String url) {
        HttpURLConnection connection = null;
        HashMap<String, Integer> responseMap = new HashMap();
        if(url.equalsIgnoreCase("https://t-info.mail.adobe.com/r/?id=h1ecb93cf,fd4ddda6,bfa0ac94")){
            System.out.println("");
        }
        try {
            int respCode = 200;
            if (url == null || url.isEmpty()) {
                System.out.println("URL is either not configured for anchor tag or it is empty");
                responseMap.put("", 500);
                return responseMap;
            }
            try {
                connection = (HttpURLConnection) (new URL(url).openConnection());
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod("GET");
                connection.connect();
                InputStream is = connection.getInputStream();
                respCode = connection.getResponseCode();
                if (respCode == 404) {
                    responseMap.put(url, 404);
                } else if (respCode == 200) {
                    String locationUrl = connection.getHeaderField("Location");
                    if (locationUrl == null) {
                        locationUrl = url;
                    }
                    String htmlcontent = getHtmlFromCTA(locationUrl);
                    if (htmlcontent != null || htmlcontent != "") {
//                    Document document = Jsoup.parse(htmlcontent);
//                    String title = document.getElementsByTag("title").text();
                        if (htmlcontent.contains("404")) {
                            responseMap.put(url, 404);

                        } else {

                            responseMap.put(url, respCode);
                        }
                    } else {
                        responseMap.put(url, respCode);
                    }
                } else {
                    String location = connection.getHeaderField("Location");
                    if (location != null && !location.contains(QAConstant.TRACKINGID)) {
                        HttpURLConnection urlConnection = (HttpURLConnection) (new URL(location).openConnection());
                        urlConnection.setInstanceFollowRedirects(false);
                        urlConnection.setRequestMethod("GET");
                        urlConnection.connect();
                        String locationUrl = urlConnection.getHeaderField("Location");
                        if(locationUrl != null&&locationUrl.contains("https:")){
                            location = locationUrl;
                        }

                    }
                    if(location == null){
                        responseMap.put(url, 404);
                        return responseMap;
                    }
                    String htmlcontent = getHtmlFromCTA(location);
                    if(htmlcontent == null){
                        responseMap.put(url, 302);
                        return responseMap;
                    }
                    if (htmlcontent != null && htmlcontent != "") {


                        if (htmlcontent.contains("404")) {
                            responseMap.put(url, 404);

                        } else {
                            String locationUrl = connection.getHeaderField("Location");
                            if (locationUrl != null && !locationUrl.contains(QAConstant.TRACKINGID)) {
                                responseMap.put(location,respCode);
                            }else {
                                responseMap.put(connection.getHeaderField("Location"), respCode);
                            }
                        }
                    } else {
                        String locationUrl = connection.getHeaderField("Location");
                            if (locationUrl != null && !locationUrl.contains(QAConstant.TRACKINGID)) {
                          if(locationUrl.contains("instagram") && !locationUrl.equals(location) ){
                              responseMap.put(connection.getHeaderField("Location"), respCode);
                          }else {
                              responseMap.put(location, respCode);
                          }
                        }
                        else{
                        responseMap.put(connection.getHeaderField("Location"), respCode);
                    }}
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                responseMap.put(url, 500);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                String location = connection.getHeaderField("Location");
                responseMap.put(location, 404);
//                responseMap.put(location, 302);
            } catch (IOException e) {
                e.printStackTrace();
                responseMap.put(url, 500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseMap;
    }


    public String getHtmlFromCTA(String url) throws IOException, FileNotFoundException {

        String inputLine = "";
        if(url.equalsIgnoreCase("https://t-info.mail.adobe.com/r/?id=h1ecb93d5,fd4ddda6,bfa0ac94")){
            System.out.println("");
        }

        for (SocialLinkEnum socialLinkEnum : SocialLinkEnum.values()) {
            System.out.println(socialLinkEnum.name());
            if (url.toLowerCase().contains(socialLinkEnum.getLinkType().toLowerCase())) {
                return inputLine;
            }
        }
//        if(url != null && !url.equals("")){
//         //   if(url.contains("adobeint-mid-prod") || url.toLowerCase().contains("tiktok") ){
//                return inputLine;
//           // }
//        }

        HttpURLConnection connection = (HttpURLConnection) (new URL(url).openConnection());
        connection.setInstanceFollowRedirects(false);
        connection.setRequestMethod("GET");
        connection.connect();
        int respcode = connection.getResponseCode();
        if(respcode==302){
            String locationUrl = connection.getHeaderField("Location");
            if(locationUrl.toLowerCase().contains("tiktok"));
             return  inputLine;
        }
//        if(respcode==200 && url.toLowerCase().contains("tiktok")){
//            return inputLine;
//        }
        if(url.toLowerCase().contains("tiktok")){
            return inputLine;
        }
        if (respcode >= 400) {
            throw new FileNotFoundException();
        }
        BufferedReader bufIn = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        while ((inputLine = bufIn.readLine()) != null) {
            //  inputLine = inputLine.concat(inputLine);
            System.out.println(inputLine);
            if (inputLine.contains("<title")) {
                break;
            }
        }

        bufIn.close();
        String titleText="";
          if(inputLine!=""&&inputLine!=null) {
              Document parsedHtml = Jsoup.parse(inputLine, QAConstant.UTF, Parser.xmlParser());
              Elements titleTag = parsedHtml.getElementsByTag("title");
               titleText = titleTag.get(0).text();
          }
        return titleText;
    }

    @Override
    public MultiValueMap<String, String> getQueryParameters(String url) {
        MultiValueMap<String, String> queryParams =
                UriComponentsBuilder.fromUriString(url).build().getQueryParams();

        return queryParams;
    }

    @Override
    public HashMap<Integer, String> callGetAPI(String url){
        HashMap<Integer, String> responseMap = new HashMap();
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        Response response = null;
        Request request = new Request.Builder()
                .addHeader("accept","application/json")
                .url(url)
                .get()
                .build();

        Call call = client.newCall(request);
        try {
            response = call.execute();
            responseMap.put(response.code(), response.body().string());
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseMap;
    }

    @Override
    public String callPostAPI(String input, String url) {

        String workfrontResponse =null;
        RequestBody body = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), input);
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        // body = RequestBody.create(input.getBytes(StandardCharsets.UTF_8));.getBytes(Charsets.US_ASCII)
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Accept", "application/json")
                .addHeader("Accept-Encoding", "identity")
                .addHeader("Content-Type","application/json; charset=utf-8")
                .build();

        final Buffer buffer = new Buffer();
        try {
            request.body().writeTo(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Call call = client.newCall(request);
        try {
            Response response = call.execute();
            workfrontResponse =  response.body().string();
            response.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return workfrontResponse;
    }

 //      @Override
//    public HashMap<String, Integer> getRedirectedUrl(String url) {
//        HttpURLConnection connection = null;
//        HashMap<String, Integer> responseMap = new HashMap();
//        int respCode = 200;
//        if (url == null || url.isEmpty()) {
//            System.out.println("URL is either not configured for anchor tag or it is empty");
//            responseMap.put("", 500);
//            return responseMap;
//        }
//        try {
//            connection = (HttpURLConnection) (new URL(url).openConnection());
//            connection.setInstanceFollowRedirects(false);
//            connection.setRequestMethod("GET");
//            connection.connect();
//            InputStream is = connection.getInputStream();
//
//            respCode = connection.getResponseCode();
//            if (respCode == 200) {
//                responseMap.put(url, respCode);
//            } else {
//                String location = connection.getHeaderField("Location");
//                responseMap.put(connection.getHeaderField("Location"), respCode);
//            }
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//            responseMap.put(url, 500);
//        } catch (IOException e) {
//            e.printStackTrace();
//            responseMap.put(url, 500);
//        }
//        return responseMap;
//    }
}
