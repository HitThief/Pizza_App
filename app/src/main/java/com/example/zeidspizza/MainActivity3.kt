package com.example.zeidspizza

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast

class MainActivity3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        val button: Button = findViewById(R.id.buttonConfirm)

        button.setOnClickListener {
            val price = intent.extras?.get("price")
            val pizzatype = intent.extras?.get("pizzatype")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            val text = "Your Order is on the way"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, text, duration)
            onClickAddName(price as Int, pizzatype as String)
            toast.show()

        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.my_menu, menu)
        return super.onCreateOptionsMenu(menu)    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var report= ReportClass()
        when(item.itemId){
            R.id.subitem1 -> report.show(supportFragmentManager, "Custom Dialog")




            R.id.subitem2 -> android.os.Process.killProcess(android.os.Process.myPid())
        }
        return true
    }

    fun onClickAddName(price: Int, pizzatype: String) {
        val values = ContentValues()
        values.put(
            PizzaProvider.PIZZA_NAME,
            pizzatype


        )
        values.put(
            PizzaProvider.PIZZA_PRICE, price
        )

        val uri = contentResolver.insert(
            PizzaProvider.CONTENT_URI, values
        )

        Toast.makeText(baseContext, uri.toString(), Toast.LENGTH_LONG).show()
    }
}