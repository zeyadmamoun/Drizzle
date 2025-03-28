package com.example.drizzle.utils.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import androidx.core.content.getSystemService
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.MutableStateFlow

class ConnectivityObserver(context: Context) {
    private val connectivityManager = context.getSystemService<ConnectivityManager>()!!
    val isConnected: MutableLiveData<Boolean> = MutableLiveData(false)

    val callback = object : NetworkCallback() {

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            val connected = networkCapabilities.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_VALIDATED
            )
            isConnected.postValue(connected)
        }

//            override fun onAvailable(network: Network) {
//                super.onAvailable(network)
//                trySend(true)
//            }

        override fun onUnavailable() {
            super.onUnavailable()
            isConnected.postValue(false)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            isConnected.postValue(false)
        }
    }

    fun unregisterCallBack(){
        connectivityManager.unregisterNetworkCallback(callback)
    }

    init {
        connectivityManager.registerDefaultNetworkCallback(callback)
    }

}