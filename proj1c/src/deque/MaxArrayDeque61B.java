package deque;
import java.util.*;
import java.util.Comparator;

public class MaxArrayDeque61B<T> extends ArrayDeque61B<T> {
    private Comparator<T> comparator;
    public MaxArrayDeque61B(Comparator<T> c) {
        comparator = c;
    }

    public T max() {
        if (this.isEmpty()) {
            return null;
        }
        T max = get(0);
        for (T x: this) {
            if (comparator.compare(x, max) > 0) {
                max = x;
            }
        }
        return max;
    }

    public T max(Comparator c) {
        if (this.isEmpty()) {
            return null;
        }
        T max = get(0);
        for (T x: this) {
            if (c.compare(x, max) > 0) {
                max = x;
            }
        }
        return max;
    }
}
