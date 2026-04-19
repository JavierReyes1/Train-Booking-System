@echo off
echo Compiling Train Booking System...

if not exist out mkdir out

javac -cp "lib\sqlite-jdbc-3.45.1.0.jar" -d out -sourcepath src src\com\trainbooking\Main.java src\com\trainbooking\model\*.java src\com\trainbooking\db\*.java src\com\trainbooking\service\*.java src\com\trainbooking\gui\*.java

if %ERRORLEVEL% == 0 (
    echo.
    echo Compilation successful!
    echo Run the app with:  run.bat
) else (
    echo.
    echo Compilation failed. Check the errors above.
)
