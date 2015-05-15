JCC=javac
JFLAGS=-classpath ".."

all:
	javacc parser.jj
	$(JCC) $(JFLAGS) MdlParser.java

clean:
	rm -f *java
	rm -f *class
