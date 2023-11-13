package com.websocket

import android.content.Context
import com.facebook.react.bridge.Promise
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.host
import io.ktor.server.application.install
import io.ktor.server.application.port
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.response.*
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import java.time.Duration
import android.provider.Settings.Secure
import io.ktor.server.response.respond
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import io.ktor.http.*

fun Application.module(context: Context){
  @Serializable
  data class Device(val deviceID: String)
  val devices = mutableListOf<Device>()

  fun getDeviceId(): String{
    return Secure.getString(context.contentResolver, Secure.ANDROID_ID)
  }


  install(WebSockets) {
    pingPeriod = Duration.ofSeconds(15)
    timeout = Duration.ofSeconds(15)
    maxFrameSize = Long.MAX_VALUE
    masking = false
  }

  install(ContentNegotiation) {
    json(Json {
      prettyPrint = true
      isLenient = true
    })
  }

  routing {
    get("/") {
      call.respondText("Hello World!")
    }

    get("/hi") {
      call.respondText("Hello ${call.request.queryParameters["name"]}")
    }

    get("/get") {
      try {
        val deviceId = getDeviceId()
        devices.add(Device(deviceID = deviceId))
        call.respond(Device(deviceID = deviceId))
      } catch (e: Exception) {
        // Log the exception for debugging purposes
        e.printStackTrace()

        // Respond with a 500 Internal Server Error and a descriptive error message
        call.respondText("Internal Server Error: ${e.message}", status = HttpStatusCode.InternalServerError)
      }
    }

    webSocket("/echo") {
      send(Frame.Text("Please enter your name"))
      for (frame in incoming) {
        frame as? Frame.Text ?: continue
        val receivedText = frame.readText()
        if (receivedText.equals("bye", ignoreCase = true)) {
          close(CloseReason(CloseReason.Codes.NORMAL, "Client said BYE"))
        } else {
          send(Frame.Text("Hi, $receivedText!"))
        }
      }
    }
  }
}

class WebsocketModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

//  private val socketServer = embeddedServer(CIO, host = "0.0.0.0", port = 8080, module = Application::module(context))
  private val socketServer = embeddedServer(CIO, host = "0.0.0.0", port = 8080, module = { module(reactContext.baseContext) })


  override fun getName(): String {
    return NAME
  }

  @ReactMethod
  fun wake(promise: Promise) {

    val status = socketServer.start(wait = false)

    val host = status.environment.config.host;
    val port = status.environment.config.port;

    promise.resolve("Server started on $host:$port")
  }

  @ReactMethod
  fun sleep(promise: Promise) {
    socketServer.stop()

    promise.resolve("Server went to sleep")
  }

  companion object {
    const val NAME = "Websocket"
  }
}
