/*------------------------------ ejemplo.l -------------------------------*/
D    [0-9]
L    [a-zA-Z]
LD   [0-9a-zA-Z]

%{
#include <string.h> 
#include <string>
#include <iostream>

using namespace std;

#include "comun.h"
#include "ejemplo.tab.h"

int ncol = 1,
    nlin = 1;

int findefichero = 0;

int buildToken(int token);  
// función que actualiza 'nlin' y 'ncol' y devuelve el token

void msgError(int nerror, int nlin,int ncol,const char *s);
// función para producir mensajes de error

%}

%x COMENTARIO

%%
%{
/* codigo local */
%}

" "                {ncol++;}
[\t]               {ncol++;}
[\n]               {nlin++;ncol=1;}
"//"(.)*           {; /* comentarios de una linea, no hacer nada */} 
"/*"                    {ncol += yyleng;BEGIN(COMENTARIO);}
<COMENTARIO>[^/*\n]+    {ncol += yyleng;}
<COMENTARIO>\n          {nlin++;ncol=1;}
<COMENTARIO><<EOF>>     {msgError(ERRLEXEOF,-1,-1,"");}
<COMENTARIO>"*/"        {ncol+=yyleng; BEGIN(INITIAL);}
<COMENTARIO>[*/]        {ncol += yyleng;}
"print"            {return buildToken(print);}
        /* Las palabras reservadas deben aparecer antes de la regla que
          reconoce los identificadores, para evitar que sean reconocidas
          como identificadores en lugar de como palabras reservadas */            
{L}({LD})*         {return buildToken(id);}
{D}+               {return buildToken(numentero);}
{D}+(\.){D}+       {return buildToken(numreal);}
"+"                {return buildToken(opas);}
"-"                {return buildToken(opas);}
"("                {return buildToken(pari);}
")"                {return buildToken(pard);}
";"                {return buildToken(pyc);}
","                {return buildToken(coma);}
.                  {msgError(ERRLEXICO,nlin,ncol,yytext);}

%%

int yywrap(void) {findefichero=1; return 1;} /* para no tener que linkar con la
                                libreria del lex */


int buildToken(int token)
{
   yylval.lexema=strdup(yytext);
   yylval.nlin=nlin;
   yylval.ncol=ncol;
   ncol+=(yyleng);
   return(token);
}
