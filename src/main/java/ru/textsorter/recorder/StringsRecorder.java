package ru.textsorter.recorder;

public class StringsRecorder extends Recorder {

    private int minLength;
    private int maxLength;
    public StringsRecorder() {
        super("strings.txt", "^.*?$");
        minLength = Integer.MAX_VALUE;
        maxLength = Integer.MIN_VALUE;
    }

    @Override
    protected void updateStatistics(String string) {
        super.updateStatistics(string);
        int length = string.length();
        minLength = Math.min(minLength, length);
        maxLength = Math.max(maxLength, length);
    }

    @Override
    public String printVerboseStat() {
        String stat = "No records\n";
        if (recordAmount != 0) {
            stat = String.format("Max: %d\nMin: %d\n", maxLength, minLength);
        }
        return stat;
    }

}
