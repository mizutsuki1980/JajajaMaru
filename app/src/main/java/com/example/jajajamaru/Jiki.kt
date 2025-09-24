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


    fun draw(canvas: Canvas){
        iro.style = Paint.Style.FILL
        iro.color = argb(255, 255, 255, 150)
            //  canvas.drawCircle(x.toFloat(),(y).toFloat(),(ookisa/2).toFloat(),iro) //自機の描画
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
        var syougaibutuCheck = syougaibutuHantei(vYokoPlusCheckyou,controller,map)
        if(syougaibutuCheck) {
            vYokoPlus = vYokoPlus + kasokudo
            //速度制限
            if(vYokoPlus>=30){ vYokoPlus = 30f} //１マス以上加速しないことで制限
            if(vYokoPlus<=-30){ vYokoPlus = -30f} //１マス以上加速しないことで制限
            worldOffsetX += vYokoPlus.toInt()
            sekaix += vYokoPlus.toInt() //世界のｘだけ動いていれば、画面上のｘはどこでもいいのかもしれない
            migihidariCharaGamenIdoSeigen(controller)
        }else{

            when (controller.houkou) {
                "migi" -> {
                    vYokoPlus = 0f
                    worldOffsetX +=  -17    //マスの半分をもどす
                    sekaix += -17
                }
                "hidari" -> {
                    vYokoPlus = 0f
                    worldOffsetX +=  17    //マスの半分をもどす
                    sekaix += 17
                }
                "nashi" -> { vYokoPlus = 0f }
                else -> vYokoPlus = 0f

            }
            //ぶつかった場合　右方向のみ
        //なるほど、ぶつかった場合もsekaixを「ぶつかる直前」まで進めないとだめなのかな
        }
    }

    fun syougaibutuHantei(vYokoPlusCheckyou: Float, controller: Controller, map:Map):Boolean{
        var checksekaix = sekaix + vYokoPlusCheckyou.toInt() //世界のｘだけ動いていれば、画面上のｘはどこでもいいのかもしれない
        if (controller.houkou == "migi") {
            checksekaix += ookisa / 2 //自機のookisaを計算に加える
        }

        if (controller.houkou == "hidari") {
            checksekaix -= ookisa / 2 //自機のookisaを計算に加える
        }

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