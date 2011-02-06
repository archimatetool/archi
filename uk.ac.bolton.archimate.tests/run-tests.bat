REM This Windows batch file will run Ant and the "run-tests.xml" script from the Command line
REM This is an alternative to running it directly from Eclipse
REM You will need to edit the Environment variables below to suit your system setup

set ECLIPSE_PLUGINS_HOME=D:\eclipse\plugins
set ANT_HOME=%ECLIPSE_PLUGINS_HOME%\org.apache.ant_1.7.0.v200706080842
set ANT_BIN=%ANT_HOME%\bin
set JUNIT_HOME=%ECLIPSE_PLUGINS_HOME%\org.junit4_4.3.1
set JAVA_HOME=C:\Program Files\Java\jdk1.6.0_04
set PATH=%PATH%;%ANT_BIN

%ANT_BIN%\ant -buildfile run-tests.xml -logfile log.txt -lib %JUNIT_HOME% -lib %ECLIPSE_PLUGINS_HOME%

