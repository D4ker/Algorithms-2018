package lesson3;

import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

// Attention: comparable supported but comparator is not
@SuppressWarnings("WeakerAccess")
public class BinaryTree<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {

    private static class Node<T> {
        final T value;

        Node<T> left = null;

        Node<T> right = null;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> root = null;

    private int size = 0;

    @Override
    public boolean add(T t) {
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<>(t);
        if (closest == null) {
            root = newNode;
        }
        else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
        }
        else {
            assert closest.right == null;
            closest.right = newNode;
        }
        size++;
        return true;
    }

    public boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     */

    // N - кол-во узлов в дереве.
    // Трудоёмкость: T = O(Log(N)) - в среднем; T = O(N) - в худшем случае.
    // Ресурсоёмкость: R = O(Log(N)) - в среднем; R = O(N) - в худшем случае.
    @Override
    public boolean remove(Object o) {
        @SuppressWarnings("unchecked")
        final T value = (T) o;
        if (root == null || o == null) return false;
        final Pair<Node<T>, Node<T>> removable = findWithParent(root, null, value);
        final Node<T> removableNode = removable.getFirst();
        final Node<T> removableNodeParent = removable.getSecond();
        if (value.compareTo(removableNode.value) == 0) {
            final Node<T> leftNode = removableNode.left;
            final Node<T> rightNode = removableNode.right;
            final int removableParentSide =
                    removableNodeParent == null ? 0 : removableNode.value.compareTo(removableNodeParent.value);
            if (leftNode == null && rightNode == null) {
                childModify(removableNodeParent, null, removableParentSide);
            } else if (leftNode != null && rightNode == null) {
                childModify(removableNodeParent, leftNode, removableParentSide);
            } else if (leftNode == null) {
                childModify(removableNodeParent, rightNode, removableParentSide);
            } else {
                final Node<T> leftNodeOfRightNode = rightNode.left;
                if (leftNodeOfRightNode == null) {
                    childModify(removableNodeParent, rightNode, removableParentSide);
                    rightNode.left = leftNode;
                } else {
                    final Pair<Node<T>, Node<T>> insteadOfRemote = findWithParent(leftNodeOfRightNode, rightNode, value);
                    final Node<T> insteadOfRemoteNode = insteadOfRemote.getFirst();
                    final Node<T> insteadOfRemoteNodeParent = insteadOfRemote.getSecond();
                    final Node<T> newNode = new Node<T>(insteadOfRemoteNode.value);
                    newNode.left = leftNode;
                    newNode.right = rightNode;
                    childModify(removableNodeParent, newNode, removableParentSide);
                    childModify(insteadOfRemoteNodeParent, insteadOfRemoteNode.right,
                            insteadOfRemoteNode.value.compareTo(insteadOfRemoteNodeParent.value));
                }
            }
        } else {
            return false;
        }
        size--;
        return true;
    }

    // Перенаначение ссылок
    private void childModify(Node<T> parent, Node<T> child, int side) {
        if (side == 0) {
            root = child;
        } else if (side < 0) {
            parent.left = child;
        } else {
            parent.right = child;
        }
    }

    // Поиск узла и его родителя в дереве.
    // L - кол-во узлов в дереве, корнем которого является start.
    // Трудоёмкость: T = O(Log(L)) - в среднем; T = O(L) - в худшем случае.
    // Ресурсоёмкость: R = O(Log(L)) - в среднем; R = O(L) - в худшем случае.
    private Pair<Node<T>, Node<T>> findWithParent(Node<T> start, Node<T> parent, T value) {
        if (start == null) return null;
        final int comparison = value.compareTo(start.value);
        final Pair<Node<T>, Node<T>> pair = new Pair<Node<T>, Node<T>>(start, parent);
        if (comparison == 0) {
            return pair;
        }
        else if (comparison < 0) {
            if (start.left == null) return pair;
            return findWithParent(start.left, start, value);
        }
        else {
            if (start.right == null) return pair;
            return findWithParent(start.right, start, value);
        }
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        }
        else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        }
        else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    public class BinaryTreeIterator implements Iterator<T> {

        private Node<T> current = null;

        private BinaryTreeIterator() {}

        /**
         * Поиск следующего элемента
         * Средняя
         */

        // N - кол-во узлов в дереве.
        // Трудоёмкость: T = O(Log(N)) - в среднем; T = O(N) - в худшем случае.
        // Ресурсоёмкость: R = O(Log(N)) - в среднем; R = O(N) - в худшем случае.
        private Node<T> findNext() {
            if (current == null) {
                return minNode(root);
            }
            if (current.right != null) {
                return minNode(current.right);
            } else {
                return findPrevNextNode(root, current.value, null, true);
            }
        }

        // Поиск предыдущего/следующего узла в итераторе.
        // L - кол-во узлов в дереве, корнем которого является start.
        // Трудоёмкость: T = O(Log(L)) - в среднем; T = O(L) - в худшем случае.
        // Ресурсоёмкость: R = O(Log(L)) - в среднем; R = O(L) - в худшем случае.
        private Node<T> findPrevNextNode(Node<T> start, T value, Node<T> desiredNode, boolean next) {
            if (start == null) return null;
            final int comparison = value.compareTo(start.value);
            if (comparison == 0) {
                return desiredNode;
            }
            else if (comparison < 0) {
                if ((next && start.value.compareTo(value) > 0) || (!next && start.value.compareTo(value) < 0)) {
                    return findPrevNextNode(start.left, value, start, next);
                }
                return findPrevNextNode(start.left, value, desiredNode, next);
            }
            else {
                if ((next && start.value.compareTo(value) > 0) || (!next && start.value.compareTo(value) < 0)) {
                    return findPrevNextNode(start.right, value, start, next);
                }
                return findPrevNextNode(start.right, value, desiredNode, next);
            }
        }

        // Поиск минимального узла в дереве с корнем node.
        // L - кол-во узлов в дереве, корнем которого является node.
        // Трудоёмкость: T = O(Log(L)) - в среднем; T = O(L) - в худшем случае.
        // Ресурсоёмкость: R = O(Log(L)) - в среднем; R = O(L) - в худшем случае.
        private Node<T> minNode(Node<T> node) {
            if (node == null) return null;
            Node<T> currentNode = node;
            Node<T> leftNode = currentNode.left;
            while (leftNode != null) {
                currentNode = leftNode;
                leftNode = currentNode.left;
            }
            return currentNode;
        }

        @Override
        public boolean hasNext() {
            return findNext() != null;
        }

        @Override
        public T next() {
            current = findNext();
            if (current == null) throw new NoSuchElementException();
            return current.value;
        }

        /**
         * Удаление следующего элемента
         * Сложная
         */

        // N - кол-во узлов в дереве.
        // Трудоёмкость: T = O(Log(N)) - в среднем; T = O(N) - в худшем случае.
        // Ресурсоёмкость: R = O(Log(N)) - в среднем; R = O(N) - в худшем случае.
        @Override
        public void remove() {
            if (current != null) {
                final T oldValue = current.value;
                final Node<T> newCurrent = findPrevNextNode(root, oldValue, null, false);
                BinaryTree.this.remove(oldValue);
                current = newCurrent;
            } else throw new NoSuchElementException();
        }
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinaryTreeIterator();
    }

    @Override
    public int size() {
        return size;
    }


    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    /**
     * Для этой задачи нет тестов (есть только заготовка subSetTest), но её тоже можно решить и их написать
     * Очень сложная
     */
    @NotNull
    @Override

    // Оценки методов BinaryTreeSet можно посмотреть в соответствующем классе.
    // Трудоёмкость: T = O(1).
    // Ресурсоёмкость: R = O(1).
    public SortedSet<T> subSet(T fromElement, T toElement) {
        if (root == null || fromElement == null || toElement == null) throw new NoSuchElementException();
        return new BinaryTreeSet<T>(this, fromElement, toElement);
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */
    @NotNull
    @Override

    // Оценки методов BinaryTreeSet можно посмотреть в соответствующем классе.
    // Трудоёмкость: T = O(1).
    // Ресурсоёмкость: R = O(1).
    public SortedSet<T> headSet(T toElement) {
        if (root == null || toElement == null) throw new NoSuchElementException();
        return new BinaryTreeSet<T>(this, null, toElement);
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    @NotNull
    @Override

    // Оценки методов BinaryTreeSet можно посмотреть в соответствующем классе.
    // Трудоёмкость: T = O(1).
    // Ресурсоёмкость: R = O(1).
    public SortedSet<T> tailSet(T fromElement) {
        if (root == null || fromElement == null) throw new NoSuchElementException();
        return new BinaryTreeSet<T>(this, fromElement, null);
    }

    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }
}
