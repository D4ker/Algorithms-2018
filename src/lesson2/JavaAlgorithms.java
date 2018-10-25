package lesson2;

import kotlin.NotImplementedError;
import kotlin.Pair;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("unused")
public class JavaAlgorithms {
    /**
     * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
     * Простая
     *
     * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
     * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
     *
     * 201
     * 196
     * 190
     * 198
     * 187
     * 194
     * 193
     * 185
     *
     * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
     * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть раньше первого.
     * Вернуть пару из двух моментов.
     * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
     * Например, для приведённого выше файла результат должен быть Pair(3, 4)
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public Pair<Integer, Integer> optimizeBuyAndSell(String inputName) {
        throw new NotImplementedError();
    }

    /**
     * Задача Иосифа Флафия.
     * Простая
     *
     * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
     *
     * 1 2 3
     * 8   4
     * 7 6 5
     *
     * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
     * Человек, на котором остановился счёт, выбывает.
     *
     * 1 2 3
     * 8   4
     * 7 6 х
     *
     * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
     * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
     *
     * 1 х 3
     * 8   4
     * 7 6 Х
     *
     * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
     *
     * 1 Х 3
     * х   4
     * 7 6 Х
     *
     * 1 Х 3
     * Х   4
     * х 6 Х
     *
     * х Х 3
     * Х   4
     * Х 6 Х
     *
     * Х Х 3
     * Х   х
     * Х 6 Х
     *
     * Х Х 3
     * Х   Х
     * Х х Х
     */
    static public int josephTask(int menNumber, int choiceInterval) {
        throw new NotImplementedError();
    }

    /**
     * Наибольшая общая подстрока.
     * Средняя
     *
     * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
     * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
     * Если общих подстрок нет, вернуть пустую строку.
     * При сравнении подстрок, регистр символов *имеет* значение.
     * Если имеется несколько самых длинных общих подстрок одной длины,
     * вернуть ту из них, которая встречается раньше в строке first.
     */

    // N - длина первого слова, M - длина второго.
    // Трудоёмкость: T = O(N * M). Связано с тем, что в коде испольован цикл в цикле.
    // Ресурсоёмкость: R = O(M). За счёт того, что при решении был испольован одномерный массив, вместо двумерного.
    static public String longestCommonSubstring(String first, String second) {
        final int[] arrayOfCounters = new int[second.length()];
        int maxCounter = 0;
        int indexOfMaxCounter = -1;
        for (int i = 0; i < first.length(); i++) {
            final char letter = first.charAt(i);
            for (int j = second.length() - 1; j >= 0; j--) {
                if (letter == second.charAt(j)) {
                    if (j != 0) {
                        // Берём левый верхний счётчик, увеличиваем значение на 1 и записываем результат в текущий
                        final int newLeftUpCounter = arrayOfCounters[j - 1] + 1;
                        arrayOfCounters[j] = newLeftUpCounter;
                        if (newLeftUpCounter > maxCounter) {
                            maxCounter = newLeftUpCounter;
                            indexOfMaxCounter = j;
                        }
                    } else {
                        arrayOfCounters[0] = 1;
                        if (1 > maxCounter) {
                            maxCounter = 1;
                            indexOfMaxCounter = 0;
                        }
                    }
                } else {
                    arrayOfCounters[j] = 0;
                }
            }
        }
        if (indexOfMaxCounter == -1) {
            return "";
        }
        final int endIndex = indexOfMaxCounter + 1;
        return second.substring(endIndex - maxCounter, endIndex);
    }

    /**
     * Число простых чисел в интервале
     * Простая
     *
     * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
     * Если limit <= 1, вернуть результат 0.
     *
     * Справка: простым считается число, которое делится нацело только на 1 и на себя.
     * Единица простым числом не считается.
     */
    static public int calcPrimesNumber(int limit) {
        throw new NotImplementedError();
    }

    /**
     * Балда
     * Сложная
     *
     * В файле с именем inputName задана матрица из букв в следующем формате
     * (отдельные буквы в ряду разделены пробелами):
     *
     * И Т Ы Н
     * К Р А Н
     * А К В А
     *
     * В аргументе words содержится множество слов для поиска, например,
     * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
     *
     * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
     * и вернуть множество найденных слов. В данном случае:
     * ТРАВА, КРАН, АКВА, НАРТЫ
     *
     * И т Ы Н     И т ы Н
     * К р а Н     К р а н
     * А К в а     А К В А
     *
     * Все слова и буквы -- русские или английские, прописные.
     * В файле буквы разделены пробелами, строки -- переносами строк.
     * Остальные символы ни в файле, ни в словах не допускаются.
     */

    // N - кол-во строк в файле, M - длина слов в файле, W - кол-во искомых слов, L - кол-во букв в искомом слове
    // Трудоёмкость: T = O(W * N * M * L).
    // Ресурсоёмкость: R = O(N * M).
    static public Set<String> baldaSearcher(String inputName, Set<String> words) throws IOException {
        final List<String> listOfLetters = new ArrayList<String>();
        try (BufferedReader br = new BufferedReader(new FileReader(inputName))) {
            String newLine;
            while ((newLine = br.readLine()) != null) {
                listOfLetters.add(newLine);
            }
        }
        final int height = listOfLetters.size();
        final int width = listOfLetters.get(0).length() / 2 + 1;
        final char[][] arrayOfLetters = new char[height][width];
        for (int i = 0; i < height; i++) {
            final String[] stringLetters = listOfLetters.get(i).split(" ");
            for (int j = 0; j < width; j++) {
                arrayOfLetters[i][j] = stringLetters[j].charAt(0);
            }
        }
        final Set<String> foundWords = new HashSet<String>();
        final boolean[][] checksArray = new boolean[height][width];
        for (String word: words) {
            updateChecksArray(checksArray);
            final char firstLetter = word.charAt(0);
            boolean nextWord = false;
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (arrayOfLetters[i][j] == firstLetter && wordIsFound(i, j, word, 1, arrayOfLetters, checksArray)) {
                        foundWords.add(word);
                        nextWord = true;
                        break;
                    }
                }
                if (nextWord) {
                    break;
                }
            }
        }
        return foundWords;
    }

    // Метод для обновления массива, хранящего путь, проходимый рекурсивной функцией wordIsFound
    // Трудоёмкость: T = O(N * M).
    private static void updateChecksArray(boolean[][] checksArray) {
        final int width = checksArray[0].length;
        for (int i = 0; i < checksArray.length; i++) {
            for (int j = 0; j < width; j++) {
                checksArray[i][j] = false;
            }
        }
    }

    /* Метод для поиска слова в массиве.
    i, j - индексы новой проверяемой буквы в массиве;
    word - искомое слово;
    wordLetterIndex - индекс текущей проверяемой буквы слова word;
    searchArray - массив для поиска;
    checksArray - массив, хранящий путь, пройденный при поиске слова (необходим для того, чтобы уже проверенную
    букву нелья было использовать второй раз). */
    // Трудоёмкость: T = O(L).
    private static boolean wordIsFound(int i, int j, String word, int wordLetterIndex, char[][] searchArray, boolean[][] checksArray) {
        checksArray[i][j] = true;
        if (wordLetterIndex == word.length()) {
            return true;
        }
        final char checkedLetter = word.charAt(wordLetterIndex);
        boolean found = false;
        final int left = j - 1;
        final int up = i - 1;
        final int right = j + 1;
        final int down = i + 1;
        final int nextIndex = wordLetterIndex + 1;
        if (found == false && left >= 0 && checksArray[i][left] == false && searchArray[i][left] == checkedLetter) {
            found = wordIsFound(i, left, word, nextIndex, searchArray, checksArray);
        }
        if (found == false && up >= 0 && checksArray[up][j] == false && searchArray[up][j] == checkedLetter) {
            found = wordIsFound(up, j, word, nextIndex, searchArray, checksArray);
        }
        if (found == false && right < searchArray[0].length && checksArray[i][right] == false && searchArray[i][right] == checkedLetter) {
            found = wordIsFound(i, right, word, nextIndex, searchArray, checksArray);
        }
        if (found == false && down < searchArray.length && checksArray[down][j] == false && searchArray[down][j] == checkedLetter) {
            found = wordIsFound(down, j, word, nextIndex, searchArray, checksArray);
        }
        return found;
    }
}
