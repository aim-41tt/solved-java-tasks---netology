package task_1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class NicknameGenerator {

    private static final AtomicInteger count3 = new AtomicInteger(0);
    private static final AtomicInteger count4 = new AtomicInteger(0);
    private static final AtomicInteger count5 = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];

        // Генерация текстов
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        // Список условий "красоты"
        List<Predicate<String>> conditions = new ArrayList<>();
        conditions.add(NicknameGenerator::isPalindrome);
        conditions.add(NicknameGenerator::isSameLetters);
        conditions.add(NicknameGenerator::isIncreasing);

        // Создание и запуск потоков
        List<Thread> threads = new ArrayList<>();
        for (Predicate<String> condition : conditions) {
            Thread thread = new Thread(() -> {
                for (String text : texts) {
                    if (condition.test(text)) {
                        updateCount(text);
                    }
                }
            });
            thread.start();
            threads.add(thread);
        }

        // Ожидание завершения всех потоков
        for (Thread thread : threads) {
            thread.join();
        }

        // Вывод результатов
        System.out.printf("Красивых слов с длиной 3: %d шт%n", count3.get());
        System.out.printf("Красивых слов с длиной 4: %d шт%n", count4.get());
        System.out.printf("Красивых слов с длиной 5: %d шт%n", count5.get());
    }

    // Метод генерации случайного текста
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    // Метод для увеличения соответствующего счетчика в зависимости от длины текста
    private static void updateCount(String text) {
        switch (text.length()) {
            case 3 -> count3.incrementAndGet();
            case 4 -> count4.incrementAndGet();
            case 5 -> count5.incrementAndGet();
        }
    }

    // Проверка на палиндром
    public static boolean isPalindrome(String text) {
        return new StringBuilder(text).reverse().toString().equals(text);
    }

    // Проверка на одинаковые буквы
    public static boolean isSameLetters(String text) {
        char firstChar = text.charAt(0);
        return text.chars().allMatch(c -> c == firstChar);
    }

    // Проверка на возрастающую последовательность букв
    public static boolean isIncreasing(String text) {
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) < text.charAt(i - 1)) {
                return false;
            }
        }
        return true;
    }
}
