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


    fun draw(canvas: Canvas) {
        iro.style = Paint.Style.FILL
        iro.color = argb(255, 255, 255, 150)
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

            //これ、チェック後に移動しなとダメじゃない？
            y -= vJump.toInt()
        }
        if (y >= 500 && y < 550) {
            isJump = false
            vJump = 50f

            //これ、チェック後に移動しなとダメじゃない？これも
            y = 500
        }

    }


    fun jikiIdo(controller: Controller, map: Map){
        val kasokudo = kasokudoYoko(controller.houkou)
        val idoGoXCheck = (vYokoPlus + kasokudo).toInt()
        val idoGoYCheck = (y - vJump).toInt()
        //ここでチェックしてからｘ、ｙともに移動させたらいいんじゃないかな
        var syougaibutuCheck = false
        var syougaibutuJump = false
        var idoGoCheck = jikiIdoCheck(controller,map,idoGoXCheck,idoGoYCheck)//あれ、縦と横で二つ真偽値はかえせねーな
    }

    

    fun jikiIdoCheck(controller: Controller, map:Map,idoGoXCheck:Int,idoGoYCheck:Int): Boolean{
        var checksekaix = idoGoXCheck
        if (controller.houkou == "migi") { checksekaix += ookisa / 2 }
        if (controller.houkou == "hidari") { checksekaix -= ookisa / 2 }
        var checkBlock = checksekaix / 32 // 7マスから map.MASU_SIZE
        val checkMasuSyuruiX = map.masu[13][checkBlock+1]  //listは０から!!!
        // ここまでｘのはなし

        var checksekaiy = idoGoYCheck //世界のｘだけ動いていれば、画面上のｘはどこでもいいのかもしれない
        var yBlock = 0
        if(checksekaiy<=500 && checksekaiy >=468){yBlock = 13}
        if(checksekaiy<=467 && checksekaiy >=436){yBlock = 12}
        if(checksekaiy<=435 && checksekaiy >=404){yBlock = 11}

        val checkMasuSyuruiJump = map.masu[yBlock][checkBlock+1]



        when (controller.houkou) {
            "migi" -> {
                vYokoPlus = 0f
                worldOffsetX += -17    //マスの半分をもどす
                sekaix += -17
            }

            "hidari" -> {
                vYokoPlus = 0f
                worldOffsetX += 17    //マスの半分をもどす
                sekaix += 17
            }

            "nashi" -> {
                vYokoPlus = 0f
            }
        }
        return true
    }

    fun jikiYokoIdo(controller: Controller, map: Map) {
        val kasokudo = kasokudoYoko(controller.houkou)
        val vYokoPlusCheckyou = vYokoPlus + kasokudo
        var syougaibutuCheck = false
        var syougaibutuJump = false


        if (isJump) {
            syougaibutuCheck = true
            syougaibutuJump = syougaibutuJump(vYokoPlusCheckyou,controller, map) //この時点では判定だけする
        } else {
            syougaibutuCheck = syougaibutuHantei(vYokoPlusCheckyou, controller, map)
        }

        if (syougaibutuCheck) {
            vYokoPlus = vYokoPlus + kasokudo
            //速度制限
            if (vYokoPlus >= 30) {
                vYokoPlus = 30f
            } //１マス以上加速しないことで制限
            if (vYokoPlus <= -30) {
                vYokoPlus = -30f
            } //１マス以上加速しないことで制限
            worldOffsetX += vYokoPlus.toInt()
            sekaix += vYokoPlus.toInt() //世界のｘだけ動いていれば、画面上のｘはどこでもいいのかもしれない
            migihidariCharaGamenIdoSeigen(controller)
        } else {
            when (controller.houkou) {
                "migi" -> {
                    vYokoPlus = 0f
                    worldOffsetX += -17    //マスの半分をもどす
                    sekaix += -17
                }

                "hidari" -> {
                    vYokoPlus = 0f
                    worldOffsetX += 17    //マスの半分をもどす
                    sekaix += 17
                }

                "nashi" -> {
                    vYokoPlus = 0f
                }
                else -> vYokoPlus = 0f
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

        var checksekaix = sekaix + vYokoPlusCheckyou.toInt() //世界のｘだけ動いていれば、画面上のｘはどこでもいいのかもしれない
        if (controller.houkou == "migi") { checksekaix += ookisa / 2 }
        if (controller.houkou == "hidari") { checksekaix -= ookisa / 2 }
        var checkBlock = checksekaix / 32 // 7マスから map.MASU_SIZE
        val checkMasuSyuruiX = map.masu[13][checkBlock+1]  //listは０から!!!
        // ここまでｘのはなし

        var checksekaiy = y - vJump.toInt() //世界のｘだけ動いていれば、画面上のｘはどこでもいいのかもしれない
        var yBlock = 0
        if(checksekaiy<=500 && checksekaiy >=468){yBlock = 13}
        if(checksekaiy<=467 && checksekaiy >=436){yBlock = 12}
        if(checksekaiy<=435 && checksekaiy >=404){yBlock = 11}


        val checkMasuSyuruiJump = map.masu[yBlock][checkBlock+1]



        //ここでｙを含めた障害物をチェックする
        //横移動との違いは、ｘ、ｙともにチェックする、ということだ。

        //ここでチェックするマスの種類をチェックするが、ｙ方向にも必要となる
        //１３が変わる？っていうこと？
        //１３っていうのは５００～４６８っていうことだと思う。
        //ｙが入っているから

        //checkMasuSyuruiJumpにはMapの１，０が入っている、、、、はず

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
        var checksekaix = sekaix + vYokoPlusCheckyou.toInt()
        if (controller.houkou == "migi") {
            checksekaix += ookisa / 2
        }

        if (controller.houkou == "hidari") {
            checksekaix -= ookisa / 2
        }

        var checkBlock = checksekaix / 32

        val checkMasuSyurui = map.masu[13][checkBlock+1]
        var checkKekka = false
        when(checkMasuSyurui){
            0 -> { checkKekka = true }
            1 -> {
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