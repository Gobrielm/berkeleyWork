package core;

import java.util.Objects;

public class Coords implements Comparable<Coords> {
    int x;
    int y;
    int distance;
    Coords prev;
    Coords(int x, int y, int distance) {
        this.x = x;
        this.y = y;
        this.distance = distance;
    }

    Coords(int x1, int y1, int x2, int y2) {
        this.x = x1;
        this.y = y1;
        distance = (int) Math.floor(Math.sqrt(Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1 - y2), 2)));
    }

    Coords(int x1, int y1, int x2, int y2, Coords prev) {
        x = x1;
        y = y1;
        this.prev = prev;
        distance = (int) Math.floor(Math.sqrt(Math.pow(Math.abs(x1 - x2), 2) + Math.pow(Math.abs(y1 - y2), 2)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coords coords = (Coords) o;
        return x == coords.x && y == coords.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public int compareTo(Coords other) {
        return Integer.compare(this.distance, other.distance);
    }
}
