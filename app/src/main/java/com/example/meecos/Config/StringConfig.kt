package com.example.meecos.Config

import android.text.Spannable
import android.text.style.UnderlineSpan

/**
 * 文字列の読み（カタカナ）の頭文字を識別し、ひらがなのどの行に振り分けられるかを返すメソッド
 * 『0→あ』からスタートし、『10→#』の中のどこかに振り分けられる
 * 空白やカタカナ以外の場合、"#"に値する10を戻り値とする
 */
fun String.selectLineNumber(): Int{
    val lineNum: Int
    if (this == "") {
        lineNum = 10
    } else {
        when (this.substring(0, 1)) {
            "ア", "イ", "ウ", "エ", "オ" -> {
                lineNum = 0
            }
            "カ", "キ", "ク", "ケ", "コ", "ガ", "ギ", "グ", "ゲ", "ゴ" -> {
                lineNum = 1
            }
            "サ", "シ", "ス", "セ", "ソ", "ザ", "ジ", "ズ", "ゼ", "ゾ" -> {
                lineNum = 2
            }
            "タ", "チ", "ツ", "テ", "ト", "ダ", "ヂ", "ヅ", "デ", "ド" -> {
                lineNum = 3
            }
            "ナ", "ニ", "ヌ", "ネ", "ノ" -> {
                lineNum = 4
            }
            "ハ", "ヒ", "フ", "ヘ", "ホ", "バ", "ビ", "ブ", "ベ", "ボ", "パ", "ピ", "プ", "ペ", "ポ" -> {
                lineNum = 5
            }
            "マ", "ミ", "ム", "メ", "モ" -> {
                lineNum = 6
            }
            "ヤ", "ユ", "ヨ" -> {
                lineNum = 7
            }
            "ラ", "リ", "ル", "レ", "ロ" -> {
                lineNum = 8
            }
            "ワ", "ヲ", "ン" -> {
                lineNum = 9
            }
            else -> {
                lineNum = 10
            }
        }
    }
    return lineNum
}

/**
 * 文字列に下線を引くメソッド
 */
fun String.drawUnderline(): Spannable {
    // 1. ファクトリーにおまかせ
    val t = Spannable.Factory.getInstance().newSpannable(this)
    // 2. 下線オブジェクト(他にも種類ある)
    val us = UnderlineSpan()
    // 3. 装飾セット(装飾オブジェクト、開始位置、終了位置、装飾オブジェクト用？フラグ)
    t.setSpan(us, 0, this.length, t.getSpanFlags(us))
    return t
}

