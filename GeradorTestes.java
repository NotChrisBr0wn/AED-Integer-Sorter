import java.io.*;
import java.util.Random;

public class GeradorTestes {
    public static void main(String[] args) {
        int[] tamanhos = {1000, 5000, 10000, 100000, 250000, 500000};
        Random rand = new Random();

        for (int n : tamanhos) {
            String nomeBin = "teste_" + n + ".bin";
            String nomeTxt = "teste_" + n + ".txt";

            // Gerar Ficheiro Binário (.bin)
            try (DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(nomeBin)))) {
                for (int i = 0; i < n; i++) {
                    dos.writeInt(rand.nextInt(1000000));
                }
                System.out.println("Gerado: " + nomeBin);
            } catch (IOException e) {
                System.err.println("Erro ao gerar " + nomeBin);
            }

            // Gerar Ficheiro de Texto (.txt)
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeTxt))) {
                for (int i = 0; i < n; i++) {
                    writer.write(rand.nextInt(1000000) + "\n");
                }
                System.out.println("Gerado: " + nomeTxt);
            } catch (IOException e) {
                System.err.println("Erro ao gerar " + nomeTxt);
            }
        }
    }
}