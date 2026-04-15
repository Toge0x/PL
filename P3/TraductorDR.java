public class TraductorDR{

    class Atributos{
        public String trad;
        public int tipo;
        public String lexema;

        public Atributos(String lexema, int tipo){
            this.lexema = lexema;
            this.tipo = tipo;
            this.trad = lexema;
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

    public void lanzarErrorSintactico(int ... tipoTokenEsperado){   // lo necesito porque pueden ser muchos tipos de error
        if(token.tipo == Token.EOF){
            System.err.print("Error sintactico: encontrado fin de fichero, esperaba");
        }else{
            System.err.print("Error sintactico (" + token.fila + "," + token.columna + "): encontrado '" + token.lexema + "', esperaba ");
        }
        // concantenar tercera columna
        //  Token            Expresion Regular          Cadena
        //  pari                (                       (
        //  pard                )                       )
        //  ...                 ...                     ...
        for(int i = 0; i < tipoTokenEsperado.length; i++){      // cogemos todos los que lleguen
            token.tipo = tipoTokenEsperado[i];
            String error = token.toString() + " ";
            System.err.print(error);
        }
        System.err.println("\n");
        System.exit(-1);
    }

    public final void emparejar(int tipoTokenEsperado){
        if(token.tipo == tipoTokenEsperado){
            token = al.siguienteToken();
        }else{
            lanzarErrorSintactico(tipoTokenEsperado);
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
    
    public Atributos opera(String trad1, int tipo1, String trad2, int tipo2, String operador){
        String traduccion1, traduccion2, sufijoOperador, traduccionResultado;

        if(tipo1 == Simbolo.REAL && tipo2 == Simbolo.REAL){     // ambos reales
            sufijoOperador = "r";
            traduccion1 = trad1;
            traduccion2 = trad2;
        }else if(tipo1 == Simbolo.REAL && tipo2 == Simbolo.ENTERO){     // uno real, uno entero
            sufijoOperador = "r";
            traduccion1 = trad1;
            traduccion2 = "itor(" + trad2 + ")";
        }else if(tipo1 == Simbolo.ENTERO && tipo2 == Simbolo.REAL){     // uno entero, uno real
            sufijoOperador = "r";
            traduccion1 = "itor(" + trad1 + ")";
            traduccion2 = trad2;
        }else{                       // ambos enteros
            sufijoOperador = "i";
            traduccion1 = trad1;
            traduccion2 = trad2;
        }

        // en el caso de un entero y un real sería
        // itor(arg_Main_main_a) +r arg_Main_main_b
        // y el tipo de Atributos sería Simbolo.REAL
        traduccionResultado = traduccion1 + " " + operador + sufijoOperador + " " + traduccion2;
        int tipo = (tipo1 == Simbolo.ENTERO && tipo2 == Simbolo.ENTERO ? Simbolo.ENTERO : Simbolo.REAL);
        return new Atributos(traduccionResultado, tipo);
    }
    
    public void lanzarErrorSemantico(int fila, int columna, String mensaje) {
        System.err.println("Error semantico (" + fila + "," + columna + "): " + mensaje);
        System.exit(1);
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
            lanzarErrorSemantico(token.fila, token.columna, "en '" + atributosDV.lexema + "', ya existe en este ambito");
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
                lanzarErrorSemantico(token.fila, token.columna, "en '" + atributosDV.lexema + "', ya existe en este ambito");
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
            Atributos atributosDV = DV();     // cogemos los atributos tipo, lexema del parametro
            String prefijo = th + "_" + atributosDV.lexema;   // construimos el prefijo

            // añadimos el nuevo símbolo, es decir, el parámetro de la función
            Simbolo simbolo = new Simbolo(atributosDV.lexema, atributosDV.tipo, prefijo);

            // si ya existe el simbolo, lanzamos error semántico
            if(tsActual.set(simbolo) == false){
                lanzarErrorSemantico(token.fila, token.columna, "en '" + atributosDV.lexema + "', ya existe en este ambito");
            }

            // I.trad := (atributos.tipo == Simbolo.ENTERO ? "int" : "float")|| " " || prefijo || ";\n";
            return (atributosDV.tipo == Simbolo.ENTERO ? "int" : "float") + " " + prefijo + ";\n\n";
        }else if(token.tipo == Token.LBRA){
            // I −→ lbra {nuevaTabla(); Cod.th := I.th||"_"} Cod rbra
            emparejar(Token.LBRA);                      // consumimos el token lbra
            tsActual = new TablaSimbolos(tsActual);     // creamos la tabla por nuevo ámbito
            String CodTrad = Cod(th + "_");             // Cod.th := I.th
            emparejar(Token.RBRA);                      // consumimos token rbra
            tsActual = tsActual.getParent();            // cerramos ámbito

            // I.trad := "{\n" || Cod.trad || "}\n";
            return "{\n" + CodTrad + "}\n\n";
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
                lanzarErrorSemantico(fila, columna, "en '" + lexema + "', no ha sido declarado");
            }
            // el tipo debe de ser de tipo entero o real, es una asignación
            if(s.tipo == Simbolo.CLASS || s.tipo == Simbolo.FUN){
                lanzarErrorSemantico(fila, columna, "en '" + lexema + "', debe ser de tipo entero o real");
            }
            // si la variable es entera y la expresión es real, error semántico
            if(s.tipo == Simbolo.ENTERO && atributosExpr.tipo == Simbolo.REAL){
                lanzarErrorSemantico(filaAsig, columnaAsig, "en '=', tipos incompatibles entero/real");
            }
            
            // si todo va bien se consigue la traducción de la expresión
            String tradExpr = (s.tipo == Simbolo.REAL && atributosExpr.tipo == Simbolo.ENTERO) ? "itor(" + atributosExpr.trad + ")" : atributosExpr.trad;
            
            // I.trad := s.nomtrad || " = " || tradExpr || ";\n";
            return s.nomtrad + " = " + tradExpr + ";\n\n";
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
                lanzarErrorSemantico(filaIf, columnaIf, "en 'if', la expresion debe ser de tipo entero");
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
            return "printf(\"" + formato + "\"," + atributosExpr.trad + ");\n\n";
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

    // Expr −→ E Exprp  {{si Exprp.esVacio:
    //                          Expr.trad := E.trad;
    //                          Expr.tipo := E.tipo
    //                   si no:
    //                          [combinar E con Exprp según tipos];
    // Expr.trad := t1 || " " || Exprp.operador || sufijoOperador || " " || t2;
    // Expr.tipo := ENTERO
    public Atributos Expr(){
        // Expr −→ E Exprp {si, ... no, ...}
        // Son el caso a     > b por ejemplo
        //             E     Exprp
        Atributos atributosE = E();
        Atributos atributosExprp = Exprp();

        if(atributosExprp == null){     // si no hay operando solo devolvemos la variable procesada en E
            return atributosE;
        }else{                          // si si la hay construimos la operación según tipos
            Atributos traduccion = opera(atributosE.trad, atributosE.tipo,
                                        atributosExprp.lexema, atributosExprp.tipo,
                                        atributosExprp.trad);

            // Expr.trad := t1 || " " || Exprp.operador || sufijoOperador || " " || t2;
            // ya tenemos el resultado en la función opera
            return new Atributos(traduccion.trad, Simbolo.ENTERO);
        }
    }

    // Exprp −→ oprel E {guardar op, trad y tipo de E}
    // Exprp −→ ϵ {return null}
    public Atributos Exprp(){
        if(token.tipo == Token.OPREL){      // es un operador relacional (==, <=, >=, !=, <, >)
            String operador = token.lexema;     // guardamos el operador y consumimos token
            emparejar(Token.OPREL);

            Atributos atributosE = E();         // procesamos operando derecho
                                                // Expr −→ E Exprp      --> a     + b
                                                // Exprp −→ oprel E     --> +     b
                                                // Exprp −→ ϵ
            return new Atributos(atributosE.trad, atributosE.tipo, operador);
        }else{
            return null;
        }
    }

    // E −→ T {Ep.trad := T.trad; Ep.tipo := T.tipo} Ep
    public Atributos E(){
        Atributos atributosT = T();
        return Ep(atributosT.trad, atributosT.tipo);
    }

    // Ep −→ opas T Ep
    // Ep.trad := resultado.trad; Ep.tipo := resultado.tipo

    // Ep −→ ϵ
    // Ep.trad := Ep.trad_iz; Ep.tipo := Ep.tipo_iz
    public Atributos Ep(String trad, int tipo){
        if(token.tipo == Token.OPAS){           // es un operador +, -
            String operador = token.lexema;
            emparejar(Token.OPAS);              // guardamos el lexema y consumimos

            Atributos atributosT = T();         // obtenemos el resto de la expresión
            Atributos resultado = opera(trad, tipo, atributosT.trad, atributosT.tipo, operador);

            return Ep(resultado.trad, resultado.tipo);
        }else{          // no hay más operadores
            return new Atributos(trad, tipo);
        }
    }

    // T −→ F {Tp.trad := F.trad; Tp.tipo := F.tipo} Tp
    // T.trad := Tp.trad; T.tipo := Tp.tipo
    public Atributos T(){
        Atributos atributosF = F();
        return Tp(atributosF.trad, atributosF.tipo);
    }

    // Tp −→ opmul F {combinar Tp.trad/tipo con F usando opera();
    // Tp1.trad := resultado.trad; Tp1.tipo := resultado.tipo} Tp1
    // Tp.trad := Tp1.trad; Tp.tipo := Tp1.tipo

    // Tp −→ ϵ
    public Atributos Tp(String trad, int tipo){
        if(token.tipo == Token.OPMUL){      // es un operador *, /
            String operador = token.lexema;
            emparejar(Token.OPMUL);             // guardamos el lexema y consumimos

            Atributos atributosF = F();         // obtenemos el resto de la expresión (F da los literales)
            Atributos resultado = opera(trad, tipo, atributosF.trad, atributosF.tipo, operador);

            return Tp(resultado.trad, resultado.tipo);
        }else{          // no hay más operadores
            return new Atributos(trad, tipo);
        }
    }

    // F −→ id {get(id.lex); si simbolo == null ERROR, si simbolo FUN o CLASS ERROR}
    // F.trad := simbolo.nomtrad; F.tipo := simbolo.tipo

    // F −→ numentero
    // F.trad := numentero.lex; F.tipo := ENTERO

    // F −→ numreal
    // F.trad := numreal.lex; F.tipo := REAL

    // F −→ pari Expr pard
    // F.trad := "(" || Expr.trad || ")"; F.tipo := Expr.tipo
    public Atributos F(){
        if(token.tipo == Token.ID){         // es un id
            int fila, columna;
            fila = token.fila;
            columna = token.columna;
            String lexema = token.lexema;   // cogemos el lexema y emparejamos
            emparejar(Token.ID);

            // buscamos el simbolo del id
            Simbolo id = tsActual.get(lexema);
            if(id == null){             // si no está declarado lanzamos error semántico
                lanzarErrorSemantico(fila, columna, "en '" + lexema + "', no ha sido declarado");
            }

            // solo puede ser de tipo real o entero
            if(id.tipo != Simbolo.REAL && id.tipo != Simbolo.ENTERO){
                lanzarErrorSemantico(fila, columna, "en '" + lexema + "', debe ser de tipo entero o real");
            }

            // devolvemos el simbolo traducido con su tipo
            // podria ser Atributos("arg_Main_main_a", Simbolo.ENTERO)
            return new Atributos(id.nomtrad, id.tipo);
        }else if(token.tipo == Token.NUMENTERO){           // es un entero
            // F −→ numentero
            String lexema = token.lexema;
            emparejar(Token.NUMENTERO);
            // devolvemos el lexema y el tipo
            // podria ser Atributos("1", Simbolo.ENTERO)
            return new Atributos(lexema, Simbolo.ENTERO);
        }else if(token.tipo == Token.NUMREAL){              // es un real
            // F −→ numreal
            String lexema = token.lexema;
            emparejar(Token.NUMREAL);
            // devolvemos el lexema y el tipo
            // podria ser Atributos("2.5", Simbolo.REAL)
            return new Atributos(lexema, Simbolo.REAL);
        }else{
            // F −→ pari Expr pard
            emparejar(Token.PARI);      // consumimos el token (
            Atributos atributosExpr = Expr();   // cogemos la expresion
            emparejar(Token.PARD);      // consumimos )
            String traduccion = "(" + atributosExpr.trad + ")";
            return new Atributos(traduccion, atributosExpr.tipo);
        }
    }
}