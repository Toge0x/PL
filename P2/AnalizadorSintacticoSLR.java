import java.util.ArrayList;
import java.util.Stack;

class AnalizadorSintacticoSLR{
    final int NUM_TERMINALES = 14;
    final int NUM_NO_TERMINALES = 9;
    final int ACEPTAR = 1000;
    final int TOTAL_ESTADOS = 39;

    static final int
        NT_S    = 0,
        NT_M    = 1,
        NT_Fun  = 2,
        NT_DV   = 3,
        NT_Tipo = 4,
        NT_Cod  = 5,
        NT_I    = 6,
        NT_E    = 7,
        NT_F    = 8;

    AnalizadorLexico al;
    Token token;

    int[][] noTerminales;
    int[][] terminales;

    ArrayList<Integer> reglasAplicadas = new ArrayList<>();    // mejor arraylist para imprimir en orden inverso
    /*  como el analizador SLR obtiene la inversa de una derivación
        por la derecha de la cadena de entrada, es necesario almacenar
        las reglas por las que se reduce en un vector y luego imprimir
        el vector en orden inverso para que salga una derivación correcta*/

    Stack<Integer> pila = new Stack<>();

    public AnalizadorSintacticoSLR(AnalizadorLexico al){
        this.al = al;
        this.terminales = new int[TOTAL_ESTADOS][NUM_TERMINALES];      // tablas de ambas partes de la tabla
        this.noTerminales = new int[TOTAL_ESTADOS][NUM_NO_TERMINALES];
        this.inicializarTablaAnalisis();
    }

    public void analizar(){
        pila.push(0);                      // empezamos en estado 0 (paso 2)
        token = al.siguienteToken();            // leemos primer token (paso 3)
        
        while(true){                            // bucle infinito hasta aceptar o error (paso 4)
            int estado = pila.peek();           // cogemos la cima de la pila (paso 5)
            int accion = terminales[estado][token.tipo];
            if(accion == ACEPTAR){              // cadena válida (paso 12)
                imprimirReglasAplicadas();      // hay que ponerlo antes porque ACEPTAR = 1000 > 0
                return;
            }else if(accion > 0){                                 // caso shift (paso 6)
                pila.push(accion);                  // apilamos el estado (paso 7)
                token = al.siguienteToken();        // leemos siguiente (paso 8)
            }else if(accion < 0){                           // caso reducción (paso 9)
                int regla = -accion;
                reglasAplicadas.add(regla);
                this.reducir(regla);                     // reducir la regla (paso 10)
            }else{
                lanzarErrorSintactico();            // lanzar error (paso 14)
            }
        }
    }

    public int longitudRegla(int regla){
        switch(regla){
            case 1: return 5;       // S −→ class id lbra M rbra
            case 2: return 2;       // M −→ Fun M
            case 3: return 2;       // M −→ S M
            case 4: return 0;       // M −→ ε
            case 5: return 6;       // Fun −→ fun id lbra M Cod rbra
            case 6: return 2;       // DV −→ Tipo id
            case 7: return 1;       // Tipo −→ int
            case 8: return 1;       // Tipo −→ float
            case 9: return 3;       // Cod −→ Cod pyc I
            case 10: return 1;      // Cod −→ I
            case 11: return 1;      // I −→ DV
            case 12: return 3;      // I −→ lbra Cod rbra
            case 13: return 3;      // I −→ id asig E
            case 14: return 2;      // I −→ print E
            case 15: return 3;      // E −→ E opas F
            case 16: return 1;      // E −→ F
            case 17: return 1;      // F −→ numentero
            case 18: return 1;      // F −→ numreal
            case 19: return 1;      // F −→ id
        }
        return -1;                  // error
    }

    int cabezaRegla(int regla){     // obtener el tipo de la cabeza de la regla
        switch(regla){
            case 1: return NT_S;
            case 2: case 3: case 4: return NT_M;
            case 5: return NT_Fun;
            case 6: return NT_DV;
            case 7: case 8: return NT_Tipo;
            case 9: case 10: return NT_Cod;
            case 11: case 12: case 13: case 14: return NT_I;
            case 15: case 16: return NT_E;
            case 17: case 18: case 19: return NT_F;
        }
        return -1;
    }

    public void reducir(int regla){
        int longitud = longitudRegla(regla);
        int cabeza = cabezaRegla(regla);
        int i = 0;

        while(i < longitud){
            pila.pop();         // popeamos |σ| veces (paso 10)
            i++;
        }
        
        int estadoActual = pila.peek();
        int nuevoEstado = noTerminales[estadoActual][cabeza];
        pila.push(nuevoEstado);     // metemos el nuevo estado (paso 11)
    }

    public void imprimirReglasAplicadas(){      // lo recorremos al revés
        int i = reglasAplicadas.size() - 1;
        StringBuilder salida = new StringBuilder();
        while(i >= 0){
            salida.append(reglasAplicadas.get(i)).append(" ");
            i--;
        }
        //salida.deleteCharAt(salida.length() - 1);   // quitar el último " " no hace falta quitarlo
        System.out.print(salida);
    }

    public void lanzarErrorSintactico(){
        if(token.tipo == Token.EOF){
            System.err.print("Error sintactico: encontrado fin de fichero, esperaba ");
        }else{
            System.err.print("Error sintactico (" + token.fila + "," + token.columna + "): encontrado '" + token.lexema + "', esperaba ");
        }
        
        int estado = pila.peek();
        int i = 0;
        while(i < NUM_TERMINALES){
            if(terminales[estado][i] != 0){
                System.err.print(Token.nombreToken.get(i) + " ");   // cogemos los tokens esperados de la tabla de la izquierda
            }
            i++;
        }
        System.exit(-1);
    }

    public void inicializarTablaAnalisis(){
        /*  ------------------------------
            INICIALIZACIÓN DE LOS ESTADOS
            ------------------------------
        */

        // Estado 0
        // Operación Shift -> 0 + (class) −→ 2
        // -----------------------------------------
        // No Terminal GOTO S −→ 1
        terminales[0][Token.CLASS] = 2;
        noTerminales[0][NT_S] = 1;

        // Estado 1
        // Operación ACEPTAR -> 1 + ($) −→ ACEPTAR
        terminales[1][Token.EOF] = ACEPTAR;

        // Estado 2
        // Operación Shift -> 2 + (id) −→ 3
        terminales[2][Token.ID] = 3;

        // Estado 3
        // Operación Shift -> 3 + (lbra) −→ 4
        terminales[3][Token.LBRA] = 4;

        // Estado 4
        // Operación Shift -> 4 + (class) −→ 2
        // Operación Shift -> 4 + (fun) −→ 8
        // -----------------------------------------
        // Operación Reduce -> 4 + (id) −→ 4
        // Operación Reduce -> 4 + (lbra) −→ 4
        // Operación Reduce -> 4 + (rbra) −→ 4
        // Operación Reduce -> 4 + (int) −→ 4
        // Operación Reduce -> 4 + (float) −→ 4
        // Operación Reduce -> 4 + (print) −→ 4
        // -----------------------------------------
        // No Terminal GOTO S −→ 7
        // No Terminal GOTO M −→ 5
        // No Terminal GOTO Fun −→ 6
        terminales[4][Token.CLASS] = 2;
        terminales[4][Token.FUN] = 8;
        terminales[4][Token.ID] = -4;
        terminales[4][Token.LBRA] = -4;
        terminales[4][Token.RBRA] = -4;
        terminales[4][Token.INT] = -4;
        terminales[4][Token.FLOAT] = -4;
        terminales[4][Token.PRINT] = -4;
        noTerminales[4][NT_S] = 7;
        noTerminales[4][NT_M] = 5;
        noTerminales[4][NT_Fun] = 6;

        // Estado 5
        // Operación Shift -> 5 + (rbra) −→ 9
        terminales[5][Token.RBRA] = 9;

        // Estado 6
        // Operación Shift -> 6 + (class) −→ 2
        // Operación Shift -> 6 + (fun) −→ 8
        // -----------------------------------------
        // Operación Reduce -> 6 + (id) −→ 4
        // Operación Reduce -> 6 + (lbra) −→ 4
        // Operación Reduce -> 6 + (rbra) −→ 4
        // Operación Reduce -> 6 + (int) −→ 4
        // Operación Reduce -> 6 + (float) −→ 4
        // Operación Reduce -> 6 + (print) −→ 4
        // -----------------------------------------
        // No Terminal GOTO S −→ 7
        // No Terminal GOTO M −→ 10
        // No Terminal GOTO Fun −→ 6
        terminales[6][Token.CLASS] = 2;
        terminales[6][Token.FUN] = 8;
        terminales[6][Token.ID] = -4;
        terminales[6][Token.LBRA] = -4;
        terminales[6][Token.RBRA] = -4;
        terminales[6][Token.INT] = -4;
        terminales[6][Token.FLOAT] = -4;
        terminales[6][Token.PRINT] = -4;
        noTerminales[6][NT_S] = 7;
        noTerminales[6][NT_M] = 10;
        noTerminales[6][NT_Fun] = 6;

        // Estado 7
        // Operación Shift -> 7 + (class) −→ 2
        // Operación Shift -> 7 + (fun) −→ 8
        // -----------------------------------------
        // Operación Reduce -> 7 + (id) −→ 4
        // Operación Reduce -> 7 + (lbra) −→ 4
        // Operación Reduce -> 7 + (rbra) −→ 4
        // Operación Reduce -> 7 + (int) −→ 4
        // Operación Reduce -> 7 + (float) −→ 4
        // Operación Reduce -> 7 + (print) −→ 4
        // -----------------------------------------
        // No Terminal GOTO S −→ 7
        // No Terminal GOTO M −→ 11
        // No Terminal GOTO Fun −→ 6
        terminales[7][Token.CLASS] = 2;
        terminales[7][Token.FUN] = 8;
        terminales[7][Token.ID] = -4;
        terminales[7][Token.LBRA] = -4;
        terminales[7][Token.RBRA] = -4;
        terminales[7][Token.INT] = -4;
        terminales[7][Token.FLOAT] = -4;
        terminales[7][Token.PRINT] = -4;
        noTerminales[7][NT_S] = 7;
        noTerminales[7][NT_M] = 11;
        noTerminales[7][NT_Fun] = 6;

        // Estado 8
        // Operación Shift -> 8 + (id) −→ 12
        terminales[8][Token.ID] = 12;

        // Estado 9
        // Operación Reduce -> 9 + (class) −→ 1
        // Operación Reduce -> 9 + (id) −→ 1
        // Operación Reduce -> 9 + (lbra) −→ 1
        // Operación Reduce -> 9 + (rbra) −→ 1
        // Operación Reduce -> 9 + (fun) −→ 1
        // Operación Reduce -> 9 + (int) −→ 1
        // Operación Reduce -> 9 + (float) −→ 1
        // Operación Reduce -> 9 + (print) −→ 1
        // Operación Reduce -> 9 + ($) −→ 1
        terminales[9][Token.CLASS] = -1;
        terminales[9][Token.ID] = -1;
        terminales[9][Token.LBRA] = -1;
        terminales[9][Token.RBRA] = -1;
        terminales[9][Token.FUN] = -1;
        terminales[9][Token.INT] = -1;
        terminales[9][Token.FLOAT] = -1;
        terminales[9][Token.PRINT] = -1;
        terminales[9][Token.EOF] = -1;

        // Estado 10
        // Operación Reduce -> 10 + (id) −→ 2
        // Operación Reduce -> 10 + (lbra) −→ 2
        // Operación Reduce -> 10 + (rbra) −→ 2
        // Operación Reduce -> 10 + (int) −→ 2
        // Operación Reduce -> 10 + (float) −→ 2
        // Operación Reduce -> 10 + (print) −→ 2
        terminales[10][Token.ID] = -2;
        terminales[10][Token.LBRA] = -2;
        terminales[10][Token.RBRA] = -2;
        terminales[10][Token.INT] = -2;
        terminales[10][Token.FLOAT] = -2;
        terminales[10][Token.PRINT] = -2;

        // Estado 11
        // Operación Reduce -> 11 + (id) −→ 3
        // Operación Reduce -> 11 + (lbra) −→ 3
        // Operación Reduce -> 11 + (rbra) −→ 3
        // Operación Reduce -> 11 + (int) −→ 3
        // Operación Reduce -> 11 + (float) −→ 3
        // Operación Reduce -> 11 + (print) −→ 3
        terminales[11][Token.ID] = -3;
        terminales[11][Token.LBRA] = -3;
        terminales[11][Token.RBRA] = -3;
        terminales[11][Token.INT] = -3;
        terminales[11][Token.FLOAT] = -3;
        terminales[11][Token.PRINT] = -3;

        // Estado 12
        // Operación Shift -> 12 + (lbra) −→ 13
        terminales[12][Token.LBRA] = 13;

        // Estado 13
        // Operación Shift -> 13 + (class) −→ 2
        // Operación Shift -> 13 + (fun) −→ 8
        // -----------------------------------------
        // Operación Reduce -> 13 + (id) −→ 4
        // Operación Reduce -> 13 + (lbra) −→ 4
        // Operación Reduce -> 13 + (rbra) −→ 4
        // Operación Reduce -> 13 + (int) −→ 4
        // Operación Reduce -> 13 + (float) −→ 4
        // Operación Reduce -> 13 + (print) −→ 4
        // -----------------------------------------
        // No Terminal GOTO S −→ 7
        // No Terminal GOTO M −→ 14
        // No Terminal GOTO Fun −→ 6
        terminales[13][Token.CLASS] = 2;
        terminales[13][Token.FUN] = 8;
        terminales[13][Token.ID] = -4;
        terminales[13][Token.LBRA] = -4;
        terminales[13][Token.RBRA] = -4;
        terminales[13][Token.INT] = -4;
        terminales[13][Token.FLOAT] = -4;
        terminales[13][Token.PRINT] = -4;
        noTerminales[13][NT_S] = 7;
        noTerminales[13][NT_M] = 14;
        noTerminales[13][NT_Fun] = 6;

        // Estado 14
        // Operación Shift -> 14 + (id) −→ 19
        // Operación Shift -> 14 + (lbra) −→ 18
        // Operación Shift -> 14 + (int) −→ 22
        // Operación Shift -> 14 + (float) −→ 23
        // Operación Shift -> 14 + (print) −→ 20
        // -----------------------------------------
        // No Terminal GOTO DV −→ 17
        // No Terminal GOTO Tipo −→ 21
        // No Terminal GOTO Cod −→ 15
        // No Terminal GOTO I −→ 16
        terminales[14][Token.ID] = 19;
        terminales[14][Token.LBRA] = 18;
        terminales[14][Token.INT] = 22;
        terminales[14][Token.FLOAT] = 23;
        terminales[14][Token.PRINT] = 20;
        noTerminales[14][NT_DV] = 17;
        noTerminales[14][NT_Tipo] = 21;
        noTerminales[14][NT_Cod] = 15;
        noTerminales[14][NT_I] = 16;

        // Estado 15
        // Operación Shift -> 15 + (rbra) −→ 24
        // Operación Shift -> 15 + (pyc) −→ 25
        terminales[15][Token.RBRA] = 24;
        terminales[15][Token.PYC] = 25;

        // Estado 16
        // Operación Reduce -> 16 + (rbra) −→ 10
        // Operación Reduce -> 16 + (pyc) −→ 10
        terminales[16][Token.RBRA] = -10;
        terminales[16][Token.PYC] = -10;

        // Estado 17
        // Operación Reduce -> 17 + (rbra) −→ 11
        // Operación Reduce -> 17 + (pyc) −→ 11
        terminales[17][Token.RBRA] = -11;
        terminales[17][Token.PYC] = -11;

        // Estado 18
        // Operación Shift -> 18 + (id) −→ 19
        // Operación Shift -> 18 + (lbra) −→ 18
        // Operación Shift -> 18 + (int) −→ 22
        // Operación Shift -> 18 + (float) −→ 23
        // Operación Shift -> 18 + (print) −→ 20
        // -----------------------------------------
        // No Terminal GOTO DV −→ 17
        // No Terminal GOTO Tipo −→ 21
        // No Terminal GOTO Cod −→ 26
        // No Terminal GOTO I −→ 16
        terminales[18][Token.ID] = 19;
        terminales[18][Token.LBRA] = 18;
        terminales[18][Token.INT] = 22;
        terminales[18][Token.FLOAT] = 23;
        terminales[18][Token.PRINT] = 20;
        noTerminales[18][NT_DV] = 17;
        noTerminales[18][NT_Tipo] = 21;
        noTerminales[18][NT_Cod] = 26;
        noTerminales[18][NT_I] = 16;

        // Estado 19
        // Operación Shift -> 19 + (asig) −→ 27
        terminales[19][Token.ASIG] = 27;

        // Estado 20
        // Operación Shift -> 20 + (id) −→ 32
        // Operación Shift -> 20 + (numentero) −→ 30
        // Operación Shift -> 20 + (numreal) −→ 31
        // -----------------------------------------
        // No Terminal GOTO E −→ 28
        // No Terminal GOTO F −→ 29
        terminales[20][Token.ID] = 32;
        terminales[20][Token.NUMENTERO] = 30;
        terminales[20][Token.NUMREAL] = 31;
        noTerminales[20][NT_E] = 28;
        noTerminales[20][NT_F] = 29;

        // Estado 21
        // Operación Shift -> 21 + (id) −→ 33
        terminales[21][Token.ID] = 33;

        // Estado 22
        // Operación Reduce -> 22 + (id) −→ 7
        terminales[22][Token.ID] = -7;

        // Estado 23
        // Operación Reduce -> 23 + (id) −→ 8
        terminales[23][Token.ID] = -8;

        // Estado 24
        // Operación Reduce -> 24 + (class) −→ 5
        // Operación Reduce -> 24 + (id) −→ 5
        // Operación Reduce -> 24 + (lbra) −→ 5
        // Operación Reduce -> 24 + (rbra) −→ 5
        // Operación Reduce -> 24 + (fun) −→ 5
        // Operación Reduce -> 24 + (int) −→ 5
        // Operación Reduce -> 24 + (float) −→ 5
        // Operación Reduce -> 24 + (print) −→ 5
        terminales[24][Token.CLASS] = -5;
        terminales[24][Token.ID] = -5;
        terminales[24][Token.LBRA] = -5;
        terminales[24][Token.RBRA] = -5;
        terminales[24][Token.FUN] = -5;
        terminales[24][Token.INT] = -5;
        terminales[24][Token.FLOAT] = -5;
        terminales[24][Token.PRINT] = -5;
        
        // Estado 25
        // Operación Shift -> 25 + (id) −→ 19
        // Operación Shift -> 25 + (lbra) −→ 18
        // Operación Shift -> 25 + (int) −→ 22
        // Operación Shift -> 25 + (float) −→ 23
        // Operación Shift -> 25 + (print) −→ 20
        // -----------------------------------------
        // No Terminal GOTO DV −→ 17
        // No Terminal GOTO Tipo −→ 21
        // No Terminal GOTO I −→ 34
        terminales[25][Token.ID] = 19;
        terminales[25][Token.LBRA] = 18;
        terminales[25][Token.INT] = 22;
        terminales[25][Token.FLOAT] = 23;
        terminales[25][Token.PRINT] = 20;
        noTerminales[25][NT_DV] = 17;
        noTerminales[25][NT_Tipo] = 21;
        noTerminales[25][NT_I] = 34;

        // Estado 26
        // Operación Shift -> 26 + (rbra) −→ 35
        // Operación Shift -> 26 + (pyc) −→ 25
        terminales[26][Token.RBRA] = 35;
        terminales[26][Token.PYC] = 25;

        // Estado 27
        // Operación Shift -> 27 + (id) −→ 32
        // Operación Shift -> 27 + (numentero) −→ 30
        // Operación Shift -> 27 + (numreal) −→ 31
        // -----------------------------------------
        // No Terminal GOTO E −→ 36
        // No Terminal GOTO F −→ 29
        terminales[27][Token.ID] = 32;
        terminales[27][Token.NUMENTERO] = 30;
        terminales[27][Token.NUMREAL] = 31;
        noTerminales[27][NT_E] = 36;
        noTerminales[27][NT_F] = 29;

        // Estado 28
        // Operación Shift -> 28 + (opas) −→ 37
        // -----------------------------------------
        // Operación Reduce -> 28 + (rbra) −→ 14
        // Operación Reduce -> 28 + (pyc) −→ 14
        terminales[28][Token.OPAS] = 37;
        terminales[28][Token.RBRA] = -14;
        terminales[28][Token.PYC] = -14;

        // Estado 29
        // Operación Reduce -> 29 + (rbra) −→ 16
        // Operación Reduce -> 29 + (pyc) −→ 16
        // Operación Reduce -> 29 + (opas) −→ 16
        terminales[29][Token.RBRA] = -16;
        terminales[29][Token.PYC] = -16;
        terminales[29][Token.OPAS] = -16;

        // Estado 30
        // Operación Reduce -> 30 + (rbra) −→ 17
        // Operación Reduce -> 30 + (pyc) −→ 17
        // Operación Reduce -> 30 + (opas) −→ 17
        terminales[30][Token.RBRA] = -17;
        terminales[30][Token.PYC] = -17;
        terminales[30][Token.OPAS] = -17;

        // Estado 31
        // Operación Reduce -> 31 + (rbra) −→ 18
        // Operación Reduce -> 31 + (pyc) −→ 18
        // Operación Reduce -> 31 + (opas) −→ 18
        terminales[31][Token.RBRA] = -18;
        terminales[31][Token.PYC] = -18;
        terminales[31][Token.OPAS] = -18;
        
        // Estado 32
        // Operación Reduce -> 32 + (rbra) −→ 19
        // Operación Reduce -> 32 + (pyc) −→ 19
        // Operación Reduce -> 32 + (opas) −→ 19
        terminales[32][Token.RBRA] = -19;
        terminales[32][Token.PYC] = -19;
        terminales[32][Token.OPAS] = -19;

        // Estado 33
        // Operación Reduce -> 33 + (rbra) −→ 6
        // Operación Reduce -> 33 + (pyc) −→ 6
        terminales[33][Token.RBRA] = -6;
        terminales[33][Token.PYC] = -6;

        // Estado 34
        // Operación Reduce -> 34 + (rbra) −→ 9
        // Operación Reduce -> 34 + (pyc) −→ 9
        terminales[34][Token.RBRA] = -9;
        terminales[34][Token.PYC] = -9;

        // Estado 35
        // Operación Reduce -> 35 + (rbra) −→ 12
        // Operación Reduce -> 35 + (pyc) −→ 12
        terminales[35][Token.RBRA] = -12;
        terminales[35][Token.PYC] = -12;

        // Estado 36
        // Operación Shift -> 36 + (opas) −→ 37
        // -----------------------------------------
        // Operación Reduce -> 36 + (rbra) −→ 13
        // Operación Reduce -> 36 + (pyc) −→ 13
        terminales[36][Token.OPAS] = 37;
        terminales[36][Token.RBRA] = -13;
        terminales[36][Token.PYC] = -13;

        // Estado 37
        // Operación Shift -> 37 + (id) −→ 32
        // Operación Shift -> 37 + (numentero) −→ 30
        // Operación Shift -> 37 + (numreal) −→ 31
        // -----------------------------------------
        // No Terminal GOTO F −→ 38
        terminales[37][Token.ID] = 32;
        terminales[37][Token.NUMENTERO] = 30;
        terminales[37][Token.NUMREAL] = 31;
        noTerminales[37][NT_F] = 38;

        // Estado 38
        // Operación Reduce -> 38 + (rbra) −→ 15
        // Operación Reduce -> 38 + (pyc) −→ 15
        // Operación Reduce -> 38 + (opas) −→ 15
        terminales[38][Token.RBRA] = -15;
        terminales[38][Token.PYC] = -15;
        terminales[38][Token.OPAS] = -15;
    }
}