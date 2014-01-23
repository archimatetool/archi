
@REM Please read the ReadMe.txt file before running this batch file.

@REM This batch file adds Windows Registry entries to associate *.archimate file types to Archi.exe.
@REM You need to right-click on this batch file and "Run as administrator".

set APP_PATH=%~dp0Archi.exe

reg add "HKCR\.archimate" /t REG_SZ /d "Archi.file" /f
reg add "HKCR\Archi.file" /t REG_SZ /d "Archi Model" /f
reg add "HKCR\Archi.file\DefaultIcon" /t REG_SZ /d "%APP_PATH%,0" /f
reg add "HKCR\Archi.file\shell" /t REG_SZ /d "open" /f
reg add "HKCR\Archi.file\shell\open\command" /t REG_SZ /d "\"%APP_PATH%\" \"%%1\"" /f

pause
