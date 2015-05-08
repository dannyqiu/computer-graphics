SOURCE_DIRECTORY=src
BUILD_DIRECTORY=bin
IGNORE_EXTENSIONS=png|ppm|class

all: compile
	java -cp $(BUILD_DIRECTORY) Main script

compile:
	mkdir -p $(BUILD_DIRECTORY)
	javac -Xlint:unchecked -Xdiags:verbose $(SOURCE_DIRECTORY)/*.java -d $(BUILD_DIRECTORY)

clean:
	find -E . -regex ".*\.($(IGNORE_EXTENSIONS))" -exec rm {} \;
