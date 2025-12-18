import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Класс для отображения содержимого директории.
 * <p>
 * Предоставляет методы для вывода списка файлов и папок с указанием их типа (расширения) и размера.
 * </p>
 */
public class DirectoryDisplay {

    /**
     * Конструктор класса DirectoryDisplay.
     * Создаёт новый экземпляр для отображения содержимого директорий.
     */
    public DirectoryDisplay() {
        // Конструктор по умолчанию
    }

    /**
     * Отображает содержимое текущей директории с типом (расширением) и размерами файлов.
     *
     * @param directoryPath путь к директории, содержимое которой нужно отобразить
     */
    public void displayDirectoryContents(String directoryPath) {
        Path path = Paths.get(directoryPath).normalize();

        if (!Files.exists(path) || !Files.isDirectory(path)) {
            System.out.println("Директория не существует или недоступна: " + directoryPath);
            return;
        }

        System.out.println("\n=== Содержимое директории: " + path.toAbsolutePath() + " ===");
        // Изменили заголовок колонки с "Тип" на "Расширение"
        System.out.printf("%-30s %-15s %-20s%n", "Имя файла/папки", "Расширение/тип", "Размер");
        System.out.println("----------------------------------------------------------------");

        try {
            Files.list(path).forEach(file -> {
                try {
                    String name = file.getFileName().toString();
                    String typeOrExtension;
                    if (Files.isDirectory(file)) {
                        typeOrExtension = "Папка"; // Для директорий оставляем "Папка"
                    } else {
                        // Получаем расширение файла
                        typeOrExtension = getFileExtension(name);
                    }
                    String size = FileSizeFormatter.formatFileSize(Files.size(file));
                    System.out.printf("%-30s %-15s %-20s%n", name, typeOrExtension, size);
                } catch (IOException e) {
                    System.err.println("Ошибка при получении информации о файле " + file.getFileName() + ": " + e.getMessage());
                }
            });
        } catch (IOException e) {
            System.err.println("Ошибка при чтении директории: " + e.getMessage());
        }
    }

    /**
     * Извлекает расширение файла из его имени.
     * Если файл не имеет расширения или является скрытым без точки (например, ".bashrc"),
     * возвращает пустую строку.
     *
     * @param fileName имя файла (например, "document.pdf", "image.jpg", "README")
     * @return строка с расширением (например, ".pdf", ".jpg") или пустая строка
     */
    private String getFileExtension(String fileName) {
        // Находим последнюю точку в имени файла
        int lastDotIndex = fileName.lastIndexOf('.');

        // Если точка есть и не является первым символом (например, .hiddenfile)
        if (lastDotIndex > 0) {
            // Возвращаем подстроку начиная с точки
            return fileName.substring(lastDotIndex);
        }

        // Если точка не найдена или находится в начале, возвращаем пустую строку
        return "";
    }
}