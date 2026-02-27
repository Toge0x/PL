import java.io.EOFException;
import java.io.IOException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class AnalizadorLexico{

    int estado;
    int fila;
    int columna;

    RandomAccessFile fichero;

    AnalizadorLexico(RandomAccessFile fich){
        this.fichero = fich;
        this.estado = 0;
        this.fila = 1;
        this.columna = 0;
    }

    private boolean esEstadoFinal(int estado){
        switch(estado){
            case 1: case 2: case 3: case 4: case 5:
            case 7: case 8: case 9: case 11: case 12:
            case 14: case 15: case 17: case 18: case 19:
            case 20: case 22: case 27: case 29: case 32: case 33:
                return true;
            default:
                return false;
        }
    }

    public char leerCaracter(){
        char currentChar;
        try{
            currentChar = (char) fichero.readByte();
            if(currentChar == '\n'){        // si salta de linea incrementamos fila y reseteamos columna
                this.fila++;
                this.columna = 0;
            }else{                          // si no es \n solo sumamos columna
                this.columna++;
            }
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
                else return -2;
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

    public void comprobar_palabras_reservadas(Token token){
        if(token.lexema.equals("class")){
            token.tipo = Token.CLASS;        // tipo CLASS
        }else if(token.lexema.equals("fun")){
            token.tipo = Token.FUN;        // tipo FUN
        }else if(token.lexema.equals("int")){
            token.tipo = Token.INT;        // tipo INT
        }else if(token.lexema.equals("float")){
            token.tipo = Token.FLOAT;        // tipo FLOAT
        }else if(token.lexema.equals("if")){
            token.tipo = Token.IF;        // tipo IF
        }else if(token.lexema.equals("else")){
            token.tipo = Token.ELSE;        // tipo ELSE
        }else if(token.lexema.equals("fi")){
            token.tipo = Token.FI;        // tipo FI
        }else if(token.lexema.equals("print")){
            token.tipo = Token.PRINT;        // tipo PRINT
        }
        // si no es ninguna será un simple id...
    }

    public void comprobar_tipo_token(int nuevo_estado, Token token){
        switch(nuevo_estado){
            case 1:
                token.tipo = Token.PARI;     // tipo PARI
                break;
            case 2:
                token.tipo = Token.PARD;     // tipo PARD
                break;
            case 3:
                token.tipo = Token.DOSP;     // tipo DOSP
                break;
            case 4:
                token.tipo = Token.LBRA;     // tipo LBRA
                break;
            case 5:
                token.tipo = Token.RBRA;     // tipo RBRA
                break;
            case 8:
                token.tipo = Token.ASIG;     // tipo ASIG
                break;
            case 9:
                token.tipo = Token.PYC;     // tipo PYC
                break;
            case 7:
            case 11:
            case 12:
            case 14:
            case 15:
                token.tipo = Token.OPREL;     // tipo OPREL
                break;
            case 18:
            case 19:
                token.tipo = Token.OPAS;     // tipo OPAS
                break;
            case 20:
            case 21:
                token.tipo = Token.OPMUL;     // tipo OPMUL
                break;
            case 27:
                token.tipo = Token.ID;    // tipo ID
                comprobar_palabras_reservadas(token);   // sabiendo que es id comprobamos reservadas
                break;
            case 29:
            case 33:
                token.tipo = Token.NUMENTERO;    // tipo NUMENTERO
                break;
            case 32:
                token.tipo = Token.NUMREAL;    // tipo NUMREAL
                break;
            default:
                break;
        }
    }

    public boolean necesitaRetroceder(int estado){
        switch(estado){
            case 8:     // =
            case 11:    // <
            case 14:    // >
            case 22:    // /
            case 27:    // id
            case 29:    // num entero
            case 32:    // num real
            case 33:    // caso especial decimal
                return true;
            default:
                return false;
        }
    }

    public Token siguienteToken(){
        String lexema = "";
        this.estado = 0;
        Token token = new Token();
        char simbolo;

        do{
            simbolo = leerCaracter();
            
            if(simbolo == Token.EOF){                   // si es fin de fichero salimos
                Token t = new Token();
                t.tipo = Token.EOF;
                t.lexema = "";
                return t;
            }

        }while(Character.isWhitespace(simbolo));        // limpiar posibles espacios delante

        while(true){
            if(simbolo == Token.EOF){               // intercepta EOF al hacer un comentario y terminar antes de llamar delta(x, EOF)
                Token t = new Token();
                t.tipo = Token.EOF;
                t.lexema = "";
                return t;
            }

            int siguiente = delta(this.estado, simbolo);        // aplicamos la transición

            if(siguiente == -2){        // tenemos error léxico
                System.err.println("Error lexico (" + fila + "," + columna + "): caracter '" + simbolo + "' incorrecto");   // lanzar error léxico
                System.exit(-1);
            }

            if(esEstadoFinal(siguiente)){
                if(simbolo != Token.EOF){
                    if(necesitaRetroceder(siguiente)){
                        try{
                            if(siguiente == 33){                                // caso doble lookahead **numentero
                                lexema = lexema.substring(0, lexema.length() - 1);      // eliminamos el '.'
                                fichero.seek(fichero.getFilePointer() - 2);     // retrocedemos 2 posiciones (caso numentero)
                                this.columna -= 2;
                            }else{
                                fichero.seek(fichero.getFilePointer() - 1);     // retrocedemos 1 posición
                                this.columna--;
                            }
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    }else{
                        lexema += simbolo;              // si no necesita retroceder añadimos el simbolo
                    }
                }
                token.lexema = lexema;
                comprobar_tipo_token(siguiente, token);     // rellenamos tipo y reservadas
                return token;
            }else{                  // no es final
                lexema += simbolo;
                this.estado = siguiente;    // actualizamos estados
                if(siguiente == 25){        // fin de comentario
                    lexema = "";
                    this.estado = 0;
                    do{                                     // borramos cualquier posible ' ', '/n' o '/t' despues de comentario
                        simbolo = leerCaracter();
                    }while(Character.isWhitespace(simbolo));
                    continue;
                }
                simbolo = leerCaracter();
            }
        }
    }
}