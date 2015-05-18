##AppDirect Integration Project
Automate AppDirect sign up tests.
##Technical Description:
For this project I have followed Page Object Model pattern. 
##Standard Directory Layout
#src/main/java 
Package Name: com.appdirect.qe.appdirectintegration.components
This package contains all the UI pages with web elements and methods to perform all the actions on the web pages.
#src/test/java
Package Name: com.appdirect.qe.appdirectintegration.tests
This package contains all the sign up tests. 
#src/util/java
Package  Name: com.appdirect.qe.appdirectintegration.utilities
This package contains all the utility classes that sign up test uses. 
#src/main/resources
This source folder contains Test cases and Test data files .
#src/test/resources
This source folder contains config.properties file which has Asserts and file paths which sign up tests uses.







Test Automation approach
Positive scenarios:
When test cases are run , first data provider sheet will be read and depending on the key  in the data provider sheet (gmail/yahoo) it will first generate an email on the fly and write the same in data provider sheet. Same email ids will be used in tests.

Negative scenarios:
Depending on the key (Existing/Invalid/empty)  in data provider sheet respective flow will be invoked and verifications will be done.

After every test cases run, test result will be written in test cases file.   


Why I chose this Test cases design :
The test cases are written in very much detail along with test data which reduces dependency on manual testing team. Test cases are grouped by priority and type which helps automation team to prioritize their automation tasks.  

Improvement Suggestions:
Help link on the sign up page is at the very bottom of the page. Some user may not notice the link. Placement of the link should be at some place where all user can easily spot the link.

Instructions for Running:
 Right click on “AppDirectTestSuite.xml” file and run as testNG suite. You can run the test cases on IE, chrome or  firefox browser by changing browser parameter value in AppDirectTestSuite.xml file

