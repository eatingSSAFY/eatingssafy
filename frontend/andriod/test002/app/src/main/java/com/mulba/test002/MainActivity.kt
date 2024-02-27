package com.mulba.test002

//import android.R

//import android.R
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


class MainActivity : AppCompatActivity() {
    private val DEFAULT = "DEFAULT"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionCheck()


        val myWebView: WebView = findViewById(R.id.webView)

        myWebView.settings.run {
            // 웹뷰 자바스크립트 허용
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            setSupportMultipleWindows(true)
            WebView.setWebContentsDebuggingEnabled(true)
            setDomStorageEnabled(true);
        }

        myWebView.addJavascriptInterface(WebAppInterface(this, myWebView), "AndroidInterface")

        myWebView.webViewClient = WebViewClient()
        myWebView.webChromeClient = WebChromeClient()
        myWebView.loadUrl("https://i10a204.p.ssafy.io")
//        myWebView.loadUrl("http://10.0.2.2:8002/settings")

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val isFirstRun = sharedPreferences.getBoolean("isFirstRun", true)
        if(isFirstRun){
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
//                    Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                // Get new FCM registration token
                val token = task.result
                // Log and toast
                val msg = token.toString()
//            Log.d("FCM", msg)
//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val permissionCheck = ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.POST_NOTIFICATIONS
                    )
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        CoroutineScope(Dispatchers.IO).launch {
                            sendAppTokenToServer(msg, false, false)
                        }
                    }
                    else {
                        CoroutineScope(Dispatchers.IO).launch {
                            sendAppTokenToServer(msg, false, true)
                        }
                    }
                }
                else {
                    CoroutineScope(Dispatchers.IO).launch {
                        sendAppTokenToServer(msg, false, true)
                    }
                }

            })

            val editor = sharedPreferences.edit()
            editor.putBoolean("isFirstRun",false)
            editor.apply()
        }
    }

    override fun onResume() {
        super.onResume()
        val myWebView: WebView = findViewById(R.id.webView)

        myWebView.reload()
    }

    override fun onBackPressed() {
        val myWebView: WebView = findViewById(R.id.webView)
        if (myWebView.canGoBack()) {
            myWebView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    private val PERMISSION_REQUEST_CODE = 5001

    private fun permissionCheck() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permissionCheck = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            )
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    PERMISSION_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(applicationContext, "알림이 거부되었습니다.", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(applicationContext, "알림이 허용되었습니다.", Toast.LENGTH_SHORT)
                        .show()

                }
            }
        }
    }

    data class AppTokenRequest(
        val appToken: String,
        val userId: String?,
        val preferenceNotification: Boolean,
        val amountNotification: Boolean
    )

    // Retrofit 인터페이스 정의
    interface ApiService {
        @POST("/api/app-token")
        suspend fun sendAppToken(@Body request: AppTokenRequest)
    }

    // Retrofit 클라이언트 설정
    object RetrofitClient {
        private const val BASE_URL = "https://i10a204.p.ssafy.io"

        fun getClient(): ApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }

    // 실제 요청 보내는 함수
    private suspend fun sendAppTokenToServer(msg: String, pref: Boolean, amount: Boolean) {
        try {
            // Retrofit을 사용하여 서버에 POST 요청을 보냅니다.
            val apiService = RetrofitClient.getClient()
            val request = AppTokenRequest(
                appToken = msg,
                userId = null,
                preferenceNotification = pref,
                amountNotification = amount
            )
            apiService.sendAppToken(request)
            // 성공적으로 요청이 처리된 경
//            Log.e("post SUCCESS", "================")
        } catch (e: Exception) {
            // 요청 처리 중 오류가 발생한 경우
            e.printStackTrace()
//            Log.e("post FAIL", "^^^^^^^^^^^^^^^^^^^", e)
        }
    }



/////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////

     inner class WebAppInterface( private val mContext: Context, private val myWebView: WebView) {
        //    val myWebView: WebView = findViewById(R.id.webView)
        @JavascriptInterface
        fun sendDataToWeb(){
            try{
                FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
//                        Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                        return@OnCompleteListener
                    }
                    // Get new FCM registration token
                    val token = task.result
                    // Log and toast
                    val msg = token.toString()
                    val js = "javascript:receiveDataFromApp('$msg')"

                    myWebView.post( Runnable {
                        myWebView.loadUrl(js);
                    })
                })
            }
            catch(e :Exception){
//                Log.e("INSTIDF", "*****************************", e)
            }
        }

         @JavascriptInterface
         fun sendPermReqWeb(){
             val permission: Boolean;
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                 val permissionCheck = ContextCompat.checkSelfPermission(
                     mContext,
                     android.Manifest.permission.POST_NOTIFICATIONS
                 )
                 if (permissionCheck == PackageManager.PERMISSION_GRANTED) permission = true
                 else permission = false
             }
             else {
                val isPermitted = NotificationManagerCompat.from(mContext).areNotificationsEnabled()
                if (isPermitted) permission = true
                else permission = false
             }

             myWebView.post( Runnable {
                 myWebView.loadUrl("javascript:receivePermFromApp('$permission')");
             })
         }

        @JavascriptInterface
        fun changeAmount(data: String, data2: String) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
//                    Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }
                // Get new FCM registration token
                val token = task.result
                // Log and toast
                val msg = token.toString()
                CoroutineScope(Dispatchers.IO).launch {
                    sendAppTokenToServer(msg, data, data2,"null")
                }
            })
        }

         @JavascriptInterface
         fun changePref(data: String, data2: String, userId: String) {

             FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                 if (!task.isSuccessful) {
//                     Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                     return@OnCompleteListener
                 }
                 // Get new FCM registration token
                 val token = task.result
                 // Log and toast
                 val msg = token.toString()
                 CoroutineScope(Dispatchers.IO).launch {
                     sendAppTokenToServer(msg, data, data2, userId)
                 }
             })
         }

        // 실제 요청 보내는 함수
        private suspend fun sendAppTokenToServer(msg: String, pref:String, amount:String, userId:String) {

            val userID :String?
            if(userId == "null") {
                userID = null
            }
            else {
                userID = userId
            }
//            Log.e("WHAT2", userId)


            try {
                // Retrofit을 사용하여 서버에 POST 요청을 보냅니다.
                val apiService = RetrofitClient.getClient()
                val request = AppTokenRequest(
                    appToken = msg,
                    userId = userID,
                    preferenceNotification = pref.toBoolean(),
                    amountNotification = amount.toBoolean()
                )
                apiService.sendAppToken(request)
                // 성공적으로 요청이 처리된 경d
//                Log.e("post SUCCESS", "//////////////////////")
//                Log.e("post pref", pref)
//                Log.e("post noti", amount)
            } catch (e: Exception) {
                // 요청 처리 중 오류가 발생한 경우
                Log.e("1","2",e)
                e.printStackTrace()
            }
        }
    }
}





