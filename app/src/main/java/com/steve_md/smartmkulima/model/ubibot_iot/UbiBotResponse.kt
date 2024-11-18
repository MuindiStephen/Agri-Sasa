package com.steve_md.smartmkulima.model.ubibot_iot

import com.google.gson.annotations.SerializedName

/**
 * Fetch only the latest data for each field
 * from the UbiBot WSI Pro (WiFi) sensor - (UbiBot Sensor)
 */
data class UbiBotResponse(
    @SerializedName("created_at") val createdAt: Int,
    @SerializedName("created_at_ISO") val createdAtISO: String,
    @SerializedName("field1(Temperature)") val field1Temperature: Double,
    @SerializedName("field2(Humidity)") val field2Humidity: Int,
    @SerializedName("field3(Voltage)") val field3Voltage: Double,
    @SerializedName("field5(GSM RSSI)") val field5GsmRssi: Int,
    @SerializedName("field6(Light)") val field6Light: Double,
    @SerializedName("field9(RS485 Soil Temperature)") val field9SoilTemperature: Double,
    @SerializedName("field10(RS485 Soil Moisture)") val field10SoilMoisture: Int,
    @SerializedName("status") val status: String,
    @SerializedName("serial") val serial: String
)
/*

sample response....
{
  "created_at": 1731918777,
  "created_at_ISO": "2024-11-18T11:32:57+03:00",
  "field1(Temperature)": 23.24025,
  "field2(Humidity)": 71,
  "field3(Voltage)": 3.805625,
  "field5(GSM RSSI)": -57,
  "field6(Light)": 34.57,
  "field9(RS485 Soil Temperature)": 19.799999,
  "field10(RS485 Soil Moisture)": 0,
  "status": "{\"status\":\"ICCID=89254021244295930511,usb=0\",\"ICCID\":\"89254021244295930511\",\"usb\":\"0\"}",
  "serial": "S33H931VGS1"
}

 */
