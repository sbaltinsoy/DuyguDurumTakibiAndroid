package com.sbaltinsoy.duygudurumtakip.duygutakip

import android.app.Application
import androidx.lifecycle.*
import com.sbaltinsoy.duygudurumtakip.duyguHtmleCevir
import com.sbaltinsoy.duygudurumtakip.veritabani.DuyguDurum
import com.sbaltinsoy.duygudurumtakip.veritabani.VeriTabani
import com.sbaltinsoy.duygudurumtakip.veritabani.VeriTabaniErisimNesnesi
import kotlinx.coroutines.launch

class DuyguTakipViewModel (
    val veritabani: VeriTabaniErisimNesnesi,
    uygulama: Application
): AndroidViewModel(uygulama){
    private var sonDuygu = MutableLiveData<DuyguDurum?>()
    var duygular = veritabani.tumDuyguVerisiniGetir()

    // DuygularString e Kullanima gerek kalmadi RecyclerView den sonra
    val duygularString = Transformations.map(duygular) { duygular ->
        duyguHtmleCevir(duygular, uygulama.resources)
    }


    private val _duyguDurumaYonlendir = MutableLiveData<DuyguDurum?>()
        val duyguDurumaYonlendir: LiveData<DuyguDurum?>
            get() = _duyguDurumaYonlendir

    private var _snackBarGoster = MutableLiveData<Boolean>()

    val snackBarGoster: LiveData<Boolean>
        get() = _snackBarGoster

    private val _duyguDetayaYonlendir = MutableLiveData<Long?>()
    val duyguDetayaYonlendir
        get() = _duyguDetayaYonlendir

    fun snackBarGosterildi() {
        _snackBarGoster.value = false
    }

    fun yonlendirmeTamamlandi(){
        _duyguDurumaYonlendir.value = null
    }

    private suspend fun sonDuyguVeriTabanindanAl(): DuyguDurum?{
        var duygu = veritabani.sonDuyguDurumuGetir()

        if (duygu?.baslamaMiliSaniyesi != duygu?.bitisMiliSaniyesi )
            duygu = null
        return duygu
    }

    private fun sonDuyguIlklendir(){
        viewModelScope.launch {
            sonDuygu.value = sonDuyguVeriTabanindanAl()
        }
    }

    init {
        sonDuyguIlklendir()
    }

    fun duyguTakibiniBaslat(){
        viewModelScope.launch {
            val yeniDuygu = DuyguDurum()
            sonDuygu.value = yeniDuygu
            veritabani.ekle(yeniDuygu)
        }
    }

    fun duyguTakibiniDurdur(){
        viewModelScope.launch {
            val sonDuygu = veritabani.sonDuyguDurumuGetir() ?: return@launch

            sonDuygu.bitisMiliSaniyesi = System.currentTimeMillis()
            veritabani.guncelle(sonDuygu)
            _duyguDurumaYonlendir.value = sonDuygu
        }
    }

    fun tumVeriyiSil(){
        viewModelScope.launch {
            veritabani.tumVeriyiTemizle()
            sonDuygu.value = null
            _snackBarGoster.value = true
        }
    }



    val baslaButonuGorunmesi = Transformations.map(sonDuygu) {
        it == null
    }

    val bitisButonuGorunmesi = Transformations.map(sonDuygu){
        it != null
    }

    val temizleButonuGorunmesi = Transformations.map(duygular) {
        it?.isNotEmpty()
    }

    fun duyguDetayaYonlendirmeTamamlandi() {
        _duyguDetayaYonlendir.value = null
    }

    fun duyguDurumTiklandi(id: Long) {
        _duyguDetayaYonlendir.value = id
    }

}