package Models.Cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrainDeck {
    private String[] trainTypes =
            { "purple", "white", "blue", "yellow", "orange", "black", "red", "green", "wild"};
    private List<TrainCard> deck;

    public TrainDeck() {
        deck = new ArrayList<>();
        init();
    }

    private void init(){
        int numCards = 12;
        //12 of each color, except 14 locomotives
        for(String type : trainTypes)
        {
            //for locomotive
            if(type.equals("wild")) { numCards = 14; }
            for(int j = 0; j < numCards; j++)
            {
                deck.add(new TrainCard(type));
            }
        }
        shuffle();
    }

    public int size() {
        return deck.size();
    }

    public TrainCard draw() {
        if(deck.size() == 0)
        {
            init();
        }
        return deck.remove(0);
    }

    private void shuffle() {
        Collections.shuffle(deck);
    }
}
