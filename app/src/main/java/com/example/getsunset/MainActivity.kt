package com.example.getsunset

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.example.getsunset.databinding.ActivityMainBinding
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
    fun GetSunset(view:View){
        var city=findViewById<EditText>(R.id.etCityName).text.toString()
        val url="https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+ city +"%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys"
        MyAsyncTask().execute(url)


    }
    inner class MyAsyncTask: AsyncTask<String, String, String>() {

        private fun ConvertStreamToString(inputStream: InputStream?): String? {
            val bufferReader= BufferedReader(InputStreamReader(inputStream))
            var line:String
            var AllString:String=""
            try {
                do{
                    line=bufferReader.readLine()
                    if(line!=null){
                        AllString+=line
                    }
                }while (line!=null)
                inputStream?.close()
            }
            catch (ex:Exception){}
            return AllString

        }

        override fun doInBackground(vararg p0: String?): String {

            try {
                val url= URL(p0[0])

                val urlConnect=url.openConnection() as HttpURLConnection
                urlConnect.connectTimeout=7000

                var inString= ConvertStreamToString(urlConnect.inputStream)
                //Cannot access to ui
                publishProgress(inString)

            }
            catch (ex:Exception){}
            return " "
        }

        override fun onProgressUpdate(vararg values: String?) {
            var json= JSONObject(values[0])
            val query=json.getJSONObject("query")
            val results=query.getJSONObject("results")
            val channel=results.getJSONObject("channel")
            val astronomy=channel.getJSONObject("astronomy")
            var sunrise=astronomy.getString("sunrise")

            binding.tvSunSetTime.text = " Sunrise time is "+ sunrise
        }

    }
}
