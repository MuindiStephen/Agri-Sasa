package com.steve_md.smartmkulima.data.room.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.steve_md.smartmkulima.model.AgroDealerOffers
import com.steve_md.smartmkulima.model.FarmInputAgroDealerCartItem

class FarmInputCartItemConverters {

    // Convert List<FarmInputAgroDealerCartItem> to a JSON String
    @TypeConverter
    fun fromCartOrderList(cartOrder: List<FarmInputAgroDealerCartItem>): String {
        val gson = Gson()
        return gson.toJson(cartOrder)
    }

    // Convert a JSON String back to List<FarmInputAgroDealerCartItem>
    @TypeConverter
    fun toCartOrderList(cartOrderString: String): List<FarmInputAgroDealerCartItem> {
        val gson = Gson()
        val listType = object : TypeToken<List<FarmInputAgroDealerCartItem>>() {}.type
        return gson.fromJson(cartOrderString, listType)
    }

    // Convert FarmInputAgroDealerCartItem to a JSON String
    @TypeConverter
    fun fromCartItem(cartItem: FarmInputAgroDealerCartItem): String {
        val gson = Gson()
        return gson.toJson(cartItem)
    }

    // Convert a JSON String back to FarmInputAgroDealerCartItem
    @TypeConverter
    fun toCartItem(cartItemString: String): FarmInputAgroDealerCartItem {
        val gson = Gson()
        val itemType = object : TypeToken<FarmInputAgroDealerCartItem>() {}.type
        return gson.fromJson(cartItemString, itemType)
    }

    // Convert AgroDealerOffers to a JSON String
    @TypeConverter
    fun fromAgroDealerOffers(offers: AgroDealerOffers): String {
        val gson = Gson()
        return gson.toJson(offers)
    }

    // Convert a JSON String back to AgroDealerOffers
    @TypeConverter
    fun toAgroDealerOffers(offersString: String): AgroDealerOffers {
        val gson = Gson()
        val offersType = object : TypeToken<AgroDealerOffers>() {}.type
        return gson.fromJson(offersString, offersType)
    }
}
