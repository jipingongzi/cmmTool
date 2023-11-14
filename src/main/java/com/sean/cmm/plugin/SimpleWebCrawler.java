package com.sean.cmm.plugin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class SimpleWebCrawler {

    public static void main(String[] args) {
        // 要爬取的网页URL
        String url = "https://example.com/";

        try {
            // 连接到目标网页，并解析HTML文档
            Document doc = Jsoup.connect(url).get();

            // 从文档中选择所有的超链接
            Elements links = doc.select("a[href]");

            // 遍历所有超链接，并打印出它们的绝对URL
            for (Element link : links) {
                System.out.println(link.absUrl("href"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
