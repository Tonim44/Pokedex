package id.co.tonim.pokedex.adapter

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.co.tonim.pokedex.DetailPokemonActivity
import id.co.tonim.pokedex.Pokemon
import id.co.tonim.pokedex.R

class PokemonAdapter (private var pokemonList: List<Pokemon>) : RecyclerView.Adapter<PokemonAdapter.NewsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false)
        return NewsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentPokemon = pokemonList[position]
        holder.nameTextView.text = currentPokemon.name

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailPokemonActivity::class.java)
            intent.putExtra(DetailPokemonActivity.EXTRA_POKEMON, currentPokemon)
            holder.itemView.context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return pokemonList.size
    }

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setPokemonList(list: List<Pokemon>) {
        pokemonList = list
        notifyDataSetChanged()
    }
}