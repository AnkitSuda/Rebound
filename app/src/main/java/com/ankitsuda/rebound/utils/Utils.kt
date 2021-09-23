package com.ankitsuda.rebound.utils

fun Float.cmprs(min: Float, max: Float) = min + (max - min) * this
fun Float.cmprs(min: Int, max: Int) = this.cmprs(min.toFloat(), max.toFloat())
fun Int.cmprs(min: Int, max: Int) = this.toFloat().cmprs(min.toFloat(), max.toFloat())
fun Int.cmprs(min: Float, max: Float) = this.toFloat().cmprs(min, max)

class Utils {
    companion object {


    }
}