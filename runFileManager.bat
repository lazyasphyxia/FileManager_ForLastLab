@echo off
REM BAT-файл для компиляции и запуска файлового менеджера из любого места

REM Установите путь к папке, где находятся ваши .java файлы
REM !! ВАЖНО: Замените C:\Users\shain\IdeaProjects\LastLab\src на реальный путь к вашей папке !!
set PROJECT_DIR=C:\Users\shain\IdeaProjects\LastLab\src

REM Проверяем, существует ли папка проекта
if not exist "%PROJECT_DIR%" (
    echo Error: Project directory does not exist: %PROJECT_DIR%
    echo Please update the PROJECT_DIR variable in this script.
    echo Press any key to exit...
    pause >nul
    exit /b 1
)

REM Переходим в папку проекта
cd /d "%PROJECT_DIR%"

REM Включаем отображение сообщений об ошибках
setlocal enableextensions enabledelayedexpansion

echo Compiling Java files in %PROJECT_DIR%...
REM Компилируем все .java файлы в указанной директории
javac *.java
if %errorlevel% neq 0 (
    echo Compilation failed. Press any key to exit...
    pause >nul
    exit /b %errorlevel%
)

echo Compilation successful.

echo Running FileManager...
REM Запускаем основной класс программы (FileManager) из указанной директории
java FileManager
if %errorlevel% neq 0 (
    echo Execution failed. Press any key to exit...
    pause >nul
)

echo Program finished. Press any key to exit...
pause >nul