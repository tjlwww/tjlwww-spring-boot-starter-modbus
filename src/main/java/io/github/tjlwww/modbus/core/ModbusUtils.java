package io.github.tjlwww.modbus.core;

import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusTransportException;
import com.serotonin.modbus4j.msg.*;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

/**
 * Modbus 工具类
 * 提供常用的 Modbus TCP 读写操作方法
 *
 * @author SDKJ
 */
@Slf4j
public class ModbusUtils {

    private final ModbusMaster master;
    private final int defaultSlaveId;

    /**
     * 构造函数
     *
     * @param master Modbus 主站对象
     */
    public ModbusUtils(ModbusMaster master) {
        this(master, 1);
    }

    /**
     * 构造函数
     *
     * @param master         Modbus 主站对象
     * @param defaultSlaveId 默认从站地址
     */
    public ModbusUtils(ModbusMaster master, int defaultSlaveId) {
        this.master = master;
        this.defaultSlaveId = defaultSlaveId;
    }

    /**
     * 读取线圈状态（功能码 0x01）
     */
    public boolean[] readCoils(int address, int count) {
        return readCoils(defaultSlaveId, address, count);
    }

    /**
     * 读取线圈状态（功能码 0x01）
     *
     * @param slaveId 从站地址
     * @param address Modbus 地址（如 00001）
     * @param count   要读取的线圈数量
     * @return        布尔数组，表示每个线圈的状态
     */
    public boolean[] readCoils(int slaveId, int address, int count) {
        int offset = ModbusAddressUtil.toOffset(address);
        try {
            ReadCoilsRequest request = new ReadCoilsRequest(slaveId, offset, count);
            ReadCoilsResponse response = (ReadCoilsResponse) master.send(request);
            if (response.isException()) {
                log.error("读取线圈失败，从站 {} 地址 {} 返回异常: {}", slaveId, address, response.getExceptionMessage());
                throw new ModbusException("读取线圈异常: " + response.getExceptionMessage());
            }
            return response.getBooleanData();
        } catch (ModbusTransportException e) {
            log.error("读取线圈通信失败，从站 {} 地址 {}: {}", slaveId, address, e.getMessage());
            throw new ModbusException("读取线圈通信失败", e);
        }
    }

    /**
     * 读取离散输入状态（功能码 0x02）
     */
    public boolean[] readDiscreteInputs(int address, int count) {
        return readDiscreteInputs(defaultSlaveId, address, count);
    }

    /**
     * 读取离散输入状态（功能码 0x02）
     *
     * @param slaveId 从站地址
     * @param address Modbus 地址（如 10001）
     * @param count   要读取的输入数量
     * @return        布尔数组，表示每个输入的状态
     */
    public boolean[] readDiscreteInputs(int slaveId, int address, int count) {
        int offset = ModbusAddressUtil.toOffset(address);
        try {
            ReadDiscreteInputsRequest request = new ReadDiscreteInputsRequest(slaveId, offset, count);
            ReadDiscreteInputsResponse response = (ReadDiscreteInputsResponse) master.send(request);
            if (response.isException()) {
                log.error("读取离散输入失败，从站 {} 地址 {} 返回异常: {}", slaveId, address, response.getExceptionMessage());
                throw new ModbusException("读取离散输入异常: " + response.getExceptionMessage());
            }
            return response.getBooleanData();
        } catch (ModbusTransportException e) {
            log.error("读取离散输入通信失败，从站 {} 地址 {}: {}", slaveId, address, e.getMessage());
            throw new ModbusException("读取离散输入通信失败", e);
        }
    }

    /**
     * 读取保持寄存器（功能码 0x03）
     */
    public short[] readHoldingRegisters(int address, int count) {
        return readHoldingRegisters(defaultSlaveId, address, count);
    }

    /**
     * 读取保持寄存器（功能码 0x03）
     *
     * @param slaveId 从站地址
     * @param address Modbus 地址（如 40001）
     * @param count   要读取的寄存器数量
     * @return        short 数组，表示每个寄存器的值
     */
    public short[] readHoldingRegisters(int slaveId, int address, int count) {
        int offset = ModbusAddressUtil.toOffset(address);
        try {
            ReadHoldingRegistersRequest request = new ReadHoldingRegistersRequest(slaveId, offset, count);
            ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) master.send(request);
            if (response.isException()) {
                log.error("读取保持寄存器失败，从站 {} 地址 {} 返回异常: {}", slaveId, address, response.getExceptionMessage());
                throw new ModbusException("读取保持寄存器异常: " + response.getExceptionMessage());
            }
            return response.getShortData();
        } catch (ModbusTransportException e) {
            log.error("读取保持寄存器通信失败，从站 {} 地址 {}: {}", slaveId, address, e.getMessage());
            throw new ModbusException("读取保持寄存器通信失败", e);
        }
    }

    /**
     * 读取输入寄存器（功能码 0x04）
     */
    public short[] readInputRegisters(int address, int count) {
        return readInputRegisters(defaultSlaveId, address, count);
    }

    /**
     * 读取输入寄存器（功能码 0x04）
     *
     * @param slaveId 从站地址
     * @param address Modbus 地址（如 30001）
     * @param count   要读取的寄存器数量
     * @return        short 数组，表示每个寄存器的值
     */
    public short[] readInputRegisters(int slaveId, int address, int count) {
        int offset = ModbusAddressUtil.toOffset(address);
        try {
            ReadInputRegistersRequest request = new ReadInputRegistersRequest(slaveId, offset, count);
            ReadInputRegistersResponse response = (ReadInputRegistersResponse) master.send(request);
            if (response.isException()) {
                log.error("读取输入寄存器失败，从站 {} 地址 {} 返回异常: {}", slaveId, address, response.getExceptionMessage());
                throw new ModbusException("读取输入寄存器异常: " + response.getExceptionMessage());
            }
            return response.getShortData();
        } catch (ModbusTransportException e) {
            log.error("读取输入寄存器通信失败，从站 {} 地址 {}: {}", slaveId, address, e.getMessage());
            throw new ModbusException("读取输入寄存器通信失败", e);
        }
    }

    /**
     * 写入单个线圈（功能码 0x05）
     */
    public void writeSingleCoil(int address, boolean value) {
        writeSingleCoil(defaultSlaveId, address, value);
    }

    /**
     * 写入单个线圈（功能码 0x05）
     *
     * @param slaveId 从站地址
     * @param address Modbus 地址（如 00001）
     * @param value   要写入的布尔值（true 为 ON，false 为 OFF）
     */
    public void writeSingleCoil(int slaveId, int address, boolean value) {
        int offset = ModbusAddressUtil.toOffset(address);
        try {
            WriteCoilRequest request = new WriteCoilRequest(slaveId, offset, value);
            WriteCoilResponse response = (WriteCoilResponse) master.send(request);
            if (response.isException()) {
                log.error("写入单个线圈失败，从站 {} 地址 {} 返回异常: {}", slaveId, address, response.getExceptionMessage());
                throw new ModbusException("写入单个线圈异常: " + response.getExceptionMessage());
            }
        } catch (ModbusTransportException e) {
            log.error("写入单个线圈通信失败，从站 {} 地址 {}: {}", slaveId, address, e.getMessage());
            throw new ModbusException("写入单个线圈通信失败", e);
        }
    }

    /**
     * 写入多个线圈（功能码 0x0F）
     */
    public void writeMultipleCoils(int address, boolean[] values) {
        writeMultipleCoils(defaultSlaveId, address, values);
    }

    /**
     * 写入多个线圈（功能码 0x0F）
     *
     * @param slaveId 从站地址
     * @param address 起始 Modbus 地址（如 00001）
     * @param values  布尔数组，表示要写入的线圈状态
     */
    public void writeMultipleCoils(int slaveId, int address, boolean[] values) {
        int offset = ModbusAddressUtil.toOffset(address);
        try {
            WriteCoilsRequest request = new WriteCoilsRequest(slaveId, offset, values);
            WriteCoilsResponse response = (WriteCoilsResponse) master.send(request);
            if (response.isException()) {
                log.error("写入多个线圈失败，从站 {} 地址 {} 返回异常: {}", slaveId, address, response.getExceptionMessage());
                throw new ModbusException("写入多个线圈异常: " + response.getExceptionMessage());
            }
        } catch (ModbusTransportException e) {
            log.error("写入多个线圈通信失败，从站 {} 地址 {}: {}", slaveId, address, e.getMessage());
            throw new ModbusException("写入多个线圈通信失败", e);
        }
    }

    /**
     * 写入单个保持寄存器（功能码 0x06）
     */
    public void writeSingleRegister(int address, int value) {
        writeSingleRegister(defaultSlaveId, address, value);
    }

    /**
     * 写入单个保持寄存器（功能码 0x06）
     *
     * @param slaveId 从站地址
     * @param address Modbus 地址（如 40001）
     * @param value   要写入的整数值（16 位）
     */
    public void writeSingleRegister(int slaveId, int address, int value) {
        int offset = ModbusAddressUtil.toOffset(address);
        try {
            WriteRegisterRequest request = new WriteRegisterRequest(slaveId, offset, value);
            WriteRegisterResponse response = (WriteRegisterResponse) master.send(request);
            if (response.isException()) {
                log.error("写入单个寄存器失败，从站 {} 地址 {} 返回异常: {}", slaveId, address, response.getExceptionMessage());
                throw new ModbusException("写入单个寄存器异常: " + response.getExceptionMessage());
            }
        } catch (ModbusTransportException e) {
            log.error("写入单个寄存器通信失败，从站 {} 地址 {}: {}", slaveId, address, e.getMessage());
            throw new ModbusException("写入单个寄存器通信失败", e);
        }
    }

    /**
     * 写入多个保持寄存器（功能码 0x10）
     */
    public void writeMultipleRegisters(int address, short[] values) {
        writeMultipleRegisters(defaultSlaveId, address, values);
    }

    /**
     * 写入多个保持寄存器（功能码 0x10）
     *
     * @param slaveId 从站地址
     * @param address 起始 Modbus 地址（如 40001）
     * @param values  short 数组，表示要写入的寄存器值
     */
    public void writeMultipleRegisters(int slaveId, int address, short[] values) {
        int offset = ModbusAddressUtil.toOffset(address);
        try {
            WriteRegistersRequest request = new WriteRegistersRequest(slaveId, offset, values);
            WriteRegistersResponse response = (WriteRegistersResponse) master.send(request);
            if (response.isException()) {
                log.error("写入多个寄存器失败，从站 {} 地址 {} 返回异常: {}", slaveId, address, response.getExceptionMessage());
                throw new ModbusException("写入多个寄存器异常: " + response.getExceptionMessage());
            }
        } catch (ModbusTransportException e) {
            log.error("写入多个寄存器通信失败，从站 {} 地址 {}: {}", slaveId, address, e.getMessage());
            throw new ModbusException("写入多个寄存器通信失败", e);
        }
    }

    public float readFloat(int address) {
        short[] registers = readHoldingRegisters(address, 2);
        int highWord = registers[0] & 0xFFFF; // 高 16 位
        int lowWord = registers[1] & 0xFFFF;  // 低 16 位
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putShort((short) highWord);
        buffer.putShort((short) lowWord);
        buffer.flip();
        return buffer.getFloat();
    }

    public void writeFloat(int address, float value) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putFloat(value);
        buffer.flip();
        short highWord = buffer.getShort();
        short lowWord = buffer.getShort();
        writeMultipleRegisters(address, new short[]{highWord, lowWord});
    }
}
