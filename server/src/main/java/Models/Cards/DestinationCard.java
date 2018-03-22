package Models.Cards;

public class DestinationCard {
    private String start;
    private String end;
    private int points;

    public DestinationCard() {}

    public String getCity1() {
        return start;
    }

    public void setCity1(String city1) {
        this.start = city1;
    }

    public String getCity2() {
        return end;
    }

    public void setCity2(String city2) {
        this.end = city2;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
