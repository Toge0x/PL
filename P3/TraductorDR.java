import java.util.concurrent.atomic.AtomicInteger;

public class TraductorDR{

    class Atributos{
        public String trad;
        public int tipo;
        public String lexema;

        public Atributos(String lexema, int tipo){
            this.lexema = lexema;
            this.tipo = tipo;
        }

        public Atributos(String lexema, int tipo, String trad){
            this.lexema = lexema;
            this.tipo = tipo;
            this.trad = trad;
        }
    }

    public Token token;
    public AnalizadorLexico al;
    public TablaSimbolos tsActual;
    public StringBuilder reglasAplicadas;
    public boolean mostrarReglas;

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

    public void comprobarFinFichero(){      // de las transparencias
        if(token.tipo != Token.EOF){
            //lanzarErrorSintactico(Token.EOF);
        }
        if(mostrarReglas == true){
            System.out.println(reglasAplicadas);    // mostramos las reglas aplicadas
        }
    }
    
    
    // Empieza en ámbito = 1

    /*  ----------------------------------------
        FUNCIONES ASOCIADAS A LOS NO TERMINALES
        ----------------------------------------
    */

    // S −→ class id lbra M rbra
    // S −→ class id {prefijo := th=="" ? id.lex : th||"_"||id.lex} lbra {nuevaTabla(); M.th:=prefijo} M rbra {cerrarTabla()};
    // S.trad := "// class " || prefijo || "\n" || M.trad;
    public String S(String th){
        // S −→ class id lbra M rbra
        // emparejamos el token class
        emparejar(Token.CLASS);
        String idLexema = token.lexema;     // guardamos el id para pasarlo abajo
                                            // para la class Siete {fun main int a; {print 6}}
                                            // será Siete_main en la traducción
        // NO podemos guardarlo después porque ya se habrá consumido el token
        // emparejamos el token id
        emparejar(Token.ID);
        
        // creamos el prefijo de M, que será la concatenación class_M
        String prefijo = "";
        if(th.isEmpty() == true){
            prefijo = idLexema;           // si el prefijo está vacío estamos en el caso del principio
        }else{
            prefijo = th + "_" + idLexema;    // si no, es que hay class anidados class Main{ class A {}}
        }

        // emparejamos el token lbra −→ se abrirá un nuevo ámbito
        emparejar(Token.LBRA);

        tsActual = new TablaSimbolos(tsActual);     // se abre el nuevo ámbito
        String tradM = M(prefijo);                  // mandamos el prefijo para abajo
                                                    // y cuando suba ya tenemos la trad montada
        emparejar(Token.RBRA);                      // se cierra el ámbito
        tsActual = tsActual.getParent();            // recuperamos la anterior tabla de simbolos

        return "// class " + prefijo + "\n" + tradM;
    }

    // M −→ {Fun.th := M.th} Fun {M1.th := M.th} M1;
    // M.trad := M.trad := Fun.trad || M1.trad

    // M −→ {S.th := M.th} S {M1.th := M.th} M1;
    // M.trad := S.trad || M1.trad

    // M −→ ε;
    // M.trad = "";
    public String M(String th){
        if(token.tipo == Token.FUN){
            // M −→ {Fun.th := M.th} Fun {M1.th := M.th} M1;
            String tradFun = Fun(th);   // Fun.th := th
                                        // pasamos el tipo heredado, en nuestro caso ID.LEXEMA
            String tradM = M(th);       // M1.th := th
            return tradFun + tradM;     // M.trad = Fun.trad || M1.trad;
        }else if(token.tipo == Token.CLASS){
            // M −→ {S.th := M.th} S {M1.th := M.th} M1;
            String tradS = S(th);       // S.th := th
            String tradM = M(th);       // M1.th := th
            return tradS + tradM;       // M.trad = S.trad || M.trad;
        }else{
            // M −→ ε
            return "";      // M.trad = "";
        }
    }

    // fun id {prefijo := Fun.th || "_" || id.lex; nuevaTabla(); A.th := prefijo}
    // A lbra {M.th := prefijo; Cod.th := prefijo}
    // M Cod rbra {cerrarTabla()};
    // Fun.trad := "void " || prefijo || "(" || A.trad || ") {\n" || M.trad || Cod.trad || "} // " || prefijo || "\n"}

    public String Fun(String th){
        // Fun −→  fun id {prefijo := Fun.th || "_" || id.lex; nuevaTabla(); A.th := prefijo}
        // emparejamos el token fun
        emparejar(Token.FUN);
        // recogemos el lexema y emparejamos el token id
        String idLexema = token.lexema;
        emparejar(Token.ID);

        // asignamos al prefijo el tipo heredado + "_" + lexema recién leído
        String prefijo = th + "_" + idLexema;   // así se construye el prefijo de la función
                                                // será lo proveniente de th := A, en caso de ser class A
                                                // y se empareja con A_funcion

        // se abre nuevo ámbito porque los parámetros se declaran en el mismo ámbito que las var locales
        // lo pone en el documento de la práctica
        // esto es nuevaTabla();
        tsActual = new TablaSimbolos(tsActual);

        String tradA = A(prefijo);              // A.th = prefijo
        // consumimos el token lbra
        emparejar(Token.LBRA);

        String tradM = M(prefijo);              // M.th = prefijo
        String tradCod = Cod(prefijo);          // Cod.th = prefijo

        // consumimos el token rbra
        emparejar(Token.RBRA);

        // cerramos el ámbito y recuperamos la anterior tabla de símbolos
        tsActual = tsActual.getParent();

        // Fun.trad := "void " || prefijo || "(" || A.trad || ") {\n" || M.trad || Cod.trad || "} // " || prefijo || "\n"}
        return "void " + prefijo + "(" + tradA + ") {\n" + tradM + tradCod + "} // " + prefijo + "\n";
    }

    // A −→ DV {tradParametros := "arg_" || A.th || "_" || DV.lexema;
    // set(DV.lexema, DV.tipo, tradParametros); Ap.th := A.th;} Ap
    // A.trad := (DV.tipo == "ENTERO" ? "int" : "float") || " " || tradParametros || Ap.trad
    public String A(String th){
        // A −→ DV Ap
        Atributos atributosDV = DV();       // llamada a DV y obtenemos tipo, trad y lexema
        String tradParametros = "arg_" + th + "_" + atributosDV.lexema;     // construimos la traducción necesaria de los parámetros

        // insertamos el símbolo en la tabla, el set del parametro de la funcion
        Simbolo dv = new Simbolo(atributosDV.lexema, atributosDV.tipo, tradParametros);

        // si ya existe el simbolo soltamos error semantico
        if(tsActual.set(dv) == false){
            //errorSemantico(token.fila, token.columna, "en '" + atributosDV.lexema + "', ya existe en este ambito");
        }
        
        String tradAp = Ap(th);     // Ap.th := A.th

        // A.trad := (DV.tipo == "ENTERO" ? "int" : "float") || " " || tradParametros || Ap.trad
        return (atributosDV.tipo == Simbolo.ENTERO ? "int" : "float") + " " + tradParametros + tradAp;
    }

    // Ap −→ pyc DV {tradParametros := "arg_" || Ap.th || "_" || DV.lexema;
    // set(DV.lexema, DV.tipo, tradParametros); Ap1.th := Ap.th} Ap1
    // Ap.trad := ", " || (DV.tipo == "ENTERO" ? "int" : "float") || tradParametros || Ap1.trad;

    // Ap −→ ϵ
    // Ap.trad = "";
    public String Ap(String th){
        // Ap −→ pyc DV {tradParametros := "arg_" || Ap.th || "_" || DV.lexema; 
        // set(DV.lexema, DV.tipo, tradParametros); Ap1.th := Ap.th} Ap1
        if(token.tipo == Token.PYC){
            // consumimos el token ';'
            emparejar(Token.PYC);

            // hacemos la llamada a DV para que nos de los parametros de la funcion
            Atributos atributosDV = DV();
            String tradParametros = "arg_" + th + "_" + atributosDV.lexema; // construimos la traducción de los parámetros

            // hacemos el set del simbolo, la nueva variable del parametro de la funcion
            Simbolo dv = new Simbolo(atributosDV.lexema, atributosDV.tipo, tradParametros);

            // si ya existe ese simbolo hay que lanzar error semantico
            if(tsActual.set(dv) == false){
                //errorSemantico(token.fila, token.columna, "en '" + atributosDV.lexema + "', ya existe en este ambito");
            }

            String tradAp1 = Ap(th);    // Ap1.th = th

            // Ap.trad := ", " || (DV.tipo == "ENTERO" ? "int" : "float") || tradParametros || Ap1.trad
            return ", " + (atributosDV.tipo == Simbolo.ENTERO ? "int" : "float") + " " + tradParametros + tradAp1;
        }else{
            // Ap −→ ϵ
            // Ap.trad := "";
            return "";
        }
    }

    // Cod −→ {I.th := th} I {Codp.th := th} Codp
    // Cod.trad = I.trad || Codp.trad;
    public String Cod(String th){
        // Cod −→ {I.th := th} I {Codp.th := th} Codp
        String tradI = I(th);
        String tradCodp = Codp(th);
        
        // Cod.trad = I.trad || Codp.trad
        return tradI + tradCodp;
    }

    // Codp −→ pyc {I.th:=th} I {Codp1.th:=th} Codp1
    // Codp.trad = I.trad || Codp1.trad;
    // Codp −→ ϵ
    // Codp.trad = "";
    public String Codp(String th){
        // Codp −→ pyc {I.th:=th} I {Codp1.th:=th} Codp1
        if(token.tipo == Token.PYC){
            emparejar(Token.PYC);
            String tradI = I(th);           // I.th = th
            String tradCodp1 = Codp(th);    // Codp1.th = th

            // Codp.trad := I.trad || Codp1.trad;
            return tradI + tradCodp1;
        }else{
            // Codp −→ ϵ
            // Codp.trad := "";
            return "";
        }
    }

    // DV −→ Tipo id {DV.tipo := Tipo.tipo; DV.lexema := id.lex}
    public Atributos DV(){
        int tipo = Tipo();      // conseguimos el tipo de la variable
        String lexema = token.lexema;   // conseguimos el lexema de la variable
        emparejar(Token.ID);            // consumimos el id

        // devolvemos los atributos sin traducción porque no tiene
        // la traducción la va construyendo quien llama al método DV
        Atributos atributos = new Atributos(lexema, tipo);
        return atributos;
    }

    // Tipo −→ int {Tipo.tipo := ENTERO}
    // Tipo −→ float {Tipo.tipo := REAL}
    public int Tipo(){
        if(token.tipo == Token.INT){
            // Tipo −→ int {Tipo.tipo := ENTERO}
            emparejar(Token.INT);
            return Simbolo.ENTERO;
        }else{
            // Tipo −→ float {Tipo.tipo := REAL}
            emparejar(Token.FLOAT);
            return Simbolo.REAL;
        }
    }

    // I −→ DV {prefijo := I.th || "_" || DV.lexema; set(DV.lexema, DV.tipo, prefijo)};
    // I.trad := (atributos.tipo == Simbolo.ENTERO ? "int" : "float") || " " || prefijo || ";\n";

    // I −→ lbra {nuevaTabla(); Cod.th := I.th || "_"} Cod rbra {cerrarTabla()};
    // I.trad := "{\n" || Cod.trad || "}\n";

    // I −→ id asig Expr {simbolo := get(id.lex); comprobar errores semánticos};
    // I.trad := simbolo.nomtrad || " = " || tradExpr || ";\n";

    // I −→ if Expr {Expr.tipo != ENTERO −→ ERROR} dosp {I1.th := I.th} I1 {Ip.th := I.th} Ip
    // I.trad := "if (" || Expr.trad || ")\n" || I1.trad || Ip.trad;

    // I −→ print Expr {formato := Expr.tipo == ENTERO ? "%d" : "%f"};
    // I.trad := "printf(\"" || formato || "\"," || Expr.trad || ");\n";
    public String I(String th){
        if(token.tipo == Token.INT || token.tipo == Token.FLOAT){
            // I −→ DV {prefijo := I.th || "_" || DV.lexema; set(DV.lexema, DV.tipo, prefijo)};
            Atributos atributos = DV();     // cogemos los atributos tipo, lexema del parametro
            String prefijo = th + "_" + atributos.lexema;   // construimos el prefijo

            // añadimos el nuevo símbolo, es decir, el parámetro de la función
            Simbolo simbolo = new Simbolo(atributos.lexema, atributos.tipo, prefijo);

            // si ya existe el simbolo, lanzamos error semántico
            if(tsActual.set(simbolo) == false){
                //errorSemantico(token.fila, token.columna, "en '" + atributosDV.lexema + "', ya existe en este ambito");
            }

            // I.trad := (atributos.tipo == Simbolo.ENTERO ? "int" : "float")|| " " || prefijo || ";\n";
            return (atributos.tipo == Simbolo.ENTERO ? "int" : "float") + " " + prefijo + ";\n";
        }else if(token.tipo == Token.LBRA){
            // I −→ lbra {nuevaTabla(); Cod.th := I.th||"_"} Cod rbra
            emparejar(Token.LBRA);                      // consumimos el token lbra
            tsActual = new TablaSimbolos(tsActual);     // creamos la tabla por nuevo ámbito
            String CodTrad = Cod(th + "_");             // Cod.th := I.th
            emparejar(Token.RBRA);                      // consumimos token rbra
            tsActual = tsActual.getParent();            // cerramos ámbito

            // I.trad := "{\n" || Cod.trad || "}\n";
            return "{\n" + CodTrad + "}\n";
        }else if(token.tipo == Token.ID){
            // I −→ id asig Expr {simbolo := get(id.lex); comprobar errores semánticos};
            // esto es el caso de a   =   b + c
            //                    id asig Expr
            // guardamos lexema, fila, columna para errores
            String lexema = token.lexema;
            int fila = token.fila;
            int columna = token.columna;
            // consumimos el token id
            emparejar(Token.ID);

            // despues de consumir id, guardamos fila y columna de asig para errores
            int filaAsig = token.fila;
            int columnaAsig = token.columna;
            // consumimos el token asig
            emparejar(Token.ASIG);

            Atributos atributosExpr = Expr();
            // obtenemos el simbolo del lexema en la tabla de simbolos
            Simbolo s = tsActual.get(lexema);

            // si no existe es que no se ha declarado esa variable
            if(s == null){
                //errorSemantico(fila, columna, "en '" + lexema + "', no ha sido declarado");
            }
            // el tipo debe de ser de tipo entero o real, es una asignación
            if(s.tipo == Simbolo.CLASS || s.tipo == Simbolo.FUN){
                //errorSemantico(fila, columna, "en '" + lexema + "', debe ser de tipo entero o real");
            }
            // si la variable es entera y la expresión es real, error semántico
            if(s.tipo == Simbolo.ENTERO && atributosExpr.tipo == Simbolo.REAL){
                //errorSemantico(filaAsig, columnaAsig, "en '=', tipos incompatibles entero/real");
            }
            
            // si todo va bien se consigue la traducción de la expresión
            String tradExpr = (s.tipo == Simbolo.REAL && atributosExpr.tipo == Simbolo.ENTERO) ? "itor(" + atributosExpr.trad + ")" : atributosExpr.trad;
            
            // I.trad := s.nomtrad || " = " || tradExpr || ";\n";
            return s.nomtrad + " = " + tradExpr + ";\n";
        }else if(token.tipo == Token.IF){
            // I −→ if Expr {Expr.tipo != ENTERO −→ ERROR} dosp {I1.th := I.th} I1 {Ip.th := I.th} Ip
            // guardamos fila, columna del token if para errores y lo consumimos
            int filaIf = token.fila;
            int columnaIf = token.columna;
            emparejar(Token.IF);

            // obtenemos los atributos de la expresión
            // este es el caso if  a>b   :  ....
            //                 if Expr dosp
            Atributos atributosExpr = Expr();

            // esta expresión debe ser de tipo entero ya que se cumple o no (0 o 1)
            if(atributosExpr.tipo != Simbolo.ENTERO){
                //errorSemantico(filaIf, columnaIf, "en 'if', la expresion debe ser de tipo entero");
            }

            // consumimos el token ':'
            emparejar(Token.DOSP);

            String tradI1 = I(th);      // I1.th := th
            String tradIp = Ip(th);     // Ip.th := th

            // I.trad := "if (" || Expr.trad || ")\n" || I1.trad || Ip.trad;
            return "if (" + atributosExpr.trad + ")\n" + tradI1 + tradIp;
        }else{
            // print Expr {formato := Expr.tipo == ENTERO ? "%d" : "%f"};
            // consumimos el token print
            emparejar(Token.PRINT);
            // esto es el caso print  a+b
            //                 print  Expr
            Atributos atributosExpr = Expr();       // obtenemos los atributos de la expresión

            String formato;                         // y construimos el formato en base a la expresión
            if(atributosExpr.tipo == Simbolo.ENTERO){
                formato = "%d";
            }else{
                formato = "%f";
            }

            // I.trad := "printf(\"" || formato || "\"," || Expr.trad || ");\n";
            return "printf(\"" + formato + "\"," + atributosExpr.trad + ");\n";
        }
    }

    // Ip −→ else {I.th := Ip.th} I fi
    // Ip.trad := "else\n" || I.trad;

    // Ip −→ fi
    // Ip.trad := "";
    public String Ip(String th){
        if(token.tipo == Token.ELSE){
            // Ip −→ else {I.th := Ip.th} I fi
            emparejar(Token.ELSE);  // consumimos el token else
            String tradI = I(th);   // I.th := Ip.th
            emparejar(Token.FI);    // emparejamos el token fi

            // Ip.trad := "else\n" || I.trad;
            return "else\n" + tradI;
        }else{
            // Ip −→ fi
            emparejar(Token.FI);

            // Ip.trad := "";
            return "";
        }
    }

}