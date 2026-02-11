import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

/*
    REPRESENTACIÓN DE LOS SÍMBOLOS DE TRANSICIÓN  |
    -----------------------------------------------

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

    static final int TOTAL_ESTADOS = 32;             // total de estados en el DT
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
        // Representamos la transición como: diagrama[ESTADO_ACTUAL][SÍMBOLO] = ESTADO_DESTINO
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

        diagrama[0][16] = 26; diagrama[26][16] = 26; diagrama[26][19] = 27;     // id

        diagrama[0][17] = 28; diagrama[28][17] = 28; diagrama[28][19] = 29; diagrama[28][18] = 30;
        diagrama[30][19] = 33; diagrama[30][17] = 31; diagrama[31][17] = 31; diagrama[31][19] = 32;
    }

    public char leerCaracter(){
        char currentChar;
        try{
            currentChar = (char) fichero.readByte();
            return currentChar;
        }catch(EOFException e){
            return Token.EOF;
        }catch(IOException e){
            e.printStackTrace();
        }
        return ' ';
    }

    public int delta(int estadoActual, char simbolo){
        switch (estadoActual){
            case 0:
                if(simbolo == '(') return 1;
                else if(simbolo == ')') return 2;
                else if(simbolo == ':') return 3;
                else if(simbolo == '{') return 4;
                else if(simbolo == '}') return 5;
                else if(simbolo == '=') return 6;
                else if(simbolo == ';') return 9;
                else if(simbolo == '<') return 10;
                else if(simbolo == '>') return 13;
                else if(simbolo == '!') return 16;
                else if(simbolo == '+') return 18;
                else if(simbolo == '-') return 19;
                else if(simbolo == '*') return 20;
                else if(simbolo == '/') return 21;
                else if(('a' <= simbolo && simbolo <= 'z') && 
                        ('A' <= simbolo && simbolo <= 'Z') && 
                        ('0' <= simbolo && simbolo <= '9')) return 26;
                else if(('0' <= simbolo && simbolo <= '9')) return 28;
                else return 600;    // cualquier otra cosa es error pero hay que verlo
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return -1;
            case 6:
                if(simbolo == '=') return 7;
                else return 8;
            case 7:
            case 8:
            case 9:
                return -1;
            case 10:
                if(simbolo == '=') return 12;
                else if(simbolo != '=') return 11;
                break;
            case 11:
            case 12:
                return -1;
            case 13:
                if(simbolo != '>') return 14;
                else if(simbolo == '=') return 15;
                break;
            case 14:
            case 15:
                return -1;
            case 16:
                if(simbolo == '=') return 17;
            case 17:
                return -1;
            case 18:
            case 19:
            case 20:
                return -1;
            case 21:
                if(simbolo == '*') return 23;
                else if(simbolo != '*') return 22;
            case 22:
                return -1;
            case 23:
                if(simbolo == '*') return 24;
                else if(simbolo != '*') return 23;
            case 24:
                if(simbolo == '*') return 24;
                else if(simbolo != '*') return 23;
                else if(simbolo == '/') return 25;
            case 25:
                return 0;
            case 26:
                if(('a' <= simbolo && simbolo <= 'z') && 
                ('A' <= simbolo && simbolo <= 'Z') && 
                ('0' <= simbolo && simbolo <= '9')) return 26;
                else return 27;
            case 27:
                return -1;
            case 28:
                if('0' <= simbolo && simbolo <= '9') return 27;
                else if(simbolo == '.') return 30;
                else return 29;
            case 29:
                return -1;
            case 30:
                if('0' <= simbolo && simbolo <= '9') return 31;
                else return 33;
            case 31:
                if('0' <= simbolo && simbolo <= '9') return 31;
                else return 32;
            case 32:
            case 33:
                return -1;
            default:
                break;
        }
    }
}