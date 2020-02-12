//Programmers: Konark Raj Mishra, Bianca Contreras-Sanz, CS310

package edu.sdsu.cs;

import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;



public class App {
    private static void fileparser(Path file) throws IOException {

        List<String> lines = Files.readAllLines(file, Charset.defaultCharset());
        List<String> nooftokens  = new LinkedList<>();
        int longestline = 0;
        int avgline= 0;
        for (String line : lines) {
            if (line.length() > longestline) {
                longestline = line.length();
            }
            avgline += line.length();
            nooftokens.addAll(Arrays.asList(line.split(" ")));
        }
        while (nooftokens.remove("")) {
        }
        Set<String> casesens = new HashSet<>();
        Set<String> caseinsens = new HashSet<>();
        String mosttoken = "";
        for (int counter3 = 0; counter3 < nooftokens.size(); counter3++) {
            casesens.add(nooftokens.get(counter3));
            caseinsens.add(nooftokens.get(counter3).toLowerCase());
            nooftokens.set(counter3, nooftokens.get(counter3).toLowerCase());
            if (Collections.frequency(nooftokens, nooftokens.get(counter3)) >
                    Collections.frequency(nooftokens, mosttoken)) {
                mosttoken = nooftokens.get(counter3);
            }
        }

        Collections.sort(nooftokens, (T1, T2) -> {
            return Collections.frequency(nooftokens, T2) - Collections.frequency(nooftokens, T1);
        });
        List<String> mostfreqtoken = new LinkedList<>();
        for (String token : nooftokens) {
            if (mostfreqtoken.size() == 10) {
                break;
            }
            if (!mostfreqtoken.contains(token)) {
                mostfreqtoken.add(token);
            }
        }

        List<String> leastfreqtokens = new LinkedList<>();
        for (int counter2 = nooftokens.size() - 1; counter2 >= 0; counter2--) {
            if (leastfreqtokens.size() == 10) {
                break;
            }
            if (!leastfreqtokens.contains(nooftokens.get(counter2))) {
                leastfreqtokens.add(nooftokens.get(counter2));
            }
        }
        avgline /= (double) lines.size();
        List<String> statfile = new LinkedList<>();
        statfile.add(String.format("%-60s: %-5d", "Length of longest line in file: ", longestline));
        statfile.add(String.format("%-60s: %-5.2f", "Average line length: ", avgline));
        statfile.add(String.format("%-60s: %-5d", "Number of space-delineated tokens (case-sensitive): ", casesens.size()));
        statfile.add(String.format("%-60s: %-5d", "Number of space-delineated tokens (case-insensitive): ", caseinsens.size()));
        statfile.add(String.format("%-60s: %-5d", "Number of all space-delineated tokens in file: ", nooftokens.size()));
        statfile.add(String.format("%-60s: %s", "Most frequently occurring tokens: ", mosttoken));
        statfile.add(String.format("%-60s: %-5d", "Count of most frequently occurring token: ", Collections.frequency(nooftokens, mosttoken)));
        statfile.add("");
        statfile.add("10 most frequent tokens with their counts:");
        for (int counter4 = 0; counter4 < mostfreqtoken.size(); counter4++) {
            statfile.add(String.format("\t%2d. %-40s - %d\n",
                    counter4 + 1, mostfreqtoken.get(counter4),
                    Collections.frequency(nooftokens, mostfreqtoken.get(counter4))));
        }
        statfile.add("");
        statfile.add("10 least frequent tokens with their counts:");
        for (int counter = 0; counter < leastfreqtokens.size(); counter++) {
            statfile.add(String.format("\t%2d. %-40s - %d\n",
                    counter + 1, leastfreqtokens.get(counter),
                    Collections.frequency(nooftokens, leastfreqtokens.get(counter))));
        }

        Files.write(file.resolveSibling(file.getFileName() + ".stats"),
                statfile, Charset.defaultCharset());
    }

    private static void filelister(Path path, List<Path> dirfiles) throws IOException {

        PathMatcher fileformat = FileSystems.getDefault().getPathMatcher("glob:**{.java,txt}");
        try (DirectoryStream<Path> streamer = Files.newDirectoryStream(path)) {
            for (Path newfile : streamer) {
                if (Files.isDirectory(newfile)) {
                    filelister(newfile, dirfiles);
                } else if (fileformat.matches(newfile)) {
                    dirfiles.add(newfile);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Enter the directory to parse files: ");
        String directory = scan.nextLine();
        if(directory == ""){
            System.getProperty("user.dir");
        }
        scan.close();
        if (args.length > 0) {
            directory = args[0];
        }
        List<Path> dirfiles = new LinkedList<>();
        filelister(Paths.get(directory), dirfiles);
        for (Path file : dirfiles) {
            fileparser(file);
        }
    }

}