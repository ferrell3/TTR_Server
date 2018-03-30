package Data;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import Models.Cards.DestinationCard;
import Models.Gameplay.Route;

public class DataHandler {
    private DestinationCardData dcData;
    private RouteData rtData;
    private HashMap<Integer, Route> routeMap;
    private HashMap<Integer, Route> doubleRouteMap;
    private RouteData drtData;

    public DataHandler() {
        routeMap = new HashMap<>();
        doubleRouteMap = new HashMap<>();
        loadData();
    }

    private void loadData() {
        dcData = new DestinationCardData();
        rtData = new RouteData();
        drtData = new RouteData();
        Gson gson = new Gson();

        try{
            JsonReader dcReader = new JsonReader(new FileReader("destinationCards.json"));
            JsonReader rtReader = new JsonReader(new FileReader("GOT_TTR_Routes.json"));
            JsonReader drtReader = new JsonReader(new FileReader("GOT_DOUBLE_TTR_Routes.json"));

            dcData = gson.fromJson(dcReader, DestinationCardData.class);
            rtData = gson.fromJson(rtReader, RouteData.class);
            drtData = gson.fromJson(drtReader, RouteData.class);
            System.out.println("Destination cards and routes successfully loaded.");
        }catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Destination cards and routes not loaded.");
        }
        initRouteMap();
    }

    public ArrayList<DestinationCard> getDestinationCards(){
        return new ArrayList<>(Arrays.asList(dcData.data));
    }

    private void initRouteMap(){
        for(int i = 0; i < rtData.data.length; i++)
        {
            Route rt = rtData.data[i];
            rt.setRouteNumber(i);
            routeMap.put(i, rt);
        }

        for(int i = 0; i < drtData.data.length; i++)
        {
            Route drt = drtData.data[i];
            drt.setRouteNumber(i);
            doubleRouteMap.put(i, drt);
        }
    }

    public HashMap<Integer, Route> getRoutes() {
        return routeMap;
    }

    public HashMap<Integer, Route> getDoubleRouteMap() {
        return doubleRouteMap;
    }

}
