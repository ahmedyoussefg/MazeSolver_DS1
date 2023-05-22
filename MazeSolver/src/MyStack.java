import java.util.EmptyStackException;

import static java.lang.System.exit;

public class MyStack {
    public static class SingleLinkedList {
        static class Node {
            Node next;
            MazeSolver.mazePoint value;
        }
    }

    public Object pop() {
        if (isEmpty()) {
            System.out.println("Error");
            exit(0);
            throw new EmptyStackException();
        }
        top = top.next;
        stack_size--;
        return top;
    }

    public Object peek() {
        if (isEmpty()) {
            System.out.println("Error");
            exit(0);
            throw new EmptyStackException();
        }
        return top;
    }

    public void push(Object element) {
        SingleLinkedList.Node temp = (SingleLinkedList.Node) element;
        temp.next = top;
        top = temp;
        stack_size++;
    }

    public boolean isEmpty() {
        return (top == null);
    }

    public int size() {
        return stack_size;
    }

    public static SingleLinkedList.Node top = null;
    public static int stack_size = 0;

}
