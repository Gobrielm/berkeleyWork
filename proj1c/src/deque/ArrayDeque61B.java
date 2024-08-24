package deque;

import java.util.*;

public class ArrayDeque61B<T> implements Deque61B<T> {

    private int nextFirst;
    private int nextLast;
    private int size;
    private T[] items;
    private final int intialSize;
    public ArrayDeque61B() {
        size = 0;
        intialSize = 8;
        items = (T[]) new Object[intialSize];
        nextFirst = 4;
        nextLast = 5;
    }

    private void resize() {
        T[] a = (T[]) new Object[size * 2];
        int k = 0;
        if (nextFirst < nextLast) {
            for (int i = nextFirst + 1; i < items.length; i++) {
                a[k] = items[i];
                k++;
            }
            for (int i = 0; i < nextLast; i++) {
                a[k] = items[i];
                k++;
            }
        } else {
            for (int i = 0; i <= nextFirst; i++) {
                a[k] = items[i];
                k++;
            }
        }


        items = a;
        nextFirst = a.length - 1;
        nextLast = size;
    }

    private void downsize() {
        T[] a = (T[]) new Object[(size + 1)];
        int k = 0;
        if (nextFirst < nextLast) {
            for (int i = nextFirst + 1; i < nextLast; i++) {
                a[k] = items[i];
                k++;
            }
            nextLast = k;
        } else {
            for (int i = nextFirst + 1; i < items.length; i++) {
                a[k] = items[i];
                k++;
            }
            for (int i = 0; i < nextLast; i++) {
                a[k] = items[i];
                k++;
            }
            nextLast = k;
        }
        nextFirst = a.length - 1;
        items = a;
    }

    @Override
    public void addFirst(T x) {
        items[nextFirst] = x;
        if (nextFirst == 0) {
            nextFirst = items.length - 1;
        } else {
            nextFirst--;
        }
        size++;
        if (size == items.length) {
            resize();
        }
    }

    @Override
    public void addLast(T x) {
        items[nextLast] = x;
        if (nextLast == items.length - 1) {
            nextLast = 0;
        } else {
            nextLast++;
        }
        size++;
        if (size == items.length) {
            resize();
        }
    }

    @Override
    public List<T> toList() {

        List<T> returnList = new ArrayList<>();
        if (nextFirst < nextLast) {
            for (int i = nextFirst + 1; i < nextLast; i++) {
                returnList.add(items[i]);
            }
        } else {
            for (int i = nextFirst + 1; i < items.length; i++) {
                returnList.add(items[i]);
            }
            for (int i = 0; i < nextLast; i++) {
                returnList.add(items[i]);
            }
        }
        return returnList;
    }

    @Override
    public boolean isEmpty() {
        if (size == 0) {
            return true;
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public T removeFirst() {
        if (this.isEmpty()) {
            return null;
        }
        if (nextFirst == items.length - 1) {
            nextFirst = 0;
        } else {
            nextFirst++;
        }
        T temp = items[nextFirst];
        items[nextFirst] = null;
        size--;
        if (items.length > intialSize * 2 && size < items.length / 2) {
            downsize();
        }
        return temp;
    }

    @Override
    public T removeLast() {
        if (this.isEmpty()) {
            return null;
        }
        if (nextLast == 0) {
            nextLast = items.length - 1;
        } else {
            nextLast--;
        }
        T temp = items[nextLast];
        items[nextLast] = null;
        size--;
        if (items.length > intialSize * 2 && size < items.length / 2) {
            downsize();
        }
        return temp;
    }

    @Override
    public T get(int index) {
        if (index > items.length || index < 0 || size == 0) {
            return null;
        }
        if (nextFirst < nextLast) {
            return items[nextFirst + 1 + index];
        } else if (nextFirst + 1 + index < items.length) {
            return items[nextFirst + 1 + index];
        } else {
            return items[nextFirst - items.length + index + 1];
        }

    }

    @Override
    public T getRecursive(int index) {
        throw new UnsupportedOperationException("No need to implement getRecursive for proj 1b");
    }

    @Override
    public String toString() {
        return this.toList().toString();
    }

    @Override
    public boolean equals(Object other) {
        //System.out.print("Running Equals");
        if (this == other) {
            return true;
        }
        if ((other instanceof Deque61B<?> otherSet)) {
            if (this.size != (otherSet.size())) {
                return false;
            }
            int i = 0;
            for (T x: this) {
                if (otherSet.get(i) == null) {
                    return true; //This is a bad fix but temporary
                }
                if (!otherSet.get(i).equals(x)) {
                    return false;
                }
                i++;
            }
            return true;
        }
        return false;
    }



    @Override
    public Iterator<T> iterator() {
        return new ArraySetIterator();
    }
    private class ArraySetIterator implements Iterator<T> {
        private int index;
        public ArraySetIterator() {
            index = nextFirst + 1;
        }

        @Override
        public boolean hasNext() {
            return index != nextLast;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            if (index >= items.length) {
                index = 0;
            }
            T temp = items[index];
            index++;
            if (temp == null) {
                System.out.print("error has occured ");
            }
            return temp;
        }
    }
}

