package com.example.jajajamaru

import android.content.Context
import android.graphics.BitmapFactory
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
    var isFirstJump = false
    var clickState = "nashi"

    val jikiIchiTyousei = 120 //クリックした位置よりちょっと上にくる。そうしないと指に隠れて見えない。

    val initialJikiX = 360 //初期位置
    val initialJikiY = 500 //初期位置

    var clickX = initialJikiX  //自機の位置は覚えておかないといけないので必要 最初だけ初期位置
    var clickY = initialJikiY  //自機の位置は覚えておかないといけないので必要 最初だけ初期位置

    var jiki =Jiki(initialJikiX, initialJikiY)
    var controller = Controller()
    var background = BackGround()

    fun beginAnimation() {
        tsugiNoSyori()  //最初に一回だけ呼ばれる
    }


    fun migiIdo(){
        if (controller.houkou == "migi"){
            jiki.x += 5
            background.x  += 15
        }
    }
    fun hidariIdo(){
        if (controller.houkou == "hidari"){
            jiki.x -= 5
            background.x  -= 15
        }
    }


    fun jumpCheckIdo(){
        //着地の瞬間にジャンプボタンが押されている、二度とボタンを押しても反応しなくなる
        if(isFirstJump){
            if (controller.isJumpButton ) {
                jiki.jumpTakasa += 28 //ずーとマイナスされているから、プラスすると滞空時間が増える
            }
        }

        if (controller.isJumpButton ){
            isFirstJump = true

            jiki.jumpTakasa -= 28
            jiki.isJump = true

            if(jiki.jumpTakasa>0 && jiki.jumpTakasa<200){
            }else {
                jiki.isJump = false
                jiki.jumpTakasa = 200
            }

        }else{

            //もし下降中だったら、isJumpはfalseにしない
            if(jiki.jumpTakasa>=0 && jiki.jumpTakasa<=200){
                jiki.jumpTakasa -= 28
            }else {
                jiki.isJump = false
                jiki.jumpTakasa = 200
            }

            if(jiki.jumpTakasa<0){  //マイナスの高さならジャンプは終了
                jiki.isJump = false
                isFirstJump = false
            }

        }

    }
    fun tsugiNoSyori() {
        migiIdo()
        hidariIdo()
        jumpCheckIdo()

        frame += 1  //繰り返し処理はここでやってる
        invalidate()
        handler.postDelayed({ tsugiNoSyori() }, 100)
    }

    override fun onDraw(canvas: Canvas) {
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.tosu, BitmapFactory.Options())
        canvas.drawBitmap(bitmap, 50.0F+(background.x.toFloat()), 200.0F, null)

        jiki.jikiJumpDraw(canvas)
        controller.draw(canvas,clickX,clickY,clickState)
    }



    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            clickState = "ACTION_DOWN"
            clickX = event.x.toInt()
            clickY = event.y.toInt()
            return true // 処理した場合はtrueを返す約束
        }

        if (event.action == MotionEvent.ACTION_UP) {
            clickState = "ACTION_UP"
            clickX = event.x.toInt()
            clickY = event.y.toInt()
            return true // 処理した場合はtrueを返す約束
        }

        if (event.action == MotionEvent.ACTION_MOVE) {
            clickState = "ACTION_MOVE"
            clickX = event.x.toInt()
            clickY = event.y.toInt()
            return true // 処理した場合はtrueを返す約束
        }
        return super.onTouchEvent(event)
    }
}

