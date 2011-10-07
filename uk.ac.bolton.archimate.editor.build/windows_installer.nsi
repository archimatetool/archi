# NSIS Installer

Name Archi

# General Symbol Definitions
!define VERSION 2.1.0
!define INSTALLER_VERSION 2.1.0.0
!define COMPANY "Bolton University"
!define URL http://archi.cetis.ac.uk
!define SRC_FOLDER "\Archi\Archi_win\*"
!define EXEFILE "\Archi\Archi-win32-${VERSION}.exe"

## Registry entries
!define REGKEY "SOFTWARE\$(^Name)"
!define FILE_EXT ".archimate"
!define FILE_EXT_REG_KEY "Archi.file"


# MUI Symbol Definitions
!define MUI_ICON branding\windows\icon.ico
!define MUI_FINISHPAGE_NOAUTOCLOSE
!define MUI_STARTMENUPAGE_REGISTRY_ROOT HKLM
!define MUI_STARTMENUPAGE_NODISABLE
!define MUI_STARTMENUPAGE_REGISTRY_KEY ${REGKEY}
!define MUI_STARTMENUPAGE_REGISTRY_VALUENAME StartMenuGroup
!define MUI_STARTMENUPAGE_DEFAULTFOLDER Archi
!define MUI_UNICON branding\windows\uninstall.ico
!define MUI_UNFINISHPAGE_NOAUTOCLOSE

# Included files
!include Sections.nsh
!include MUI2.nsh
!include FileFunc.nsh

# Reserved Files
ReserveFile "${NSISDIR}\Plugins\AdvSplash.dll"

# Variables
Var StartMenuGroup

# Installer pages
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_STARTMENU Application $StartMenuGroup
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES

# Installer languages
!insertmacro MUI_LANGUAGE English

# Installer attributes
OutFile "${EXEFILE}"
InstallDir $PROGRAMFILES\Archi
CRCCheck on
XPStyle on
ShowInstDetails hide
VIProductVersion "${INSTALLER_VERSION}"
VIAddVersionKey ProductName Archi
VIAddVersionKey ProductVersion "${VERSION}"
VIAddVersionKey CompanyName "${COMPANY}"
VIAddVersionKey CompanyWebsite "${URL}"
VIAddVersionKey FileVersion "${VERSION}"
VIAddVersionKey FileDescription "Archi Installer"
VIAddVersionKey LegalCopyright "(c) Bolton University 2010"
InstallDirRegKey HKLM "${REGKEY}" Path
ShowUninstDetails hide

# Full access is needed
RequestExecutionLevel highest


# Installer sections
Section -Main SEC0000
    SetOutPath $INSTDIR
    Call ClearTarget
    SetOverwrite on
    File /r "${SRC_FOLDER}"
    WriteRegStr HKLM "${REGKEY}\Components" Main 1
SectionEnd

Section -post SEC0001
    WriteRegStr HKLM "${REGKEY}" Path $INSTDIR
    SetOutPath $INSTDIR
    WriteUninstaller $INSTDIR\uninstall.exe
    CreateShortcut "$DESKTOP\$(^Name).lnk" $INSTDIR\$(^Name).exe
    !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
    SetOutPath $SMPROGRAMS\$StartMenuGroup
    CreateShortcut "$SMPROGRAMS\$StartMenuGroup\$(^Name).lnk" $INSTDIR\$(^Name).exe
    CreateShortcut "$SMPROGRAMS\$StartMenuGroup\docs.lnk" $INSTDIR\docs
    CreateShortcut "$SMPROGRAMS\$StartMenuGroup\examples.lnk" $INSTDIR\examples
    CreateShortcut "$SMPROGRAMS\$StartMenuGroup\Uninstall $(^Name).lnk" $INSTDIR\uninstall.exe
    !insertmacro MUI_STARTMENU_WRITE_END
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayName "$(^Name)"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayVersion "${VERSION}"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" Publisher "${COMPANY}"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" URLInfoAbout "${URL}"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayIcon $INSTDIR\uninstall.exe
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" UninstallString $INSTDIR\uninstall.exe
    WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" NoModify 1
    WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" NoRepair 1
    Call WriteFileAssociation
SectionEnd

# Macro for selecting uninstaller sections
!macro SELECT_UNSECTION SECTION_NAME UNSECTION_ID
    Push $R0
    ReadRegStr $R0 HKLM "${REGKEY}\Components" "${SECTION_NAME}"
    StrCmp $R0 1 0 next${UNSECTION_ID}
    !insertmacro SelectSection "${UNSECTION_ID}"
    GoTo done${UNSECTION_ID}
next${UNSECTION_ID}:
    !insertmacro UnselectSection "${UNSECTION_ID}"
done${UNSECTION_ID}:
    Pop $R0
!macroend

# Uninstaller sections
Section /o -un.Main UNSEC0000
    RmDir /r /REBOOTOK $INSTDIR
    DeleteRegValue HKLM "${REGKEY}\Components" Main
SectionEnd

Section -un.post UNSEC0001
    DeleteRegKey HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)"
    Delete /REBOOTOK $INSTDIR\uninstall.exe
    Delete /REBOOTOK "$DESKTOP\$(^Name).lnk"
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\$(^Name).lnk"
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\docs.lnk"
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\examples.lnk"
    Delete /REBOOTOK "$SMPROGRAMS\$StartMenuGroup\Uninstall $(^Name).lnk"
    DeleteRegValue HKLM "${REGKEY}" StartMenuGroup
    DeleteRegValue HKLM "${REGKEY}" Path
    DeleteRegKey /IfEmpty HKLM "${REGKEY}\Components"
    DeleteRegKey /IfEmpty HKLM "${REGKEY}"
    Call un.RemoveFileAssociation
    RmDir /REBOOTOK $SMPROGRAMS\$StartMenuGroup
    RmDir /REBOOTOK $INSTDIR
SectionEnd

# Installer functions
Function .onInit
    InitPluginsDir
    Push $R1
    File /oname=$PLUGINSDIR\spltmp.bmp ..\uk.ac.bolton.archimate.editor\splash.bmp
    advsplash::show 1000 600 400 -1 $PLUGINSDIR\spltmp
    Pop $R1
    Pop $R1
FunctionEnd

# Uninstaller functions
Function un.onInit
    ReadRegStr $INSTDIR HKLM "${REGKEY}" Path
    !insertmacro MUI_STARTMENU_GETFOLDER Application $StartMenuGroup
    !insertmacro SELECT_UNSECTION Main ${UNSEC0000}
FunctionEnd

# Clear the target folders
Function ClearTarget
    RmDir /r $INSTDIR\configuration
    RmDir /r $INSTDIR\docs
    RmDir /r $INSTDIR\examples
    RmDir /r $INSTDIR\jre
    RmDir /r $INSTDIR\plugins
FunctionEnd

Function WriteFileAssociation
    WriteRegStr HKCR "${FILE_EXT}" "" "${FILE_EXT_REG_KEY}"
    WriteRegStr HKCR "${FILE_EXT_REG_KEY}" "" "Archi Model"
    WriteRegStr HKCR "${FILE_EXT_REG_KEY}\shell" "" "open"
    WriteRegStr HKCR "${FILE_EXT_REG_KEY}\DefaultIcon" "" "$INSTDIR\$(^Name).exe,0"
    WriteRegStr HKCR "${FILE_EXT_REG_KEY}\shell\open\command" "" '"$INSTDIR\$(^Name).exe" "%1"'
    ${RefreshShellIcons}
FunctionEnd

Function un.RemoveFileAssociation
    DeleteRegKey HKCR "${FILE_EXT}"
    DeleteRegKey HKCR "${FILE_EXT_REG_KEY}"
    ${RefreshShellIcons}
FunctionEnd

