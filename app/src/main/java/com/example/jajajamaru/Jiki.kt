package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color.argb
import android.graphics.Paint

class Jiki(var x:Int, var y:Int) {
    val ookisa = 100
    val iro = Paint()
    var sekaix = 224    //世界の左端から７マス　32＊7が初期位置
    var worldOffsetX = 0    //いる
    var vYokoPlus = 0f

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


    fun jikiYokoIdo(controller: Controller,map: Map) {
        val kasokudo = kasokudoYoko(controller.houkou)
        val vYokoPlusCheckyou = vYokoPlus + kasokudo
        var syougaibutuCheck = syougaibutuHantei(vYokoPlusCheckyou,map)



        if(syougaibutuCheck) {
            vYokoPlus = vYokoPlus + kasokudo
            //速度制限
            if(vYokoPlus>=50){ vYokoPlus = 50f}
            if(vYokoPlus<=-50){ vYokoPlus = -50f}
            worldOffsetX += vYokoPlus.toInt()
            sekaix += vYokoPlus.toInt() //世界のｘだけ動いていれば、画面上のｘはどこでもいいのかもしれない
            migihidariCharaGamenIdoSeigen(controller)
        }else{
            //ぶつかった場合
            vYokoPlus = 0f
            //なるほど、ぶつかった場合もsekaixを「ぶつかる直前」まで進めないとだめなのかな

        }
    }

    fun syougaibutuHantei(vYokoPlusCheckyou: Float,map:Map):Boolean{

        val checksekaix = sekaix + vYokoPlusCheckyou.toInt() //世界のｘだけ動いていれば、画面上のｘはどこでもいいのかもしれない
        var checkBlock = checksekaix / 32 // 7マスから map.MASU_SIZE
        val checkMasuSyurui = map.masu[13][checkBlock+1]  //listは０から!!!
        var checkKekka = false

        when(checkMasuSyurui){
            0 -> { checkKekka = true }
            1 -> {
                //障害物にあたっている判定
                checkKekka = false
            }
            else ->{checkKekka = true }
        }
        return checkKekka
        //止まる、けれども、なんかめり込む
        //ここでは処理はできてるっぽい
        //じゃ、なんでめりこむのか？
        //即止めないから？
        //いや止めてるんだけどなー
    }



    private fun migihidariCharaGamenIdoSeigen(controller: Controller) {
        if (controller.houkou == "migi") {
            if (vYokoPlus > 0) {
                if (x <= 400) {
                    x += vYokoPlus.toInt()
                }
            }
        }
        if (controller.houkou == "hidari") {
            if (vYokoPlus < 0) {
                if (x >= 300) {
                    x += vYokoPlus.toInt()
                }
            }
        }
    }
    fun kasokudoYoko(houkou:String):Float {
        when (houkou) {
            "migi" -> {return 5.0f }
            "hidari" -> { return -5.0f }
            "nashi" -> { if (vYokoPlus == 0f) { return 0.0f }
                if (vYokoPlus > 0) { return -2.5f } else { return 2.5f } }
            else -> return 0f
        }
    }


}