package com.hrg.myapplication.utils.networkHelper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ConnectionNet @Inject constructor(@ApplicationContext private val context: Context) {

    fun isNetworkConnected(): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            result = when {
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> true
                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.let {
                result = when (it.type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    ConnectivityManager.TYPE_VPN -> true
                    ConnectivityManager.TYPE_BLUETOOTH -> true
                    ConnectivityManager.TYPE_DUMMY -> true
                    ConnectivityManager.TYPE_MOBILE_DUN -> true
                    ConnectivityManager.TYPE_MOBILE_HIPRI -> true
                    ConnectivityManager.TYPE_MOBILE_MMS -> true
                    ConnectivityManager.TYPE_MOBILE_SUPL -> true
                    ConnectivityManager.TYPE_WIMAX -> true
                    else -> false
                }
            }
        }
        return result
    }
}