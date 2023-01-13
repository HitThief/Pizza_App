package com.example.zeidspizza

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast

import android.widget.*


class MainActivity : AppCompatActivity() {
    var price: Int = 0
    var options2 = arrayOf(
        "Pepperoni Pizza",
        "BBQ Chicken Pizza",
        "Buffalo Pizza",
        "Hawaiian Pizza",
        "Margherita Pizza"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button: Button = findViewById(R.id.order)
        val button2: Button = findViewById(R.id.proceed)
        val buttonDelete: Button = findViewById(R.id.buttonDelete)
        val edtxt1: EditText = findViewById(R.id.NumOfPizzas)
        val total: TextView = findViewById(R.id.total)
        val sizeval: Spinner = findViewById(R.id.size)
        val typeVal: Spinner = findViewById(R.id.type)
        var flag: String = "Large"
        var pizzatype = options2[0]
        var options1 = arrayOf("Large", "Medium", "Small")
        sizeval.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options1)
        typeVal.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options2)

        buttonDelete.setOnClickListener(){
            contentResolver.delete(PizzaProvider.CONTENT_URI, null, null)
        }

        button.setOnClickListener { view ->
            var x: Int = edtxt1.text.toString().toInt();
            if (flag == "Large") {
                price = 4
                total.text = "Your Total Is : " + mul(x, price).toString();
            } else if (flag == "Medium") {
                price = 3
                total.text = "Your Total Is : " + mul(x, price).toString();
            } else {
                price = 2
                total.text = "Your Total Is : " + mul(x, price).toString();
            }

            button2.setVisibility(View.VISIBLE)

        }
        button2.setOnClickListener { view ->
            val intent = Intent(this, MainActivity2::class.java)
            intent.putExtra("price", price)
            intent.putExtra("pizzatype", pizzatype)
            startActivity(intent)
        }


        sizeval.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                flag = options1.get(p2) //p2 is the index of selected item
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }


        }

        typeVal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                 pizzatype = options2.get(p2) //p2 is the index of selected item
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }


        }



    }
        override fun onCreateOptionsMenu(menu: Menu): Boolean {
            val inflater = menuInflater
            inflater.inflate(R.menu.my_menu, menu)
            return super.onCreateOptionsMenu(menu)
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            var report = ReportClass()
            when (item.itemId) {
                R.id.subitem1 -> report.show(supportFragmentManager, "Custom Dialog")


                R.id.subitem2 -> android.os.Process.killProcess(android.os.Process.myPid())
            }
            return true;
        }




        fun onClickRetrieveStudents(view: View?) {
            val URL = "content://com.example.MyApplication.PizzaProvider"
            val pizzas = Uri.parse(URL)
            //  val c = contentResolver!!.query(students,null,null,null,"name"
            var c = contentResolver.query(pizzas, null, null, null, null)
            if (c != null) {
                if (c?.moveToFirst() == true) {
                    do {

                        Toast.makeText(
                            this,
                            c.getString(c.getColumnIndex(PizzaProvider._ID)) + ", " + c.getString(
                                c.getColumnIndex(
                                    PizzaProvider.PIZZA_NAME
                                )
                            ) + ", " + c.getString(
                                c.getColumnIndex(
                                    PizzaProvider.PIZZA_PRICE
                                )
                            ),
                            Toast.LENGTH_SHORT
                        ).show()
                    } while (c.moveToNext())

                }
            }

        }
    public fun mul(a: Int, b: Int): Int {
        return a * b;}



    }









