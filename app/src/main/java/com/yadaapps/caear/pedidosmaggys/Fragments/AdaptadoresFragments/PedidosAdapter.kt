package com.yadaapps.caear.pedidosmaggys.Fragments.AdaptadoresFragments

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.firebase.database.FirebaseDatabase
import com.yadaapps.caear.appfuentedesodamaggys.DatosPedidos
import com.yadaapps.caear.pedidosmaggys.R
import kotlinx.android.synthetic.main.datos_pedidos.view.*

class PedidosAdapter(var list: MutableList<DatosPedidos>): RecyclerView.Adapter<PedidosAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.datos_pedidos,parent,false)
        return ViewHolder(v)
    }
    override fun getItemCount(): Int {
        return list.size
    }
    override fun onBindViewHolder(holder: PedidosAdapter.ViewHolder, position: Int) {
        holder.bindItems(list[position])
    }
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {


        fun bindItems(data: DatosPedidos){

            val menu = itemView.tvMenu1
            val cant= itemView.tvCant1
            val llevar= itemView.tvLlevar1
            val txCliente=itemView.txt_cliente
            val txDire=itemView.txt_dire
            val txTel=itemView.txt_tel
            val btnConfirmar=itemView.btnConfirmar
            val btnDelete = itemView.btnEntregar
            val prepa=itemView.tvPreparando




           // estado = itemView.tvPreparando
            txCliente.text=data.cliente
            txDire.text=data.nota
            txTel.text=data.fecha
            menu.text = data.menu
            cant.text = data.cant
            llevar.text = data.llevar
            val estado= data.estado
            when (estado) {
                "A confirmar" ->{
                    prepa.text="A confirmar"
                    prepa.setTextColor(Color.RED)
                    btnConfirmar.visibility=View.VISIBLE}
                "Preparando..." -> {
                    prepa.text="Preparando..."
                    prepa.setTextColor(Color.BLUE)
                    btnConfirmar.visibility = View.INVISIBLE
                    btnDelete.visibility = View.VISIBLE
                }
            "Listo!" -> {
                prepa.text="Listo!"
                prepa.setTextColor(Color.GREEN)
                        btnConfirmar.visibility=View.INVISIBLE
                        btnDelete.visibility=View.INVISIBLE
                    }
                }




            btnConfirmar.setOnClickListener {
                    val myBaseDeDatos = FirebaseDatabase.getInstance().getReference("Confirmados")
                    myBaseDeDatos.child(data.telefono).child("estado").setValue("Preparando...")
            }

            btnDelete.setOnClickListener {
                deleteInfo(data)
            }


        }
        private fun deleteInfo(data: DatosPedidos) {

            val myBaseDeDatos = FirebaseDatabase.getInstance().getReference("Confirmados")
            myBaseDeDatos.child(data.telefono).child("estado").setValue("Listo!")



        }
    }
}