package com.sbaltinsoy.duygudurumtakip.duygudurum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbaltinsoy.duygudurumtakip.veritabani.VeriTabaniErisimNesnesi
import kotlinx.coroutines.launch

class DuyguDurumViewModel(
    private val duyguId: Long = 0L,
    val veritabani: VeriTabaniErisimNesnesi
) : ViewModel() {

   private val _duyguTakibeYonlendir = MutableLiveData<Boolean?>()

    val duyguTakibeYonlendir: LiveData<Boolean?>
        get() = _duyguTakibeYonlendir

    fun yonlendirmeTamamlandi(){
        _duyguTakibeYonlendir.value = null
    }

    fun duyguDurumSecimiYap(durum: Int){
        viewModelScope.launch {
            val duygu = veritabani.getir(duyguId)?: return@launch
            duygu.durum = durum
            veritabani.guncelle(duygu)

            _duyguTakibeYonlendir.value = true
        }
    }
}