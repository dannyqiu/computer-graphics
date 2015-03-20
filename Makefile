all:
	javac -Xlint:unchecked -Xdiags:verbose *.java
	java Main

clean:
	rm -f pic*
	rm -f *.png
	rm -f *.class
