package ru.textsorter;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;


public class CmdArgs {

    @Parameter
    List<String> filesPaths = new ArrayList<>();

    @Parameter(names = "-s")
    boolean isShortStat = false;

    @Parameter(names = "-f")
    boolean isVerboseStat = false;

    @Parameter(names = "-a")
    boolean isLinesAppended = false;

    @Parameter(names = "-o")
    String outputPath = "";

    @Parameter(names = "-p")
    String prefix = "";

}
