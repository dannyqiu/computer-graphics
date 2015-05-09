SOURCE_DIRECTORY=src
BUILD_DIRECTORY=bin
COMPILE_FLAGS=-Xlint:unchecked -Xdiags:verbose
CLEAN_EXTENSIONS=png|ppm|class

all: compile test

compile:
	mkdir -p $(BUILD_DIRECTORY)
	javac $(COMPILE_FLAGS) $(SOURCE_DIRECTORY)/*.java -d $(BUILD_DIRECTORY)

clean:
	find -E . -regex ".*\.($(CLEAN_EXTENSIONS))" -exec rm {} \;

test:
	java -cp $(BUILD_DIRECTORY) Main script
