# Modbus Spring Boot Starter

一个简单易用的 Modbus TCP Spring Boot Starter，基于 modbus4j 实现，支持自动配置和快速集成。

## 特性

- ✨ **自动配置**：基于 Spring Boot 自动配置机制，开箱即用
- 🔌 **Modbus TCP 支持**：支持 Modbus TCP 协议通信
- 📦 **完整的功能码支持**：支持常用的 Modbus 功能码操作
- 🛠️ **简单易用**：提供 `ModbusUtils` 工具类，简化 Modbus 操作
- 🔧 **灵活配置**：通过实现 `ModbusConfigProvider` 接口自定义配置来源

## 环境要求

- JDK 17+
- Spring Boot 3.5.5+

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>io.github.tjlwww</groupId>
    <artifactId>tjlwww-spring-boot-starter-modbus</artifactId>
    <version>0.1.0</version>
</dependency>
```

### 2. 实现配置提供者

在您的 Spring Boot 项目中实现 `ModbusConfigProvider` 接口：

```java
import io.github.tjlwww.modbus.config.ModbusConfig;
import io.github.tjlwww.modbus.config.ModbusConfigProvider;
import org.springframework.stereotype.Component;

@Component
public class MyModbusConfigProvider implements ModbusConfigProvider {
    
    @Override
    public ModbusConfig getModbusConfig() {
        ModbusConfig config = new ModbusConfig();
        config.setIpAddress("192.168.1.100");  // Modbus 从站 IP
        config.setPort(502);                    // Modbus 端口，默认 502
        return config;
    }
}
```

### 3. 使用 ModbusUtils

注入 `ModbusUtils` 并开始使用：

```java
import io.github.tjlwww.modbus.core.ModbusUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModbusService {
    
    @Autowired
    private ModbusUtils modbusUtils;
    
    public void example() {
        // 读取保持寄存器
        short[] values = modbusUtils.readHoldingRegisters(40001, 10);
        
        // 写入单个寄存器
        modbusUtils.writeSingleRegister(40001, 100);
        
        // 读取线圈状态
        boolean[] coils = modbusUtils.readCoils(1, 8);
        
        // 写入单个线圈
        modbusUtils.writeSingleCoil(1, true);
    }
}
```

## API 说明

### ModbusUtils 工具类

`ModbusUtils` 提供了以下方法：

#### 读操作

| 方法 | 功能码 | 说明 |
|------|--------|------|
| `readCoils(address, count)` | 0x01 | 读取线圈状态 |
| `readDiscreteInputs(address, count)` | 0x02 | 读取离散输入状态 |
| `readHoldingRegisters(address, count)` | 0x03 | 读取保持寄存器 |
| `readInputRegisters(address, count)` | 0x04 | 读取输入寄存器 |
| `readFloat(address)` | - | 读取 Float 类型数据（占用 2 个寄存器） |

#### 写操作

| 方法 | 功能码 | 说明 |
|------|--------|------|
| `writeSingleCoil(address, value)` | 0x05 | 写入单个线圈 |
| `writeSingleRegister(address, value)` | 0x06 | 写入单个保持寄存器 |
| `writeMultipleCoils(address, values)` | 0x0F | 写入多个线圈 |
| `writeMultipleRegisters(address, values)` | 0x10 | 写入多个保持寄存器 |
| `writeFloat(address, value)` | - | 写入 Float 类型数据（占用 2 个寄存器） |

### Modbus 地址说明

本项目使用标准的 Modbus 地址约定：

| 地址范围 | 数据类型 | 访问类型 |
|---------|---------|---------|
| 00001-09999 | 线圈（Coil） | 读/写 |
| 10001-19999 | 离散输入（Discrete Input） | 只读 |
| 30001-39999 | 输入寄存器（Input Register） | 只读 |
| 40001-49999 | 保持寄存器（Holding Register） | 读/写 |

**示例：**

```java
// 读取地址 40001 开始的 10 个保持寄存器
short[] data = modbusUtils.readHoldingRegisters(40001, 10);

// 读取地址 00001 开始的 8 个线圈
boolean[] coils = modbusUtils.readCoils(1, 8);

// 写入地址 40100 的寄存器
modbusUtils.writeSingleRegister(40100, 500);
```

## 高级用法

### 指定从站 ID

默认从站 ID 为 1，如果需要与不同从站通信，可以使用带 `slaveId` 参数的方法：

```java
// 读取从站 2 的保持寄存器
short[] values = modbusUtils.readHoldingRegisters(2, 40001, 10);

// 向从站 3 写入寄存器
modbusUtils.writeSingleRegister(3, 40001, 100);
```

### 读写 Float 类型数据

Modbus 协议本身不直接支持 Float，但可以通过 2 个寄存器（32位）组合：

```java
// 读取 Float（从地址 40001 开始读取 2 个寄存器）
float temperature = modbusUtils.readFloat(40001);

// 写入 Float（向地址 40001 开始写入 2 个寄存器）
modbusUtils.writeFloat(40001, 25.5f);
```

### 自定义连接参数

可以通过创建自定义的 `ModbusMaster` Bean 来覆盖默认配置：

```java
@Bean
public ModbusMaster customModbusMaster(ModbusConfigProvider configProvider) {
    ModbusConfig config = configProvider.getModbusConfig();
    IpParameters params = new IpParameters();
    params.setHost(config.getIpAddress());
    params.setPort(config.getPort());
    
    ModbusFactory factory = new ModbusFactory();
    ModbusMaster master = factory.createTcpMaster(params, true);
    master.setTimeout(5000);  // 自定义超时时间
    master.setRetries(3);     // 自定义重试次数
    
    try {
        master.init();
    } catch (ModbusInitException e) {
        throw new IllegalStateException("Modbus 初始化失败", e);
    }
    return master;
}
```

## 异常处理

所有 Modbus 操作可能抛出 `ModbusException`，建议进行适当的异常处理：

```java
try {
    short[] values = modbusUtils.readHoldingRegisters(40001, 10);
    // 处理数据
} catch (ModbusException e) {
    log.error("Modbus 读取失败: {}", e.getMessage());
    // 处理异常
}
```

## 项目结构

```
tjlwww-spring-boot-starter-modbus
├── src/main/java/io/github/tjlwww/modbus
│   ├── config
│   │   ├── ModbusAutoConfiguration.java    # 自动配置类
│   │   ├── ModbusConfig.java               # Modbus 配置实体
│   │   └── ModbusConfigProvider.java       # 配置提供者接口
│   └── core
│       ├── ModbusAddressUtil.java          # 地址工具类
│       ├── ModbusException.java            # 自定义异常
│       └── ModbusUtils.java                # Modbus 工具类
└── src/main/resources/META-INF/spring
    └── org.springframework.boot.autoconfigure.AutoConfiguration.imports
```

## 依赖说明

本项目依赖以下核心库：

- `modbus4j 3.0.5`：Modbus 协议实现
- `Spring Boot 3.5.5`：Spring Boot 框架
- `Lombok 1.18.38`：简化代码

## 注意事项

1. **仓库配置**：由于 `modbus4j` 不在 Maven 中央仓库，项目已配置 Infinite Automation 仓库
2. **连接管理**：默认配置为短连接模式，每次操作后会断开连接
3. **线程安全**：`ModbusMaster` 是线程安全的，可以在多线程环境中使用
4. **超时设置**：默认超时时间为 2000ms，重试次数为 2 次

## 许可证

本项目采用 Apache License 2.0 许可证。

## 作者

- **作者**：TJLWWW
- **项目地址**：https://github.com/tjlwww/tjlwww-spring-boot-starter-modbus

## 更新日志

### v1.0.0 (2024)
- 首次发布
- 支持 Modbus TCP 基础功能
- 提供完整的读写操作 API
- 支持 Float 类型数据读写
