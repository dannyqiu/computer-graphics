all: compile
	java Main script

compile:
	javac -Xlint:unchecked -Xdiags:verbose *.java

generate: compile
	java Main
	java Main awesome

clean:
	rm -f pic*
	rm -f *.png
	rm -f *.class
