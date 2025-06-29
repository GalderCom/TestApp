package com.example.testapp_20

import kotlinx.serialization.Serializable

class DataClass {
    @Serializable
     data class Bin(
         var bin: String?,
         val number: NumberInfo?,
         val scheme: String?,
         val type: String?,
         val brand: String?,
         val prepaid: Boolean?,
         val country: Country?,
         val bank: Bank?
    ){
        @Serializable
        data class NumberInfo(
            val length: Int?,
            val luhn: Boolean?
        )
        @Serializable
        data class Country(
            val numeric: String?,
            val alpha2: String?,
            val name: String?,
            val emoji: String?,
            val currency: String?,
            val latitude: Int?,
            val longitude: Int?
        )
        @Serializable
        data class Bank(
            val name: String?,
            val url: String?,
            val phone: String?,
            val city: String?
        )
    }
}