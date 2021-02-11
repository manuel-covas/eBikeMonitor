package pt.manuelcovas.ebikemonitor.datatypes.ESPeBike;

public enum ESPeBikeResponseType {

    EBIKE_COMMAND_NONE,
    EBIKE_COMMAND_LOG_RETRIEVE,
    EBIKE_COMMAND_GET_SETTINGS,
    EBIKE_COMMAND_GET_ADC_CHARACTERISTICS,
    EBIKE_COMMAND_SYSTEM_STATS_STREAM,
    EBIKE_RESPONSE_SYSTEM_STATS_UPDATE,
    EBIKE_COMMAND_AUTH_GET_CHALLENGE,
    EBIKE_COMMAND_AUTHED_COMMAND,
    EBIKE_COMMAND_AUTHED_COMMAND_PUT_SETTINGS,
    EBIKE_COMMAND_AUTHED_COMMAND_TOGGLE_UNLOCK;

    @Override
    public String toString() {
        switch(this) {
            case EBIKE_COMMAND_LOG_RETRIEVE:                 return "Retreive Log";
            case EBIKE_COMMAND_GET_SETTINGS:                 return "Get ESP-eBike NVS settings";
            case EBIKE_COMMAND_GET_ADC_CHARACTERISTICS:      return "Get shunt value and BQ769x0 ADC characteristics";
            case EBIKE_COMMAND_SYSTEM_STATS_STREAM:          return "Toggle system stats stream";
            case EBIKE_RESPONSE_SYSTEM_STATS_UPDATE:         return "System stats stream update packet";
            case EBIKE_COMMAND_AUTH_GET_CHALLENGE:           return "Get currently active authentication challenge";
            case EBIKE_COMMAND_AUTHED_COMMAND:               return "Run authenticated command";
            case EBIKE_COMMAND_AUTHED_COMMAND_PUT_SETTINGS:  return "[Authenticated] Overwrite ESP-eBike NVS settings";
            case EBIKE_COMMAND_AUTHED_COMMAND_TOGGLE_UNLOCK: return "[Authenticated] Unlock or lock ESP-eBike";
            default: throw new IllegalArgumentException();
        }
    }

}