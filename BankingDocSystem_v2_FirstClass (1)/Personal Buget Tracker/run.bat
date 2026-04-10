@echo off
REM Run script for Personal Budget Tracker

set JAVA_HOME=C:\Users\godav\.jdks\openjdk-25.0.2
set JAVAFX_PATH=C:\Users\godav\Downloads\openjfx-25.0.2_windows-x64_bin-sdk (1)\javafx-sdk-25.0.2\lib
set MYSQL_PATH=C:\Users\godav\Downloads\mysql-connector-j-9.6.0\mysql-connector-j-9.6.0

"%JAVA_HOME%\bin\java.exe" ^
--module-path "%JAVAFX_PATH%" ^
--add-modules javafx.controls,javafx.fxml ^
--add-opens javafx.graphics/javafx.scene=ALL-UNNAMED ^
-cp "%MYSQL_PATH%\mysql-connector-j-9.6.0.jar;C:\Users\godav\Downloads\BankingDocSystem_v2_FirstClass (1)\Personal Buget Tracker\out\production\Personal Buget Tracker" ^
com.budgettracker.Main

pause
