package Data;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.io.FilterReader;
import java.util.ArrayList;
import java.util.Arrays;

import Models.Cards.DestinationCard;
import Models.Gameplay.Route;

public class DataHandler {
    private DestinationCardData dcData;
    private RouteData rtData;

    public DataHandler() { loadData(); }

    private void loadData() {
        dcData = new DestinationCardData();
        rtData = new RouteData();
        Gson gson = new Gson();

        try{
            JsonReader dcReader = new JsonReader(new FileReader("destinationCards.json"));
            JsonReader rtReader = new JsonReader(new FileReader("OrigTTR_Routes.json"));
            dcData = gson.fromJson(dcReader, DestinationCardData.class);
            rtData = gson.fromJson(rtReader, RouteData.class);
            System.out.println("Destination cards and routes successfully loaded.");
        }catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Destination cards and routes not loaded.");
        }
    }

    public ArrayList<DestinationCard> getDestinationCards(){
        return new ArrayList<>(Arrays.asList(dcData.data));
    }

    public ArrayList<Route> getRoutes() {
        return new ArrayList<>(Arrays.asList(rtData.data));
    }

}
