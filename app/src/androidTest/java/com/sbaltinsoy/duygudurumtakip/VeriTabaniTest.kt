package com.sbaltinsoy.duygudurumtakip

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.sbaltinsoy.duygudurumtakip.veritabani.DuyguDurum
import com.sbaltinsoy.duygudurumtakip.veritabani.VeriTabani
import com.sbaltinsoy.duygudurumtakip.veritabani.VeriTabaniErisimNesnesi
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.lang.Exception
import kotlin.Throws

@RunWith(AndroidJUnit4::class)
class VeriTabaniTest {
    private lateinit var veriTabaniErisimNesnesi: VeriTabaniErisimNesnesi
    private lateinit var veritabai: VeriTabani

    @Before
    fun veritabaniOlustur(){
        val baglam = InstrumentationRegistry.getInstrumentation().targetContext

        veritabai = Room.inMemoryDatabaseBuilder(baglam, VeriTabani::class.java)
            .allowMainThreadQueries()
            .build()
        veriTabaniErisimNesnesi = veritabai.veriTabaniErisimNesnesi
    }

    @After
    @Throws(IOException::class)
    fun veritabaniKapat(){
        veritabai.close()
    }

    @Test
    @Throws(Exception::class)
    fun birDuyguEkleVeOku(){
        val duygu = DuyguDurum()
        runBlocking {
            veriTabaniErisimNesnesi.ekle(duygu)
            val sonEklenenDuygu = veriTabaniErisimNesnesi.sonDuyguDurumuGetir()
            assertEquals(sonEklenenDuygu?.durum, -1)
        }
    }
}