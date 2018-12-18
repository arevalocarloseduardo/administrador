package com.yadaapps.caear.appfuentedesodamaggys

import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.google.firebase.auth.FirebaseAuth
import com.yadaapps.caear.pedidosmaggys.Fragments.DatosImagenes
import com.yadaapps.caear.pedidosmaggys.R
import kotlinx.android.synthetic.main.menus.view.*
import java.lang.Exception
import java.util.*

class RecyclerAdapter(var list: MutableList<DatosImagenes>): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.menus,parent,false)
        return ViewHolder(v)
    }
    override fun getItemCount(): Int {
        return list.size
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerAdapter.ViewHolder, position: Int) {
        holder.bindItems(list[position])
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        lateinit var auth: FirebaseAuth
        @RequiresApi(Build.VERSION_CODES.O)
        fun bindItems(data: DatosImagenes) {
            val checkBox1 = itemView.cb_disponible
            val cant = itemView.edPrecio
            val btn = itemView.btn_quitar
            val text = data.mimageUrl
            val actualizar = itemView.bnt_actualizar
            val card = itemView.cardBtn

            val precio = data.precio
            cant.setText(precio)
            checkBox1.isSelected=data.disponible.toBoolean()

            actualizar.setOnClickListener {
                val precioA=cant.text.toString()
                val disponible=checkBox1.isChecked.toString()
                val categoria= data.categoria
                val nombreProducto = data.producto


                val ref = FirebaseDatabase.getInstance().getReference("/$categoria/$nombreProducto")//carlos= $uid creo una base de datos re piola
                val user = DatosImagenes("Carlos", nombreProducto, data.mimageUrl, precioA, data.descripcion,categoria,disponible,data.producto)//guardo la imagen y
                ref.setValue(user)

            }

            btn.setOnClickListener {
                val categoria= data.categoria
                val nombreProducto = data.producto
                val myBaseDeDatos = FirebaseDatabase.getInstance().getReference("/$categoria/")
                myBaseDeDatos.child(nombreProducto).removeValue()
            }
            itemView.progressBarMenu.visibility = View.VISIBLE
            Picasso.get().load(text).into(itemView.imagenMenu,
                object : com.squareup.picasso.Callback {
                    override fun onError(e: Exception?) {
                    }
                    override fun onSuccess() {
                        itemView.progressBarMenu.visibility = View.GONE
                    }
                })
//${data.descripcion}
            itemView.setOnClickListener{
                Toast.makeText(itemView.context,"${data.descripcion}",Toast.LENGTH_LONG).show()
            }
        }
    }
}
