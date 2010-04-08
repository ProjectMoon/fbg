; This file is part of the Factbook Generator.
;
; The Factbook Generator is free software: you can redistribute it and/or modify
; it under the terms of the GNU General Public License as published by
; the Free Software Foundation, either version 3 of the License, or
; (at your option) any later version.
; 
; The Factbook Generator is distributed in the hope that it will be useful,
; but WITHOUT ANY WARRANTY; without even the implied warranty of
; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
; GNU General Public License for more details.
; 
; You should have received a copy of the GNU General Public License
; along with The Factbook Generator.  If not, see <http://www.gnu.org/licenses/>.
;
; Copyright 2008, 2009 Bradley Brown, Dustin Yourstone, Jeffrey Hair, Paul Halvorsen, Tu Hoang

;--------------------------------
;Factbook Generator NSIS Script for Windows
;Linux/Unix users should use the provided install.sh.

;This should be compiled while this file is inside the SVN directory.
;--------------------------------

;Pertinent Data
;Version, etc
; The name of the installer
Name "Factbook Generator"

; The file to write
OutFile "..\InstallFBG.exe"

; The default installation directory
InstallDir "C:\Program Files\FactbookGenerator"

; Request application privileges for Windows Vista
RequestExecutionLevel user

;--------------------------------

; License Stuff
LicenseText "This installer will install the Factbook Generator. You must accept the GPLv3 License Terms to continue."
LicenseData "..\..\license.txt"

; Pages

Page license
Page components
Page directory
Page instfiles

;--------------------------------

; The stuff to install
Section "Factbook Generator Core (Required)"
	; Set output path to the installation directory.
	SetOutPath $INSTDIR

	;Install everything, including the source code.
	;This will need to be fixed later for only stable files.
	File ..\..\FactbookGenerator.jar
	File ..\FBG.exe
	File ..\..\license.txt

	;Userinfo test

	;Create start menu shortcuts
	CreateDirectory "$SMPROGRAMS\LightSoft Solutions"
	CreateShortCut "$SMPROGRAMS\LightSoft Solutions\Factbook Generator.lnk" "$INSTDIR\FBG.exe"
SectionEnd ; end the section

Section "Libraries (Required)"
	SetOutPath $INSTDIR\lib
	File ..\..\lib\beansbinding-1.2.1.jar
	File ..\..\lib\jcommon-1.0.16.jar
	File ..\..\lib\jfreechart-1.0.13.jar
	File ..\..\lib\jtds-1.2.2.jar
	File ..\..\lib\swing-layout-1.0.jar
	File ..\..\lib\js.jar
	File ..\..\lib\js-14.jar
SectionEnd
