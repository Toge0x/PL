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

    Stack<Integer> pila = new Stack<>();

    public AnalizadorSintacticoSLR(AnalizadorLexico al){
        this.al = al;
    }

    public void inicializarTablaAnalisis(){
        noTerminales = new int[TOTAL_ESTADOS][NUM_NO_TERMINALES];
        terminales = new int[TOTAL_ESTADOS][NUM_TERMINALES];

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
        // Operación Shift -> 3 + (rbra) −→ 4
        terminales[3][Token.RBRA] = 4;

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
        // Operación Shift -> 8 + (lbra) −→ 12
        terminales[8][Token.LBRA] = 12;

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
        terminales[13][Token.ID] = 19;
        terminales[13][Token.LBRA] = 18;
        terminales[13][Token.INT] = 22;
        terminales[13][Token.FLOAT] = 23;
        terminales[13][Token.PRINT] = 20;
        noTerminales[13][NT_DV] = 17;
        noTerminales[13][NT_Tipo] = 21;
        noTerminales[13][NT_Cod] = 15;
        noTerminales[13][NT_I] = 16;
    }
}