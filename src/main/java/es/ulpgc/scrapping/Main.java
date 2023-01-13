package es.ulpgc.scrapping;

public class Main {
    public static void main(String[] args) {
        new APIRest(new BookingScrapperJSON()).start();
    }
}