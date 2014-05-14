#!/bin/bash

VERSIONSTRING=`grep -ir "string program_version=" src/* | cut -d"=" -f2 | sed -e"s/;//g" | sed -e's/"//g'`
echo "program.version="$VERSIONSTRING > version.properties

cat MemoFileProgrammer.spec.in | sed -e"s/@VERSION@/$VERSIONSTRING/" > MemoFileProgrammer.spec