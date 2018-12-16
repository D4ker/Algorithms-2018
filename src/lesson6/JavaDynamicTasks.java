package lesson6;

import kotlin.NotImplementedError;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class JavaDynamicTasks {
    /**
     * Наибольшая общая подпоследовательность.
     * Средняя
     *
     * Дано две строки, например "nematode knowledge" и "empty bottle".
     * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
     * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
     * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
     * Если общей подпоследовательности нет, вернуть пустую строку.
     * При сравнении подстрок, регистр символов *имеет* значение.
     */
    public static String longestCommonSubSequence(String first, String second) {
        throw new NotImplementedError();
    }

    /**
     * Наибольшая возрастающая подпоследовательность
     * Средняя
     *
     * Дан список целых чисел, например, [2 8 5 9 12 6].
     * Найти в нём самую длинную возрастающую подпоследовательность.
     * Элементы подпоследовательности не обязаны идти подряд,
     * но должны быть расположены в исходном списке в том же порядке.
     * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
     * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
     * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
     */

    // N - кол-во элементов в списке.
    // Трудоёмкость: T = O(N * log(N)). За счёт использования при решении бинарного поиска.
    // Ресурсоёмкость: R = O(N).
    public static List<Integer> longestIncreasingSubSequence(List<Integer> list) {
        if (list.isEmpty()) return new ArrayList<>();

        final int n = list.size();
        final Integer[] minEndElementForLength = new Integer[n];
        final int[] indexes = new int[n];
        final boolean[] checks = new boolean[n];

        // Заполнение вспомогательного массива бесконечностями
        for (int i = 0; i < n; i++) {
            minEndElementForLength[i] = Integer.MAX_VALUE;
        }

        // Вычисление длин (индексов) подпоследовательностей
        int minEndIndexOfMaxSequence = 0;
        for (int i = 0; i < n; i++) {
            final int j = binarySearch(checks, minEndElementForLength, list.get(i));
            if ((j == 0 || minEndElementForLength[j - 1] <= list.get(i)) && list.get(i) <= minEndElementForLength[j]) {
                if (j > indexes[minEndIndexOfMaxSequence]) {
                    minEndIndexOfMaxSequence = i;
                    indexes[i] = j;
                } else {
                    indexes[i] = j;
                }
                minEndElementForLength[j] = list.get(i);
                checks[j] = true;
            }
        }

        // Восстановление ответа
        final List<Integer> subSequence = new ArrayList<>();
        int currentFindingIndex = indexes[minEndIndexOfMaxSequence] - 1;
        for (int i = 0; i <= currentFindingIndex; i++) {
            subSequence.add(0);
        }
        subSequence.add(list.get(minEndIndexOfMaxSequence));

        for (int i = minEndIndexOfMaxSequence - 1; i >= 0; i--) {
            final int index = indexes[i];
            final Integer value = list.get(i);
            if (index == currentFindingIndex && value <= subSequence.get(index + 1)) {
                currentFindingIndex--;
                subSequence.set(index, value);
            } else if (index > currentFindingIndex && value <= subSequence.get(index + 1)) {
                subSequence.set(index, value);
            }
        }

        return subSequence;
    }

    // Вспомогательный метод для бинарного поиска
    private static int binarySearch(boolean[] checks, Integer[] a, final Integer key) {
        int low = 0;
        int high = a.length - 1;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = a[mid].compareTo(key);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                if (checks[mid] == false) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            }
        }
        return low;
    }

    /**
     * Самый короткий маршрут на прямоугольном поле.
     * Сложная
     *
     * В файле с именем inputName задано прямоугольное поле:
     *
     * 0 2 3 2 4 1
     * 1 5 3 4 6 2
     * 2 6 2 5 1 3
     * 1 4 3 2 6 2
     * 4 2 3 1 5 0
     *
     * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
     * В каждой клетке записано некоторое натуральное число или нуль.
     * Необходимо попасть из верхней левой клетки в правую нижнюю.
     * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
     * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
     *
     * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
     */

    // N - высота поля, M - ширина.
    // Трудоёмкость: T = O(N * M).
    // Ресурсоёмкость: R = O(M).
    public static int shortestPathOnField(String inputName) throws IOException {
        int[] oldArrayOfWeights;
        final int width;
        try (BufferedReader br = new BufferedReader(new FileReader(inputName))) {
            String newLine;
            if ((newLine = br.readLine()) != null) {
                width = newLine.length() / 2 + 1;
                oldArrayOfWeights = new int[width];
                oldArrayOfWeights[0] = 0;
                for (int i = 1; i < width; i++) {
                    oldArrayOfWeights[i] = oldArrayOfWeights[i - 1] + newLine.charAt(i * 2) - 48;
                }
            } else {
                throw new IllegalArgumentException(); // Пустой файл
            }
            final int[] arrayOfWeights = new int[width];
            while ((newLine = br.readLine()) != null) {
                arrayOfWeights[0] = oldArrayOfWeights[0] + newLine.charAt(0) - 48;
                for (int i = 1; i < width; i++) {
                    arrayOfWeights[i] = newLine.charAt(i * 2) - 48 +
                            Math.min(arrayOfWeights[i - 1], Math.min(oldArrayOfWeights[i - 1], oldArrayOfWeights[i]));
                }
                System.arraycopy(arrayOfWeights, 0, oldArrayOfWeights, 0, oldArrayOfWeights.length);
            }
        }
        return oldArrayOfWeights[width - 1];
    }

    // Задачу "Максимальное независимое множество вершин в графе без циклов"
    // смотрите в уроке 5
}
