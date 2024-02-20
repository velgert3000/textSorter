package ru.textsorter.recorder;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public abstract class Recorder {

    final protected String name;
    protected int recordAmount;
    private Path path;
    private final String regex;
    private BufferedWriter writer;
    private Recorder next;
    private boolean isAppendLines;
    private boolean isError;

    protected Recorder(String name, String regex) {
        this.name = name;
        this.regex = regex;
        this.isAppendLines = false;
        isError = false;
        recordAmount = 0;
    }

    public static Recorder createChain(String prefix, String path, boolean isAppendLines, Recorder... recorders) {
        Path filePath = Paths.get(path);
        recorders[0].path = filePath.resolve(prefix + recorders[0].name);
        recorders[0].isAppendLines = isAppendLines;
        for (int i = 1; i < recorders.length; i++) {
            recorders[i].path = filePath.resolve(prefix + recorders[i].name);
            recorders[i].isAppendLines = isAppendLines;
            recorders[i - 1].next(recorders[i]);
        }
        return recorders[0];
    }

    public Recorder next(Recorder next) {
        this.next = next;
        return next;
    }

    protected void passToNext(String line) {
        if (next != null) {
            next.processLine(line);
        } else {
            System.err.println("Line process exception: unhandled line " + line);
        }
    }

    public void processLine(String line) {
        if (Pattern.matches(regex, line)) {
            write(line);
        } else {
            passToNext(line);
        }
    }

    public void write(String value) {
        if (!isError) {
            try {
                if (writer == null) {
                    writer = new BufferedWriter(new FileWriter(path.toFile(), isAppendLines));
                }
                writer.write(value + "\n");
                updateStatistics(value);
            } catch (IOException e) {
                System.err.println("IOException: cant create or write to file " + path);
                isError = true;
            }
        }
    }

    public void close() {
        if (writer != null) {
            try {
                writer.close();
            } catch (IOException e) {
                System.err.println("IOException: file closing error " + path);
            }
        }
        if (next != null) {
            next.close();
        }
    }

    public String showStatistics(boolean isVerbose) {
        String stat = "File " + name + " has " + recordAmount + " records\n";
        if (isVerbose) {
            stat += printVerboseStat() + "\n";
        }
        if (next != null) {
            stat += next.showStatistics(isVerbose);
        }
        return stat;
    }

    protected void updateStatistics(String string) {
        ++recordAmount;
    }

    abstract public String printVerboseStat();

}
