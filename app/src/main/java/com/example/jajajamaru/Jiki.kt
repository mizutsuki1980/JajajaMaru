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


    fun jikiXido(controller: Controller){   //実際にjikiの位置を動かす処理
        CharaCameraIdoSeigen(controller)//キャラクターのカメラワークで画面内を制限
         if(charaWorldIdoSeigen()) {//ワールドの画面端で移動を制限
            //ワールド内なら移動してOK
             worldOffsetX += xPlus.toInt()
             sekaix += xPlus.toInt()
         }

   }

    fun idoSyori(controller: Controller, map: Map){
        idoMigiHidari(controller,map)       //横移動 x軸
        idoUeShita(controller,map)        //縦移動　y軸
    }


    fun idoMigiHidari(controller: Controller, map: Map){
        val kasokudoX = kasokudoYoko(controller.houkou)//次の速度を計算
        xPlus = xPlus + kasokudoX // 速度をプラス
        if (xPlus >= 30) { xPlus = 30f } //速度制限 //１マス以上加速しないことで制限
        if (xPlus <= -30) { xPlus = -30f } //速度制限 //１マス以上加速しないことで制限
        val checkX = xPlus + kasokudoYoko(controller.houkou)//次の位置を計算

        //現在のキャラの右端、左端
        val charaMigihajiX = (sekaix +(ookisa/2)).toInt()
        val charaHidarihajiX = (sekaix -(ookisa/2)).toInt()

        //次の位置の右端、左端
        val checkCharaMigihajiX = (checkX +(ookisa/2)).toInt()
        val checkCharaHidarihajiX = (checkX -(ookisa/2)).toInt()

        //右端を決めるためのチェック
        //次の右端の座標ｘはわかっているのだから、true か　falseで返せるはず。
        if(mapCheck(map,checkCharaMigihajiX)){

        }
        //左端を決めるためのチェック


        var syougaiCheckX = syougaiX(checkX, controller, map)//次の位置に障害物あるか？
        if (isJump) { syougaiCheckX = false }//ジャンプなら障害物は無視

        if (syougaiCheckX) { //横方向に障害物があった場合
            when (controller.houkou) {
                "migi" -> { syougaibutuSyoriX(-17,-17) }//右に障害物があれば半マス戻す
                "hidari" -> { syougaibutuSyoriX(17,17) }//左に障害物があれば半マス戻す
                "nashi" -> { xPlus = 0f }//なしだとどーすんの？ちゃんと決めてない。ここだ！多分すりぬけるの。
                else -> xPlus = 0f
            }
        } else { //障害物がなかった場合
            jikiXido(controller)//障害物がなければ、はじめて時期を移動させる
        }
    }
    fun mapCheck(map:Map,checkCharaMigihajiX:Int): Boolean{
        var check = false
        val checkBlock = checkCharaMigihajiX / 32
        if(map.masu[13][checkBlock] == 1){check = false}
        return check
    }


    fun syougaiX(checkX: Float, controller: Controller, map:Map):Boolean{
        var checksekaix = sekaix + checkX.toInt() //世界のｘだけ動いていれば、画面上のｘはどこでもいいのかもしれない
        if (controller.houkou == "migi") { checksekaix += ookisa / 2 }
        if (controller.houkou == "hidari") { checksekaix -= ookisa / 2 }
        var checkBlock = checksekaix / 32 // 7マスから map.MASU_SIZE
        var checkMasuSyurui = map.masu[13][checkBlock+3] //+2はリストの＋１と隣のマスの＋１
        if (controller.houkou == "hidari") { checkMasuSyurui = map.masu[13][checkBlock] }//-1は隣のマス
        //checkBlock+3とかcheckBlock-1にしてたのは、キャラクターの大きさを考慮しての為
        //ここちゃんと計算した方がいいんじゃないか説
        var checkKekka = false
        when(checkMasuSyurui){
            0 -> { checkKekka = false }     //障害物にあたっていない判定
            1 -> { checkKekka = true }     //障害物にあたっている判定
            else ->{checkKekka = true }
        }
        return checkKekka
    }



    fun syougaibutuSyoriX(worldOffsetXPlus:Int, sekaixPlus:Int){    //jikiの位置が当たったら戻す処理
        xPlus = 0f  //加速はいったん０にする
        worldOffsetX += worldOffsetXPlus    //マスの半分をもどす
        sekaix += sekaixPlus
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




    //ここをリファクタリングしてみる
    //やっぱｘPlusCheckっていうやつは、なくてもいける。なんか画面でみると、一つ左のブロックの高さを参照しているようにみえる。
    fun syougaiY( controller: Controller, map:Map):Boolean{
        var checksekaix = sekaix
        if (controller.houkou == "migi") { checksekaix += ookisa / 2 }
        if (controller.houkou == "hidari") { checksekaix -= ookisa / 2 }
        var checkBlock = checksekaix / 32
        val checkMasuSyuruiX = map.masu[13][checkBlock+1]
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