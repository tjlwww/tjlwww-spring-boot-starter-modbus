package io.github.tjlwww.modbus.core;

public class ModbusAddressUtil {

    public enum ModbusDataType {
        COIL, DISCRETE_INPUT, INPUT_REGISTER, HOLDING_REGISTER
    }

    public static int toOffset(int modbusAddress) {
        // Modbus地址从1开始，偏移量从0开始
        return modbusAddress % 10000 - 1;
    }

    public static ModbusDataType getDataType(int modbusAddress) {
        if (modbusAddress >= 1 && modbusAddress < 10000) return ModbusDataType.COIL;
        if (modbusAddress >= 10001 && modbusAddress < 20000) return ModbusDataType.DISCRETE_INPUT;
        if (modbusAddress >= 30001 && modbusAddress < 40000) return ModbusDataType.INPUT_REGISTER;
        if (modbusAddress >= 40001 && modbusAddress < 50000) return ModbusDataType.HOLDING_REGISTER;
        throw new IllegalArgumentException("无效的 Modbus 地址: " + modbusAddress);
    }

}
