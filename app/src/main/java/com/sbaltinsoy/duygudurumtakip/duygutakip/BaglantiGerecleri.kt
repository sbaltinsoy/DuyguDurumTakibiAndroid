package com.sbaltinsoy.duygudurumtakip.duygudurum

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.sbaltinsoy.duygudurumtakip.R
import com.sbaltinsoy.duygudurumtakip.duyguDurumuMetneCevir
import com.sbaltinsoy.duygudurumtakip.sureyiMetneCevir
import com.sbaltinsoy.duygudurumtakip.veritabani.DuyguDurum

@BindingAdapter("duyguSuresi")
fun TextView.duyguSuresiAta(duyguDurum: DuyguDurum?) {
    duyguDurum?.let {
        text =
            sureyiMetneCevir(
                duyguDurum.baslamaMiliSaniyesi,
                duyguDurum.bitisMiliSaniyesi,
                context.resources
            )
    }
}

@BindingAdapter("duyguDurumu")
fun TextView.duyguDurumuAta(duyguDurum: DuyguDurum?) {
    duyguDurum?.let {
        text =
            duyguDurumuMetneCevir(duyguDurum.durum, context.resources)
    }

}

@BindingAdapter("duyguIcon")
fun ImageView.setSleepImage(duyguDurum: DuyguDurum?) {
    duyguDurum?.let {
        setImageResource(
            when (duyguDurum.durum) {
                0 -> R.drawable.food
                1 -> R.drawable.angry
                2 -> R.drawable.anxious
                3 -> R.drawable.crying
                5 -> R.drawable.expressionless
                6 -> R.drawable.thinking
                7 -> R.drawable.fall_in_love
                8 -> R.drawable.sleeping
                else -> R.drawable.laughing
            }
        )
    }
}
