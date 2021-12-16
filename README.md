#CONNECTED APP

1. download maven

2. run command: <br>
   mvn -q compile exec:java -Dexec.args="persons_file contacts_file person_id"

example
```
mvn -q compile exec:java -Dexec.args="persons.json contacts.json 1" 
```
