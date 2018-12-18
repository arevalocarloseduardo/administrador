package com.yadaapps.caear.pedidosmaggys.Fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.yadaapps.caear.pedidosmaggys.R
import kotlinx.android.synthetic.main.activity_subir_imagen.*
import kotlinx.android.synthetic.main.fragment_subir.*
import java.util.*


class SubirFragment : Fragment() {
    var selectedPhotoUrl :Uri? = null
    lateinit var mButtonChooseImage : Button
    lateinit var mButtonUpload : Button
    lateinit var mBar: ProgressBar
    lateinit var mImagen: ImageView
    lateinit var mEditText: EditText
    lateinit var mCate: EditText
    lateinit var mNombr: EditText
    lateinit var mPrec: EditText
    lateinit var mDesc: EditText
    lateinit var mTextView: TextView
    lateinit var mStorage: StorageReference
    lateinit var mDatabaseReference: DatabaseReference
    lateinit var referenciaImagenes : DatabaseReference
    lateinit var referenciaPedidos : DatabaseReference
    lateinit var referenciaConfirmados : DatabaseReference

    var categoriaTotal="menu"
    internal lateinit var spiner:Spinner
    internal var listaSpin= arrayOf("Menus","Postres","Bebidas","Platos Frios","Platos a la carta","Combos","Licuados","Comidas Rapidas","Sandwiches")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spiner = spinner
        val arrayAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_dropdown_item, listaSpin)
        spiner?.adapter = arrayAdapter
        mButtonChooseImage=mbutton
        mButtonUpload=mbutton2


        mButtonUpload.setOnClickListener {
            subirImagenAFirebase()
        }
        mButtonChooseImage.setOnClickListener {
            OpenfileChooser()
        }

        spiner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (parent?.getItemAtPosition(position)) {
                    "Menus" -> categoriaTotal="menus"
                    "Postres" ->  categoriaTotal="postres"
                    "Bebidas" -> categoriaTotal="bebidas"
                    "Platos Frios" -> categoriaTotal="platosfrios"
                    "Platos a la carta" -> categoriaTotal="platosalacarta"
                    "Combos" -> categoriaTotal="combos"
                    "Licuados" -> categoriaTotal="licuados"
                    "Comidas Rapidas" -> categoriaTotal="comidarapida"
                    "Sandwiches" -> categoriaTotal="sandwiches"

                }
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        val v=inflater.inflate(R.layout.fragment_subir, container, false)
        mStorage= FirebaseStorage.getInstance().getReference("uploads")
        mDatabaseReference=FirebaseDatabase.getInstance().getReference("uploads")



        referenciaImagenes = FirebaseDatabase.getInstance().getReference("usuarios")
        referenciaPedidos = FirebaseDatabase.getInstance().getReference("Pedidos")
        referenciaConfirmados = FirebaseDatabase.getInstance().getReference("Confirmados")




        return v
}
    private fun subirImagenAFirebase() {

        if (selectedPhotoUrl==null)return//si no hay imagen seleccionada que salga de la funcion
        val filename = UUID.randomUUID().toString()// guardo una variable con un nombre ramdom
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")//indico donde guardar la imagen juntamente con el nombre ramdom
        ref.putFile(selectedPhotoUrl!!)
            .addOnSuccessListener {
                //y guarda la imagen en la direccion donde le indique
                Log.d("tag", "yape:${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("tag", "yase:$it")
                    mBar=mprogressBar
                    mImagen=mimageView
                    mEditText=meditText
                    mPrec =edtPrecio
                    mNombr=edtNombrePro
                    mDesc=edtDesc

                    val nombreP= mNombr.text.toString()
                    val prec= mPrec.text.toString()
                    val descri= mDesc.text.toString()
                    salvarUsuarioFirebase(it.toString(),categoriaTotal,nombreP,prec,descri)
                    Picasso.get().load(it.toString()).into(mImagen)
                }}
            .addOnFailureListener {//obtengo la direccion de la imagen subida a firebase y se la mando a la funcion
            }
    }

    private fun salvarUsuarioFirebase(imagenUrl: String,categoria: String,nombreProducto: String,precio: String,descripcion: String
    ) //recibo la direccion de la imagen
    {

        val ref = FirebaseDatabase.getInstance().getReference("/$categoria/$nombreProducto")//carlos= $uid creo una base de datos re piola
        val user = DatosImagenes("Carlos", nombreProducto, imagenUrl, precio, descripcion,categoriaTotal,"true",nombreProducto)//guardo la imagen y
        ref.setValue(user)
    }

    private fun OpenfileChooser() {
        //esta funcion sirve para abrir la galeria y seleccionar una foto
        val intent = Intent(Intent.ACTION_PICK)//ACTION_GET_CONTEN
        intent.type = "image/*"
        startActivityForResult(intent,0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==0 && resultCode == Activity.RESULT_OK && data !=null){
            //se selecciona una imagen de la galeria

            // intent.flags=Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)// con esto se cierra todas las activitys anteriores

            selectedPhotoUrl = data.data
            //se guarda el dato en selecedPhotoUrl
            //val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedPhotoUrl)
            //val bitmapDrawable = BitmapDrawable(bitmap)
            //mbutton.setBackgroundDrawable(bitmapDrawable)
        }
    }


    companion object {
        fun newInstance(): SubirFragment{
            val fragment=SubirFragment()
            return fragment
        }
    }
}