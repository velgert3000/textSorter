package ru.textsorter;

import com.beust.jcommander.JCommander;
import ru.textsorter.recorder.FloatsRecorder;
import ru.textsorter.recorder.IntegersRecorder;
import ru.textsorter.recorder.Recorder;
import ru.textsorter.recorder.StringsRecorder;

import java.io.*;
import java.util.List;

public class App  {

    public static void main( String[] args ) {
        CmdArgs cmdArgs = new CmdArgs();
        parseCmdArgs(cmdArgs, args);
        Recorder recordsChain = Recorder.createChain(cmdArgs.prefix, cmdArgs.outputPath, cmdArgs.isLinesAppended,
                new IntegersRecorder(), new FloatsRecorder(), new StringsRecorder());
        processFiles(cmdArgs.filesPaths, recordsChain);
        showStatistics(cmdArgs.isShortStat, cmdArgs.isVerboseStat, recordsChain);
    }

    static private void parseCmdArgs(CmdArgs cmdArgs, String[] args) {
        JCommander.newBuilder()
                .addObject(cmdArgs)
                .build()
                .parse(args);
    }

    static private void processFiles(List<String> filesPaths, Recorder filterChain) {
        for (String path : filesPaths) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path))) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    filterChain.processLine(line);
                }
            } catch (IOException e) {
                System.err.println("IOException: file process error " + path);
            }
        }
        filterChain.close();
    }

    private static void showStatistics(boolean isShort, boolean isVerbose, Recorder chain) {
        if (isShort || isVerbose) {
            System.out.println(chain.showStatistics(isVerbose));
        }
    }

}
