package es.ulpgc.scrapping;

import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;

public class BookingScrapperJSON implements HotelScrapperToJSON {

    @Override
    public Document getHotelDocument(String hotel) throws IOException {
        String beginningURL = "https://www.booking.com/hotel/es/";
        String endURL = ".es.html";
        return Jsoup.connect(beginningURL + hotel + endURL).get();
    }

    @Override
    public Document getReviewsDocument(String hotel) throws IOException {
        String beginningURL = "https://www.booking.com/reviews/es/hotel/";
        String endURL = ".es.html";
        return Jsoup.connect(beginningURL + hotel + endURL).get();
    }

    @Override
    public String getHotelComments(Document document) {
        Elements CommentBlock = document.getElementsByClass("review_item clearfix ");
        Map<String, Map<String, String>> comments = new LinkedHashMap<>();
        int counter = 1;
        for (Element element : CommentBlock) {
            Map<String, String> comment = new LinkedHashMap<>();
            CommentCreator(comments, counter, element, comment);
            counter++;
        }
        return JSONObject.toJSONString(comments);
    }

    private static void CommentCreator(Map<String, Map<String, String>> comments, int counter, Element element, Map<String, String> comment) {
        comment.put("reviewer", element.getElementsByClass("reviewer_name").text());
        comment.put("writtenDate", element.getElementsByClass("review_item_date").text());
        comment.put("country", element.getElementsByClass("reviewer_country").text());
        comment.put("title", element.getElementsByClass("review_item_header_content").text());
        comment.put("score", element.getElementsByClass("review-score-badge").text());
        comment.put("tags", element.getElementsByClass("review_info_tag ").text().replace("\u2022", "|"));
        comment.put("negativeComments", element.getElementsByClass("review_neg ").text());
        comment.put("positiveComments", element.getElementsByClass("review_pos ").text());
        comment.put("stayDate", element.getElementsByClass("review_staydate ").text());
        comments.put(String.format("Comment number " + counter), comment);
    }

    @Override
    public String getHotelRatings(Document document) {
        Elements rating = document.getElementsByClass("a1b3f50dcd b2fe1a41c3 a1f3ecff04 e187349485 d19ba76520");
        Map<String, String> ratingMap = new HashMap<>();
        RatingsCreator(rating, ratingMap);
        return JSONObject.toJSONString(ratingMap);
    }

    private static void RatingsCreator(Elements rating, Map<String, String> ratingMap) {
        for (Element element : rating) {
            Elements ratingTitle = element.getElementsByClass("d6d4671780");
            Elements mark = element.getElementsByClass("ee746850b6 b8eef6afe1");
            ratingMap.put(ratingTitle.text(), mark.text());
        }
    }

    @Override
    public String getHotelServices(Document document) {
        Elements servicesGroup = document.getElementsByClass("hotel-facilities-group");
        Map<String, List<String>> serviceMap = new HashMap<>();
        ServiceCreator(servicesGroup, serviceMap);
        return JSONObject.toJSONString(serviceMap);
    }

    private static void ServiceCreator(Elements servicesGroup, Map<String, List<String>> serviceMap) {
        for (Element element : servicesGroup) {
            List<String> servicesList = new ArrayList<>();
            Elements title = element.getElementsByClass("bui-title__text hotel-facilities-group__title-text");
            Elements service = element.getElementsByClass("bui-list__description");
            for (Element element1 : service) {
                servicesList.add(element1.text());
            }
            serviceMap.put(title.text(), servicesList);
        }
    }

    @Override
    public String getHotelLocation(Document document) {
        Map<String, String> location = new LinkedHashMap<>();
        LocationCreator(document, location);
        return JSONObject.toJSONString(location);
    }

    private static void LocationCreator(Document document, Map<String, String> location) {
        Elements place = document.getElementsByClass("\n" + "hp_address_subtitle\n" + "js-hp_address_subtitle\n" + "jq_tooltip\n");
        Elements coordinatesElement = document.getElementsByClass("jq_tooltip loc_block_link_underline_fix \n" + "map_static_zoom show_map map_static_hover jq_tooltip map_static_button_hoverstate maps-more-static-focus txp-fix-hover\n");
        String coordinates = coordinatesElement.attr("data-atlas-latlng");
        location.put("address", place.text());
        location.put("coordinates", coordinates);
    }
}
