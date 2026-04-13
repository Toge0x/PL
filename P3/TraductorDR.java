public class TraductorDR{

    public Token token;
    public AnalizadorLexico al;
    public TablaSimbolos tsActual;
    public StringBuilder reglasAplicadas;

    public TraductorDR(AnalizadorLexico al){
        this.al = al;
        this.token = al.siguienteToken();
        this.tsActual = new TablaSimbolos(null);
    }

    public final void emparejar(int tipoTokenEsperado){
        if(token.tipo == tipoTokenEsperado){
            token = al.siguienteToken();
        }else{
            //lanzarErrorSintactico(tipoTokenEsperado);
        }
    }

    
    // Empieza en ámbito = 1

    /*  ----------------------------------------
        FUNCIONES ASOCIADAS A LOS NO TERMINALES
        ----------------------------------------
    */

    // S −→ class id lbra M rbra
    // S.trad −→ "// class " + ID.LEXEMA + "\n" + M.trad;
    public String S(String th){
        // S −→ class id lbra M rbra
        emparejar(Token.CLASS);
        String idLexema = token.lexema;   // guardamos el id para pasarlo abajo
                                        // para la class Siete {fun main int a; {print 6}}
                                        // será Siete_main en la traducción
        emparejar(Token.ID);
        emparejar(Token.LBRA);

        String prefijo = "";
        if(th.isEmpty() == true){
            prefijo = idLexema;           // si el prefijo está vacío estamos en el caso del principio
        }else{
            prefijo = th + "_" + idLexema;    // si no, es que hay class anidados class Main{ class A {}}
        }

        tsActual = new TablaSimbolos(tsActual);     // nuevo ámbito
        String tradM = M(prefijo);                  // mandamos el prefijo para abajo
                                                    // y cuando suba ya tenemos la trad montada
        emparejar(Token.RBRA);                      // se cierra el ámbito
        tsActual = tsActual.getParent();

        return "// class " + idLexema + "\n" + tradM;     // S.trad = "// class " + ID.LEXEMA + "\n" + M.trad;
    }

    // M −→ Fun M
    // M.trad −→ Fun.trad || M.trad;
    // M −→ S M
    // M.trad −→ S.trad || M.trad;
    // M −→ ε
    // M.trad = "";
    public String M(String th){
        if(token.tipo == Token.FUN){
            // M −→ Fun M
            String tradFun = Fun(th);   // Fun.th := th
                                        // pasamos el tipo heredado, en nuestro caso ID.LEXEMA
            String tradM = M(th);       // M1.th := th
            return tradFun + tradM;     // M.trad = Fun.trad || M.trad;
        }else if(token.tipo == Token.CLASS){
            // M −→ S M
            String tradS = S(th);       // S.th := th
            String tradM = M(th);       // M1.th := th
            return tradS + tradM;       // M.trad = S.trad || M.trad;
        }else{
            // M −→ ε
            return "";      // M.trad = "";
        }
    }

    // Fun −→ fun id A lbra M Cod rbra
    public String Fun(String th){
        // Fun −→ fun id A lbra M Cod rbra
        emparejar(Token.FUN);
        String idLexema = token.lexema;
        emparejar(Token.ID);

        String prefijo = th + "_" + idLexema;   // le asignaremos a los parámetros los nombres
                                                // con el prefijo delante
                                                // NO PUEDE SER SOLO ID.LEXEMA

        tsActual = new TablaSimbolos(tsActual); // la lista de atributos de A se declara en su ámbito
                                                // // nuevo ámbito

        String tradA = A(prefijo);              // obtenemos la traduccion de A
        emparejar(Token.LBRA);

        String tradM = M(prefijo);              // obtenemos la traduccion de M
        String tradCod = Cod(prefijo);          // obtenemos la traduccion de Cod

        emparejar(Token.RBRA);                  // cerramos el ámbito
        tsActual = tsActual.getParent();

        return "void " + prefijo + "(" + tradA + ") {\n" + tradM + tradCod + "} // " + prefijo + "\n";      // Fun.trad = "void " || prefijo || "(" || A.trad || ") {\n" || M.trad || Cod.trad || "} // " || prefijo || "\n";
    }


}