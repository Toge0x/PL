public class AnalizadorSintacticoDR{
    public AnalizadorLexico al;         // para llamar al siguienteToken
    public Token token;                 // para almacenar la info del token
    public boolean mostrarReglas;       // mostrar la sucesión de reglas aplicadas

    public AnalizadorSintacticoDR(AnalizadorLexico al){
        this.al = al;
    }

    public void lanzarErrorSintactico(int ... tipoTokenEsperado){   // lo necesito porque pueden ser muchos tipos de error
        // TODO: aun no se que hay que hacer
    }

    public final void emparejar(int tipoTokenEsperado){
        if(token.tipo == tipoTokenEsperado){
            token = al.siguienteToken();
        }else{
            lanzarErrorSintactico(tipoTokenEsperado);
        }
    }

    /*  ----------------------------------------
        FUNCIONES ASOCIADAS A LOS NO TERMINALES
        ----------------------------------------
    */

    // Conjuntos de predicción
    // Regla 1: S −→ class id lbra M rbra = {class}
    public void S(){
        if(token.tipo == Token.CLASS){
            emparejar(Token.CLASS);
            emparejar(Token.ID);
            emparejar(Token.LBRA);
            M();
            emparejar(Token.RBRA);
        }else{
            lanzarErrorSintactico(Token.CLASS);   // pueden ser varios tipos, en este caso solo tenemos 1 en la predicción
        }
    }

    // Conjuntos de predicción
    // Regla 2: M −→ Fun M  = {fun}
    // Regla 3: M −→ S M = {class}
    // Regla 4: M −→ ϵ = {rbra int float lbra id if print}
    public void M(){
        if(token.tipo == Token.FUN){
            FUN();
            M();
        }else if(token.tipo == Token.CLASS){
            S();
            M();
        }else if(token.tipo == Token.RBRA || token.tipo == Token.INT ||token.tipo == Token.FLOAT ||
            token.tipo == Token.LBRA || token.tipo == Token.ID || token.tipo == Token.IF || token.tipo == Token.PRINT){
                // vacío, es epsilon
        }else{
            lanzarErrorSintactico(Token.FUN, Token.CLASS, Token.RBRA, Token.INT, Token.FLOAT, Token.LBRA, Token.ID, Token.IF, Token.PRINT);
        }
    }

    // Conjuntos de predicción
    // Regla 5: Fun −→ fun id A lbra M Cod rbra = {fun}
    public void FUN(){
        if(token.tipo == Token.FUN){
            emparejar(Token.FUN);
            emparejar(Token.ID);
            A();
            emparejar(Token.LBRA);
            M();
            Cod();
            emparejar(Token.RBRA);
        }else{
            lanzarErrorSintactico(Token.FUN);
        }
    }

    // Conjuntos de predicción
    // Regla 6: A −→ DV Ap = {int float}
    public void A(){
        if(token.tipo == Token.INT || token.tipo == Token.FLOAT){
            DV();
            Ap();
        }else{
            lanzarErrorSintactico(Token.INT, Token.FLOAT);
        }
    }

    // Conjuntos de predicción
    // Regla 7: Ap −→ pyc DV Ap = {pyc}
    // Regla 8: Ap −→ ϵ = {lbra}
    public void Ap(){
        if(token.tipo == Token.PYC){
            emparejar(Token.PYC);
            DV();
            Ap();
        }else if(token.tipo == Token.LBRA){
            // epsilon
        }else{
            lanzarErrorSintactico(Token.PYC, Token.LBRA);
        }
    }

    // Conjuntos de predicción
    // Regla 9: DV −→ Tipo id = {int float}
    public void DV(){
        if(token.tipo == Token.INT || token.tipo == Token.FLOAT){
            Tipo();
            emparejar(Token.ID);
        }else{
            lanzarErrorSintactico(Token.INT, Token.FLOAT);
        }
    }

    // Conjuntos de predicción
    // Regla 10: Tipo −→ int = {int}
    // Regla 11: Tipo −→ float = {float}
    public void Tipo(){
        if(token.tipo == Token.INT){
            emparejar(Token.INT);
        }else if(token.tipo == Token.FLOAT){
            emparejar(Token.FLOAT);
        }else{
            lanzarErrorSintactico(Token.INT, Token.FLOAT);
        }
    }

    // Conjuntos de predicción
    // Regla 12: Cod −→ I Codp = {int float lbra id if print}
    public void Cod(){
        if(token.tipo == Token.INT || token.tipo == Token.FLOAT || token.tipo == Token.LBRA
            || token.tipo == Token.ID || token.tipo == Token.IF || token.tipo == Token.PRINT){
            I();
            Codp();
        }else{
            lanzarErrorSintactico(Token.INT, Token.FLOAT, Token.LBRA, Token.ID, Token.IF, Token.PRINT);
        }
    }

    // Conjuntos de predicción
    // Regla 13: Codp −→ pyc I Codp = {pyc}
    // Regla 14: Codp −→ ϵ = {rbra}
    public void Codp(){
        if(token.tipo == Token.PYC){
            emparejar(Token.PYC);
            I();
            Codp();
        }else if(token.tipo == Token.RBRA){
            // epsilon
        }else{
            lanzarErrorSintactico(Token.INT, Token.FLOAT, Token.LBRA, Token.ID, Token.IF, Token.PRINT);
        }
    }

    // Conjuntos de predicción
    // Regla 15: I −→ DV = {int float}
    // Regla 16: I −→ lbra Cod rbra = {lbra}
    // Regla 17: I −→ id asig Expr = {id}
    // Regla 18: I −→ if Expr dosp I Ip = {if}
    // Regla 19: I −→ print Expr = {print}
    public void I(){
        if(token.tipo == Token.INT || token.tipo == Token.FLOAT){
            DV();
        }else if(token.tipo == Token.LBRA){
            emparejar(Token.LBRA);
            Cod();
            emparejar(Token.RBRA);
        }else if(token.tipo == Token.ID){
            emparejar(Token.ID);
            emparejar(Token.ASIG);
            Expr();
        }else if(token.tipo == Token.IF){
            emparejar(Token.IF);
            Expr();
            emparejar(Token.DOSP);
            I();
            Ip();
        }else if(token.tipo == Token.PRINT){
            emparejar(Token.PRINT);
            Expr();
        }else{
            lanzarErrorSintactico(Token.INT, Token.FLOAT, Token.LBRA, Token.ID, Token.IF, Token.PRINT);
        }
    }

    // Conjuntos de predicción
    // Regla 20: Ip −→ else I fi = {else}
    // Regla 21: Ip −→ fi = {fi}
    public void Ip(){
        if(token.tipo == Token.ELSE){
            emparejar(Token.ELSE);
            I();
            emparejar(Token.FI);
        }else if(token.tipo == Token.FI){
            emparejar(Token.FI);
        }else{
            lanzarErrorSintactico(Token.ELSE, Token.FI);
        }
    }

    // Conjuntos de predicción
    // Regla 22: Expr −→ E Exprp = {id numentero numreal pari}
    public void Expr(){
        if(token.tipo == Token.ID || token.tipo == Token.NUMENTERO
            || token.tipo == Token.NUMREAL || token.tipo == Token.PARI){
                E();
                Exprp();
        }else{
            lanzarErrorSintactico(Token.ID, Token.NUMENTERO, Token.NUMREAL, Token.PARI);
        }
    }

}