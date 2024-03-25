package com.todoapp.kitamarket

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast

import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.todoapp.kitamarket.databinding.ActivityQrGeneratorBinding

class QRGeneratorActivity: AppCompatActivity() {

    var articleId: String? = null
    private lateinit var imageViewQRCode: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityQrGeneratorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val addMoreButton = binding.addMoreButtonQR
        val personalNumber = intent.getStringExtra(Constant.PERSONAL_NUMBER_EXTRA)

        articleId = intent.getStringExtra(Constant.ARTICLE_ID_EXTRA)
        imageViewQRCode = binding.QRImageView
        binding.articleIdValueQR.text = articleId

        addMoreButton.setOnClickListener {
            val intent = Intent(this, ArticleCreationActivity::class.java)
            intent.putExtra(Constant.PERSONAL_NUMBER_EXTRA, personalNumber)
            startActivity(intent)
            finish()
        }

        binding.doneAddingButtonQR.setOnClickListener {
            finish()

        }
    }

    override fun onStart() {
        super.onStart()
        getQRCode(articleId!!)
    }

    fun getQRCode(data: String){
        if (data.isEmpty()) {

            Toast.makeText(this, R.string.empty_data_error, Toast.LENGTH_SHORT).show()
        } else {
            val writer = QRCodeWriter()
            try {
                val bitMatrixWidth = 512
                val bitMatrixHight = 512

                val bitMatrix = writer.encode(
                    data,
                    BarcodeFormat.QR_CODE,
                    bitMatrixWidth,
                    bitMatrixHight)
                val width = bitMatrix.width
                val height = bitMatrix.height
                val widthRange = 0 until width
                val hightRange = 0 until height
                val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
                for (x in widthRange) {
                    for (y in hightRange) {
                        bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                    }
                }
                imageViewQRCode.setImageBitmap(bmp)

            } catch (error: WriterException) {

                    Toast.makeText(this,
                        R.string.QR_code_createing_error,
                        Toast.LENGTH_LONG).show()

            }
        }
    }
}