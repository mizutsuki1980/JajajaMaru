package com.example.jajajamaru

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Color.argb
import android.graphics.Paint
import android.graphics.Rect
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

    var jiki =Jiki(initialJikiX, initialJikiY)

    val iro = Paint()

    fun beginAnimation() {
        tsugiNoSyori()  //最初に一回だけ呼ばれる
    }

    fun migiIdo(){
        jiki.x += 20
    }
    fun hdiariIdo(){
        jiki.x -= 20
    }

    fun tsugiNoSyori() {
        frame += 1  //繰り返し処理はここでやってる
        invalidate()
        handler.postDelayed({ tsugiNoSyori() }, 100)
    }

    override fun onDraw(canvas: Canvas) {
      jiki.draw(canvas)
        val iroCont = Paint()
        iroCont.style = Paint.Style.FILL
        iroCont.color = argb(100, 255, 255, 150)

        canvas.drawRect(shikakuRectXYSub(250.toInt() ,250.toInt(),250), iroCont)   //
        //中心点からつくっているようだ

    }

    fun shikakuRectXYSub(xxx:Int,yyy:Int,ooookisa:Int): Rect {
        val left = xxx  - ooookisa / 2
        val right = xxx  + ooookisa / 2
        val top = yyy  - ooookisa / 2
        val bottom = yyy + ooookisa / 2
        val m = Rect(left, top, right,bottom)
        return m
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            isFirstMove = true
            clickX = event.x.toInt()
            clickY = event.y.toInt()
            return true // 処理した場合はtrueを返す約束
        }

        if (event.action == MotionEvent.ACTION_UP) {
            isFirstMove = true
            clickX = event.x.toInt()
            clickY = event.y.toInt()
            return true // 処理した場合はtrueを返す約束
        }

        if (event.action == MotionEvent.ACTION_MOVE) {
            isFirstMove = true
            clickX = event.x.toInt()
            clickY = event.y.toInt()
            return true // 処理した場合はtrueを返す約束
        }
        return super.onTouchEvent(event)
    }
    //isFirstMoveとかいるんかな？clickXとclickYの初期値も。
    
    //押され続けている、っていうのは、ｘ、ｙが動かなかったら、という判定にすればいいのか
    //というか、今押されているいるｘｙがどこなのか？で左右上を決めればいいのでは。
}

