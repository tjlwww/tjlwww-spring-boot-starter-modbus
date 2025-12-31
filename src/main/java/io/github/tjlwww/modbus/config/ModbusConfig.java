package io.github.tjlwww.modbus.config;

import lombok.Data;

@Data
public class ModbusConfig {

    /**
     * 主键ID
     */
    private Integer id;
    /**
     * IP地址
     */
    private String ipAddress;
    /**
     * 端口
     */
    private Integer port;


}
