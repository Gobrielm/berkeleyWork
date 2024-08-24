package deque;

import java.util.*;

public class LinkedListDeque61B<T> implements Deque61B<T> {
    public class Node {
        T item;
        Node next = this;
        Node prev = this;
        public Node(T x) {
            item = x;
        }
        public T getRecursiveNode(int index) {
            //Checks the size first
            if (index == 0) {
                return item;
            }

            return next.getRecursiveNode(index - 1);
        }
    }
    private Node sentinal;
    private int size;
    public LinkedListDeque61B() {
        size = 0;
        sentinal = new Node(null);
    }

    @Override
    public void addFirst(T x) {
        Node temp = new Node(x);
        Node wasNext = sentinal.next;
        temp.next = wasNext;
        wasNext.prev = temp;

        temp.prev = sentinal;
        sentinal.next = temp;

        size++;
    }

    @Override
    public void addLast(T x) {
        Node temp = new Node(x);
        Node wasLast = sentinal.prev;
        temp.prev = wasLast;
        wasLast.next = temp;

        temp.next = sentinal;
        sentinal.prev = temp;
        size++;
    }

    @Override
    public List<T> toList() {
        List<T> returnList = new ArrayList<>();
        Node current = sentinal;
        while (current.next != null && current.next != sentinal) {
            current = current.next;
            returnList.add(current.item);
        }
        return returnList;
    }

    @Override
    public boolean isEmpty() {
        if (sentinal.next == sentinal.prev && sentinal.prev == sentinal) {
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
        if (size < 1) {
            return null;
        }
        Node willDelete = sentinal.next;
        sentinal.next = willDelete.next;
        willDelete.next.prev = sentinal;
        willDelete.prev = null;
        willDelete.next = null;
        size--;
        return willDelete.item;
    }

    @Override
    public T removeLast() {
        if (size < 1) {
            return null;
        }
        Node willDelete = sentinal.prev;
        sentinal.prev = willDelete.prev;
        willDelete.prev.next = sentinal;

        willDelete.next = null;
        willDelete.prev = null;
        size--;
        return willDelete.item;
    }

    @Override
    public T get(int index) {
        //Checks the size first
        if (index > size || index < 0) {
            return null;
        }
        Node current = sentinal.next;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.item;
    }

    @Override
    public T getRecursive(int index) {
        if (index > size || index < 0) {
            return null;
        }
        return sentinal.next.getRecursiveNode(index);
    }

    @Override
    public String toString() {
        return this.toList().toString();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if ((other instanceof Deque61B<?> otherSet)) {
            if (this.size != (otherSet.size())) {
                return false;
            }
            int i = 0;
            for (T x: this) {
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
        return new Iterator<T>() {
            Node current = sentinal.next;
            @Override
            public boolean hasNext() {
                return current != sentinal;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T temp = current.item;
                current = current.next;
                return temp;
            }
        };
    }
}

