Firstly clone mygit repo (git@github.com:Skill-Zozo/tumelo.git) and checkout the serverside_clientside branch (git checkout serverside_clientside)

------------SETUP THE SERVER---------
1. Install eclipse (Java EE)
2. Download the apache jars here: http://tomcat.apache.org/download-80.cgi
3. Setup an apache server, follow the instructions here: http://www.eclipse.org/webtools/community/education/web/t320/Configuring_an_Application_Server_in_Eclipse.pdf
4. Make a new Dynamic Web Project, call it "testshit"
5. Under Java Resources, import the following files:
	Sever.Java
	DBI.Java
	BCrypt.java
-----------END OF SERVER SETUP---------

-----------CLIENT DEPENDECIES----------
1. Get the mysql-connector here: https://dev.mysql.com/downloads/connector/j/5.0.html
2. Move it to where you saved your apache jars
3. Also move the file 'java-json.jar' to your apache lib folder
_______________________________________

-----------RUNNING DOORS---------------
0. In the makefile you need to change the variable CLIENT_JARS to "/jars/*:YOUR/PATH/TO/APACHE/LIB/FOLDER"
1. Run the server on eclipse (Run Sever.java)
2. Run 'make compile_client'
3. run 'make run_client'

*************** Happy Days**************
Sorry for the delay, this thing had heavy bugs!