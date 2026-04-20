#!/bin/bash

flex ejemplo.l
bison -d ejemplo.y
g++ -Wno-write-strings -o ejemplo ejemplo.tab.c lex.yy.c
