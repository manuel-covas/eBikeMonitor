package pt.manuelcovas.ebikemonitor.datatypes.ESPeBike;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import pt.manuelcovas.ebikemonitor.datatypes.bq76930.CellBal;
import pt.manuelcovas.ebikemonitor.datatypes.bq76930.SysCtrl1;
import pt.manuelcovas.ebikemonitor.datatypes.bq76930.SysCtrl2;
import pt.manuelcovas.ebikemonitor.datatypes.bq76930.SysStat;

public class SystemStats {

    public static int LENGTH = SysStat.LENGTH + CellBal.LENGTH + SysCtrl1.LENGTH + SysCtrl2.LENGTH + CellVoltages.LENGTH + 5 * 8;

    public SysStat sysStat;
    public CellBal cellBal;
    public SysCtrl1 sysCtrl1;
    public SysCtrl2 sysCtrl2;
    public CellVoltages cellVoltages;
    public double packVoltage;
    public double packCurrent;
    public double ts1Voltage;
    public double ts2Voltage;
    public double throttlePercentage;

    public SystemStats(byte[] payload) {

        int index = 0;

        sysStat = new SysStat(Arrays.copyOfRange(payload, index, index + SysStat.LENGTH));
        index += SysStat.LENGTH;
        cellBal = new CellBal(Arrays.copyOfRange(payload, index, index + CellBal.LENGTH));
        index += CellBal.LENGTH;
        sysCtrl1 = new SysCtrl1(Arrays.copyOfRange(payload, index, index + SysCtrl1.LENGTH));
        index += SysCtrl1.LENGTH;
        sysCtrl2 = new SysCtrl2(Arrays.copyOfRange(payload, index, index + SysCtrl2.LENGTH));
        index += SysCtrl2.LENGTH;
        cellVoltages = new CellVoltages(Arrays.copyOfRange(payload, index, index + CellVoltages.LENGTH));
        index += CellVoltages.LENGTH;

        ByteBuffer byteBuffer = ByteBuffer.wrap(payload, index, 5 * 8).order(ByteOrder.LITTLE_ENDIAN);
        packVoltage = byteBuffer.getDouble();
        packCurrent = byteBuffer.getDouble();
        ts1Voltage = byteBuffer.getDouble();
        ts2Voltage = byteBuffer.getDouble();
        throttlePercentage = byteBuffer.getDouble();
    }

    public byte[] serialize() {

        int index = 0;
        ByteBuffer byteBuffer = ByteBuffer.allocate(LENGTH).order(ByteOrder.LITTLE_ENDIAN);

        byteBuffer.put(sysStat.serialize(), index, SysStat.LENGTH);
        index += SysStat.LENGTH;
        byteBuffer.put(cellBal.serialize(), index, CellBal.LENGTH);
        index += CellBal.LENGTH;
        byteBuffer.put(sysCtrl1.serialize(), index, SysCtrl1.LENGTH);
        index += SysCtrl1.LENGTH;
        byteBuffer.put(sysCtrl2.serialize(), index, SysCtrl2.LENGTH);
        index += SysCtrl2.LENGTH;
        byteBuffer.put(cellVoltages.serialize(), index, CellVoltages.LENGTH);
        index += CellVoltages.LENGTH;

        byteBuffer.position(index);
        byteBuffer.putDouble(packVoltage);
        byteBuffer.putDouble(packCurrent);
        byteBuffer.putDouble(ts1Voltage);
        byteBuffer.putDouble(ts2Voltage);
        byteBuffer.putDouble(throttlePercentage);

        return byteBuffer.array();
    }
}
