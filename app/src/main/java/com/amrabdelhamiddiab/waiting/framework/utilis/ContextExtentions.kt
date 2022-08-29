package com.amrabdelhamiddiab.waiting.framework.utilis

import android.content.Context
import android.widget.Toast

fun Context.toast(message: CharSequence) =
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()


/*
fun myFun(context: Context) {
    context.toast("Hello world!")
}
*/