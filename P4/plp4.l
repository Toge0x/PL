D  [0-9]
L  [a-zA-Z_]
LD [0-9a-zA-Z_]

%{
#include <string.h>
#include <string>
#include <iostream>
using namespace std;
#include "comun.h"
#include "plp4.tab.h"
int ncol = 1, nlin = 1, findefichero = 0;

int buildToken(int token);
// función que actualiza nlin y ncol y devuelve el token

void msgError(int nerror, int nlin, int ncol, const char *s);
// función para producir mensajes de error
%}


%x COMENTARIO

%%
%{
/* codigo local */
%}


" "          {ncol++;}
[\t]         {ncol++;}
[\n]         {nlin++; ncol=1;}
"//"(.)*     {; /* comentarios de una linea, no hacer nada */}
"/*"                 { ncol += yyleng; BEGIN(COMENTARIO); }
<COMENTARIO>[^/*\n]+ { ncol += yyleng; }
<COMENTARIO>\n       { nlin++; ncol=1; }
<COMENTARIO><<EOF>>  { msgError(ERRLEXEOF,-1,-1,""); }
<COMENTARIO>"*/"     { ncol += yyleng; BEGIN(INITIAL); }
<COMENTARIO>[*/]     { ncol += yyleng; }
"class"      {return buildToken(CLASS);}
"fun"        {return buildToken(FUN);}
"int"        {return buildToken(INT);}
"float"      {return buildToken(FLOAT);}
"if"         {return buildToken(IF);}
"else"       {return buildToken(ELSE);}
"fi"         {return buildToken(FI);}
"print"      {return buildToken(PRINT);}
{L}({LD})*   {return buildToken(ID);}
{D}+         {return buildToken(NUMENTERO);}
{D}+(\.){D}+ {return buildToken(NUMREAL);}
"+"          {return buildToken(OPAS);}
"-"          {return buildToken(OPAS);}
"*"          {return buildToken(OPMUL);}
"/"          {return buildToken(OPMUL);}
"<"          {return buildToken(OPREL);}
">"          {return buildToken(OPREL);}
"<="         {return buildToken(OPREL);}
">="         {return buildToken(OPREL);}
"=="         {return buildToken(OPREL);}
"!="         {return buildToken(OPREL);}
"="          {return buildToken(ASIG);}
"{"          {return buildToken(LBRA);}
"}"          {return buildToken(RBRA);}
"("          {return buildToken(PARI);}
")"          {return buildToken(PARD);}
";"          {return buildToken(PYC);}
":"          {return buildToken(DOSP);}
.            {msgError(ERRLEXICO, nlin, ncol, yytext);}

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