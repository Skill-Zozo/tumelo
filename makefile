CLIENT_SOURCES = Device.java Room.java Location.java Profile.java Connector.java Main.java BCrypt.java

CLIENT_JARS = ":jars/*:/usr/local/apache-tomcat-8.0.32/lib/*"

SERVER_JARS = ":jars/*:/usr/local/apache-tomcat-8.0.32/lib/*"

compile_client:
	javac -cp $(CLIENT_JARS) $(CLIENT_SOURCES)

compile_server:
	javac -cp $(SERVER_JARS) Sever.java DBI.java

run_server:
	java Sever

run_client:
	java -cp $(CLIENT_JARS) Main

clean:
	rm -rf *.class