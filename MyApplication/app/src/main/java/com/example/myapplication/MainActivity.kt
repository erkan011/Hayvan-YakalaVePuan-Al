package com.unalfaruk.catchthekenny

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import java.util.Random

class MainActivity : AppCompatActivity() {
    var txtSayac: TextView? = null
    var txtPuan: TextView? = null
    var puan = 0
    var kalanZaman: Long = 0
    var imageView1: ImageView? = null
    var imageView2: ImageView? = null
    var imageView3: ImageView? = null
    var imageView4: ImageView? = null
    var imageView5: ImageView? = null
    var imageView6: ImageView? = null
    var imageView7: ImageView? = null
    var imageView8: ImageView? = null
    var imageView9: ImageView? = null
    lateinit var kostebekler: Array<ImageView?>
    var btnYeniden: Button? = null
    var btnDuraklat: Button? = null
    var zamanlayici: CountDownTimer? = null
    var handler: Handler? = null
    var runnable: Runnable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView1 = findViewById(R.id.imageView) as ImageView?
        imageView2 = findViewById(R.id.imageView2) as ImageView?
        imageView3 = findViewById(R.id.imageView3) as ImageView?
        imageView4 = findViewById(R.id.imageView4) as ImageView?
        imageView5 = findViewById(R.id.imageView5) as ImageView?
        imageView6 = findViewById(R.id.imageView6) as ImageView?
        imageView7 = findViewById(R.id.imageView7) as ImageView?
        imageView8 = findViewById(R.id.imageView8) as ImageView?
        imageView9 = findViewById(R.id.imageView9) as ImageView?
        btnYeniden = findViewById(R.id.btnYeniden) as Button?
        btnDuraklat = findViewById(R.id.btnDuraklat) as Button?
        txtPuan = findViewById(R.id.txtPuan) as TextView?
        kostebekler = arrayOf(
            imageView1,
            imageView2,
            imageView3,
            imageView4,
            imageView5,
            imageView6,
            imageView7,
            imageView8,
            imageView9
        )
        puan = 0
        txtSayac = findViewById(R.id.txtSayac) as TextView?

        //Bu fonksiyon aldığı parametreye göre oyun süresini başlatıyor.
        zamanlayiciBaslat(30000)
    }

    fun zamanlayiciBaslat(sure: Long) {
        zamanlayici = object : CountDownTimer(sure, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                /*Kalan zamanı sürekli bir değişkende tutuyoruz, böylece kullanıcı "Duraklat" dediğinde,
                tekrar devam edeceği zaman zamanlayıcıyı sıfırdan kuracağız ama kaldığı zamandan başlatacağız.*/
                kalanZaman = millisUntilFinished
                txtSayac!!.text = "Zaman: " + millisUntilFinished / 1000
            }

            override fun onFinish() {
                txtSayac!!.text = "Süre bitti."
                handler!!.removeCallbacks(runnable!!)
                kostebekOnClickKaldir()
                btnYeniden!!.visibility = View.VISIBLE
                btnDuraklat!!.visibility = View.INVISIBLE
            }
        }
        (zamanlayici as CountDownTimer).start()
        kostebegiGizle()
    }

    //ImageView olan köstebeklerin onClick fonksiyonu
    fun puanArtir(view: View?) {
        puan++
        txtPuan!!.text = "Puan: $puan"
    }

    //Köstebeklere ait OnClick fonksiyonunu devre dışı bırakıyor.
    fun kostebekOnClickKaldir() {
        for (kostebek in kostebekler) {
            kostebek!!.setOnClickListener(null)
        }
    }

    //Köstebeklere ait OnClik fonksiyonunu devreye alıyor.
    fun kostebekOnClickEkle() {
        for (kostebek in kostebekler) {
            kostebek!!.setOnClickListener { v -> puanArtir(v) }
        }
    }

    //Rastgele köstebek gösteren fonksiyon
    fun kostebegiGizle() {
        handler = Handler()
        runnable = object : Runnable {
            override fun run() {
                for (kostebek in kostebekler) {
                    kostebek!!.visibility = View.INVISIBLE
                }
                val rastgele = Random()
                val rastgeleSayi = rastgele.nextInt(8 - 0)
                kostebekler[rastgeleSayi]!!.visibility = View.VISIBLE
                handler!!.postDelayed(this, 500)
            }
        }
        handler!!.post(runnable as Runnable)
    }

    //Yeniden butonunun onClick fonksiyonu
    fun yenidenBaslat(view: View) {
        //Puanı sıfırlıyoruz.
        puan = 0
        txtPuan!!.text = "Puan: $puan"

        //Zamanlayıcıyı tekrar ayarlıyoruz.
        zamanlayiciBaslat(30000)

        //Tıklana "Yeniden" butonunu gizliyoruz.
        view.visibility = View.INVISIBLE

        //Köstebeklere onClick fonksiyonunu ekliyoruz.
        kostebekOnClickEkle()
        btnDuraklat!!.visibility = View.VISIBLE
    }

    //Duraklat/Devam Et butonunun onClick fonksiyonu
    fun duraklatDevamEt(view: View) {
        //Tıklanan nesnenin buton olduğunu belirtiyoruz.
        val tiklananNesne = view as Button

        //Butonun yazısını çekiyoruz, yazıya göre işlem yapacağız.
        val komut = tiklananNesne.text as String

        //Duraklat komutu istenmişse
        if (komut == "Duraklat") {
            //Geriye sayan sayacımızı durduruyoruz.
            zamanlayici!!.cancel()
            //Rastgele köstebek gösteren fonksiyonu da durduruyoruz.
            handler!!.removeCallbacks(runnable!!)
            //Köstebeklere ait onClick fonksiyonunu da kaldırıyoruz.
            kostebekOnClickKaldir()
            //Butonun yazısını "Devam Et" olarak değiştiriyoruz.
            tiklananNesne.text = "Devam Et"
        } else {
            //Köstebeklere ait onClick fonksiyonunu ekliyoruz.
            kostebekOnClickEkle()
            //Geriye sayan sayacımızı başlatıyoruz.
            zamanlayiciBaslat(kalanZaman)
            //Butonun yazısını "Duraklat olarak değiştiriyoruz.
            tiklananNesne.text = "Duraklat"
        }
    }
}
