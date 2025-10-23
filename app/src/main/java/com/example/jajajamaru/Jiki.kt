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


    fun charaWorldIdoSeigen(): Boolean {
        val checkSekaix = sekaix + xPlus.toInt()
        if (checkSekaix >= 100 && checkSekaix <= (224+32*25)) { //25マスまでしか進めませんよ、みたいな
            return true
        } else {
            xPlus = 0f  //加速はいったん０にする
            return false
        }
    }


    fun idoSyori(controller: Controller, map: Map){
        idoMigiHidari(controller,map)       //横移動 x軸
        idoUeShita(controller,map)        //縦移動　y軸
    }



    fun idoMigiHidari(controller: Controller, map: Map){
        val kasokudox = kasokudoYoko(controller.houkou)

        //速度制限をつける
        val maxSpeed = 45f
        val xPlus1SokudoSeigenCand = xPlus + kasokudox
        val xPlus1Cand = if(xPlus1SokudoSeigenCand>= maxSpeed){maxSpeed}else if(xPlus1SokudoSeigenCand<= -maxSpeed){-maxSpeed}else{xPlus1SokudoSeigenCand}
        val x1CandA = sekaix + xPlus1Cand.toInt()


        var checkSyougaibutu = true
        //障害物チェックをつける //今は素通り
        if (controller.houkou == "migi") {
            val checkPoint = x1CandA
            val checkBlock = (( checkPoint/ 32)+6)
            checkSyougaibutu = if(map.masu[13][checkBlock] == 1){true}else{false}

        }
        val sekaix1Cand = if(checkSyougaibutu){x1CandA}else{sekaix}



        val sekaix1 = if(sekaix1Cand>349){349}else if(sekaix1Cand<0){0}else{sekaix1Cand}
        val xPlus1 =  if(sekaix1Cand>877){0f}else if(sekaix1Cand<0){0f}else{xPlus1Cand}

        sekaix  = sekaix1
        xPlus = xPlus1

    }

    fun CharaCameraIdoSeigen(controller: Controller) {
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




    fun idoUeShita(controller: Controller, map: Map){
        jumpSyori(controller,map)   // ジャンプ処理　落下、障害物に当たるなど　//なんかこの位置にないとダメ
        var syougaiCheckY  = syougaiY(controller, map)
        if (isJump) { if(syougaiCheckY){ isJump = false } }        //縦方向に障害物があった場合、ジャンプを中止する。
    }

    fun jumpSyori(controller: Controller,map:Map) {
        if (controller.houkou == "jump") {  //ジャンプしてなかったらジャンプする
            if (isJump == false) {
                isJump = true
                vJump = 50f
                y -= vJump.toInt()
            }
        }
        if (isJump) {   //ジャンプ中ならジャンプを継続する
            vJump = vJump + kasokudoJump()
            y -= vJump.toInt()
        }
    }




    fun syougaiY( controller: Controller, map:Map):Boolean{
        var checksekaix = sekaix
        if (controller.houkou == "migi") { checksekaix += ookisa / 2 }
        if (controller.houkou == "hidari") { checksekaix -= ookisa / 2 }
        var checkBlock = checksekaix / 32
        var checksekaiy = y - vJump.toInt()
        var yBlock = 0
        if(checksekaiy<=550 && checksekaiy >=501){yBlock = 14}
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



    fun kasokudoYoko(houkou:String):Float {
        when (houkou) {
            "migi" -> {return 5.0f }
            "migi" -> {return 5.0f }
            "hidari" -> { return -5.0f }
            "nashi" -> { if (xPlus == 0f) { return 0.0f }
                if (xPlus > 0) { return -2.5f } else { return 2.5f } }
            else -> return 0f
        }
    }


}