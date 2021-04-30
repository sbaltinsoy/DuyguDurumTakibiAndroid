package com.sbaltinsoy.duygudurumtakip.veritabani

import androidx.lifecycle.LiveData
import androidx.room.*

// nesne kalibi
@Dao // CRUD
interface VeriTabaniErisimNesnesi {
    @Insert
    suspend fun ekle(duyguDurum: DuyguDurum)

    @Query(value = "SELECT * FROM duygu_durum_tablosu ORDER BY kimlikNumarasi DESC")
    fun tumDuyguVerisiniGetir(): LiveData<List<DuyguDurum>>

    @Query(value = "SELECT * FROM duygu_durum_tablosu WHERE kimlikNumarasi = :kimlik")
    suspend fun getir(kimlik: Long): DuyguDurum?

    @Query("SELECT * FROM duygu_durum_tablosu WHERE kimlikNumarasi = :anahtar")
    fun kimlikGetir(anahtar: Long): LiveData<DuyguDurum?>

    @Query(value = "SELECT * FROM duygu_durum_tablosu ORDER BY kimlikNumarasi DESC LIMIT 1")
    suspend fun sonDuyguDurumuGetir(): DuyguDurum?

    @Update
    suspend fun guncelle(duyguDurum: DuyguDurum)

    @Query("DELETE FROM duygu_durum_tablosu")
    suspend fun tumVeriyiTemizle()


    //@Query("DELETE FROM duygu_durum_tablosu WHERE kimlikNumarasi = :kimlik")
    //fun duyguDurumSil(kimlik: Long)

    @Delete
    suspend fun sil(duyguDurum: DuyguDurum)

}