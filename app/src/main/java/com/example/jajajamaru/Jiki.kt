package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color.argb
import android.graphics.Paint

class Jiki(var x:Int, var y:Int) {
    val ookisa = 100
    val iro = Paint()
    var sekaix = 224    //世界の左端から７マス　32＊7が初期位置
    var worldOffsetX = 0    //いる
    var xPlus = 0f


    fun draw(canvas: Canvas) {//わかりやすいように戻した、自機の位置を黄色いマルで表示
        iro.style = Paint.Style.FILL
        iro.color = argb(255, 255, 255, 150)
        canvas.drawCircle(x.toFloat(),(y).toFloat(),(ookisa/2).toFloat(),iro)
    }

    var isJump = false
    var vJump = 0f

    fun kasokudoJump(): Float {
        return -5.0f
    }

    fun jumpSyori(controller: Controller) {
        if (controller.houkou == "jump") {
            if (isJump == false) {
                isJump = true
                vJump = 50f
                y -= vJump.toInt()
            }
        }
        if (isJump) {
            vJump = vJump + kasokudoJump()
            y -= vJump.toInt()
        }
        if (y >= 500 && y < 550) {
            isJump = false
            vJump = 50f
            y = 500
        }
    }


    fun jikiYokoIdo(controller: Controller, map: Map) {
        val kasokudoX = kasokudoYoko(controller.houkou)
        val checkX = xPlus + kasokudoYoko(controller.houkou)
        var syougaibutuX = false
        var syougaibutuJump = false

        if (isJump) {
            syougaibutuX = true
            syougaibutuJump = syougaibutuJump(checkX,controller, map) //この時点では判定だけする
        } else {
            syougaibutuX = syougaibutuHantei(checkX, controller, map)
        }

        if (syougaibutuX) {
            //障害物がなかった場合
            xPlus = xPlus + kasokudoX
            //速度制限
            if (xPlus >= 30) { xPlus = 30f } //１マス以上加速しないことで制限
            if (xPlus <= -30) { xPlus = -30f } //１マス以上加速しないことで制限

            worldOffsetX += xPlus.toInt()
            sekaix += xPlus.toInt()
            migihidariCharaGamenIdoSeigen(controller)//画面端で移動を制限
        } else {
            //障害物があった場合
            when (controller.houkou) {
                "migi" -> {
                    xPlus = 0f
                    worldOffsetX += -17    //マスの半分をもどす
                    sekaix += -17
                }

                "hidari" -> {
                    xPlus = 0f
                    worldOffsetX += 17    //マスの半分をもどす
                    sekaix += 17
                }

                "nashi" -> {
                    xPlus = 0f
                }
                else -> xPlus = 0f
            }
        }

        if (isJump) {
            if(syougaibutuJump){

            }else{
                isJump = false
            }
        }

    }

    fun syougaibutuJump( vYokoPlusCheckyou:Float,controller: Controller, map:Map):Boolean{
        var checksekaix = sekaix + vYokoPlusCheckyou.toInt()
        if (controller.houkou == "migi") { checksekaix += ookisa / 2 }
        if (controller.houkou == "hidari") { checksekaix -= ookisa / 2 }
        var checkBlock = checksekaix / 32
        val checkMasuSyuruiX = map.masu[13][checkBlock+1]

        var checksekaiy = y - vJump.toInt()
        var yBlock = 0
        if(checksekaiy<=500 && checksekaiy >=468){yBlock = 13}
        if(checksekaiy<=467 && checksekaiy >=436){yBlock = 12}
        if(checksekaiy<=435 && checksekaiy >=404){yBlock = 11}


        val checkMasuSyuruiJump = map.masu[yBlock][checkBlock+1]

        var checkKekka = false
        when(checkMasuSyuruiJump){
            0 -> { checkKekka = true }
            1 -> {
                checkKekka = false
            }
            else ->{checkKekka = true }
        }
        return checkKekka
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
            if (xPlus > 0) {
                if (x <= 400) {
                    x += xPlus.toInt()
                }
            }
        }
        if (controller.houkou == "hidari") {
            if (xPlus < 0) {
                if (x >= 300) {
                    x += xPlus.toInt()
                }
            }
        }
    }
    fun kasokudoYoko(houkou:String):Float {
        when (houkou) {
            "migi" -> {return 5.0f }
            "hidari" -> { return -5.0f }
            "nashi" -> { if (xPlus == 0f) { return 0.0f }
                if (xPlus > 0) { return -2.5f } else { return 2.5f } }
            else -> return 0f
        }
    }


}