package com.example.testapp_20.Activitys

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.provider.Telephony
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.testapp_20.ConnectApi
import com.example.testapp_20.R
import com.example.testapp_20.databinding.ActivityMainBinding
import androidx.lifecycle.lifecycleScope
import com.example.testapp_20.DataClass
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException

class MainActivity : AppCompatActivity() {

    private lateinit var id: ActivityMainBinding
    private val client = ConnectApi()
    private var currentBin = ""

    private var historyArray:ArrayList<DataClass.Bin> = ArrayList();

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        id = ActivityMainBinding.inflate(layoutInflater)
        setContentView(id.root)


        historyArray = loadHistoryFromPrefs()


        id.btnHistory.setOnClickListener {


            if (historyArray.size>=1)
            {
                val intent = Intent(this@MainActivity, HistoryActivity::class.java)
                startActivity(intent)

            }
            else{
                showToast("История пуста")
            }

        }


        id.btnMetaData.setOnClickListener {
            val inputBin = id.editText.text.toString()
            when {
                inputBin == currentBin -> return@setOnClickListener
                inputBin.length < 6 -> showToast("Недостаточно символов")
                else -> getData(inputBin)
            }
        }
    }

    private fun saveHistoryToPrefs(history: ArrayList<DataClass.Bin>) {
        val prefs = getSharedPreferences("BIN_HISTORY_PREFS", MODE_PRIVATE)
        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
        }
        val jsonString = json.encodeToString(history)
        prefs.edit().putString("history_list", jsonString).apply()
    }

    private fun loadHistoryFromPrefs(): ArrayList<DataClass.Bin> {
        val prefs = getSharedPreferences("BIN_HISTORY_PREFS", MODE_PRIVATE)
        val jsonString = prefs.getString("history_list", null)

        return if (jsonString != null) {
            try {
                ArrayList(Json.decodeFromString<List<DataClass.Bin>>(jsonString))
            } catch (e: Exception) {
                ArrayList()
            }
        } else {
            ArrayList()
        }
    }


    private fun getData(bin: String) {
        id.pbLoading.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                val response = client.getBinInfo(bin)
                if (response.isSuccessful) {
                    response.body()?.let { binInfo ->
                        currentBin = bin
                        binInfo.bin = bin
                        displayBinInfo(binInfo)

                        historyArray.add(binInfo)
                        saveHistoryToPrefs(historyArray)

                    } ?: showToast("Данные не найдены")
                } else {
                    handleError(response.code())
                }
            } catch (e: SocketTimeoutException) {
                showToast("Таймаут соединения. Проверьте интернет")
            } catch (e: ConnectException) {
                showToast("Нет подключения к интернету")
            } catch (e: IOException) {
                showToast("Сетевая ошибка: ${e.localizedMessage}")
            } catch (e: Exception) {
                showToast("Ошибка: ${e.localizedMessage}")
            } finally {
                id.pbLoading.visibility = View.GONE
            }
        }
    }



    @SuppressLint("SetTextI18n")
    private fun displayBinInfo(binInfo: DataClass.Bin) {
        with(binInfo) {
            id.tvCountry.text = country?.name ?: "N/A"
            id.tvLocate.text = "${country?.latitude ?: "N/A"} ${country?.longitude ?: "N/A"}"
            id.tvBank.text = bank?.name ?: "N/A"
            id.tvType.text = scheme ?: "N/A"
            id.tvUrl.text = bank?.url ?: "N/A"
            id.tvPhone.text = bank?.phone ?: "N/A"
            id.tvCity.text = bank?.city ?: "N/A"
        }
        id.layoutCard.visibility = View.VISIBLE
    }

    private fun handleError(code: Int) {
        currentBin = ""
        val errorMsg = when (code) {
            404 -> "BIN не найден"
            429 -> "Слишком много запросов"
            in 500..599 -> "Ошибка сервера"
            else -> "Ошибка: $code"
        }
        showToast(errorMsg)
    }

    private fun showToast(message: String) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
    }
}