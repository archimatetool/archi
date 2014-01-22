@REM Unregister all Windows Registry file association entries for Archi 32-bit and 64-bit
@REM You need to right-click on this batch file and "Run as administrator".

reg delete "HKCR\.archimate" /f
reg delete "HKCR\Archi.file" /f

pause
