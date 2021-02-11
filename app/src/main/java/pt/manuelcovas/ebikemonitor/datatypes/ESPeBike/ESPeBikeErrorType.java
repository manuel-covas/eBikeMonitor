package pt.manuelcovas.ebikemonitor.datatypes.ESPeBike;

public enum ESPeBikeErrorType {
    EBIKE_OK,                                     // Success.
    EBIKE_NVS_INIT_ERASE_FAIL,                    // NVS Init: Failed to erase flash chip.
    EBIKE_NVS_INIT_FAIL,                          // NVS Init: ESP-IDF nvs_flash_init() failed.
    EBIKE_NVS_INIT_OPEN_FAIL,                     // NVS Init: Failed to open NVS namespace EBIKE_NVS_NAMESPACE with mode NVS_READWRITE.
    EBIKE_GPIO_INIT_CONFIG_FAIL,                  // GPIO Init: ESP-IDF gpio_config() failed.
    EBIKE_GPIO_INIT_PWM_CONFIG_FAIL,              // GPIO Init: Failed to configure PWM (LEDC).
    EBIKE_GPIO_PWM_TOGGLE_FAIL,                   // GPIO: Failed to toggle PWM (LEDC).
    EBIKE_GPIO_PWM_SET_DUTY_FAIL,                 // GPIO: Failed to set duty cycle of PWM (LEDC).
    EBIKE_ADC_INIT_SET_WIDTH_FAIL,                // ADC Init: Failed to set ADC1 reading width.
    EBIKE_ADC_INIT_SET_ATTEN_FAIL,                // ADC Init: Failed to set ADC1 throttle channel attenuation.
    EBIKE_BLE_INIT_OUTGOING_QUEUE_CREATE_FAIL,    // BLE Init: Outgoing message queue creation failed.
    EBIKE_BLE_INIT_OUTGOING_TASK_CREATE_FAIL,     // BLE Init: Outgoing message task creation failed.
    EBIKE_BLE_INIT_CONTROLLER_INIT_FAIL,          // BLE Init: Initializing bluetooth controller with BT_CONTROLLER_INIT_CONFIG_DEFAULT failed.
    EBIKE_BLE_INIT_ENABLE_CONTROLLER_FAIL,        // BLE Init: Bluetooth controller enabling failed.
    EBIKE_BLE_INIT_BLUEDROID_INIT_FAIL,           // BLE Init: Bluedroid stack initialization failed.
    EBIKE_BLE_INIT_BLUEDROID_ENABLE_FAIL,         // BLE Init: Bluedroid stack enabling failed.
    EBIKE_BLE_INIT_GAP_CALLBACK_REGISTER_FAIL,    // BLE Init: GAP callback function registration failed.
    EBIKE_BLE_INIT_GATTS_CALLBACK_REGISTER_FAIL,  // BLE Init: GATT server callback function registration failed.
    EBIKE_BLE_INIT_GATTS_APP_REGISTER_FAIL,       // BLE Init: GATT server callback function registration failed.
    EBIKE_BLE_INIT_SET_BT_NAME_FAIL,              // BLE Init: Failed to set device's bluetooth name.
    EBIKE_BLE_INIT_SET_ADV_DATA_FAIL,             // BLE Init: Failed to set desired BLE advertising data.
    EBIKE_BLE_INIT_START_ADV_FAIL,                // BLE Init: Failed to start BLE advertising.
    EBIKE_BLE_TX_NOT_CONNECTED,                   // BLE Transmit: An attempt to send data over BLE was made but no connection was active.
    EBIKE_BLE_TX_BAD_ARGUMENTS,                   // BLE Transmit: Incorrect parameters passed to eBike_ble_tx()
    EBIKE_BLE_TX_MALLOC_FAIL,                     // BLE Transmit: Malloc failed in eBike_queue_ble_message()
    EBIKE_BLE_TX_QUEUE_FAIL,                      // BLE Transmit: Failed to add BLE message to outgoing queue.
    EBIKE_BLE_COMMAND_TOO_SHORT,                  // BLE Command: Received data was too short.
    EBIKE_BLE_COMMAND_UNKNOWN,                    // BLE Command: Unknown command.
    EBIKE_BLE_AUTHED_COMMAND_UNKNOWN,             // BLE Command: Unknown authenticated command.
    EBIKE_LOG_INIT_MALLOC_FAIL,                   // BLE Log Init: malloc for the log's buffer failed.
    EBIKE_LOG_TX_MALLOC_FAIL,                     // BLE Log Transmit: malloc for a log chunk failed in eBike_log_send()
    EBIKE_AUTH_INIT_MALLOC_FAIL,                  // Authentication Init: Malloc failed
    EBIKE_AUTH_INIT_PARSE_KEY_FAIL,               // Authentication Init: Parsing of built in public key failed.
    EBIKE_AUTHED_COMMAND_FAIL,                    // Authentication: Signature verification failed.
    EBIKE_NVS_SETTINGS_GET_FAIL,                  // NVS Settings: Read from NVS failed.
    EBIKE_NVS_SETTINGS_PUT_FAIL,                  // NVS Settings: Write to NVS failed.
    EBIKE_NVS_SETTINGS_CRC_MISMATCH,              // NVS Settings: CRC check failed. Could happen when reading or writing settings.
    EBIKE_BMS_INIT_I2C_CONFIG_FAIL,               // BMS Init: I2C driver configuring failed.
    EBIKE_BMS_INIT_I2C_INSTALL_FAIL,              // BMS Init: I2C driver activation failed.
    EBIKE_BMS_I2C_BUILD_COMMAND_FAIL,             // BMS I2C Communication: Failure while preparing I2C command to communicate with BQ769x0.
    EBIKE_BMS_I2C_COMMAND_FAIL,                   // BMS I2C Communication: I2C data exchange failed. (not acknowledged or other)
    EBIKE_BMS_I2C_CRC_MISMATCH;                   // BMS I2C Communication: BQ769x0 communication CRC mismatched. (explained in the chip's datasheet)

    @Override
    public String toString() {
        switch(this) {
            case EBIKE_OK:                                    return "Success.";
            case EBIKE_NVS_INIT_ERASE_FAIL:                   return "NVS Init: Failed to erase flash chip.";
            case EBIKE_NVS_INIT_FAIL:                         return "NVS Init: ESP-IDF nvs_flash_init() failed.";
            case EBIKE_NVS_INIT_OPEN_FAIL:                    return "NVS Init: Failed to open NVS namespace EBIKE_NVS_NAMESPACE with mode NVS_READWRITE.";
            case EBIKE_GPIO_INIT_CONFIG_FAIL:                 return "GPIO Init: ESP-IDF gpio_config() failed.";
            case EBIKE_GPIO_INIT_PWM_CONFIG_FAIL:             return "GPIO Init: Failed to configure PWM (LEDC).";
            case EBIKE_GPIO_PWM_TOGGLE_FAIL:                  return "GPIO: Failed to toggle PWM (LEDC).";
            case EBIKE_GPIO_PWM_SET_DUTY_FAIL:                return "GPIO: Failed to set duty cycle of PWM (LEDC).";
            case EBIKE_ADC_INIT_SET_WIDTH_FAIL:               return "ADC Init: Failed to set ADC1 reading width.";
            case EBIKE_ADC_INIT_SET_ATTEN_FAIL:               return "ADC Init: Failed to set ADC1 throttle channel attenuation.";
            case EBIKE_BLE_INIT_OUTGOING_QUEUE_CREATE_FAIL:   return "BLE Init: Outgoing message queue creation failed.";
            case EBIKE_BLE_INIT_OUTGOING_TASK_CREATE_FAIL:    return "BLE Init: Outgoing message task creation failed.";
            case EBIKE_BLE_INIT_CONTROLLER_INIT_FAIL:         return "BLE Init: Initializing bluetooth controller with BT_CONTROLLER_INIT_CONFIG_DEFAULT failed.";
            case EBIKE_BLE_INIT_ENABLE_CONTROLLER_FAIL:       return "BLE Init: Bluetooth controller enabling failed.";
            case EBIKE_BLE_INIT_BLUEDROID_INIT_FAIL:          return "BLE Init: Bluedroid stack initialization failed.";
            case EBIKE_BLE_INIT_BLUEDROID_ENABLE_FAIL:        return "BLE Init: Bluedroid stack enabling failed.";
            case EBIKE_BLE_INIT_GAP_CALLBACK_REGISTER_FAIL:   return "BLE Init: GAP callback function registration failed.";
            case EBIKE_BLE_INIT_GATTS_CALLBACK_REGISTER_FAIL: return "BLE Init: GATT server callback function registration failed.";
            case EBIKE_BLE_INIT_GATTS_APP_REGISTER_FAIL:      return "BLE Init: GATT server callback function registration failed.";
            case EBIKE_BLE_INIT_SET_BT_NAME_FAIL:             return "BLE Init: Failed to set device's bluetooth name.";
            case EBIKE_BLE_INIT_SET_ADV_DATA_FAIL:            return "BLE Init: Failed to set desired BLE advertising data.";
            case EBIKE_BLE_INIT_START_ADV_FAIL:               return "BLE Init: Failed to start BLE advertising.";
            case EBIKE_BLE_TX_NOT_CONNECTED:                  return "BLE Transmit: An attempt to send data over BLE was made but no connection was active.";
            case EBIKE_BLE_TX_BAD_ARGUMENTS:                  return "BLE Transmit: Incorrect parameters passed to eBike_ble_tx()";
            case EBIKE_BLE_TX_MALLOC_FAIL:                    return "BLE Transmit: Malloc failed in eBike_queue_ble_message()";
            case EBIKE_BLE_TX_QUEUE_FAIL:                     return "BLE Transmit: Failed to add BLE message to outgoing queue.";
            case EBIKE_BLE_COMMAND_TOO_SHORT:                 return "BLE Command: Received data was too short.";
            case EBIKE_BLE_COMMAND_UNKNOWN:                   return "BLE Command: Unknown command.";
            case EBIKE_BLE_AUTHED_COMMAND_UNKNOWN:            return "BLE Command: Unknown authenticated command.";
            case EBIKE_LOG_INIT_MALLOC_FAIL:                  return "BLE Log Init: malloc for the log's buffer failed.";
            case EBIKE_LOG_TX_MALLOC_FAIL:                    return "BLE Log Transmit: malloc for a log chunk failed in eBike_log_send()";
            case EBIKE_AUTH_INIT_MALLOC_FAIL:                 return "Authentication Init: Malloc failed";
            case EBIKE_AUTH_INIT_PARSE_KEY_FAIL:              return "Authentication Init: Parsing of built in public key failed.";
            case EBIKE_AUTHED_COMMAND_FAIL:                   return "Authentication: Signature verification failed.";
            case EBIKE_NVS_SETTINGS_GET_FAIL:                 return "NVS Settings: Read from NVS failed.";
            case EBIKE_NVS_SETTINGS_PUT_FAIL:                 return "NVS Settings: Write to NVS failed.";
            case EBIKE_NVS_SETTINGS_CRC_MISMATCH:             return "NVS Settings: CRC check failed. Could happen when reading or writing settings.";
            case EBIKE_BMS_INIT_I2C_CONFIG_FAIL:              return "BMS Init: I2C driver configuring failed.";
            case EBIKE_BMS_INIT_I2C_INSTALL_FAIL:             return "BMS Init: I2C driver activation failed.";
            case EBIKE_BMS_I2C_BUILD_COMMAND_FAIL:            return "BMS I2C Communication: Failure while preparing I2C command to communicate with BQ769x0.";
            case EBIKE_BMS_I2C_COMMAND_FAIL:                  return "BMS I2C Communication: I2C data exchange failed. (not acknowledged or other)";
            case EBIKE_BMS_I2C_CRC_MISMATCH:                  return "BMS I2C Communication: BQ769x0 communication CRC mismatched. (explained in the chip's datasheet)";
            default: throw new IllegalArgumentException();
        }
    }
}