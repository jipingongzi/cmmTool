package xc.investigation.base.init;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

/**
 * @author ibm
 */
public class BankSpider {

    public static void main(String[] args) throws IOException {
        Document doc =
                Jsoup.connect("https://www.cardbaobao.com/wangdian/gongshang/sichuan/?page=1").maxBodySize(0).get();
        String title = doc.title();
        System.out.println(title);

        List<Element> bankNumberEle = doc.getElementsByClass("Cfl_zs");
        Integer bankNumber = Integer.valueOf(bankNumberEle.get(0).text());
        System.out.println(bankNumber);

        List<Element> bankPageEle = doc.getElementsByClass("Cfl_zo");
        Integer bankPage = Integer.valueOf(bankPageEle.get(0).text());
        System.out.println(bankPage);

        for (int i = 1; i <= bankPage; i++) {
            parasePage(i);
        }


    }

    private static void parasePage(int pageNumber) throws IOException {
        System.out.println("========================第" + pageNumber + "页数据：===============================");
        Document doc = Jsoup.connect("https://www.cardbaobao.com/wangdian/gongshang/sichuan/?page=" + pageNumber)
                .maxBodySize(0).get();
        List<Element> data = doc.getElementsByTag("dd");
        for (Element e : data) {
            String bank = e.getElementsByClass("taa1").get(0).text();
            String address = e.getElementsByClass("taa2").get(0).text();
            System.out.println(bank + "-----" + address);
        }
    }
}
