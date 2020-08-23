package utilities;

import java.util.List;

public class DoubleLinked<T> {
    public static class Node<T> {
        private T t;
        private Node<T> prev, next;

        protected Node(Node<T> prev, Node<T> next, T t) {
            this.prev = prev;
            this.next = next;
            this.t = t;
        }

        protected void push(Node<T> next) {
            this.next = next;
        }

        public Node<T> next() { return next; }
        public Node<T> prev() { return prev; }

        public boolean hasNext() { return next != null; }
        public boolean hasPrev() { return prev != null; }

        public T get() {
            return t;
        }
    }

    private Node<T> start, last;
    private int size;

    public DoubleLinked() {
        size = 0;
    }
    public DoubleLinked(List<T> list) {
        super();
        for(T t : list) {
            push(t);
        }
    }

    public void push(T t) {
        if(start == null) {
            start = new Node<>(null, null, t);
            last = start;
        }
        else {
            last.push(new Node<T>(last, null, t));
            last = last.next();
        }
        size++;
    }

    public Node<T> getStart() {
        return start;
    }
}
