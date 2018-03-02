package Models.Cards;

import java.util.Collections;
import java.util.List;
import Server.Database;

public class DestinationDeck {
    private int size;
    private List<DestinationCard> deck;

    public DestinationDeck() {
        deck = Database.getInstance().getDestinationCards();
        shuffle();
    }

    //We don't ever need to set the size of the deck
    public int size() {
        return deck.size();
    }

    public DestinationCard draw() {
        return deck.remove(0);
    }

    private void shuffle() {
        Collections.shuffle(deck);
    }
}
