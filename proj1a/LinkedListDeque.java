public class LinkedListDeque<T> {
    private class Node {
        private Node(T item) {
            this.item = item;
            this.next = null;
            this.prev = null;
        }
        private T item;
        private Node next;
        private Node prev;
    }

    /* A LinkedListDeque contains a circular sentinel and an int to cache size */
    private Node sentinel;
    private int size;

    public LinkedListDeque() {
        size = 0;
        sentinel = new Node(null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
    }

    public void addFirst(T item) {
        Node temp = sentinel.next;
        sentinel.next = new Node(item);
        // Setting the new first node's prev pointer to sentinel
        sentinel.next.prev = sentinel;
        // Setting the new first node's next pointer to the old first
        sentinel.next.next = temp;
        // Setting the prev pointer of the old first to be the new first node
        temp.prev = sentinel.next;
        size++;
    }

    public void addLast(T item) {
        // Storing the old last node
        Node temp = sentinel.prev;
        // Creating new node
        temp.next = new Node(item);
        // Setting sentinel prev pointer to new last
        sentinel.prev = temp.next;
        // The new node's prev is the old last
        temp.next.prev = temp;
        // The new node's next is the sentinel
        temp.next.next = sentinel;
        size++;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void printDeque() {
        Node curr = sentinel.next;
        while (curr != sentinel) {
            System.out.print(curr.item + " ");
            curr = curr.next;
        }
        System.out.println();
    }

    public T removeFirst() {
        Node temp;
        if (size == 0) {
            return null;
        } else {
            temp = sentinel.next;
            sentinel.next = sentinel.next.next;
            sentinel.next.prev = sentinel;
        }
        size--;
        return temp.item;
    }

    public T removeLast() {
        Node temp;
        if (size == 0) {
            return null;
        } else {
            temp = sentinel.prev;
            sentinel.prev = sentinel.prev.prev;
            sentinel.prev.next = sentinel;
        }
        size--;
        return temp.item;
    }

    public T get(int index) {
        if (size <= index) {
            return null;
        } else {
            // The first node has an index of 0
            Node curr = sentinel;
            // Move to the current index of the list
            for (int i = 0; i <= index; i++) {
                curr = curr.next;
            }
            return curr.item;
        }
    }
}
