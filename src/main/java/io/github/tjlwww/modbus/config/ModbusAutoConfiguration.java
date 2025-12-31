package io.github.tjlwww.modbus.config;

import com.serotonin.modbus4j.ModbusFactory;
import com.serotonin.modbus4j.ModbusMaster;
import com.serotonin.modbus4j.exception.ModbusInitException;
import com.serotonin.modbus4j.ip.IpParameters;
import io.github.tjlwww.modbus.core.ModbusUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@ConditionalOnClass(ModbusMaster.class)
@Slf4j
public class ModbusAutoConfiguration {

    @Bean
    @ConditionalOnBean(ModbusConfigProvider.class)
    @ConditionalOnMissingBean
    public ModbusMaster modbusMaster(ModbusConfigProvider modbusConfigProvider) {
        // 获取 Modbus 配置
        ModbusConfig modbusConfig = modbusConfigProvider.getModbusConfig();
        IpParameters ipParameters = new IpParameters();
        ipParameters.setHost(modbusConfig.getIpAddress()); // 从站 IP 地址
        ipParameters.setPort(modbusConfig.getPort());  //端口
        ipParameters.setEncapsulated(false);   // 是否使用封装
        // 创建 Modbus 工厂并初始化主站
        ModbusFactory factory = new ModbusFactory();
        ModbusMaster master = factory.createTcpMaster(ipParameters, true); // false 表示不保持连接
        master.setTimeout(2000);  // 超时时间（毫秒）
        master.setRetries(2);     // 重试次数
        // 初始化主站
        try {
            master.init();
            log.info("Modbus Master 初始化成功，连接地址: {}:{}", modbusConfig.getIpAddress(), modbusConfig.getPort());
        } catch (ModbusInitException e) {
            log.error("Modbus Master 初始化失败: {}", e.getMessage(), e);
            //throw new IllegalStateException("Modbus 连接初始化失败", e);
        }
        return master;
    }

    /**
     * 创建 ModbusUtils Bean
     */
    @Bean
    @ConditionalOnBean(ModbusMaster.class)
    @ConditionalOnMissingBean
    public ModbusUtils modbusUtils(ModbusMaster modbusMaster) {
        return new ModbusUtils(modbusMaster);
    }
}
