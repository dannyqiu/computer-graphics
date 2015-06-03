SOURCE_DIRECTORY=src
BUILD_DIRECTORY=bin
COMPILE_FLAGS=-Xlint:unchecked -source 1.7
CLEAN_EXTENSIONS=png|ppm|class

all: subs compile

subs:
	mkdir -p $(BUILD_DIRECTORY)
	$(MAKE) -C $(SOURCE_DIRECTORY)/parseTables
	$(MAKE) -C $(SOURCE_DIRECTORY)/parser
	mkdir -p $(BUILD_DIRECTORY)/parseTables && cp $(SOURCE_DIRECTORY)/parseTables/*.class $(BUILD_DIRECTORY)/parseTables
	mkdir -p $(BUILD_DIRECTORY)/parser && cp $(SOURCE_DIRECTORY)/parser/*.class $(BUILD_DIRECTORY)/parser

compile:
	mkdir -p $(BUILD_DIRECTORY)
	javac $(COMPILE_FLAGS) -cp $(BUILD_DIRECTORY) $(SOURCE_DIRECTORY)/*.java -d $(BUILD_DIRECTORY)

clean:
	$(MAKE) -C $(SOURCE_DIRECTORY)/parseTables clean
	$(MAKE) -C $(SOURCE_DIRECTORY)/parser clean
	find -E . -regex ".*\.($(CLEAN_EXTENSIONS))" -exec rm {} \;

test:
	java -cp $(BUILD_DIRECTORY) Main test.mdl
