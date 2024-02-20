package ru.textsorter.recorder;


public class IntegersRecorder extends Recorder {

    private long min;
    private long max;
    private long sum;

    public IntegersRecorder() {
        super("integers.txt", "^-?(0b?)?[0-9]+$|^-?0[Xx][0-9A-F]+$");
        min = Long.MAX_VALUE;
        max = Long.MIN_VALUE;
        sum = 0;
    }

    @Override
    protected void updateStatistics(String string) {
        super.updateStatistics(string);
        try {
            long value = Long.parseLong(string);
            Double.valueOf(string);
            min = Math.min(value, min);
            max = Math.max(value, max);
            sum += value;
        } catch (NumberFormatException e) {
            System.err.println("Parsing error, line " + string);
            recordAmount--;
            passToNext(string);
        }
    }

    @Override
    public String printVerboseStat() {
        String stat = "No records\n";
        if (recordAmount != 0) {
             stat = String.format("Max: %d\nMin: %d\nAvg: %d\nSum: %d\n", max, min, sum / recordAmount, sum);
        }
        return stat;
    }

}
