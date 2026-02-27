import java.io.RandomAccessFile;

public class DebugLexico {

    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Uso: java DebugLexico <fichero>");
            return;
        }

        try {

            RandomAccessFile entrada =
                    new RandomAccessFile(args[0], "r");

            System.out.println("=== CONTENIDO BYTE A BYTE ===");

            entrada.seek(0);

            System.out.println("\n=== ANALISIS LEXICO ===");

            AnalizadorLexico al =
                    new AnalizadorLexico(entrada);

            Token t;

            do {
                t = al.siguienteToken();

                System.out.println(
                        "TOKEN -> " +
                        Token.nombreToken.get(t.tipo) +
                        " | lexema: \"" + t.lexema + "\""
                );

                //Thread.sleep(2000);

            } while (t.tipo != Token.EOF);
            

            entrada.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}