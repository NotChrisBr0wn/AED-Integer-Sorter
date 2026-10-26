import java.io.*;

public class IntSorter {

    // Manipulação de Ficheiros
    public static class FileManager {

        // le ficheiro de acordo com a extensao
        public static int[] lerFicheiro(String nomeFicheiro, int n) throws IOException {
            if (nomeFicheiro.endsWith(".bin") || nomeFicheiro.endsWith(".dat")) {
                return lerBinario(nomeFicheiro, n);
            } else {
                return lerTexto(nomeFicheiro, n);
            }
        }

        // detetar extensao e guardar ficheiro ordenado
        public static void guardarFicheiro(String nomeFicheiro, int[] dados) throws IOException {
            String nomeSaida = "ordenado_" + nomeFicheiro;
            if (nomeFicheiro.endsWith(".bin") || nomeFicheiro.endsWith(".dat")) {
                guardarBinario(nomeSaida, dados);
            } else {
                guardarTexto(nomeSaida, dados);
            }
        }

        // --- Leitura e Escrita em Texto ---
        private static int[] lerTexto(String nomeFicheiro, int n) throws IOException {
            int[] dados = new int[n];
            try (BufferedReader reader = new BufferedReader(new FileReader(nomeFicheiro))) {
                String linha;
                int idx = 0;
                while ((linha = reader.readLine()) != null && idx < n) {
                    String[] tokens = linha.trim().split("\\s+");
                    for (String token : tokens) {
                        if (!token.isEmpty() && idx < n) {
                            dados[idx++] = Integer.parseInt(token);
                        }
                    }
                }
            }
            return dados;
        }

        private static void guardarTexto(String nomeFicheiro, int[] dados) throws IOException {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeFicheiro))) {
                for (int val : dados) {
                    writer.write(val + "\n");
                }
            }
        }

        // --- Leitura e Escrita em Binário ---
        private static int[] lerBinario(String nomeFicheiro, int n) throws IOException {
            int[] dados = new int[n];
            try (DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(nomeFicheiro)))) {
                for (int i = 0; i < n; i++) {
                    dados[i] = dis.readInt();
                }
            }
            return dados;
        }

        private static void guardarBinario(String nomeFicheiro, int[] dados) throws IOException {
            try (DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(nomeFicheiro)))) {
                for (int val : dados) {
                    dos.writeInt(val);
                }
            }
        }
    }

    // Alogritmos de ordenacao
    public static class SortAlgorithms {

        // --- QuickSort ---
        public static void quickSort(int[] arr, int low, int high) {
            if (low < high) {
                int pi = partition(arr, low, high);
                quickSort(arr, low, pi - 1);
                quickSort(arr, pi + 1, high);
            }
        }

        private static int partition(int[] arr, int low, int high) {
            int mid = low + (high - low) / 2;
            if (arr[mid] < arr[low]) swap(arr, low, mid);
            if (arr[high] < arr[low]) swap(arr, low, high);
            if (arr[high] < arr[mid]) swap(arr, mid, high);
            swap(arr, mid, high);

            int pivot = arr[high];
            int i = (low - 1);
            for (int j = low; j < high; j++) {
                if (arr[j] < pivot) {
                    i++;
                    swap(arr, i, j);
                }
            }
            swap(arr, i + 1, high);
            return i + 1;
        }

        // --- ShellSort ---
        public static void shellSort(int[] arr) {
            int n = arr.length;
            for (int gap = n / 2; gap > 0; gap /= 2) {
                for (int i = gap; i < n; i++) {
                    int temp = arr[i];
                    int j;
                    for (j = i; j >= gap && arr[j - gap] > temp; j -= gap) {
                        arr[j] = arr[j - gap];
                    }
                    arr[j] = temp;
                }
            }
        }

        // --- BubbleSort ---
        public static void bubbleSort(int[] arr) {
            int n = arr.length;
            boolean swapped;
            for (int i = 0; i < n - 1; i++) {
                swapped = false;
                for (int j = 0; j < n - i - 1; j++) {
                    if (arr[j] > arr[j + 1]) {
                        swap(arr, j, j + 1);
                        swapped = true;
                    }
                }
                if (!swapped) break;
            }
        }

        private static void swap(int[] arr, int i, int j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    // Main
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Como usar: java IntSorter <nome_ficheiro> <tamanho_n>");
            return;
        }

        String nomeFicheiro = args[0];
        int n;

        try {
            n = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Erro: O segundo argumento deve ser um numero inteiro.");
            return;
        }

        try {
            System.out.println("A ler " + n + " inteiros de " + nomeFicheiro + "...");
            int[] dadosOriginais = FileManager.lerFicheiro(nomeFicheiro, n);

            System.out.println("\n=== ESTUDO COMPARATIVO DE DESEMPENHO ===");

            // Avaliação do QuickSort
            int[] dadosQuick = dadosOriginais.clone();
            long tInicio = System.nanoTime();
            SortAlgorithms.quickSort(dadosQuick, 0, dadosQuick.length - 1);
            long tFim = System.nanoTime();
            double tempoQuick = (tFim - tInicio) / 1e6;
            System.out.printf("QuickSort : %.3f ms\n", tempoQuick);

            // Avaliação do ShellSort
            int[] dadosShell = dadosOriginais.clone();
            tInicio = System.nanoTime();
            SortAlgorithms.shellSort(dadosShell);
            tFim = System.nanoTime();
            double tempoShell = (tFim - tInicio) / 1e6;
            System.out.printf("ShellSort : %.3f ms\n", tempoShell);

            // Avaliação do BubbleSort
            if (n <= 50000) {
                int[] dadosBubble = dadosOriginais.clone();
                tInicio = System.nanoTime();
                SortAlgorithms.bubbleSort(dadosBubble);
                tFim = System.nanoTime();
                double tempoBubble = (tFim - tInicio) / 1e6;
                System.out.printf("BubbleSort: %.3f ms\n", tempoBubble);
            } else {
                System.out.println("BubbleSort: Ignorado para N > 50.000 (complexidade O(n^2) impraticavel)");
            }

            // Grava o ficheiro final ordenado (QuickSort)
            FileManager.guardarFicheiro(nomeFicheiro, dadosQuick);
            System.out.println("\nFicheiro ordenado gravado com sucesso.");

        } catch (IOException e) {
            System.err.println("Erro ao processar ficheiros: " + e.getMessage());
        }
    }
}