package lesson3;

import kotlin.NotImplementedError;
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

    // H - высота дерева.
    // Трудоёмкость: T = O(H).
    // Ресурсоёмкость: R = O(H).
    @Override
    public boolean remove(Object o) {
        final T value = (T) o;
        if (root == null || o == null) return false;
        final List<Node<T>> removable = findWithParent(root, null, value);
        Node<T> removableNode = removable.get(0);
        Node<T> removableNodeParent = removable.get(1);
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
                Node<T> leftNodeOfRightNode = rightNode.left;
                if (leftNodeOfRightNode == null) {
                    childModify(removableNodeParent, rightNode, removableParentSide);
                    rightNode.left = leftNode;
                } else {
                    final List<Node<T>> insteadOfRemote = findWithParent(leftNodeOfRightNode, rightNode, value);
                    Node<T> insteadOfRemoteNode = insteadOfRemote.get(0);
                    Node<T> insteadOfRemoteNodeParent = insteadOfRemote.get(1);
                    Node<T> newNode = new Node<T>(insteadOfRemoteNode.value);
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
    // L - высота дерева, корнем которого является start.
    // Трудоёмкость: T = O(L).
    // Ресурсоёмкость: R = O(L).
    private List<Node<T>> findWithParent(Node<T> start, Node<T> parent, T value) {
        if (start == null) return null;
        int comparison = value.compareTo(start.value);
        final List<Node<T>> list = new ArrayList<Node<T>>();
        list.add(start);
        list.add(parent);
        if (comparison == 0) {
            return list;
        }
        else if (comparison < 0) {
            if (start.left == null) return list;
            return findWithParent(start.left, start, value);
        }
        else {
            if (start.right == null) return list;
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

        // H - высота дерева.
        // Трудоёмкость: T = O(H).
        // Ресурсоёмкость: R = O(H).
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

        // L - высота дерева, корнем которого является start.
        // Трудоёмкость: T = O(L).
        // Ресурсоёмкость: R = O(L).
        private Node<T> findPrevNextNode(Node<T> start, T value, Node<T> desiredNode, boolean next) {
            if (start == null) return null;
            int comparison = value.compareTo(start.value);
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

        // H - высота дерева.
        // Трудоёмкость: T = O(H).
        // Ресурсоёмкость: R = O(H).
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
    public SortedSet<T> subSet(T fromElement, T toElement) {
        // TODO
        throw new NotImplementedError();
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */
    @NotNull
    @Override

    // Оценка для худшего случая.
    // N - кол-во узлов в дереве.
    // Трудоёмкость: T = O(N).
    // Ресурсоёмкость: R = O(N).
    public SortedSet<T> headSet(T toElement) {
        if (root == null || toElement == null) throw new NoSuchElementException();
        final SortedSet<T> headSet = new TreeSet<T>();
        headSetSearcher(headSet, root, toElement);
        return headSet;
    }

    private void headSetSearcher(SortedSet<T> headSet, Node<T> node, T value) {
        if (node != null) {
            headSetSearcher(headSet, node.left, value);
            if (node.value.compareTo(value) < 0) {
                headSet.add(node.value);
                headSetSearcher(headSet, node.right, value);
            }
        }
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    @NotNull
    @Override

    // Оценка для худшего случая.
    // N - кол-во узлов в дереве.
    // Трудоёмкость: T = O(N).
    // Ресурсоёмкость: R = O(N).
    public SortedSet<T> tailSet(T fromElement) {
        if (root == null || fromElement == null) throw new NoSuchElementException();
        final SortedSet<T> tailSet = new TreeSet<T>();
        tailSetSearcher(tailSet, root, fromElement);
        return tailSet;
    }

    private void tailSetSearcher(SortedSet<T> tailSet, Node<T> node, T value) {
        if (node != null) {
            tailSetSearcher(tailSet, node.left, value);
            if (node.value.compareTo(value) >= 0) {
                tailSet.add(node.value);
            }
            tailSetSearcher(tailSet, node.right, value);
        }
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
