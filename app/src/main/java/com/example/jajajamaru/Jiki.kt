package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color.argb
import android.graphics.Paint

class Jiki(var x:Int, var y:Int) {
    val ookisa = 200
    val iro = Paint()

    var isJump = false
    var jumpFrame = 0
    var jumpTakasa = 0

    val TAMA_NASI_STATE = 1
    val NORMAL_STATE = 2
    val TAMA_HIT_STATE = 3
    val TAMA_HIT_END_STATE = 4
    var status = TAMA_NASI_STATE // 最初は玉が画面内に無い状態


    init{
        iro.style = Paint.Style.FILL
        iro.color = argb(255, 255, 255, 150)
    }

    fun jumpSyori(){
        when (status) {
            TAMA_NASI_STATE -> {

            }

            NORMAL_STATE -> {


                if (y < 5) {
                    status = TAMA_NASI_STATE
                } // 画面外に出たら無しの状態に一旦遷移
            }

            TAMA_HIT_STATE -> {

            }

            TAMA_HIT_END_STATE -> {

            }
        }

    }


    fun draw(canvas: Canvas){
        canvas.drawCircle(x.toFloat(),y.toFloat(),(ookisa/2).toFloat(),iro) //自機の描画
    }
    fun jumpdraw(canvas: Canvas,jumpTakasa:Int){
        canvas.drawCircle(x.toFloat(),(y-jumpTakasa).toFloat(),(ookisa/2).toFloat(),iro) //自機の描画
    }

}