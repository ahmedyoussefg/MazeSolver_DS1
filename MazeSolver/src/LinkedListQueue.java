import static java.lang.System.exit;

public class LinkedListQueue {
    public SingleLinkedList.Node front = null;
    public SingleLinkedList.Node rear = null;
    public int size = 0;

    public static class SingleLinkedList {
        /* Implement your linked list class here*/

        public static class Node {
            MazeSolver.mazePoint data;
            Node next;
        }

        Node head = null;

        public Object get(int index) {
            Node node = new Node();
            int counter = 0;
            node = head;
            while (counter != index && node.next != null) {
                node = node.next;
                counter++;
            }
            // needs special handling for out of boundary indexes.
            return node;
        }
    }

    /*** Inserts an item at the queue front.
     * @param item -> item to be enqueued */
    public void enqueue(Object item) {
        if (this.isEmpty()) {
            this.front = (SingleLinkedList.Node) item;
        } else {
            rear.next = (SingleLinkedList.Node) item;
        }
        this.rear = (SingleLinkedList.Node) item;
        this.size++;
    }

    /*** Removes the object at the queue rear and returns it.*/
    public Object dequeue() {
        if (this.isEmpty()) {
            System.out.println("Error");
            exit(0);
        }
        this.front = front.next;
        this.size--;
        return front;
    }

    /*** Tests if this queue is empty.*/
    public boolean isEmpty() {
        return (this.size() == 0);
    }

    /*** Returns the number of elements in the queue*/
    public int size() {
        return this.size;
    }

    public void printQueue() {
        SingleLinkedList temp = new SingleLinkedList();
        temp.head = this.front;
        System.out.print("[");
        for (int i = this.size() - 1; i >= 0; i--) {
            SingleLinkedList.Node elem = (SingleLinkedList.Node) temp.get(i);
            if (i != 0)
                System.out.print(elem.data + ", ");
            else
                System.out.print(elem.data);
        }
        System.out.print("]");
    }
}
