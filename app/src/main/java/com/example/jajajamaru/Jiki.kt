package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color.argb
import android.graphics.Paint

class Jiki(var x:Int, var y:Int) {
    val ookisa = 200
    val iro = Paint()

    var isJump = false
    var jumpFrame = 0
    var jumpTakasa = 200

    val NORMAL_STATE = 1
    val JUMP_UP_STATE = 2
    val JUMP_TOP_STATE = 3
    val JUMP_DOWN_STATE = 4
    var jumpStatus = NORMAL_STATE // 最初はNORMAL_STATE


    init{
        iro.style = Paint.Style.FILL
        iro.color = argb(255, 255, 255, 150)
    }

    fun jumpSyori(){
        when (jumpStatus) {
            NORMAL_STATE -> {

            }

            JUMP_UP_STATE -> {


                if (y < 5) {
                    jumpStatus = JUMP_TOP_STATE
                } // 画面外に出たら無しの状態に一旦遷移
            }

            JUMP_TOP_STATE -> {

            }

            JUMP_DOWN_STATE -> {

            }
        }

    }


    fun draw(canvas: Canvas){
        canvas.drawCircle(x.toFloat(),y.toFloat(),(ookisa/2).toFloat(),iro) //自機の描画
    }
    fun jikiJumpDraw(canvas: Canvas){
        if(isJump) {
            jumpdraw(canvas,jumpTakasa)
        }else{
            draw(canvas)
        }

    }


    fun jumpdraw(canvas: Canvas,jumpTakasa:Int){
        canvas.drawCircle(x.toFloat(),(y-jumpTakasa).toFloat(),(ookisa/2).toFloat(),iro) //自機の描画
    }

}