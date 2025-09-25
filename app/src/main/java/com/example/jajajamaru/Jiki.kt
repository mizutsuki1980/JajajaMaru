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
        //  canvas.drawCircle(x.toFloat(),(y).toFloat(),(ookisa/2).toFloat(),iro) //自機の描画
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
        //ジャンプの処理はこの辺で行っている。ｙが０地点ならジャンプを終了する、みたいなのを書く
        if (y >= 500 && y < 550) {
            isJump = false
            vJump = 50f
            y = 500
        }
        //これを、障害物によって変えるようにする。


    }


    fun jikiYokoIdo(controller: Controller, map: Map) {
        val kasokudo = kasokudoYoko(controller.houkou)
        val vYokoPlusCheckyou = vYokoPlus + kasokudo
        var syougaibutuCheck = false
        var syougaibutuJump = false

        //まず、ここでジャンプか、ジャンプじゃないか、分ける。
        // ジャンプじゃないなら、今まで通りの処理でいい。

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
             //もしジャンプかつ、障害物に当たってるよ、という判定なら、ここにくる

            }else{
                isJump = false
            }
        }

    }

    fun syougaibutuJump( vYokoPlusCheckyou:Float,controller: Controller, map:Map):Boolean{
        //x,yは既にわかっているわけで、そこから障害物かどうか判定する、

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
                //障害物にあたっている判定
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