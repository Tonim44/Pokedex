package id.co.tonim.pokedex.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import id.co.tonim.pokedex.R
import org.json.JSONArray
import org.json.JSONObject

class AbilityAdapter(private val abilities: JSONArray) : RecyclerView.Adapter<AbilityAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val abilityNameTextView: TextView = itemView.findViewById(R.id.nameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_ability, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val abilityObject: JSONObject = abilities.getJSONObject(position).getJSONObject("ability")
        val abilityName = abilityObject.getString("name")
        holder.abilityNameTextView.text = abilityName
    }

    override fun getItemCount(): Int {
        return abilities.length()
    }
}
