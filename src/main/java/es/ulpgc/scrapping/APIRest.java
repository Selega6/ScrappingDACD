package es.ulpgc.scrapping;

import org.jsoup.nodes.Document;
import spark.Request;
import spark.Response;

import java.io.IOException;

import static spark.Spark.get;

public class APIRest {
    private final HotelScrapperToJSON scrapper;

    public APIRest(HotelScrapperToJSON scrapper) {
        this.scrapper = scrapper;
    }

    public void start() {
        get("/hotels/:name", (req, res) ->
        {
            Document document = RequestedHotelDocument(req, res);
            return scrapper.getHotelLocation(document);
        });
        get("/hotels/:name/comments", (req, res) ->
        {
            Document document = RequestedReviewDocument(req, res);
            return scrapper.getHotelComments(document);
        });
        get("/hotels/:name/ratings", (req, res) ->
        {
            Document document = RequestedHotelDocument(req, res);
            return scrapper.getHotelRatings(document);
        });
        get("/hotels/:name/services", (req, res) ->
        {
            Document document = RequestedHotelDocument(req, res);
            return scrapper.getHotelServices(document);
        });
    }
    private Document RequestedHotelDocument(Request req, Response res) throws IOException {
        res.type("application/json");
        String name = req.params(":name");
        return scrapper.getHotelDocument(name);
    }

    private Document RequestedReviewDocument(Request req, Response res) throws IOException {
        res.type("application/json");
        String name = req.params(":name");
        return scrapper.getReviewsDocument(name);
    }
}
