package org.panta.misskey_for_android_v2.service

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.util.Log
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.panta.misskey_for_android_v2.interfaces.ErrorCallBackListener
import org.panta.misskey_for_android_v2.repository.Notification
import org.panta.misskey_for_android_v2.repository.SecretRepository
import org.panta.misskey_for_android_v2.storage.SharedPreferenceOperator
import org.panta.misskey_for_android_v2.usecase.PagingController
import org.panta.misskey_for_android_v2.view_data.NotificationViewData
import java.lang.IllegalArgumentException
import java.util.*

class NotificationService : Service() {

    private lateinit var notificationRepository: Notification
    private lateinit var pagingController: PagingController<NotificationViewData>

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        //~init
        val conProperty = SecretRepository(SharedPreferenceOperator(applicationContext)).getConnectionInfo()
        if(conProperty == null){
            Log.d("NotificationService", "connectionInfo不明のため停止します")
            this.stopSelf()
            return
        }
        notificationRepository = Notification(conProperty.domain, conProperty.i)

        pagingController = PagingController(notificationRepository, object : ErrorCallBackListener{
            override fun callBack(e: Exception) {
                Log.w("NotificationService", "エラー発生", e)
            }
        })
        //init~

        pagingController.getInit { out ->
            val notReadNotifications = out.filter{ inner ->
                ! inner.notificationProperty.isRead
            }

            notReadNotifications.forEach{inner ->
                Log.d("NotificationService", "未読の通知 ${inner.notificationProperty}")
            }

            watchDogNotification(20000)
        }


    }

    private fun watchDogNotification(sleepMillSeconds: Long){
        if(sleepMillSeconds.toString().length < 4) throw IllegalArgumentException("watchDogNotificationsError　1000ミリ秒未満を指定することはできません。")
        GlobalScope.launch{
            while(true){
                pagingController.getNewItems {
                    val notReadNotifications = it.filter{ inner ->
                        ! inner.notificationProperty.isRead
                    }

                    notReadNotifications.forEach{inner ->
                        Log.d("NotificationService", "未読の通知 ${inner.notificationProperty}")
                    }
                }
                delay(sleepMillSeconds)
            }
        }
    }

    private fun showNotificationCompat(){
      val notification = Notification.

    }
}
