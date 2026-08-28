@echo off
setlocal

set "SCRIPT_DIR=%~dp0"
java -jar "%SCRIPT_DIR%java-tool-example.jar" %*

endlocal
