package lesson3;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class BinaryTreeSet<T extends Comparable<T>> extends AbstractSet<T> implements SortedSet<T> {

    private final BinaryTree<T> tree;

    private final T start;
    private final T end;

    BinaryTreeSet(BinaryTree<T> tree, T start, T end) {
        this.tree = tree;
        this.start = start;
        this.end = end;
    }

    // N - кол-во узлов в дереве tree.
    // Трудоёмкость: T = O(Log(N)) - в среднем; T = O(N) - в худшем случае.
    // Ресурсоёмкость: R = O(Log(N)) - в среднем; R = O(N) - в худшем случае.
    @Override
    public boolean add(T element) {
        if (elementInInterval(element)) return tree.add(element);
        return false;

    }

    // N - кол-во узлов в дереве tree.
    // Трудоёмкость: T = O(Log(N)) - в среднем; T = O(N) - в худшем случае.
    // Ресурсоёмкость: R = O(Log(N)) - в среднем; R = O(N) - в худшем случае.
    @Override
    public boolean remove(Object o) {
        @SuppressWarnings("unchecked")
        final T element = (T) o;
        if (elementInInterval(element)) return tree.remove(element);
        return false;
    }

    private boolean elementInInterval(T element) {
        if (start == null && end == null) return false;
        return (start == null || element.compareTo(start) >= 0) && (end == null || element.compareTo(end) < 0);
    }

    public class BinaryTreeSetIterator implements Iterator<T> {

        private final Iterator<T> treeIt = BinaryTreeSet.this.tree.iterator();

        private T next = null;

        // Оценка для худшего случая.
        // N - кол-во узлов в дереве tree.
        // Трудоёмкость: T = O(N^2).
        // Ресурсоёмкость: R = O(N^2).

        /* Пояснение к оценке: Цикл while в худшем случае выполнется N раз. Каждую итерацию мы складываем сложности
        * методов treeIt.hasNext() и treeIt.next(), которые используют метод findNext(), из-за чего имеют сложность
        * O(Log(N)) в среднем или O(N) в худшем случае. Если рассмотреть случай, когда каждый узел нашего дерева tree
        * имеет только левого ребёнка, то при первой итерации сложность поиска будет равна N - максимальной сложности
        * метода findNext() (из-за того, что он использует minNode()); при второй итерации сложность будет равна
        * N - 1 (из-за того, что метод findNext() использует findPrevNextNode()); при третьей итерации N - 2 и т. д.
        * В реультате итоговая сложность будет равна O(N + (N - 1) + (N - 2) + ... + 2 + 1) == O(((N + 1) * N) / 2) ==
        * == O(N^2), что и требовалось доказать. */
        BinaryTreeSetIterator() {
            while (treeIt.hasNext()) {
                final T next = treeIt.next();
                if (elementInInterval(next)) {
                    this.next = next;
                    break;
                }
            }
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        // Оценка объясняется тем, что в коде используются методы treeIt.hasNext()
        // и treeIt.next(), которые, в свою очередь, используют метод findNext().
        // N - кол-во узлов в дереве tree.
        // Трудоёмкость: T = O(Log(N)) - в среднем; T = O(N) - в худшем случае.
        // Ресурсоёмкость: R = O(Log(N)) - в среднем; R = O(N) - в худшем случае.
        @Override
        public T next() {
            if (next == null) throw new NoSuchElementException();
            final T result = next;
            next = treeIt.hasNext() ? treeIt.next() : null;
            if (!elementInInterval(next)) next = null;
            return result;
        }

        // N - кол-во узлов в дереве tree.
        // Трудоёмкость: T = O(Log(N)) - в среднем; T = O(N) - в худшем случае.
        // Ресурсоёмкость: R = O(Log(N)) - в среднем; R = O(N) - в худшем случае.
        @Override
        public void remove() {
            treeIt.remove();
        }
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinaryTreeSetIterator();
    }

    // Оценка для худшего случая.
    // N - кол-во узлов в дереве tree.
    // Трудоёмкость: T = O(N).
    // Ресурсоёмкость: R = O(N).
    @Override
    public int size() {
        int size = 0;
        for (T element : BinaryTreeSet.this.tree) {
            if (elementInInterval(element)) size++;
            if (end != null && element.compareTo(end) >= 0) break;
        }
        return size;
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        final T element = (T) o;
        return tree.contains(o) && elementInInterval(element);
    }

    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return tree.comparator();
    }

    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        return new BinaryTreeSet<T>(tree, fromElement, toElement);
    }

    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        return new BinaryTreeSet<T>(tree, null, toElement);
    }

    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return new BinaryTreeSet<T>(tree, fromElement, null);
    }

    // Оценка для худшего случая.
    // N - кол-во узлов в дереве tree.
    // Трудоёмкость: T = O(N).
    // Ресурсоёмкость: R = O(N).
    @Override
    public T first() {
        for (T element : BinaryTreeSet.this.tree) {
            if (elementInInterval(element)) return element;
        }
        throw new NoSuchElementException();
    }

    // Оценка для худшего случая.
    // N - кол-во узлов в дереве tree.
    // Трудоёмкость: T = O(N).
    // Ресурсоёмкость: R = O(N).
    @Override
    public T last() {
        T last = null;
        for (T element : BinaryTreeSet.this.tree) {
            if (elementInInterval(element)) last = element;
            if (end != null && element.compareTo(end) >= 0) break;
        }
        if (last == null) throw new NoSuchElementException();
        return last;
    }
}
