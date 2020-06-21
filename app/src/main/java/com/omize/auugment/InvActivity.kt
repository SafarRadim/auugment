package com.omize.auugment

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*

class InvActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        var listx = findViewById<ListView>(R.id.invlist)
        var newitems = ItemAdds(this)
        listx.adapter = newitems

        listx.setOnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
            Toast.makeText(this, "Líní vývojáři mi nepřidělili žádnou extra funkci: Já jsem ${newitems.itemnames[position]}", Toast.LENGTH_LONG).show()
        }
    }
}

class ItemAdds(private val context: Activity) : BaseAdapter() {
    val itemnames = arrayOf("Pomeranč", "Maso", "Slanina", "Vajičko", "Volské oko", "Sůl", "Brambora", "Hranolky", "Pšenice", "Chléb")
    val itemress = arrayOf("O0", "O1", "O2", "O3", "O4", "O5", "O6", "O7", "O8", "O9")
    val itemimgs = arrayOf(R.drawable.orange, R.drawable.meat, R.drawable.bacon, R.drawable.egg, R.drawable.eggcooked, R.drawable.salt, R.drawable.potato, R.drawable.fries, R.drawable.wheat, R.drawable.bread)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var xx = context.layoutInflater.inflate(R.layout.invrow, null)
        var itemname = xx.findViewById<TextView>(R.id.foodname)
        var itemcount = xx.findViewById<TextView>(R.id.foodcount)
        var itemimg = xx.findViewById<ImageView>(R.id.foodico)
        var sharedPref = context.getSharedPreferences("inv", Context.MODE_PRIVATE)
        var t1 = sharedPref.getInt(itemress[position], 0).toString()
        var t2 = "${t1}x"
        itemname.setText(itemnames[position])
        itemcount.setText(t2)
        itemimg.setImageResource(itemimgs[position])
        return xx
    }

    override fun getCount(): Int {
        return itemimgs.size
    }

    override fun getItem(position: Int): Any {
        return itemimgs[position]
    }

    override fun getItemId(position: Int): Long {
        return itemimgs[position].toLong()
    }

}