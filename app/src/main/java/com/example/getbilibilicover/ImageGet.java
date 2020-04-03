package com.example.getbilibilicover;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class ImageGet {

    private static String bid;

    public static String getBid() {
        return bid;
    }

    public static void setBid(String bid) {
        ImageGet.bid = bid;
    }

    private static String url;

    public ImageGet(String bid) {
        setBid(bid);
        GetUrl();
    }

    public static void main(String[] args) {
        GetImageUrl(GetUrl("BV127411X7K6"));

    }

    public static String GetUrl(String bid) {
        String url = "https://api.bilibili.com/x/web-interface/view?bvid=" + bid;
        setUrl(url);
        return url;
    }

    public static String GetUrl() {
        String url = "https://api.bilibili.com/x/web-interface/view?bvid=" + bid;
        setUrl(url);
        return url;
    }

    private static JSONObject imagejson;

    public static String GetImageUrl(String url) {
        Document doc;
        String imgurl;
        try {
            doc = Jsoup.connect(url).ignoreContentType(true).header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36 Edg/80.0.361.69").get();
            System.out.println(doc.html());
            String code = JSON.parseObject(doc.text()).getString("code");
            if (code.equals("0")) {
                imagejson = JSON.parseObject(doc.text()).getJSONObject("data");
                imgurl = imagejson.getString("pic");
            } else {
                imgurl = code;
            }
            // System.out.println(imgurl);
            return imgurl;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            return e.getMessage();
        }

    }

    public String GetImageUrl() {
        Document doc;
        String imgurl;
        try {
            doc = Jsoup.connect(url).ignoreContentType(true).header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36 Edg/80.0.361.69").get();
            System.out.println(doc.html());
            String code = JSON.parseObject(doc.text()).getString("code");
            if (code.equals("0")) {
                imagejson = JSON.parseObject(doc.text()).getJSONObject("data");
                imgurl = imagejson.getString("pic");
            } else {
                imgurl = code;
            }
            // System.out.println(imgurl);
            return imgurl;
        } catch (IOException e) {
            // TODO Auto-generated catch block
//			e.printStackTrace();
            return e.getMessage();
        }

    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        ImageGet.url = url;
    }
}