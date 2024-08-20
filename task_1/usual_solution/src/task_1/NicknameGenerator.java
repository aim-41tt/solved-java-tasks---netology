package task_1;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class NicknameGenerator {

    // Статические счетчики для "красивых" слов длиной 3, 4 и 5
    private static final AtomicInteger count3 = new AtomicInteger(0);
    private static final AtomicInteger count4 = new AtomicInteger(0);
    private static final AtomicInteger count5 = new AtomicInteger(0);

    // Метод генерации случайного текста
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    // Метод проверки на палиндром
    public static boolean isPalindrome(String text) {
        int n = text.length();
        for (int i = 0; i < n / 2; i++) {
            if (text.charAt(i) != text.charAt(n - i - 1)) {
                return false;
            }
        }
        return true;
    }

    // Метод проверки на одинаковые буквы
    public static boolean isSameLetters(String text) {
        char firstChar = text.charAt(0);
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) != firstChar) {
                return false;
            }
        }
        return true;
    }

    // Метод проверки на возрастающую последовательность букв
    public static boolean isIncreasing(String text) {
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) < text.charAt(i - 1)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        String[] texts = new String[100_000];
        
        // Генерация текстов
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        // Поток проверки на палиндром
        Thread palindromeThread = new Thread(() -> {
            for (String text : texts) {
                if (isPalindrome(text)) {
                    updateCount(text);
                }
            }
        });

        // Поток проверки на одинаковые буквы
        Thread sameLettersThread = new Thread(() -> {
            for (String text : texts) {
                if (isSameLetters(text)) {
                    updateCount(text);
                }
            }
        });

        // Поток проверки на возрастающую последовательность букв
        Thread increasingThread = new Thread(() -> {
            for (String text : texts) {
                if (isIncreasing(text)) {
                    updateCount(text);
                }
            }
        });

        // Запуск потоков
        palindromeThread.start();
        sameLettersThread.start();
        increasingThread.start();

        // Ожидание завершения всех потоков
        palindromeThread.join();
        sameLettersThread.join();
        increasingThread.join();

        // Вывод результатов
        System.out.println("Красивых слов с длиной 3: " + count3.get() + " шт");
        System.out.println("Красивых слов с длиной 4: " + count4.get() + " шт");
        System.out.println("Красивых слов с длиной 5: " + count5.get() + " шт");
    }

    // Метод для увеличения соответствующего счетчика в зависимости от длины текста
    private static void updateCount(String text) {
        int length = text.length();
        if (length == 3) {
            count3.incrementAndGet();
        } else if (length == 4) {
            count4.incrementAndGet();
        } else if (length == 5) {
            count5.incrementAndGet();
        }
    }
}
