; Archi Windows Build Script for Inno Setup
; Assumes that the source files are located at D:\Archi\Archi_win\
; And the outputted exe file will be generated to D:\Archi\

#define VERSION "2.7.0"
#define APPNAME "Archi"
#define APP_EXE "Archi.exe"
#define SOURCE_DIR "target\products\Archi\Archi_win"

; Registry entries
#define FILE_EXT ".archimate"
#define FILE_EXT_REG_KEY "Archi.file"


[Setup]
; NOTE: The value of AppId uniquely identifies this application.
; Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{D909FE1F-742F-4D89-A1A3-E08CD6DA50AF}
AppName={#APPNAME}
AppVersion={#VERSION}
AppVerName={#APPNAME} {#VERSION}
AppPublisher=Phillip Beauvoir
AppPublisherURL=http://www.archimatetool.com
AppSupportURL=http://www.archimatetool.com
AppUpdatesURL=http://www.archimatetool.com
DefaultDirName={pf}\{#APPNAME}
DefaultGroupName={#APPNAME}
OutputBaseFilename={#APPNAME}-win32-Setup-{#VERSION}
OutputDir={#SOURCE_DIR}\..
Compression=lzma
SolidCompression=yes
UninstallDisplayIcon={app}\{#APP_EXE},0
AllowNoIcons=yes
WizardImageFile=compiler:WizModernImage-IS.BMP
WizardSmallImageFile=compiler:WizModernSmallImage-IS.BMP

; Updates Shell icons
ChangesAssociations=yes

; Installer versions
VersionInfoProductVersion={#VERSION}
VersionInfoVersion=1.0.0.0
VersionInfoCopyright=Phillip Beauvoir

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}";

[Files]
Source: "{#SOURCE_DIR}\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs

[Icons]
Name: "{group}\{#APPNAME}"; Filename: "{app}\{#APP_EXE}"
Name: "{group}\Docs"; Filename: "{app}\docs"
Name: "{group}\Examples"; Filename: "{app}\examples"
Name: "{group}\{cm:UninstallProgram,Archi}"; Filename: "{uninstallexe}"
Name: "{commondesktop}\{#APPNAME}"; Filename: "{app}\{#APP_EXE}"; Tasks: desktopicon

[Run]
Filename: "{app}\Archi.exe"; Description: "{cm:LaunchProgram,Archi}"; Flags: nowait postinstall skipifsilent

[InstallDelete]
Type: filesandordirs; Name: "{app}\configuration";
Type: filesandordirs; Name: "{app}\docs";
Type: filesandordirs; Name: "{app}\examples";
Type: filesandordirs; Name: "{app}\jre";
Type: filesandordirs; Name: "{app}\plugins";

[Registry]
Root: HKCR; Subkey: ".archimate"; ValueType: string; ValueData: "{#FILE_EXT_REG_KEY}"; Flags: uninsdeletekey
Root: HKCR; Subkey: "{#FILE_EXT_REG_KEY}"; ValueType: string; ValueData: "Archi Model"; Flags: uninsdeletekey
Root: HKCR; Subkey: "{#FILE_EXT_REG_KEY}\shell"; ValueType: string; ValueData: "open";
Root: HKCR; Subkey: "{#FILE_EXT_REG_KEY}\shell\open\command"; ValueType: string; ValueData: """{app}\{#APP_EXE}"" ""%1""";
Root: HKCR; Subkey: "{#FILE_EXT_REG_KEY}\DefaultIcon"; ValueType: string; ValueData: "{app}\{#APP_EXE},0";
