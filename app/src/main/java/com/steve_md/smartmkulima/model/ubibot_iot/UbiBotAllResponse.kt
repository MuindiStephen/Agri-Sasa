package com.steve_md.smartmkulima.model.ubibot_iot

import com.google.gson.annotations.SerializedName

/**
 * Pull all data from the UbiBot WSI Pro (WiFi) sensor - (UbiBot Sensor)
 * That is being updated in realtime.
 * To Represent graphs & charts for each field.
 *
 * RealTime Data....
 */
data class UbiBotAllResponse(
    @SerializedName("created_at") val createdAt: Int?=10000,
    @SerializedName("created_at_ISO") val createdAtISO: String?="",
    @SerializedName("field1(Temperature)") val field1Temperature: String?="",
    @SerializedName("field2(Humidity)") val field2Humidity: String?="",
    @SerializedName("field3(Voltage)") val field3Voltage: Double?=0.0,
    @SerializedName("field4(WIFI RSSI)") val field4WifiRssi: String?="",
    @SerializedName("field5(GSM RSSI)") val field5GsmRssi: String?="",
    @SerializedName("field6(Light)") val field6Light: String?="",
    @SerializedName("field9(RS485 Soil Temperature)") val field9SoilTemperature: String?="",
    @SerializedName("field10(RS485 Soil Moisture)") val field10SoilMoisture: String?="",
    @SerializedName("lat") val latitude: String?="",
    @SerializedName("long") val longitude: String?="",
    @SerializedName("elev") val elevation: String?="",
    @SerializedName("status") val status: String?="",
    @SerializedName("serial") val serial: String?=""
)
