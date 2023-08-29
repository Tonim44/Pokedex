package id.co.tonim.pokedex

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import id.co.tonim.pokedex.adapter.AbilityAdapter
import id.co.tonim.pokedex.databinding.ActivityDetailPokemonBinding
import id.co.tonim.pokedex.tools.LoadingDialog
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class DetailPokemonActivity : AppCompatActivity() {

    private lateinit var pokemon: Pokemon
    private lateinit var binding: ActivityDetailPokemonBinding
    var namePokemon: String? = null
    private lateinit var loading: LoadingDialog

    companion object {
        const val EXTRA_POKEMON = "extra_pokemon"
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivityDetailPokemonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.back.setOnClickListener(View.OnClickListener { onBackPressed() })

        // Get the Pokemon from the intent
        pokemon = intent.getParcelableExtra<Pokemon>(EXTRA_POKEMON) as Pokemon
        namePokemon = pokemon.name
        binding.pokemonNameTextView.text = namePokemon

        loading = LoadingDialog(this)
        loading.startLoading()

        fetchPokemonData()
    }

    private fun fetchPokemonData() {

        val client = OkHttpClient()
        val url = "https://pokeapi.co/api/v2/pokemon/${namePokemon}/"

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
                        val sprites = jsonObject.getJSONObject("sprites")
                        val picture = sprites.getString("front_default")

                        // Load image into ImageView on the main thread
                        runOnUiThread {
                            val abilitiesArray = jsonObject.getJSONArray("abilities")
                            val layoutManager = LinearLayoutManager(this@DetailPokemonActivity)
                            binding.recyclerView.layoutManager = layoutManager
                            val abilityAdapter = AbilityAdapter(abilitiesArray)
                            binding.recyclerView.adapter = abilityAdapter
                            Picasso.get().load(picture).into(binding.imageView)

                            loading.isDismiss()
                        }

                        runOnUiThread {
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
