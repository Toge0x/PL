import java.io.RandomAccessFile;
import java.util.Arrays;

public class AnalizadorLexico{

    static final int TOTAL_ESTADOS = 0;        // total de estados en el DT
    int fila = 1;
    int columna = 1;
    int[][] diagrama;

    RandomAccessFile fichero;

    AnalizadorLexico(RandomAccessFile fich){
        inicializar_diagrama_transiciones();
        fichero = fich;
        /*
        .
        .
        .
        */
    }

    public void inicializar_diagrama_transiciones(){
        // inicializamos el diagrama de manera que i,j representan el paso
        // del estado i al j con una debida condición en el switch
        diagrama = new int[TOTAL_ESTADOS][TOTAL_ESTADOS];
        for(int i = 0; i < TOTAL_ESTADOS; i++)
            Arrays.fill(diagrama[i], -1);           // -1 por el system.exit

        // ponemos a mano la condición del paso de un estado a otro
        diagrama[0][1] = 1;     // aparece un '('
        diagrama[0][2] = 2;     // aparece un ')'
        diagrama[0][3] = 3;     // aparece un ':'
        diagrama[0][4] = 4;     // aparece un '{'
        diagrama[0][5] = 5;     // aparece un '}'
        diagrama[0][6] = 6;     // aparece un '='
        diagrama[0][7] = 7;     // aparece un ';'
        diagrama[0][8] = 8;     // aparece un '<'

        //diagrama[8][100000] = ;     // aparece un '<='


        /*
        .
        .
        .
        .
        */
    }

    


}