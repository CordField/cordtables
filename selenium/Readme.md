# CORDTables QA Automation 
This module covers the automation testing suite for the cord tables applications. It can be integrated with devops pipelines or can also be executed independently from developer's machine. 

### Features
CORDTables QA Automation covers currently automation testing of following modules: 
* User Registration
* User Login/Logout
* Main Page 
* Up Prayer Request schema 
* Admin People schema

### Environment 
* Java JDK 1.8 or above should be installed
* Maven 

### Environment Configurations 
 The automation test suite is built with the environment configurable through properties defined into application.properties. The configurations can also be provided at runtime through execution argument or environment variables. 
 Below is the list of the configurations and their definition. 
 | Property Name | Description | Example|
 | ----------- | ----------- |----------- |
 |site.url|Url of the environment where cordtable UI application is deployed|http://localhost:3333/|
|site.user.name|user name of the user to sign in |dev@test.com|
|site.user.password|password of the user to sign in|password|
|site.user.registration.name|initial of username to be used for testing registration process|testuser|
|site.user.registration.password|initial of username to be used for testing registration process|password|
|browser.config.driver.type|Browser type to be used for testing. Currently it is configured and setup only for the Google chrome browser|CHROME|
|browser.config.debug.enabled|Enable the additional browser logs for debugging. Default set to false. |false|
|test.execution.background.disabled|Used to run the automation test suites in visible mode in case we want to see the execution of those on browser. Default set to true to run it in background on headless chrome browser. |false|

### Execution instructions
* ##### Executing automation testsuite from source code
    * clone the sourcecode from the git repository: 
    ```git clone <<corttable repository URL>>```
