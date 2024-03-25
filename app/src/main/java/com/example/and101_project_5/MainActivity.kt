package com.example.and101_project_5

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var searchInput: EditText
    private lateinit var searchButton: ImageButton
    private lateinit var nextPokemon: Button
    private lateinit var pokeImg: ImageView
    private lateinit var pokeName: TextView
    private lateinit var heightValue: TextView
    private lateinit var weightValue: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        searchInput = findViewById(R.id.searchInput)
        searchButton = findViewById(R.id.searchButton)
        nextPokemon = findViewById(R.id.nextPokemon)
        pokeImg = findViewById(R.id.pokeImg)
        pokeName = findViewById(R.id.pokeName)
        heightValue = findViewById(R.id.hValue)
        weightValue = findViewById(R.id.wValue)


        setupListeners()
    }

    private fun setupListeners() {
        searchButton.setOnClickListener {
            val query = searchInput.text.toString().trim()
            if (query.isNotEmpty()) {
                fetchPokemonData(query)
            }
        }

        nextPokemon.setOnClickListener {
            fetchPokemonData((0..1301).random().toString())
        }
    }

    private fun fetchPokemonData(query: String) {
        val client = AsyncHttpClient()
        val url = "https://pokeapi.co/api/v2/pokemon/$query"

        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JsonHttpResponseHandler.JSON) {
                // Parse the response
                val jsonObject = json.jsonObject
                val name = jsonObject.getString("name")
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                val height = jsonObject.getInt("height")
                val weight = jsonObject.getInt("weight")
                val imageUrl = jsonObject.getJSONObject("sprites").getString("front_default")

                // Update UI with fetched data
                pokeName.text = name
                heightValue.text = "${height / 10.0} m"
                weightValue.text = "${weight / 10.0} kg"
                Glide.with(this@MainActivity).load(imageUrl).into(pokeImg)
            }

            override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?) {
                Log.e("Pokemon API", "Failure $response", throwable)
            }
        })
    }
}