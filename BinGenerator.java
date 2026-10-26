import java.io.*;

public class BinGenerator {
    public static void main(String[] args) {
        int[] numeros = {1, 10, 30, 500, 20, 145, 322, 1000};
        try (DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("dados.dat")))) {
            for (int val : numeros) {
                dos.writeInt(val);
            }
            System.out.println("Ficheiro binario dados.dat gerado com sucesso!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}