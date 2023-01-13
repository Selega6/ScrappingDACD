package es.ulpgc.scrapping;

import org.jsoup.nodes.Document;

import java.io.IOException;

public interface HotelScrapperToJSON {
    Document getHotelDocument(String hotel) throws IOException;
    Document getReviewsDocument(String hotel) throws IOException;
    String getHotelComments(Document document);
    String getHotelRatings(Document document);
    String getHotelServices(Document document);
    String getHotelLocation(Document document);

}
