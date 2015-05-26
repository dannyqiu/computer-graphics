#!/bin/bash

if [ "$#" -ne 1 ]; then
    echo "Usage: $0 mdl-file"
    exit 1
fi
java -cp bin Main $1
