package com.example.jajajamaru

import android.graphics.Canvas
import android.graphics.Color.argb
import android.graphics.Paint

class Jiki(var x:Int, var y:Int) {
    val ookisa = 100
    val iro = Paint()
    var sekaix = 224    //世界の左端から７マス　32＊7が初期位置
    var worldOffsetX = 0    //いる    消したら何が起きるかわからないが、いる
    var xPlus = 0f


    fun draw(canvas: Canvas) { //わかりやすいように戻した、自機の位置を黄色いマルで表示
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

        //現在、強制的に着地している。落下はしない。ここで止めてるから。
        if (y >= 500 && y < 550) {
            isJump = false
            vJump = 50f
            y = 500
        }
    }

    fun charaWorldIdoSeigen(): Boolean {
        val checkSekaix = sekaix + xPlus.toInt()
        if (checkSekaix >= 100 && checkSekaix <= (224+32*25)) { //25マスまでしか進めませんよ、みたいな
            return true
        } else {
            xPlus = 0f  //加速はいったん０にする
            return false
        }
    }


    fun jikiXido(controller: Controller){   //実際にjikiの位置を動かす処理
        CharaCameraIdoSeigen(controller)//キャラクターのカメラワークで画面内を制限
         if(charaWorldIdoSeigen()) {//ワールドの画面端で移動を制限
            //ワールド内なら移動してOK
             worldOffsetX += xPlus.toInt()
             sekaix += xPlus.toInt()
         }

   }

    fun jikiIdo(controller: Controller, map: Map) {
        val kasokudoX = kasokudoYoko(controller.houkou)
        val checkX = xPlus + kasokudoYoko(controller.houkou)
        var syougaiCheckX = false
        var syougaiCheckY = false

        jumpSyori(controller)   // ジャンプ処理　落下、障害物に当たるなど　//なんかこの位置にないとダメ

        syougaiCheckX = syougaiX(checkX, controller, map)

        if (isJump) {        //ジャンプしてたら横方向の障害物無視
            syougaiCheckX = false
            syougaiCheckY = syougaiY(checkX,controller, map)
        }
        if (syougaiCheckX) { //障害物があった場合
            when (controller.houkou) {
                "migi" -> { syougaibutuSyoriX(-17,-17) }
                "hidari" -> { syougaibutuSyoriX(17,17) }
                "nashi" -> { xPlus = 0f }
                else -> xPlus = 0f
            }
        } else { //障害物がなかった場合
            xPlus = xPlus + kasokudoX // 速度をプラス
            if (xPlus >= 30) { xPlus = 30f } //速度制限 //１マス以上加速しないことで制限
            if (xPlus <= -30) { xPlus = -30f } //速度制限 //１マス以上加速しないことで制限
            jikiXido(controller)
        }

        //ここで着地判定してる。ｙが障害物があるかどうか。なるほどー
        //ここちゃんと書かないとだめだな。
        if (isJump) {
            if(syougaiCheckY){
                isJump = false
            }
        }
    }

    fun jumpChakuchi(){

    }

    fun syougaibutuSyoriX(worldOffsetXPlus:Int, sekaixPlus:Int){    //jikiの位置が当たったら戻す処理
        xPlus = 0f  //加速はいったん０にする
        worldOffsetX += worldOffsetXPlus    //マスの半分をもどす
        sekaix += sekaixPlus
    }

    fun syougaiY(xPlusCheck:Float, controller: Controller, map:Map):Boolean{
        var checksekaix = sekaix + xPlusCheck.toInt()
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
            0 -> { checkKekka = false }
            1 -> { checkKekka = true }
            else ->{checkKekka = false }
        }
        return checkKekka
    }



    fun syougaiX(vYokoPlusCheckyou: Float, controller: Controller, map:Map):Boolean{
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
            0 -> { checkKekka = false }     //障害物にあたっていない判定
            1 -> { checkKekka = true }     //障害物にあたっている判定
            else ->{checkKekka = true }
        }
        return checkKekka
    }


    private fun CharaCameraIdoSeigen(controller: Controller) {
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