package com.sbaltinsoy.duygudurumtakip.duygudetay

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sbaltinsoy.duygudurumtakip.veritabani.DuyguDurum
import com.sbaltinsoy.duygudurumtakip.veritabani.VeriTabaniErisimNesnesi
import kotlinx.coroutines.launch

class DuyguDetayViewModel(
    private val duyguId: Long = 0L,
    val veritabani: VeriTabaniErisimNesnesi
) : ViewModel() {
    private val _duyguTakibeYonlendir = MutableLiveData<Boolean?>()

    val duyguTakibeYonlendir: LiveData<Boolean?>
        get() = _duyguTakibeYonlendir


    val duyguDurum: LiveData<DuyguDurum?>

    init {
        duyguDurum = veritabani.kimlikGetir(duyguId)
    }

    fun yonlendirmeTamamlandi() {
        _duyguTakibeYonlendir.value = null
    }

    fun duyguDurumuSil() {
        viewModelScope.launch {
            val duygu = veritabani.getir(duyguId) ?: return@launch
            veritabani.sil(duygu)

            _duyguTakibeYonlendir.value = true
        }
    }

    fun yonlendirmeTiklandi() {
        _duyguTakibeYonlendir.value = true
    }

}