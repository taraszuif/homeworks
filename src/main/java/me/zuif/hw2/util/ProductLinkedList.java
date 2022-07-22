package me.zuif.hw2.util;

import me.zuif.hw2.model.Product;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;

public class ProductLinkedList<E extends Product> implements Iterable<E> {
    private final Set<Integer> versions;
    private Node<E> first;
    private Node<E> last;
    private int size;

    public ProductLinkedList() {
        size = 0;
        versions = new HashSet<>();
    }

    public void addFirst(E product, int version) {
        final Node<E> f = first;
        final Node<E> newNode = new Node<E>(product, version, LocalDateTime.now(), first, null);
        first = newNode;
        if (f == null)
            last = newNode;
        else
            f.previous = newNode;
        size++;
        versions.add(newNode.version);
    }

    public E findByVersion(int version) {
        if (validateVersion(version)) {
            for (Node<E> tmp = first; tmp != null; tmp = tmp.next) {
                if (tmp.version == version) {
                    return tmp.product;
                }
            }
        }
        throw new NoSuchElementException("There are no products with this version");
    }


    public boolean deleteByVersion(int version) {
        if (validateVersion(version)) {
            for (Node<E> tmp = first; tmp != null; tmp = tmp.next) {
                if (tmp.version == version) {
                    unlink(tmp);
                    return true;
                }
            }
        }
        return false;
    }

    public LocalDateTime getFirstVersionDate() {
        if (last == null) {
            throw new NoSuchElementException("There are no first version date");
        }
        return last.date;
    }

    public LocalDateTime getLastVersionDate() {
        if (first == null) {
            throw new NoSuchElementException("There are no last version date");
        }
        return first.date;
    }

    private E unlink(Node<E> node) {
        final E product = node.product;
        final Node<E> next = node.next;
        final Node<E> previous = node.previous;

        if (previous == null) {
            first = next;
        } else {
            previous.next = next;
            node.previous = null;
        }
        if (next == null) {
            last = previous;
        } else {
            next.previous = previous;
            node.next = null;
        }
        node.product = null;
        versions.remove(node.version);
        size--;
        return product;
    }

    public boolean setByVersion(int version, E product) {
        if (validateVersion(version)) {
            for (Node<E> tmp = first; tmp != null; tmp = tmp.next) {
                if (tmp.version == version) {
                    tmp.product = product;
                    return true;
                }
            }
        }
        return false;
    }

    public boolean validateVersion(int version) {
        return versions.contains(version);
    }

    public int getVersionCount() {
        return versions.size();
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedListIterator();
    }


    @Override
    public void forEach(Consumer<? super E> action) {
        Iterable.super.forEach(action);
    }

    @Override
    public Spliterator<E> spliterator() {
        return Iterable.super.spliterator();
    }

    private class LinkedListIterator implements Iterator<E> {
        private Node<E> next;
        private int index;

        public LinkedListIterator() {
            index = 0;
            next = first;
        }

        @Override
        public boolean hasNext() {
            return index < size;
        }


        @Override
        public E next() {
            if (hasNext()) {
                Node<E> tmp = next;
                next = next.next;
                index++;
                return tmp.product;
            }

            throw new NoSuchElementException();
        }
    }

    private class Node<T> {
        public int version;
        public LocalDateTime date;
        public T product;
        public Node next;
        public Node previous;

        public Node(T product, int version, LocalDateTime date, Node<T> next, Node<T> previous) {
            this.product = product;
            this.version = version;
            this.date = date;
            this.next = next;
            this.previous = previous;
        }
    }
}
