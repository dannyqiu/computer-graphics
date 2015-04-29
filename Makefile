all: compile
	java Main script

compile:
	javac -Xlint:unchecked -Xdiags:verbose *.java

clean:
	rm -f pic*
	rm -f *.png
	rm -f *.ppm
	rm -f *.class
