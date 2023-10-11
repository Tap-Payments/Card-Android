package company.tap.tapcardformkit.open.web_wrapper

import android.app.Application
import android.content.Context
import android.util.Log
import io.reactivex.rxjava3.exceptions.UndeliverableException
import io.reactivex.rxjava3.functions.Consumer
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import java.io.IOException
import java.net.SocketException

class ApplicationClass : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: ApplicationClass? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler(Consumer { e: Throwable ->
            var e = e
            if (e is UndeliverableException) {
                e = e.cause!!
            }
            if (e is IOException || e is SocketException) {
                // fine, irrelevant network problem or API that throws on cancellation
                return@Consumer
            }
            if (e is InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                return@Consumer
            }
            if (e is NullPointerException || e is IllegalArgumentException) {
                // that’s likely a bug in the application
                println("eee$e")
                Thread.currentThread().uncaughtExceptionHandler
                    .uncaughtException(Thread.currentThread(), e)
                return@Consumer
            }
            if (e is IllegalStateException) {
                // that’s a bug in RxJava or in a custom operator
                Thread.currentThread().uncaughtExceptionHandler.toString()
                // .handleException(Thread.currentThread(), e);
                return@Consumer
            }
            Log.e("warn", "Undeliverable exception received, not sure what to do", e)
        })
    }
}
