package pt.manuelcovas.ebikemonitor;

public class BatteryCell {

    private boolean balancing = false;
    private double voltage = 0;

    public boolean isBalancing() {
        return balancing;
    }

    public double getVoltage() {
        return voltage;
    }

    public void update(byte[] data) {

    }
}
