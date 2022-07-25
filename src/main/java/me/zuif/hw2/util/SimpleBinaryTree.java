package me.zuif.hw2.util;

import me.zuif.hw2.model.Product;
import me.zuif.hw2.model.phone.Phone;
import me.zuif.hw2.service.PhoneService;

import java.util.Stack;

public class SimpleBinaryTree<E extends Product> {
    private final ProductComparator<E> productComparator = new ProductComparator<>();
    private Node<E> root;

    public static void main(String[] args) {
        PhoneService phoneService = PhoneService.getInstance();
        phoneService.createAndSaveProducts(10);
        SimpleBinaryTree<Phone> phoneTree = new SimpleBinaryTree<>();
        for (Phone phone : phoneService.findAll()) {
            phoneTree.add(phone);
        }

        //Если выводить объект полностью то в консоли дерево плывёт, поэтому сделал вывод по округлённой цене, количеству
        // или имени
        System.out.println("------------------------------------Title mode-------------------------\n");
        phoneTree.printTree("TITLE");
        System.out.println("------------------------------------Count mode-------------------------\n");
        phoneTree.printTree("COUNT");
        System.out.println("------------------------------------Price mode-------------------------\n");
        phoneTree.printTree("PRICE");
        System.out.println("------------------------------------Type mode-------------------------\n");
        phoneTree.printTree("TYPE");


        System.out.println("Sum left: " + phoneTree.sumLeftBranch());
        System.out.println(" ");
        System.out.println("Sum right " + phoneTree.sumRightBranch());
    }

    private Node addRecursive(Node current, E value) {
        if (current == null) {
            return new Node<E>(value);
        }
        if (productComparator.compare((E) current.product, value) < 0) {
            current.left = addRecursive(current.left, value);
        } else if (productComparator.compare((E) current.product, value) > 0) {
            current.right = addRecursive(current.right, value);
        } else {
            // value already exists
            return current;
        }

        return current;
    }

    public void printTree(String mode) {
        Stack globalStack = new Stack();


        globalStack.push(root);
        int gaps = 32;
        boolean isRowEmpty = false;

        while (isRowEmpty == false) {
            Stack localStack = new Stack();
            isRowEmpty = true;

            for (int j = 0; j < gaps; j++)
                System.out.print(' ');
            while (globalStack.isEmpty() == false) {
                Node temp = (Node) globalStack.pop();
                if (temp != null) {
                    switch (mode) {
                        case "PRICE" -> System.out.print(Math.ceil(temp.product.getPrice()));
                        case "TITLE" -> System.out.print(temp.product.getTitle());
                        case "COUNT" -> System.out.print(temp.product.getCount());
                        case "TYPE" -> System.out.print(temp.product.getType().name());
                        default -> throw new IllegalStateException("Unknown print mode: " + mode);
                    }
                    localStack.push(temp.left);
                    localStack.push(temp.right);
                    if (temp.left != null ||
                            temp.right != null)
                        isRowEmpty = false;
                } else {
                    System.out.print("__");
                    localStack.push(null);
                    localStack.push(null);
                }
                for (int j = 0; j < gaps * 2 - 2; j++)
                    System.out.print(' ');
            }
            System.out.println();
            gaps /= 2;
            while (localStack.isEmpty() == false)
                globalStack.push(localStack.pop());
            System.out.println();
        }

    }

    public double sumLeftBranch() {
        if (root.left == null) {
            return 0;
        }
        return sum(root.left);
    }

    public double sumRightBranch() {
        if (root.right == null) {
            return 0;
        }
        return sum(root.right);
    }

    private double sum(Node<E> root) {
        if (root == null)
            return 0;
        return (root.product.getPrice() + sum(root.left) +
                sum(root.right));
    }


    public void add(E value) {
        root = addRecursive(root, value);
    }

    private class Node<T extends Product> {
        public T product;
        public Node<T> left;
        public Node<T> right;

        Node(T product) {
            this.product = product;
            right = null;
            left = null;
        }

    }
}
