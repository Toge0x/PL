%{
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <string>
#include <iostream>
using namespace std;
#include "comun.h"
#include "TablaSimbolos.h"

extern int ncol, nlin, findefichero;
string prefijo = "";       // prefijo del ámbito actual
bool provieneDV = false;   // true si DV viene de A, false si viene de I

/* las necesarias para flex */
extern int yylex();
extern char *yytext;
extern int yyleng;
extern FILE *yyin;

int yyerror(char *s);

TablaSimbolos *tsActual = new TablaSimbolos(NULL);

const int ERRYADECL=1, ERRNODECL=2, ERRTIPOS=3, ERRNOSIMPLE=4, ERRNOENTERO=5;
Atributos opera(string trad1, int tipo1, string trad2, int tipo2, string operador);
void errorSemantico(int nerror, char *lexema, int fila, int columna);
%}

/* simbolos terminales */
%token CLASS FUN INT FLOAT IF ELSE FI PRINT
%token ID NUMENTERO NUMREAL
%token OPAS OPMUL OPREL ASIG
%token LBRA RBRA PARI PARD PYC DOSP

%%

/* reglas gramaticales */

X : S{
   int tk = yylex();
   if (tk != 0) yyerror("");
};


S : CLASS ID { 
      $$.cod = string($2.lexema);      // guardamos el lexema en el marcador

      if(prefijo.empty() == true){
         prefijo = string($2.lexema);                    // si no hay prefijo previo
      }else{
         prefijo = prefijo + "_" + string($2.lexema);    // si venimos de clase anidada
      }

      tsActual = new TablaSimbolos(tsActual);    // abrimos nuevo ámbito
   } LBRA M RBRA
   {
      $$.cod = "// class " + prefijo + "\n" + $5.cod;
      prefijo = $3.cod;                      // restauramos el prefijo anterior, es como usar el atributo heredado
      tsActual = tsActual->getParent();
   };


M : M SF{
   $$.cod = $1.cod + $2.cod;
}
   | /* ϵ */
{
   $$.cod = "";
};


SF : S {$$.cod = $1.cod;}
   | Fun {$$.cod = $1.cod;};


Fun : FUN ID {
        $$.cod = prefijo;              // guardamos el prefijo anterior en el marcador
        prefijo = prefijo + "_" + string($2.lexema);
        tsActual = new TablaSimbolos(tsActual);    // abrimos ámbito ya que parámetros se declaran dentro
    } A LBRA M Cod RBRA
    {
        $$.cod = "void " + prefijo + "(" + $4.cod + ") {\n" + $6.cod + $7.cod + "} // " + prefijo + "\n\n";
        prefijo = $3.cod;                          // restauramos el prefijo anterior
        tsActual = tsActual->getParent();
    }
;


A : A PYC {provieneDV = true;} DV{  // necesario saber que va a llamar de DV desde aquí para la traduccion
   $$.cod = $1.cod + "," + $4.cod;
}
  | {provieneDV = true;} DV   // igual
{
   $$.cod = $2.cod;
};


DV : Tipo ID{

   Simbolo s;
   s.nombre = $2.lexema;      // asignamos lexema y tipo
   s.tipo = $1.tipo;

   if(provieneDV == true){    // si viene de DV se forma como argumento
      s.nomtrad = "arg_" + prefijo + "_" + string($2.lexema);
   }else{
      s.nomtrad = prefijo + "_" + string($2.lexema);  // si viene de I se forma como variable normal
   }

   if(tsActual->set(s) == false){      // si ya está declarado en la tabla de símbolos
      errorSemantico(ERRYADECL, $2.lexema, $2.nlin, $2.ncol);
   }
   $$.cod = (s.tipo == ENTERO ? "int" : "float") + string(" ") + s.nomtrad;
   $$.tipo = $1.tipo;
};


Tipo : INT{
   $$.tipo = ENTERO;
   $$.cod = "int";
}
   | FLOAT
{
   $$.tipo = REAL;
   $$.cod = "float";
};


Cod : Cod PYC I{
   $$.cod = $1.cod + $3.cod;
}
   | I
{
   $$.cod = $1.cod;
};


I : {provieneDV = false;} DV{     // necesario sabes que proviene de aqui
   $$.cod = $2.cod + ";\n";
}
   | LBRA
{
   $$.cod = prefijo;
   prefijo = prefijo + "_";
   tsActual = new TablaSimbolos(tsActual);   // abrimos ámbito nuevo
} Cod RBRA
{
   $$.cod = "{\n" + $3.cod + "}\n";    // construimos la traducción
   prefijo = $2.cod;
   tsActual = tsActual->getParent();   // y cerramos ámbito
}
   | ID ASIG Expr
{
   Simbolo *s = tsActual->get($1.lexema);

   // vamos a comprobar que no es nulo, es decir, existe el simbolo
   if(s == NULL){
      errorSemantico(ERRNODECL, $1.lexema, $1.nlin, $1.ncol);
   }

   // comprobamos que es de tipo entero o real (ha juntado class y fun en tablasimbolos.h)
   if(s->tipo == CLASSFUN){
      errorSemantico(ERRNOSIMPLE, $1.lexema, $1.nlin, $1.ncol);
   }

   // y comprobamos que ambos sean de tipos compatibles, es lo mismo que en la p3
   if(s->tipo == ENTERO && $3.tipo == REAL){
      errorSemantico(ERRTIPOS, $2.lexema, $2.nlin, $2.ncol);
   }

   string tradExpr = (s->tipo == REAL && $3.tipo == ENTERO) ? "itor(" + $3.cod + ")" : $3.cod;
   $$.cod = "  " + s->nomtrad + " = " + tradExpr + ";\n";
}
   | IF Expr DOSP I Ip
{
   // comprobamos que la expresion del if sea entera, igual que en la p3
   if($2.tipo != ENTERO){
      errorSemantico(ERRNOENTERO, $1.lexema, $1.nlin, $1.ncol);
   }
   $$.cod = "if (" + $2.cod + ")\n" + $4.cod + $5.cod;
}
| PRINT Expr
{
   string formato = ($2.tipo == ENTERO) ? "%d" : "%f";      // obtenemos el formato de impresion de la traduccion
   $$.cod = "  printf(\"" + formato + "\"," + $2.cod + ");\n";
};


Ip : ELSE I FI{
   $$.cod = "else\n" + $2.cod;
}
   | FI
{
   $$.cod = "";
};


Expr : E OPREL E{
   Atributos res = opera($1.cod, $1.tipo, $3.cod, $3.tipo, string($2.lexema));   // obtenemos trad y tipo de la expresion oprel
   $$.cod = res.cod;
   $$.tipo = ENTERO;  // el resultado de un relacional siempre es entero
}
   | E
{
   $$.cod = $1.cod;
   $$.tipo = $1.tipo;
};


E : E OPAS T{
   Atributos res = opera($1.cod, $1.tipo, $3.cod, $3.tipo, string($2.lexema));      // obtenemos trad y tipo de la expresion opas
   $$.cod = res.cod;
   $$.tipo = res.tipo;
}
   | T
{
   $$.cod = $1.cod;
   $$.tipo = $1.tipo;
};


T : T OPMUL F{
   Atributos res = opera($1.cod, $1.tipo, $3.cod, $3.tipo, string($2.lexema));      // obtenemos trad y tipo de la expresion opmul
   $$.cod = res.cod;
   $$.tipo = res.tipo;
}
   | F
{
   $$.cod = $1.cod;
   $$.tipo = $1.tipo;
};


F : NUMENTERO{
   $$.tipo = ENTERO;
   $$.cod = string($1.lexema);
}
   | NUMREAL
{
   $$.tipo = REAL;
   $$.cod = string($1.lexema);
}
   | ID
{
   Simbolo *s = tsActual->get($1.lexema);

   // comprobamos que el id exista, es decir, esté declarado, igual que p3
   if(s == NULL){
      errorSemantico(ERRNODECL, $1.lexema, $1.nlin, $1.ncol);
   }

   // comprobamos que no sea classfun, es decir, que sea entero o real
   if(s->tipo == CLASSFUN){
      errorSemantico(ERRNOSIMPLE, $1.lexema, $1.nlin, $1.ncol);
   }

   $$.tipo = s->tipo;
   $$.cod = s->nomtrad;
}
   | PARI Expr PARD
{
   $$.tipo = $2.tipo;
   $$.cod = "(" + $2.cod + ")";
};

%% 

/* literalmente la misma funcion que en la p3 */
Atributos opera(string trad1, int tipo1, string trad2, int tipo2, string operador)
{
   string traduccion1, traduccion2, sufijoOperador, traduccionResultado;
   Atributos resultado;

   if(tipo1 == REAL && tipo2 == REAL){         // ambos reales
      sufijoOperador = "r";
      traduccion1 = trad1;
      traduccion2 = trad2;
   }else if(tipo1 == REAL && tipo2 == ENTERO){ // uno real, uno entero
      sufijoOperador = "r";
      traduccion1 = trad1;
      traduccion2 = "itor(" + trad2 + ")";
   }else if(tipo1 == ENTERO && tipo2 == REAL){ // uno entero, uno real
      sufijoOperador = "r";
      traduccion1 = "itor(" + trad1 + ")";
      traduccion2 = trad2;
   }else{                                      // ambos enteros
      sufijoOperador = "i";
      traduccion1 = trad1;
      traduccion2 = trad2;
   }

   // en el caso de un entero y un real sería
   // itor(arg_Main_main_a) +r arg_Main_main_b
   // y el tipo de Atributos sería REAL
   traduccionResultado = traduccion1 + " " + operador + sufijoOperador + " " + traduccion2;
   int tipo = (tipo1 == ENTERO && tipo2 == ENTERO) ? ENTERO : REAL;

   resultado.cod = traduccionResultado;
   resultado.tipo = tipo;
   return resultado;
}

void errorSemantico(int nerror, char *lexema, int fila, int columna)
{
    fprintf(stderr,"Error semantico (%d,%d): en '%s', ",fila,columna,lexema);
    switch (nerror) {
      case ERRYADECL: fprintf(stderr,"ya existe en este ambito\n"); break;
      case ERRNODECL: fprintf(stderr,"no ha sido declarado\n"); break;
      case ERRTIPOS: fprintf(stderr,"tipos incompatibles entero/real\n"); break;
      case ERRNOSIMPLE: fprintf(stderr,"debe ser de tipo entero o real\n"); break;
      case ERRNOENTERO: fprintf(stderr,"la expresion debe ser de tipo entero\n"); break;
    }
    exit(-1);
}

void msgError(int nerror, int nlin, int ncol, const char *s)
{
    switch (nerror) {
        case ERRLEXICO: fprintf(stderr,"Error lexico (%d,%d): caracter '%s' incorrecto\n",nlin,ncol,s); break;
        case ERRSINT: fprintf(stderr,"Error sintactico (%d,%d): en '%s'\n",nlin,ncol,s); break;
        case ERREOF: fprintf(stderr,"Error sintactico: fin de fichero inesperado\n"); break;
        case ERRLEXEOF: fprintf(stderr,"Error lexico: fin de fichero inesperado\n"); break;
    }
    exit(1);
}

int yyerror(char *s)
{
    if (findefichero)
        msgError(ERREOF,-1,-1,"");
    else
        msgError(ERRSINT,nlin,ncol-strlen(yytext),yytext);
    return 0;
}

int main(int argc, char *argv[])
{
    FILE *fent;
    if (argc == 2)
    {
        fent = fopen(argv[1],"rt");
        if (fent) { yyin = fent; yyparse(); fclose(fent); }
        else fprintf(stderr,"No puedo abrir el fichero\n");
    }
    else fprintf(stderr,"Uso: plp4 <nombre de fichero>\n");
}