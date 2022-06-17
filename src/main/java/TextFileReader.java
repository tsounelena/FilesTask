import java.io.*;
import java.util.*;

public class TextFileReader {

    public TextFileReader() {
        this.allTextFiles = new ArrayList<>();
    }

    private static final FileFilter isTextFile = file -> !file.isDirectory() && file.getName().endsWith(".txt");
    private static final FileFilter isDirectory = File::isDirectory;

    private final ArrayList<File> allTextFiles;

    public void searchAndUnion(String path) {
        File mainFile = new File(path);
        this.searchTextFiles(mainFile);
        String outputPath = mainFile.getAbsolutePath() + "/result.txt";
        writeToOneFile(outputPath);
    }

    public void searchTextFiles(File mainFile) {
        File[] textFiles = mainFile.listFiles(isTextFile);
        if (textFiles != null && textFiles.length != 0) {
            Collections.addAll(this.allTextFiles, textFiles);
        }

        File[] directories = mainFile.listFiles(isDirectory);
        if (directories != null && directories.length != 0) {
            for (File directory : directories) {
                searchTextFiles(directory);
            }
        }
    }

    public void writeToOneFile(String outputFileName) {
        this.allTextFiles.sort(Comparator.comparing(File::getName));

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFileName))
        ) {
            for (File file : this.allTextFiles) {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(file.getAbsolutePath()));
                int character;
                while ((character = bufferedReader.read()) != -1) {
                    bufferedWriter.write(character);
                }
                bufferedReader.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
