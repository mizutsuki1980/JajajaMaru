package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color.argb
import android.graphics.Paint

class Jiki(var x:Int, var y:Int) {
    val ookisa = 100
    val iro = Paint()
    var sekaix = 224    //世界の左端から７マス　32＊7が初期位置

    var motoTakasa = y

    val NORMAL_STATE = 1
    val JUMP_UPDOWN_STATE = 2
    val JUMP_RAKKA_STATE = 3
    val JUMP_END_STATE = 4
    var jumpStatus = NORMAL_STATE // 最初はNORMAL_STATE


    init{
        iro.style = Paint.Style.FILL
        iro.color = argb(255, 255, 255, 150)
    }

    var jumpFrame = 10

    fun jumpSyori() {
    }

    fun migiIdo(){
        x += 0
    }
    fun hidariIdo(){
        x -= 0
    }

    fun draw(canvas: Canvas){
        canvas.drawCircle(x.toFloat(),(y).toFloat(),(ookisa/2).toFloat(),iro) //自機の描画
    }

    var isJump = false
    var vJump = 0f

    fun kasokudoJump():Float {
        return -5.0f
    }
    fun jumpSyori(controller: Controller){
        if(controller.houkou=="jump"){
            if(isJump==false){
                isJump=true
                vJump = 50f
                y -= vJump.toInt()
            }
        }
        if(isJump) {
            vJump = vJump + kasokudoJump()
            y -= vJump.toInt()
        }
    }


}