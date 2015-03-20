all:
	javac -Xlint:unchecked -Xdiags:verbose *.java
	java Main

clean:
	rm -f pic*
