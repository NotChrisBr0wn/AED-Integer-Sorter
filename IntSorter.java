import java.io.*;

public class IntSorter {

    // ---MANIPULAÇÃO DE FICHEIROS---
    public static class FileManager {

        // --- Leitura e Escrita de Ficheiros ---
        public static int[] lerFicheiro(String nomeFicheiro, int n) throws IOException {
            if (nomeFicheiro.endsWith(".bin") || nomeFicheiro.endsWith(".dat")) {
                return lerBinario(nomeFicheiro, n);
            } else {
                return lerTexto(nomeFicheiro, n);
            }
        }

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

    // --- ALGORITMOS DE ORDENAÇÃO ---
    public static class SortAlgorithms {

        // Quick Sort
        public static void quickSort(int[] arr, int low, int high) {
            if (low < high) {
                int pi = partition(arr, low, high);
                quickSort(arr, low, pi - 1); // Ordena elementos antes da partição
                quickSort(arr, pi + 1, high); // Ordena elementos depois da partição
            }
        }

        private static int partition(int[] arr, int low, int high) {
            // Seleciona medidana de 3 
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

        // --- Shell Sort (Sequência Shell N/2) ---
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

        // --- Bubble Sort ---
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

    // --- Método Main ---
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Uso: java IntSorter <nome_ficheiro> <tamanho_n>");
            return;
        }

        String nomeFicheiro = args[0];
        int n = Integer.parseInt(args[1]);

        try {
            System.out.println("A ler " + n + " inteiros de " + nomeFicheiro + "...");
            int[] dados = FileManager.lerFicheiro(nomeFicheiro, n);

            long inicio = System.currentTimeMillis();
            
            // Aplicação do QuickSort
            SortAlgorithms.quickSort(dados, 0, dados.length - 1);
            
            long fim = System.currentTimeMillis();
            System.out.println("Ordenação concluída em " + (fim - inicio) + " ms.");

            FileManager.guardarFicheiro(nomeFicheiro, dados);
            System.out.println("Ficheiro ordenado gravado com sucesso.");

        } catch (IOException e) {
            System.err.println("Erro ao processar ficheiros: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("O argumento do tamanho deve ser um número inteiro válido.");
        }
    }
}