package Models.Gameplay;

public class Route {
    private String owner;
    private int points;
    private int length;
//    private String name;
    private String start;
    private String end;

    public Route() {}

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getName()
    {
        return start + " - " + end;
//        return name;
    }

//    public void setName(String name) {
//        this.name = name;
//    }

    public String getStartCity() {
        return start;
    }

    public void setStartCity(String startCity) {
        this.start = startCity;
    }

    public String getEndCity() {
        return end;
    }

    public void setEndCity(String endCity) {
        this.end = endCity;
    }
}
