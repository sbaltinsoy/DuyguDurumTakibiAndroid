package com.sbaltinsoy.duygudurumtakip.duygutakip

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sbaltinsoy.duygudurumtakip.veritabani.VeriTabaniErisimNesnesi

class DuyguTakipViewModelFactory(
    private val veriKaynagi: VeriTabaniErisimNesnesi,
    private val uygulama: Application,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DuyguTakipViewModel::class.java)) {
            return DuyguTakipViewModel(veriKaynagi, uygulama) as T
        }
        throw IllegalArgumentException("Bilinmeyen ViewModel Sınıfı")
    }
}