package com.sbaltinsoy.duygudurumtakip.duygudurum

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.sbaltinsoy.duygudurumtakip.R
import com.sbaltinsoy.duygudurumtakip.databinding.DuyguDurumFragmentBinding
import com.sbaltinsoy.duygudurumtakip.databinding.DuyguTakipFragmentBinding
import com.sbaltinsoy.duygudurumtakip.veritabani.VeriTabani


class DuyguDurumFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val veriBagi: DuyguDurumFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.duygu_durum_fragment, container, false
        )

        val argumanlar = DuyguDurumFragmentArgs.fromBundle(requireArguments())
        val uygulama = requireNotNull(this.activity).application

        val veriKaynagi = VeriTabani.ornegiGetir(uygulama).veriTabaniErisimNesnesi
        val duyguDurumGoruntuModelFactory =
            DuyguDurumViewModelFactory(argumanlar.duyguAnahtar, veriKaynagi)

        val duyguDurumGoruntuModel = ViewModelProvider(
            this,
            duyguDurumGoruntuModelFactory
        ).get(DuyguDurumViewModel::class.java)

        veriBagi.duyguDurumGoruntuModel = duyguDurumGoruntuModel

        duyguDurumGoruntuModel.duyguTakibeYonlendir.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                this.findNavController()
                    .navigate(DuyguDurumFragmentDirections.actionDuyguDurumFragmentToDuyguTakipFragment())
                duyguDurumGoruntuModel.yonlendirmeTamamlandi()
            }
        })

        return veriBagi.root
    }
}