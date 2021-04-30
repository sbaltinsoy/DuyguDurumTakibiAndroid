package com.sbaltinsoy.duygudurumtakip.duygutakip


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.sbaltinsoy.duygudurumtakip.R
import com.sbaltinsoy.duygudurumtakip.databinding.DuyguGoruntuDuzeniBinding
import com.sbaltinsoy.duygudurumtakip.veritabani.DuyguDurum
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException
private val ELEMAN_TIPI_BASLIK = 3
private val ELEMAN_TIPI_DUYGU = 1
private val adapterScope = CoroutineScope(Dispatchers.Default)

class DuyguGoruntuFarkGeriBildirim : DiffUtil.ItemCallback<VeriElemani>() {
    override fun areItemsTheSame(eski: VeriElemani, yeni: VeriElemani): Boolean {
        return eski.id == yeni.id
    }

    override fun areContentsTheSame(eski: VeriElemani, yeni: VeriElemani): Boolean {
        return eski == yeni
    }
}

class TiklamaTakipcisi(val tiklamaTakipcisi: (duyguId: Long) -> Unit) {
    fun tiklandi(duygu: DuyguDurum) = tiklamaTakipcisi(duygu.kimlikNumarasi)
}

sealed class VeriElemani {
    abstract val id: Long

    data class duyguDurumElemani(val duyguDurum: DuyguDurum) : VeriElemani() {
        override val id = duyguDurum.kimlikNumarasi
    }

    object baslik : VeriElemani() {
        override val id = Long.MIN_VALUE
    }
}

class BaslikGoruntuOlusturucu(view: View) : RecyclerView.ViewHolder(view) {
    companion object {
        fun from(parent: ViewGroup): BaslikGoruntuOlusturucu {

            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.text_eleman_goruntusu, parent, false)

            return BaslikGoruntuOlusturucu(view)
        }
    }
}

class DuyguGoruntuAdaptoru(val tiklamaTakipcisi: TiklamaTakipcisi) :
    ListAdapter<VeriElemani, RecyclerView.ViewHolder>(DuyguGoruntuFarkGeriBildirim()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ELEMAN_TIPI_BASLIK -> BaslikGoruntuOlusturucu.from(parent)
            ELEMAN_TIPI_DUYGU -> GoruntuOlusturucu.from(parent)

            else -> throw ClassCastException("Unknown viewType ${viewType}")
        }
    }

    override fun onBindViewHolder(tutucu: RecyclerView.ViewHolder, veriSirasi: Int) {
        when (tutucu) {
            is GoruntuOlusturucu -> {
                val eleman = getItem(veriSirasi) as VeriElemani.duyguDurumElemani
                tutucu.bagla(eleman.duyguDurum, tiklamaTakipcisi)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is VeriElemani.baslik -> ELEMAN_TIPI_BASLIK
            is VeriElemani.duyguDurumElemani -> ELEMAN_TIPI_DUYGU
            else -> ELEMAN_TIPI_DUYGU
        }
    }

    fun addHeaderAndSubmitList(list: List<DuyguDurum>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(VeriElemani.baslik)
                else -> listOf(VeriElemani.baslik) + list.map { VeriElemani.duyguDurumElemani(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    class GoruntuOlusturucu private constructor(private val baglanti: DuyguGoruntuDuzeniBinding) :
        RecyclerView.ViewHolder(baglanti.root) {

        fun bagla(eleman: DuyguDurum, tiklamaTakipcisi: TiklamaTakipcisi) {
            baglanti.duygu = eleman
            baglanti.executePendingBindings()
            baglanti.tiklamaTakibi = tiklamaTakipcisi
        }

        companion object {
            fun from(parent: ViewGroup): GoruntuOlusturucu {
                val layoutInflater = LayoutInflater.from(parent.context)

                val baglanti =
                    DuyguGoruntuDuzeniBinding.inflate(layoutInflater, parent, false)

                return GoruntuOlusturucu(baglanti)
            }
        }
    }
}


/*

class DuyguGoruntuAdaptoru : RecyclerView.Adapter<DuyguGoruntuAdaptoru.GoruntuOlusturucu>() {

    var veri = listOf<DuyguDurum>()
        set(deger){
            field = deger
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoruntuOlusturucu {
        return GoruntuOlusturucu.from(parent)
    }

    // goruntu tutucu
    override fun onBindViewHolder(tutucu: GoruntuOlusturucu, veriSirasi: Int) {
        val eleman = veri[veriSirasi]

        tutucu.bagla(eleman)
    }

    override fun getItemCount(): Int = veri.size


    class GoruntuOlusturucu private constructor(elemanGoruntusu: View) :
        RecyclerView.ViewHolder(elemanGoruntusu) {
        val duyguSuresi: TextView = itemView.findViewById(R.id.duygu_suresi)
        val duyguDurum: TextView = itemView.findViewById(R.id.duygu_durum)
        val duyguResim: ImageView = itemView.findViewById(R.id.duygu_resim)

        fun bagla(eleman: DuyguDurum) {
            val kaynak = itemView.context.resources

            duyguSuresi.text = sureyiMetneCevir(eleman.baslamaMiliSaniyesi, eleman.bitisMiliSaniyesi, kaynak)
            duyguDurum.text = duyguDurumuMetneCevir(eleman.durum, kaynak)

            duyguResim.setImageResource(
                when(eleman.durum) {
                    0 -> R.drawable.food
                    1 -> R.drawable.angry
                    2 -> R.drawable.anxious
                    3 -> R.drawable.crying
                    4 -> R.drawable.laughing
                    5 -> R.drawable.expressionless
                    6 -> R.drawable.thinking
                    7 -> R.drawable.fall_in_love
                    8 -> R.drawable.sleeping
                    else -> R.drawable.laughing
                }
            )
        }

        companion object {
            fun from(parent: ViewGroup): GoruntuOlusturucu {
                val layoutInflater = LayoutInflater.from(parent.context)
                val goruntu = layoutInflater.inflate(R.layout.duygu_goruntu_duzeni, parent, false)

                return GoruntuOlusturucu(goruntu)
            }
        }
    }
}

 */
