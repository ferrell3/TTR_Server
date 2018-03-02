package Data;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;

import Models.DestinationCard;

public class DataHandler {
    private DestinationCardData dcData;

    public DataHandler() { loadData(); }

    private void loadData() {
        dcData = new DestinationCardData();
        Gson gson = new Gson();

        try{
            JsonReader dcReader = new JsonReader(new FileReader("destinationCards.json"));
            dcData = gson.fromJson(dcReader, DestinationCardData.class);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<DestinationCard> getDestinationCards(){
        return new ArrayList<>(Arrays.asList(dcData.data));
    }
}
