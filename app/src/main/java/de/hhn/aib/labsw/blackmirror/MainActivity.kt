package de.hhn.aib.labsw.blackmirror

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.fasterxml.jackson.databind.JsonNode
import de.hhn.aib.labsw.blackmirror.controller.API.MirrorApiListener
import de.hhn.aib.labsw.blackmirror.controller.API.TopicListener
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : AppCompatActivity(), TopicListener {

    val mas = MirrorApiListener()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //example for sending
        val httpClient = OkHttpClient()
        val request = Request.Builder()
            .url("ws://10.0.2.2:80")
            .build()

        val webSocket = httpClient.newWebSocket(request, mas)
        webSocket.send("{\n" +
                "  \"topic\":\"location\",\n" +
                "  \"payload\":{\n" +
                "    \"lat\":49.12194481375503,\n" +
                "    \"lon\":9.211276846308193\n" +
                "  }\n" +
                "}")

        // subscribe this activity
        mas.subscribe("location", this)

        val helloBtn = findViewById<Button>(R.id.btn_hello)
        helloBtn.setOnClickListener {
            val helloWorldLbl = findViewById<TextView>(R.id.lbl_helloWorld)
            helloWorldLbl.visibility = View.VISIBLE;
        }
    }

    override fun dataReceived(topic: String?, `object`: JsonNode?) {
        println("data received")
        val mapper = mas.getJSONMapper()
        println(mapper.writeValueAsString(`object`))
    }
}