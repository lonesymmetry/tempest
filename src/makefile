all: Main.class

Main.class: main/java/control/Main.java
	javac main/java/control/Main.java 

.PHONY: clean
clean:
	find -name \*.class |xargs rm -f;
	rm -rf out/;
