package com.example.testapp_20.Activitys

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp_20.CustomAdapterHistory
import com.example.testapp_20.DataClass
import com.example.testapp_20.databinding.ActivityHistoryBinding
import kotlinx.serialization.json.Json

private lateinit var id: ActivityHistoryBinding
class HistoryActivity : AppCompatActivity() {


    lateinit var mRecyclerHistory: RecyclerView;

    lateinit var customAdapterHistory: CustomAdapterHistory;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        id = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(id.root)

        mRecyclerHistory = id.historyList

        customAdapterHistory = CustomAdapterHistory(loadHistoryFromPrefs())
        mRecyclerHistory.adapter = customAdapterHistory;



        mRecyclerHistory.adapter = customAdapterHistory

        id.btnBack.setOnClickListener {
            finish()
        }

    }

    private fun loadHistoryFromPrefs(): ArrayList<DataClass.Bin> {
        val prefs = getSharedPreferences("BIN_HISTORY_PREFS", MODE_PRIVATE)
        val jsonString = prefs.getString("history_list", null)
        return if (jsonString != null) {

                ArrayList(Json.decodeFromString<List<DataClass.Bin>>(jsonString))

        } else {
            ArrayList()
        }
    }


}