package io.github.tjlwww.modbus.core;

/**
 * Modbus 操作异常
 *
 * @author SDKJ
 */
public class ModbusException extends RuntimeException {

    public ModbusException(String message) {
        super(message);
    }

    public ModbusException(String message, Throwable cause) {
        super(message, cause);
    }
}
