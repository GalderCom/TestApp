package com.example.testapp_20.Activitys

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.testapp_20.ConnectApi
import com.example.testapp_20.R
import com.example.testapp_20.databinding.ActivityMainBinding

import androidx.lifecycle.lifecycleScope
import androidx.transition.Visibility
import com.example.testapp_20.DataClass
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException


class MainActivity : AppCompatActivity() {

    private lateinit var id: ActivityMainBinding
    private val client = ConnectApi()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        id = ActivityMainBinding.inflate(layoutInflater)
        setContentView(id.root )



            var itemCard: DataClass.Bin?

            var bin = ""
            id.btnMetaData.setOnClickListener {

                if (id.editText.text.toString() != bin)
                {
                    if (id.editText.text.length >= 6) {
                        lifecycleScope.launch {
                            try {
                                bin = id.editText.text.toString()
                                val response = client.getBinInfo(bin)

                                if (response.isSuccessful) {
                                    val binInfo = response.body()

                                    if (binInfo != null) {

                                        itemCard = binInfo

                                        id.tvCountry.text = binInfo.country?.name
                                        id.tvLocate.text = binInfo.country?.latitude.toString()+ " " +  binInfo.country?.longitude.toString()
                                        id.tvPhone.text = binInfo.bank?.phone
                                        id.tvBank.text = binInfo.bank?.name
                                        id.tvType.text= binInfo.scheme
                                        id.tvUrl.text = binInfo.bank?.url
                                        id.tvCity.text = binInfo.bank?.city
                                        id.layoutCard.visibility = View.VISIBLE
                                    }

                                } else {
                                    val errorMsg = when (response.code()) {
                                        404 -> "BIN не найден"
                                        429 -> "Слишком много запросов"
                                        in 500..599 -> "Ошибка сервера"
                                        else -> "Ошибка: ${response.code()}"
                                    }
                                    bin = ""
                                    Toast.makeText(
                                        this@MainActivity,
                                        errorMsg,
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } catch (e: SocketTimeoutException) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Таймаут соединения. Проверьте интернет",
                                    Toast.LENGTH_LONG
                                ).show()

                            } catch (e: ConnectException) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Нет подключения к интернету",
                                    Toast.LENGTH_LONG
                                ).show()
                            } catch (e: IOException) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Сетевая ошибка: ${e.localizedMessage}",
                                    Toast.LENGTH_LONG
                                ).show()
                            } catch (e: Exception) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Ошибка: ${e.localizedMessage}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    }
                    else{
                        Toast.makeText(
                            this@MainActivity,
                            "Недостаточно символов",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }








        /*     ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
    }



}