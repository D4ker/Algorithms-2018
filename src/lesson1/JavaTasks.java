package lesson1;

import kotlin.NotImplementedError;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
public class JavaTasks {
    /**
     * Сортировка времён
     *
     * Простая
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС,
     * каждый на отдельной строке. Пример:
     *
     * 13:15:19
     * 07:26:57
     * 10:00:03
     * 19:56:14
     * 13:15:19
     * 00:40:31
     *
     * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
     * сохраняя формат ЧЧ:ММ:СС. Одинаковые моменты времени выводить друг за другом. Пример:
     *
     * 00:40:31
     * 07:26:57
     * 10:00:03
     * 13:15:19
     * 13:15:19
     * 19:56:14
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public void sortTimes(String inputName, String outputName) {
        throw new NotImplementedError();
    }

    /**
     * Сортировка адресов
     *
     * Средняя
     *
     * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
     * где они прописаны. Пример:
     *
     * Петров Иван - Железнодорожная 3
     * Сидоров Петр - Садовая 5
     * Иванов Алексей - Железнодорожная 7
     * Сидорова Мария - Садовая 5
     * Иванов Михаил - Железнодорожная 7
     *
     * Людей в городе может быть до миллиона.
     *
     * Вывести записи в выходной файл outputName,
     * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
     * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
     *
     * Железнодорожная 3 - Петров Иван
     * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
     * Садовая 5 - Сидоров Петр, Сидорова Мария
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public void sortAddresses(String inputName, String outputName) {
        throw new NotImplementedError();
    }

    /**
     * Сортировка температур
     *
     * Средняя
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
     * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
     * Например:
     *
     * 24.7
     * -12.6
     * 121.3
     * -98.4
     * 99.5
     * -12.6
     * 11.0
     *
     * Количество строк в файле может достигать ста миллионов.
     * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
     * Повторяющиеся строки сохранить. Например:
     *
     * -98.4
     * -12.6
     * -12.6
     * 11.0
     * 24.7
     * 99.5
     * 121.3
     */

    // N - кол-во строк во входном файле.
    // Трудоёмкость: T = O(N * log(N)). За счёт того, что Collections.sort соритрует слияниями.
    // Ресурсоёмкость: R = O(N). За счёт того, что испольован список длиной N.
    static public void sortTemperatures(String inputName, String outputName) throws IOException {
        final List<Double> listOfTemps = new ArrayList<Double>();
        try (BufferedReader br = new BufferedReader(new FileReader(inputName))) {
            String newLine;
            while ((newLine = br.readLine()) != null) {
                final Double newTemp = Double.valueOf(newLine);
                if (newTemp >= -273 && newTemp <= 500) {
                    listOfTemps.add(newTemp);
                } else throw new IllegalArgumentException();
            }
        }
        Collections.sort(listOfTemps);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputName))) {
            for (Double temp : listOfTemps) {
                bw.write(String.valueOf(temp));
                bw.newLine();
            }
        }
    }

    /**
     * Сортировка последовательности
     *
     * Средняя
     * (Задача взята с сайта acmp.ru)
     *
     * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
     *
     * 1
     * 2
     * 3
     * 2
     * 3
     * 1
     * 2
     *
     * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
     * а если таких чисел несколько, то найти минимальное из них,
     * и после этого переместить все такие числа в конец заданной последовательности.
     * Порядок расположения остальных чисел должен остаться без изменения.
     *
     * 1
     * 3
     * 3
     * 1
     * 2
     * 2
     * 2
     */

    // N - кол-во строк во входном файле.
    // Трудоёмкость: T = O(N).
    // Ресурсоёмкость: R = O(N). В худшем случае, если во входном файле отсутствуют одинаковые числа, длина Map составит N
    static public void sortSequence(String inputName, String outputName) throws IOException {
        final HashMap<Integer, Integer> mapOfCounters = new HashMap<Integer, Integer>();
        Integer maxValue = -1;
        Integer keyOfMaxValue = -1;
        try (BufferedReader br = new BufferedReader(new FileReader(inputName))) {
            String newLine;
            while ((newLine = br.readLine()) != null) {
                final Integer newKey = Integer.valueOf(newLine);
                if (newKey > 0) {
                    Integer value = mapOfCounters.get(newKey);
                    if (value != null) {
                        mapOfCounters.put(newKey, ++value);
                    } else {
                        mapOfCounters.put(newKey, 1);
                        value = 1;
                    }
                    if (value > maxValue || keyOfMaxValue == -1) {
                        maxValue = value;
                        keyOfMaxValue = newKey;
                    } else if (value.equals(maxValue) && newKey < keyOfMaxValue) {
                        keyOfMaxValue = newKey;
                    }
                } else throw new IllegalArgumentException();
            }
        }
        try (BufferedReader br = new BufferedReader(new FileReader(inputName))) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputName))) {
                String newLine;
                while ((newLine = br.readLine()) != null) {
                    if (!Integer.valueOf(newLine).equals(keyOfMaxValue)) {
                        bw.write(newLine);
                        bw.newLine();
                    }
                }
                for (int i = 0; i < maxValue; i++) {
                    bw.write(String.valueOf(keyOfMaxValue));
                    bw.newLine();
                }
            }
        }
    }

    /**
     * Соединить два отсортированных массива в один
     *
     * Простая
     *
     * Задан отсортированный массив first и второй массив second,
     * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
     * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
     *
     * first = [4 9 15 20 28]
     * second = [null null null null null 1 3 9 13 18 23]
     *
     * Результат: second = [1 3 4 9 9 13 15 20 23 28]
     */
    static <T extends Comparable<T>> void mergeArrays(T[] first, T[] second) {
        throw new NotImplementedError();
    }
}
