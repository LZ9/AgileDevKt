package com.lodz.android.pandora.widget.photoview

import android.content.Context
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.VelocityTracker
import android.view.ViewConfiguration

/**
 * Does a whole lot of gesture detecting.
 */
internal class CustomGestureDetector(context: Context, listener: OnGestureListener) {

    private val INVALID_POINTER_ID = -1

    private var mActivePointerId = INVALID_POINTER_ID
    private var mActivePointerIndex = 0
    private val mDetector: ScaleGestureDetector

    private var mVelocityTracker: VelocityTracker? = null
    private var mIsDragging = false
    private var mLastTouchX = 0F
    private var mLastTouchY = 0F
    private val mTouchSlop: Float
    private val mMinimumVelocity: Float
    private val mListener: OnGestureListener

    init {
        val configuration = ViewConfiguration.get(context)
        mMinimumVelocity = configuration.scaledMinimumFlingVelocity.toFloat()
        mTouchSlop = configuration.scaledTouchSlop.toFloat()

        mListener = listener
        mDetector = ScaleGestureDetector(context, object : ScaleGestureDetector.OnScaleGestureListener {
            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                val scaleFactor = detector?.scaleFactor
                if (scaleFactor == null) {
                    return false
                }
                if (java.lang.Float.isNaN(scaleFactor) || java.lang.Float.isInfinite(scaleFactor)) {
                    return false
                }
                if (scaleFactor >= 0) {
                    mListener.onScale(scaleFactor, detector.focusX, detector.focusY)
                }
                return true
            }

            override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
                return true
            }

            override fun onScaleEnd(detector: ScaleGestureDetector?) {

            }
        })

    }


    private fun getActiveX(ev: MotionEvent): Float {
        try {
            return ev.getX(mActivePointerIndex)
        } catch (e: Exception) {
            return ev.x
        }

    }

    private fun getActiveY(ev: MotionEvent): Float {
        try {
            return ev.getY(mActivePointerIndex)
        } catch (e: Exception) {
            return ev.y
        }

    }

    fun isScaling(): Boolean {
        return mDetector.isInProgress
    }

    fun isDragging(): Boolean {
        return mIsDragging
    }

    fun onTouchEvent(ev: MotionEvent): Boolean {
        try {
            mDetector.onTouchEvent(ev)
            return processTouchEvent(ev)
        } catch (e: IllegalArgumentException) {
            // Fix for support lib bug, happening when onDestroy is called
            return true
        }

    }

    private fun processTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.action and MotionEvent.ACTION_MASK
        if (action == MotionEvent.ACTION_DOWN) {
            mActivePointerId = ev.getPointerId(0)
            mVelocityTracker = VelocityTracker.obtain()
            mVelocityTracker?.addMovement(ev)

            mLastTouchX = getActiveX(ev)
            mLastTouchY = getActiveY(ev)
            mIsDragging = false
        }

        if (action == MotionEvent.ACTION_MOVE) {
            val x = getActiveX(ev)
            val y = getActiveY(ev)
            val dx = x - mLastTouchX
            val dy = y - mLastTouchY

            if (!mIsDragging) {
                // Use Pythagoras to see if drag length is larger than
                // touch slop
                mIsDragging = Math.sqrt((dx * dx + dy * dy).toDouble()) >= mTouchSlop
            }

            if (mIsDragging) {
                mListener.onDrag(dx, dy)
                mLastTouchX = x
                mLastTouchY = y

                mVelocityTracker?.addMovement(ev)
            }
        }

        if (action == MotionEvent.ACTION_CANCEL) {
            mActivePointerId = INVALID_POINTER_ID
            mVelocityTracker?.recycle()
            mVelocityTracker = null
        }

        if (action == MotionEvent.ACTION_UP) {
            mActivePointerId = INVALID_POINTER_ID
            if (mIsDragging && null != mVelocityTracker) {
                mLastTouchX = getActiveX(ev)
                mLastTouchY = getActiveY(ev)

                // Compute velocity within the last 1000ms
                mVelocityTracker?.addMovement(ev)
                mVelocityTracker?.computeCurrentVelocity(1000)

                val vX = mVelocityTracker!!.xVelocity
                val vY = mVelocityTracker!!.yVelocity

                // If the velocity is greater than minVelocity, call
                // listener
                if (Math.max(Math.abs(vX), Math.abs(vY)) >= mMinimumVelocity) {
                    mListener.onFling(mLastTouchX, mLastTouchY, -vX, -vY)
                }
            }

            // Recycle Velocity Tracker
            if (null != mVelocityTracker) {
                mVelocityTracker?.recycle()
                mVelocityTracker = null
            }
        }

        if (action == MotionEvent.ACTION_POINTER_UP) {
            val pointerIndex = Util.getPointerIndex(ev.action)
            val pointerId = ev.getPointerId(pointerIndex)
            if (pointerId == mActivePointerId) {
                // This was our active pointer going up. Choose a new
                // active pointer and adjust accordingly.
                val newPointerIndex = if (pointerIndex == 0) 1 else 0
                mActivePointerId = ev.getPointerId(newPointerIndex)
                mLastTouchX = ev.getX(newPointerIndex)
                mLastTouchY = ev.getY(newPointerIndex)
            }
        }

        mActivePointerIndex = ev.findPointerIndex(if (mActivePointerId != INVALID_POINTER_ID) mActivePointerId else 0)
        return true
    }
}