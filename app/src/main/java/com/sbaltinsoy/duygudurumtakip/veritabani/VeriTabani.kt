package com.sbaltinsoy.duygudurumtakip.veritabani

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// singleton => factory => static
@Database(entities = [DuyguDurum::class], version = 1,exportSchema = false)
abstract class VeriTabani: RoomDatabase() {
    abstract val veriTabaniErisimNesnesi: VeriTabaniErisimNesnesi

    companion object{
        @Volatile
        private var ORNEK_NESNE: VeriTabani? = null

        fun ornegiGetir(baglam: Context): VeriTabani{
            synchronized(this){
                var ornek = ORNEK_NESNE
                if(ornek == null){
                    ornek = Room.databaseBuilder(
                        baglam.applicationContext,
                        VeriTabani::class.java,
                        "duygu_durum_veritabani"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    ORNEK_NESNE = ornek
                }

                return ornek
            }
        }
    }
}