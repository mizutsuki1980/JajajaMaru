package com.example.jajajamaru

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Color.argb
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class MyCustomView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    var frame = 0
    var dgCount = 0
    var scoreCount = 0
    var isFirstMove = false //動きだしたら弾も出るようにする
    val jikiIchiTyousei = 120 //クリックした位置よりちょっと上にくる。そうしないと指に隠れて見えない。

    val initialJikiX = 300 //初期位置
    val initialJikiY = 800 //初期位置

    var clickX = initialJikiX  //自機の位置は覚えておかないといけないので必要 最初だけ初期位置
    var clickY = initialJikiY  //自機の位置は覚えておかないといけないので必要 最初だけ初期位置


    val iro = Paint()

    fun beginAnimation() {
        tsugiNoSyori()  //最初に一回だけ呼ばれる
    }

    fun tsugiNoSyori() {
        frame += 1  //繰り返し処理はここでやってる
        invalidate()
        handler.postDelayed({ tsugiNoSyori() }, 100)

    }
    override fun onDraw(canvas: Canvas) {
        //仮にここに置いておく色の設定
        iro.style = Paint.Style.FILL
        iro.color = argb(255, 255, 255, 150)


    //        gameover(canvas) //canvasっていう何かが勝手に作られているんだろうか。varとかしてない。
        canvas.drawCircle((300).toFloat(),(400).toFloat(),(20).toFloat(),iro) //自機の描画

    }

}

