package com.sbaltinsoy.duygudurumtakip.duygutakip

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sbaltinsoy.duygudurumtakip.R
import com.sbaltinsoy.duygudurumtakip.databinding.DuyguTakipFragmentBinding
import com.sbaltinsoy.duygudurumtakip.veritabani.VeriTabani
class DuyguTakipFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // binding => iple bağlama
        val veriBagi: DuyguTakipFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.duygu_takip_fragment, container, false
        )

        val uygulama = requireNotNull(this.activity).application
        val veriKaynagi = VeriTabani.ornegiGetir(uygulama).veriTabaniErisimNesnesi

        val goruntuModelFactory = DuyguTakipViewModelFactory(veriKaynagi, uygulama)

        val duyguTakipGoruntuModel =
            ViewModelProvider(this, goruntuModelFactory).get(DuyguTakipViewModel::class.java)

        veriBagi.setLifecycleOwner(this)

        veriBagi.duyguTakipGoruntuModel = duyguTakipGoruntuModel

        duyguTakipGoruntuModel.duyguDurumaYonlendir.observe(viewLifecycleOwner, { duygu ->
            duygu?.let {
                this.findNavController().navigate(
                    DuyguTakipFragmentDirections.actionDuyguTakipToDuyguDurumFragment(duygu.kimlikNumarasi)
                )
                duyguTakipGoruntuModel.yonlendirmeTamamlandi()
            }
        })

        duyguTakipGoruntuModel.duyguDetayaYonlendir.observe(viewLifecycleOwner, { duyguId ->
            duyguId?.let {
                this.findNavController().navigate(
                    DuyguTakipFragmentDirections.actionDuyguTakipToDuyguDetayFragment(duyguId)
                )
                duyguTakipGoruntuModel.duyguDetayaYonlendirmeTamamlandi()
            }
        })

        duyguTakipGoruntuModel.snackBarGoster.observe(viewLifecycleOwner, {
            if (it == true) { // Observed state is true.
                // alternatif olarak toast mesajı da gösterilebilir
                /*
                Toast.makeText(
                    context,
                    getString(R.string.temizlendi_mesaji),
                    Toast.LENGTH_LONG,
                ).show()
                */

                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    getString(R.string.temizlendi_mesaji),
                    Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                duyguTakipGoruntuModel.snackBarGosterildi()
            }
        })

        val adaptor = DuyguGoruntuAdaptoru(TiklamaTakipcisi { duyguId ->
            duyguTakipGoruntuModel.duyguDurumTiklandi(duyguId)
        })

        veriBagi.duyguListesi.adapter = adaptor

        duyguTakipGoruntuModel.duygular.observe(viewLifecycleOwner, {
            it?.let {
                adaptor.addHeaderAndSubmitList(it)
            }
        })

        val manager = GridLayoutManager(activity, 3)

        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(siraNumarasi: Int) = when (siraNumarasi % 4) {
                0 -> 3
                else -> 1
            }
        }

        veriBagi.duyguListesi.layoutManager = manager

        return veriBagi.root
    }

}