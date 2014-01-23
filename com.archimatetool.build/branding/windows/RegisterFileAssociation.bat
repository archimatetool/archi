@REM Read the ReadMe.txt file.
@REM THis batch file adds Windows Registry entries to associate *.archimate file types to Archi.
@REM This batch file needs to be in the same directory as the Archi.exe file and you need to rename
@REM Archi32.exe or Archi64.exe to Archi.exe and Archi32.ini or Archi64.ini to Archi.ini.
@REM Note: *.archimate desktop icons will not show until you logout and log back in, or restart.
@REM You need to right-click on this batch file and "Run as administrator".

set APP_PATH=%~dp0Archi.exe

reg add "HKCR\.archimate" /t REG_SZ /d "Archi.file" /f
reg add "HKCR\Archi.file" /t REG_SZ /d "Archi Model" /f
reg add "HKCR\Archi.file\DefaultIcon" /t REG_SZ /d "%APP_PATH%,0" /f
reg add "HKCR\Archi.file\shell" /t REG_SZ /d "open" /f
reg add "HKCR\Archi.file\shell\open\command" /t REG_SZ /d "\"%APP_PATH%\" \"%%1\"" /f

pause
