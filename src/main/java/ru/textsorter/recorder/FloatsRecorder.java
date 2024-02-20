package ru.textsorter.recorder;


public class FloatsRecorder extends Recorder {

    private double min;
    private double max;
    private double sum;

    public FloatsRecorder() {
        super("floats.txt", "^-?[0-9]*\\.[0-9]+([Ee][+-]?[0-9]+)?$");
        min = Double.MAX_VALUE;
        max = Double.MIN_VALUE;
        sum = 0;
    }

    @Override
    protected void updateStatistics(String string) {
        super.updateStatistics(string);
        try {
            double value = Double.parseDouble(string);
            Double.valueOf(string);
            min = Math.min(value, min);
            max = Math.max(value, max);
            sum += value;
        } catch (NumberFormatException e) {
            System.err.println("Parsing error, line " + string + ", file " + name);
        }
    }

    @Override
    public String printVerboseStat() {
        String stat = "No records\n";
        if (recordAmount != 0) {
            stat = String.format("Max: %.2f\nMin: %.2f\nAvg: %.2f\nSum: %.2f\n", max, min, sum / recordAmount, sum);
        }
        return stat;
    }

}
