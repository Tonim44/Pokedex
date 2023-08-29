package id.co.tonim.pokedex

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import id.co.tonim.pokedex.adapter.PokemonAdapter
import id.co.tonim.pokedex.databinding.ActivityMainBinding
import id.co.tonim.pokedex.tools.LoadingDialog
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var pokemonAdapter: PokemonAdapter
    lateinit var searchView: SearchView
    private lateinit var binding: ActivityMainBinding
    private lateinit var loading: LoadingDialog
    companion object {
        lateinit var pokemonList: MutableList<Pokemon>
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        pokemonList = mutableListOf()
        pokemonAdapter = PokemonAdapter(pokemonList)
        recyclerView.adapter = pokemonAdapter

        loading = LoadingDialog(this)
        loading.startLoading()

        fetchPokemonData()

        searchView = binding.searchView
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    performSearch(newText)
                }
                return true
            }
        })
    }

    private fun performSearch(query: String) {
        val filteredPokemonList = MainActivity.pokemonList.filter { pokemon ->
            pokemon.name!!.contains(query, ignoreCase = true)
        }
        pokemonAdapter.setPokemonList(filteredPokemonList)
    }

    private fun fetchPokemonData() {

        val client = OkHttpClient()
        val url = "https://pokeapi.co/api/v2/pokemon/"

        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                runOnUiThread {
                    loading.isDismiss()
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) throw IOException("Unexpected code $response")

                    val responseData = response.body?.string()
                    responseData?.let {
                        val jsonObject = JSONObject(it)
                        val jsonArray = jsonObject.getJSONArray("results")

                        for (i in 0 until jsonArray.length()) {
                            val pokemonObject = jsonArray.getJSONObject(i)
                            val name = pokemonObject.getString("name")
                            val pokemon = Pokemon(name)
                            pokemonList.add(pokemon)
                        }

                        runOnUiThread {
                            pokemonAdapter.notifyDataSetChanged()
                            loading.isDismiss()
                        }
                    } ?: run {
                        runOnUiThread {
                            loading.isDismiss()
                        }
                    }
                }
            }
        })
    }

}