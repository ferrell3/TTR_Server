package Data;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

import Models.Cards.DestinationCard;

public class DataHandler {
    private DestinationCardData dcData;

    public DataHandler() { loadData(); }

    private void loadData() {
        dcData = new DestinationCardData();
        Gson gson = new Gson();

        try{
            JsonReader dcReader = new JsonReader(new FileReader("destinationCards.json"));
            dcData = gson.fromJson(dcReader, DestinationCardData.class);
            System.out.println("Destination cards successfully loaded.");
        }catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Destination cards not loaded.");
        }
    }

    public ArrayList<DestinationCard> getDestinationCards(){
        return new ArrayList<>(Arrays.asList(dcData.data));
    }
}
