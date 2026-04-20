#ifndef _COMUN_H_
#define _COMUN_H_

#include <string>
using namespace std;

typedef struct{
    char *lexema;
    int nlin, ncol;
    int tipo;
    string cod;
} Atributos;

#define YYSTYPE Atributos

#define ERRLEXICO 1
#define ERRSINT 2
#define ERREOF 3
#define ERRLEXEOF 4

void msgError(int nerror, int nlin, int ncol, const char *s);

#endif