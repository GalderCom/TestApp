package com.example.testapp_20

class DataClass {

     class Bin(
        val number: NumberInfo?,
        val scheme: String?,
        val type: String?,
        val brand: String?,
        val prepaid: Boolean?,
        val country: Country?,
        val bank: Bank?
    ) {
        data class NumberInfo(
            val length: Int?,
            val luhn: Boolean?
        )

        data class Country(
            val numeric: String?,
            val alpha2: String?,
            val name: String?,
            val emoji: String?,
            val currency: String?,
            val latitude: Int?,
            val longitude: Int?
        )

        data class Bank(
            val name: String?,
            val url: String?,
            val phone: String?,
            val city: String?
        )
    }
}