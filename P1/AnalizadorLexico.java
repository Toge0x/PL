import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class AnalizadorLexico{

    int estado;
    int fila = 1;
    int columna = 1;

    RandomAccessFile fichero;

    AnalizadorLexico(RandomAccessFile fich){
        fichero = fich;
        // leer fichero secuencialmente
    }

    private boolean esEstadoFinal(int estado){
        switch(estado){
            case 1: case 2: case 3: case 4: case 5:
            case 7: case 8: case 9:
            case 11: case 12:
            case 14: case 15:
            case 17:
            case 18: case 19: case 20:
            case 22:
            case 27:
            case 29:
            case 32:
                return true;
            default:
                return false;
        }
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
                else if(Character.isLetter(simbolo)) return 26;
                else if(Character.isDigit(simbolo)) return 28;
                else return -1;
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
                else return 11;
            case 11:
            case 12:
                return -1;
            case 13:
                if(simbolo == '=') return 15;
                else return 14;
            case 14:
            case 15:
                return -1;
            case 16:
                if(simbolo == '=') return 17;
                else return -1;
            case 17:
                return -1;
            case 18:
            case 19:
            case 20:
                return -1;
            case 21:
                if(simbolo == '*') return 23;
                else return 22;
            case 22:
                return -1;
            case 23:
                if(simbolo == '*') return 24;
                else return 23;
            case 24:
                if(simbolo == '*') return 24;
                else if(simbolo == '/') return 25;
                else return 23;
            case 25:
                return 0;
            case 26:
                if(Character.isLetter(simbolo) ||
                (Character.isDigit(simbolo))) return 26;
                else return 27;
            case 27:
                return -1;
            case 28:
                if(Character.isDigit(simbolo)) return 28;
                else if(simbolo == '.') return 30;
                else return 29;
            case 29:
                return -1;
            case 30:
                if(Character.isDigit(simbolo)) return 31;
                else return 33;
            case 31:
                if(Character.isDigit(simbolo)) return 31;
                else return 32;
            case 32:
            case 33:
                return -1;
            default:
                return -2;
        }
    }

    public Token siguienteToken(){
        String lexema;
        this.estado = 0;
        Token token = new Token();

        do{
            char simbolo = leerCaracter();
            if(simbolo == Token.EOF){
                return null;
            }
            int nuevo_estado = delta(estado, simbolo);
            
        }while(true);

    }
}