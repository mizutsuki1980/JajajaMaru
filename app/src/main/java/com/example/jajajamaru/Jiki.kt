package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color.argb
import android.graphics.Paint

class Jiki(var x:Int, var y:Int) {
    val ookisa = 100
    val iro = Paint()

    var isJump = false
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

    var jumpFrameYYY = 0
    var motoTakasaYYY = y

    var jumpLimit = 10


    fun jumpChuSyori() {
        jumpLimit --
        if(jumpLimit==0){ //0ならリセット
            y=motoTakasa
            jumpLimit = 10
            isJump = false
        }

        when (jumpLimit) {
            9 -> {y -= 40}
            8 -> {y -= 40}
            7 -> {y -= 10}
            6 -> {y -= 10}
            5 -> {y += 20}
            4 -> {y += 20}
            3 -> {y += 20}
            2 -> {y += 20}
            1 -> {y += 20}
        }



    }

    fun jumpJyoutai(){
        when (jumpStatus) {
            NORMAL_STATE -> {
                motoTakasaYYY = y
                jumpFrame = 10
            }

            JUMP_UPDOWN_STATE -> {
                jumpChuSyori()
                if(jumpFrameYYY == 0){
                    jumpStatus == JUMP_RAKKA_STATE
                }
            }

            JUMP_RAKKA_STATE -> {
                jumpStatus == JUMP_END_STATE
            }

            JUMP_END_STATE -> {
                jumpStatus == NORMAL_STATE
            }
        }
    }




    fun migiIdo(){
    //    x += 32
        x += 0
    }



    fun hidariIdo(){
        //x -= 32
        x -= 0
    }
    fun ueIdo(){
        y-= 0
    }
    fun shitaIdo(){
        y+= 0
    }

    fun draw(canvas: Canvas){
        canvas.drawCircle(x.toFloat(),(y).toFloat(),(ookisa/2).toFloat(),iro) //自機の描画
    }

}