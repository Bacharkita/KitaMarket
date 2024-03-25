package com.todoapp.kitamarket

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.zxing.Result

import me.dm7.barcodescanner.zxing.ZXingScannerView

class QRScanActivity : AppCompatActivity(),ZXingScannerView.ResultHandler {

    val requestPermissionCode = 1
    var scannerView : ZXingScannerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scannerView = ZXingScannerView(this)
        setContentView(scannerView)
        setPermission()
    }

    override fun handleResult(result: Result?) {

        val id = result.toString()
        if ( id.length == Constant.ARTICLE_ID_LENGTH){
            if(!SellActivity.isArticleInserted(id)){
                Db.getArticle(result.toString()){ article, completionResult->
                    if(completionResult == R.string.successed ){

                        SellActivity.articlesList.add(article)


                    }else{
                        Toast.makeText(this,
                            R.string.QR_code_reading_error,
                            Toast.LENGTH_SHORT).show()
                    }

                }
            }else{
                Toast.makeText(
                    this,
                    getString(R.string.article_exist),
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
        Toast.makeText(this,
            R.string.QR_code_dissmatch_id_length,
            Toast.LENGTH_SHORT).show()
        scannerView?.setResultHandler (this)
        scannerView?.startCamera()
    }

    override fun onResume() {
        super.onResume()
        scannerView?.setResultHandler (this)
        scannerView?.startCamera()
    }

    override fun onStop() {
        super.onStop()
    }

    private fun setPermission(){
        val permission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if (permission!= PackageManager.PERMISSION_GRANTED){
            makeRequest()
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),
            requestPermissionCode

        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            requestPermissionCode-> {
                if (grantResults.isEmpty() || grantResults.first() != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(
                        applicationContext,
                        getString(R.string.camera_permission_required),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}