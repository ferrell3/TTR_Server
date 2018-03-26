package Models.Gameplay;

/**
 * Created by kiphacking on 3/22/18.
 */

public class Score {
    private int total;
    private int routePoints;
    private int posDestPoints;
    private int negDestPoints;
    private int longestRoad;


    public Score() {
        total = 0;
        routePoints = 0;
        posDestPoints = 0;
        negDestPoints = 0;
        longestRoad = 0;
    }

    public int getTotal() {
        return total;
    }

    public int getRoutePoints() {
        return routePoints;
    }

    public int getPosDestPoints() {
        return posDestPoints;
    }

    public int getNegDestPoints() {
        return negDestPoints;
    }

    public int getLongestRoad() {
        return longestRoad;
    }

    // Calculates route points based off route length and updates total points
    public void addRoutePoints(int routeLength) {
        switch (routeLength){
            case 1:
                this.routePoints += 1;
                this.total += 1;
                break;
            case 2:
                this.routePoints += 2;
                this.total += 2;
                break;
            case 3:
                this.routePoints += 4;
                this.total += 4;
                break;
            case 4:
                this.routePoints += 7;
                this.total += 7;
                break;
            case 5:
                this.routePoints += 10;
                this.total += 10;
                break;
            case 6:
                this.routePoints += 15;
                this.total += 15;
                break;
        }

//        this.routePoints += routePoints;
//        this.total += routePoints;
    }

    public void addPosDestPoints(int posDestPoints) {
        this.posDestPoints += posDestPoints;
        this.total += posDestPoints;
    }

    public void addNegDestPoints(int negDestPoints) {
        this.negDestPoints -= negDestPoints;
        this.total -= negDestPoints;
    }

    public void addLongestRoad() {
        this.longestRoad += 10;
        this.total += 10;
    }
}
