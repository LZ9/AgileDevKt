package com.lodz.android.corekt.anko

import java.nio.ByteBuffer

/**
 * Byte扩展类
 * @author zhouL
 * @date 2021/10/26
 */

/** 将数组[other]线性合并到当前数组之后 */
fun ByteArray.merger(other: ByteArray): ByteArray {
    val result = ByteArray(this.size + other.size)
    System.arraycopy(this, 0, result, 0, this.size)
    System.arraycopy(other, 0, result, this.size, other.size)
    return result
}

/** ByteArray转Int（高字节在前） */
@Deprecated("一般情况请试用低字节在前的toByteArrayLF方法")
fun Int.toByteArrayHF(): ByteArray {
    val byteArray = ByteArray(4)
    byteArray[0] = (this and 0xff).toByte()
    byteArray[1] = (this shr 8 and 0xff).toByte()
    byteArray[2] = (this shr 16 and 0xff).toByte()
    byteArray[3] = (this shr 24 and 0xff).toByte()
    return byteArray
}

/** ByteArray转Int（低字节在前） */
fun Int.toByteArrayLF(): ByteArray {
    val byteArray = ByteArray(4)
    byteArray[3] = (this and 0xff).toByte()
    byteArray[2] = (this shr 8 and 0xff).toByte()
    byteArray[1] = (this shr 16 and 0xff).toByte()
    byteArray[0] = (this shr 24 and 0xff).toByte()
    return byteArray
}

/** ByteArray转Int */
fun ByteArray.toInt(): Int = ByteBuffer.wrap(this).int
/** ByteArray转Int */
@JvmOverloads
fun ByteArray.toInt(offset: Int, length: Int = this.size): Int = ByteBuffer.wrap(this, offset, length).int

/** ByteArray转Short */
fun ByteArray.toShort(): Short = ByteBuffer.wrap(this).short
/** ByteArray转Short */
@JvmOverloads
fun ByteArray.toShort(offset: Int, length: Int = this.size): Short = ByteBuffer.wrap(this, offset, length).short

/** ByteArray转Long */
fun ByteArray.toLong(): Long = ByteBuffer.wrap(this).long
/** ByteArray转Long */
@JvmOverloads
fun ByteArray.toLong(offset: Int, length: Int = this.size): Long = ByteBuffer.wrap(this, offset, length).long

/** ByteArray转Float */
fun ByteArray.toFloat(): Float = ByteBuffer.wrap(this).float
/** ByteArray转Float */
@JvmOverloads
fun ByteArray.toFloat(offset: Int, length: Int = this.size): Float = ByteBuffer.wrap(this, offset, length).float

/** ByteArray转Double */
fun ByteArray.toDouble(): Double = ByteBuffer.wrap(this).double
/** ByteArray转Double */
@JvmOverloads
fun ByteArray.toDouble(offset: Int, length: Int = this.size): Double = ByteBuffer.wrap(this, offset, length).double