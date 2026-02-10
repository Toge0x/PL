import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;

/*
    REPRESENTACIÓN DE LOS CARACTERES DE TRANSICIÓN  |
    -------------------------------------------------

    '(' = 1,
    ')' = 2,
    ':' = 3,
    '{' = 4,
    '}' = 5,
    '=' = 6,
    ';' = 7,
    '<' = 8,
    '>' = 9,
    '!' = 10,
    '+' = 11,
    '-' = 12,
    '*' = 13,
    '/' = 14,
    'a-zA-Z' = 15,
    'a-zA-Z0-9' = 16,
    '0-9' = 17,
    '.' = 18,
    'OTRO' = 19

*/

public class AnalizadorLexico{

    static final int TOTAL_ESTADOS = 0;             // total de estados en el DT
    static final int TOTAL_TRANSICIONES = 19;       // total de caracteres que hacen transicionar al menos 1 vez
    int fila = 1;
    int columna = 1;
    int[][] diagrama;

    RandomAccessFile fichero;

    AnalizadorLexico(RandomAccessFile fich){
        inicializar_diagrama_transiciones();
        fichero = fich;
        // leer fichero secuencialmente
    }

    public void inicializar_diagrama_transiciones(){
        for(int i = 0; i < TOTAL_ESTADOS; i++){     // rellenamos la matriz con -1
            Arrays.fill(diagrama[i], -1);
        }
        // Representamos la transición como: diagrama[ESTADO_ACTUAL][CARACTER] = ESTADO_DESTINO
        diagrama[0][1] = 1;
        diagrama[0][2] = 2;
        diagrama[0][3] = 3;
        diagrama[0][4] = 4;
        diagrama[0][5] = 5;

        diagrama[0][6] = 6; diagrama[6][6] = 7; diagrama[6][19] = 8;

        diagrama[0][7] = 9;
        diagrama[0][8] = 10; diagrama[10][19] = 11; diagrama[10][6] = 12;
        diagrama[0][9] = 13; diagrama[13][19] = 14; diagrama[13][6] = 15;
        diagrama[0][10] = 16; diagrama[16][6] = 17;

        diagrama[0][11] = 18;               // '+'
        diagrama[0][12] = 19;               // '-'  TENER EN CUENTA QUE HE AUMENTADO EN 1 LOS ESTADOS
        diagrama[0][13] = 20;               // '*'

        diagrama[0][14] = 21; diagrama[21][19] = 22; diagrama[21][13] = 23; diagrama[23][19] = 23;
        diagrama[23][13] = 24; diagrama[24][13] = 24; diagrama[24][19] = 23; diagrama[24][14] = 25; diagrama[25][19] = 0;

    }

    


}