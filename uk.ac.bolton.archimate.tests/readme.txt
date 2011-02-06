These JUnit tests can be run in more than one way:

1.	In the Eclipse IDE using the integrated Eclipse/JUnit window.
	
	To do this, in the Package Explorer right-click on the "org.tencompetence.ldauthor.tests"
	project and select "Run As->Junit Plug-In Test".  This will run all tests.  To run individual tests
	right-click on a Test class and select "Run As->Junit Plug-In Test".
	

2.  As an Ant script with output created as html reports.
	
	To do this, launch the "run-tests.xml" Ant script by creating an "External Tools"
    Run Configuration (menu "Run->External tools->External Tools...") that has the
    junit.jar added to the classpath on the "Classpath" tab ("junit.jar" is found in the plugins
    folder of your Eclipse installation.)
    Please note that you may need to set your own paths in the script for your own setup.
    Please read the description in the Ant Script itself.
    
    
3.	From the Command line.

	To do this, run the "run-tests.bat" file from Windows.  This will launch the Ant script
	"run-tests.xml" and also generate a log.txt file.
	Please note that you may need to set your own paths in the batch file for your own setup.
	A Linux script could be created easily based on this one.


Phillip Beauvoir
