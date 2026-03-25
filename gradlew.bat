@rem
@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  Gradle startup script for Windows
@rem
@rem ##########################################################################
@setlocal
@set DIRNAME=%~dp0
@if "%DIRNAME%" == "" set DIRNAME=.
@set APP_BASE_NAME=%~n0
@set APP_HOME=%DIRNAME%
@java -Xmx2048m -jar "%APP_HOME%\gradle\wrapper\gradle-wrapper.jar" %*
@endlocal
