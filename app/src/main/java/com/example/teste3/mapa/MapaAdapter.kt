package com.example.teste3.mapa

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.teste3.views.CustomMapView

data class AndarMapa(
    val drawableRes: Int,
    val pontoX: Float,
    val pontoY: Float,
    val temPonto: Boolean
)

class MapaAdapter(private val andares: List<AndarMapa>) :
    RecyclerView.Adapter<MapaAdapter.MapaViewHolder>() {

    inner class MapaViewHolder(val mapaView: CustomMapView) :
        RecyclerView.ViewHolder(mapaView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapaViewHolder {
        val mapaView = CustomMapView(parent.context)
        mapaView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        return MapaViewHolder(mapaView)
    }

    override fun onBindViewHolder(holder: MapaViewHolder, position: Int) {
        val andar = andares[position]
        if (andar.temPonto) {
            holder.mapaView.setMapa(andar.drawableRes, andar.pontoX, andar.pontoY)
        } else {
            holder.mapaView.setMapa(andar.drawableRes, -1f, -1f)
        }
    }

    override fun getItemCount() = andares.size
}