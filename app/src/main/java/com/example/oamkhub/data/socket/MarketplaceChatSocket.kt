package com.example.oamkhub.data.socket

import android.util.Log
import com.example.oamkhub.data.network.RetrofitInstance
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject

object MarketplaceChatSocket {
    private const val TAG        = "ChatSocket"
    private val SERVER_URL get() = RetrofitInstance.BASE_URL

    private var socket: Socket? = null

    /** Call once with the logged‑in user’s JWT. */
    fun init(token: String) {
        // if we already had a socket, disconnect it first
        socket?.disconnect()

        val opts = IO.Options().apply {
            forceNew      = true
            reconnection  = true
            transports    = arrayOf("websocket")
            auth          = mapOf("token" to "Bearer $token")
        }
        socket = IO.socket(SERVER_URL, opts).apply {
            on(Socket.EVENT_CONNECT)       { Log.d(TAG, "✅ CONNECTED (${id()})") }
            on(Socket.EVENT_CONNECT_ERROR) { args -> Log.e(TAG, "❌ CONNECT_ERROR: ${args.getOrNull(0)}") }
            on(Socket.EVENT_DISCONNECT)    { Log.d(TAG, "⚠️ DISCONNECTED") }
        }
    }

    fun connect() {
        socket?.takeUnless { it.connected() }?.let {
            Log.d(TAG, "Attempting socket.connect()…")
            it.connect()
        }
    }

    fun disconnect() {
        socket?.takeIf { it.connected() }?.disconnect()
    }

    fun joinRoom(marketplaceId: String, buyerId: String) {
        socket?.emit("joinRoom", JSONObject().apply {
            put("marketplace_id", marketplaceId)
            put("buyer_id", buyerId)
        })
    }

    /** Send one chat message. */
    fun sendChat(
        marketplaceId: String,
        buyerId:       String,
        message:       String,
        userId:        String
    ) {
        socket?.emit("chatMessage", JSONObject().apply {
            put("marketplace_id", marketplaceId)
            put("buyer_id",       buyerId)
            put("messages",       message)
            put("user_id",        userId)
        })
    }

    /** Listen for incoming chat messages. */
    fun onNewMessage(listener: (JSONObject) -> Unit): Emitter.Listener {
        val l = Emitter.Listener { args ->
            (args.firstOrNull() as? JSONObject)?.let(listener)
        }
        socket?.on("newMessage", l)
        return l
    }
    fun offNewMessage(listener: Emitter.Listener) {
        socket?.off("newMessage", listener)
    }

    /** Listen for new‐thread notifications on the seller side. */
//    fun onNotifySeller(listener: (JSONObject) -> Unit): Emitter.Listener {
//        val l = Emitter.Listener { args ->
//            (args.firstOrNull() as? JSONObject)?.let(listener)
//        }
//        socket?.on("notifySeller", l)
//        return l
//    }
//    fun offNotifySeller(listener: Emitter.Listener) {
//        socket?.off("notifySeller", listener)
//    }
}