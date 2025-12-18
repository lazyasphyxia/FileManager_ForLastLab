// DirectoryManager.java
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Класс для выполнения операций управления директориями: создание, удаление.
 */
public class DirectoryManager {

    /**
     * Обрабатывает команду создания директории.
     *
     * @param command           полная строка команды, начинающаяся с 'mkdir'
     * @param currentDirectory текущая директория, в которой создается новая
     */
    public void handleMkdirCommand(String command, String currentDirectory) {
        String[] parts = command.split("\\s+", 2); // Разбиваем на "mkdir" и "имя_директории"
        if (parts.length < 2) {
            System.out.println("Неверный формат команды. Используйте: mkdir <имя_директории>");
            return;
        }

        String dirName = parts[1].trim(); // Обрезаем лишние пробелы
        if (dirName.isEmpty()) {
            System.out.println("Имя директории не может быть пустым.");
            return;
        }

        Path newDirPath = Paths.get(currentDirectory).resolve(dirName).normalize();

        try {
            Files.createDirectories(newDirPath); // createDirectories создаст все промежуточные директории, если нужно
            System.out.println("Директория создана: " + newDirPath.toAbsolutePath());
        } catch (FileAlreadyExistsException e) {
            System.err.println("Ошибка: директория уже существует: " + newDirPath);
        } catch (IOException e) {
            System.err.println("Ошибка при создании директории: " + e.getMessage());
        }
    }

    /**
     * Обрабатывает команду удаления файла или пустой директории.
     *
     * @param command           полная строка команды, начинающаяся с 'rm'
     * @param currentDirectory текущая директория, в которой происходит удаление
     */
    public void handleRmCommand(String command, String currentDirectory) {
        String[] parts = command.split("\\s+", 2); // Разбиваем на "rm" и "имя_файла_или_директории"
        if (parts.length < 2) {
            System.out.println("Неверный формат команды. Используйте: rm <имя_файла_или_директории>");
            return;
        }

        String targetName = parts[1].trim(); // Обрезаем лишние пробелы
        if (targetName.isEmpty()) {
            System.out.println("Имя цели для удаления не может быть пустым.");
            return;
        }

        Path targetPath = Paths.get(currentDirectory).resolve(targetName).normalize();

        try {
            // Проверяем, существует ли файл/директория
            if (!Files.exists(targetPath)) {
                System.out.println("Файл или директория не существует: " + targetPath);
                return;
            }

            // Проверяем, является ли директория пустой перед удалением
            if (Files.isDirectory(targetPath)) {
                if (Files.list(targetPath).findFirst().isPresent()) {
                    System.out.println("Ошибка: директория не пуста: " + targetPath);
                    System.out.println("Команда 'rm' может удалять только файлы и пустые директории.");
                    return;
                }
            }

            Files.delete(targetPath); // Удаляет файл или пустую директорию
            System.out.println("Удалено: " + targetPath.toAbsolutePath());

        } catch (NoSuchFileException e) {
            // Хотя мы проверили exists(), теоретически возможна ситуация между проверкой и удалением
            System.err.println("Файл или директория не найдена для удаления: " + e.getFile());
        } catch (IOException e) {
            System.err.println("Ошибка при удалении: " + e.getMessage());
        }
    }
}