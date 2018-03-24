package Models.Gameplay;

public class Route {
    private String owner;
    private int points;
    private int length;
//    private String name;
    private String start;
    private String end;
    private String color;
    private int routeNumber;
    private int startX;
    private int startY;
    private int endX;
    private int endY;


    public Route() {}

    public Route(String startPt, String endPt, int edgeWeight){
        start = startPt;
        end = endPt;
        length = edgeWeight;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

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

    public int getRouteNumber() {
        return routeNumber;
    }

    public void setRouteNumber(int routeNumber) {
        this.routeNumber = routeNumber;
    }

    public int getStartX() {
        return startX;
    }

    public void setStartX(int startX) {
        this.startX = startX;
    }

    public int getStartY() {
        return startY;
    }

    public void setStartY(int startY) {
        this.startY = startY;
    }

    public int getEndX() {
        return endX;
    }

    public void setEndX(int endX) {
        this.endX = endX;
    }

    public int getEndY() {
        return endY;
    }

    public void setEndY(int endY) {
        this.endY = endY;
    }
}
