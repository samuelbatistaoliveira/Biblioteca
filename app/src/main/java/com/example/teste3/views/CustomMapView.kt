package com.example.teste3.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import com.example.teste3.R


class CustomMapView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // ── Mapa ──────────────────────────────────────────────────
    private var mapaBitmap: Bitmap? = null
    private var pontoX: Float = 0.5f
    private var pontoY: Float = 0.5f
    private var temPonto: Boolean = true

    // ── Transformação (zoom + pan) ────────────────────────────
    private val matrix = Matrix()
    private val matrixValues = FloatArray(9)

    private var scaleFactor = 1f
    private val minScale = 0.8f
    private val maxScale = 5f

    private var lastTouchX = 0f
    private var lastTouchY = 0f
    private var isDragging = false
    private var activePointerId = MotionEvent.INVALID_POINTER_ID

    // ── Ponto de localização ──────────────────────────────────
    private val paintPonto = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#C0392B")
        style = Paint.Style.FILL
    }
    private val paintPontoBorda = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#FFFFFF")
        style = Paint.Style.STROKE
        strokeWidth = 3f
    }
    private val paintPulso = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#C0392B")
        style = Paint.Style.STROKE
        strokeWidth = 2f
    }

    private var pulsoAlpha = 255
    private var pulsoRaio = 0f
    private val raioBase = 14f
    private val raioMaxPulso = 32f
    private var pulsoAnimator: ValueAnimator? = null

    // ── Pinch-to-zoom ─────────────────────────────────────────
    private val scaleDetector = ScaleGestureDetector(context,
        object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
            override fun onScale(detector: ScaleGestureDetector): Boolean {
                val novoScale = scaleFactor * detector.scaleFactor
                scaleFactor = novoScale.coerceIn(minScale, maxScale)
                matrix.postScale(
                    detector.scaleFactor,
                    detector.scaleFactor,
                    detector.focusX,
                    detector.focusY
                )
                limitarPan()
                invalidate()
                return true
            }
        })

    init {
        iniciarAnimacaoPulso()
    }

    /**
     * Define o mapa e a posição do ponto.
     * @param drawableRes  R.drawable.map_terreo ou R.drawable.map_primeiro_andar
     * @param px           Posição X proporcional (0.0f a 1.0f). Use -1f para sem ponto.
     * @param py           Posição Y proporcional (0.0f a 1.0f). Use -1f para sem ponto.
     */
    fun setMapa(drawableRes: Int, px: Float, py: Float) {
        temPonto = px >= 0f && py >= 0f
        pontoX = if (temPonto) px else 0.5f
        pontoY = if (temPonto) py else 0.5f

        val drawable = ContextCompat.getDrawable(context, drawableRes)
        mapaBitmap = (drawable as? android.graphics.drawable.BitmapDrawable)?.bitmap
            ?: run {
                val bmp = Bitmap.createBitmap(
                    drawable!!.intrinsicWidth,
                    drawable.intrinsicHeight,
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bmp)
                drawable.setBounds(0, 0, canvas.width, canvas.height)
                drawable.draw(canvas)
                bmp
            }
        resetMatrix()
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        resetMatrix()
    }

    private fun resetMatrix() {
        val bmp = mapaBitmap ?: return
        if (width == 0 || height == 0) return

        val scaleX = width.toFloat() / bmp.width
        val scaleY = height.toFloat() / bmp.height

        // minOf garante que a imagem inteira aparece sem cortes
        val initialScale = minOf(scaleX, scaleY)
        scaleFactor = initialScale

        // Centraliza a imagem
        val dx = (width - bmp.width * initialScale) / 2f
        val dy = (height - bmp.height * initialScale) / 2f

        matrix.reset()
        matrix.postScale(initialScale, initialScale)
        matrix.postTranslate(dx, dy)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val bmp = mapaBitmap ?: return

        // Fundo bege para as bordas que ficam fora da imagem
        canvas.drawColor(Color.parseColor("#F5EFE6"))

        canvas.save()
        canvas.concat(matrix)

        canvas.drawBitmap(bmp, 0f, 0f, null)

        if (temPonto) {
            val px = pontoX * bmp.width
            val py = pontoY * bmp.height

            // Pulso
            paintPulso.alpha = pulsoAlpha
            canvas.drawCircle(px, py, pulsoRaio, paintPulso)

            // Ponto principal
            canvas.drawCircle(px, py, raioBase, paintPonto)
            canvas.drawCircle(px, py, raioBase, paintPontoBorda)

            // Cruz central
            val crossSize = raioBase * 0.4f
            paintPontoBorda.strokeWidth = 2f
            canvas.drawLine(px - crossSize, py, px + crossSize, py, paintPontoBorda)
            canvas.drawLine(px, py - crossSize, px, py + crossSize, paintPontoBorda)
        }

        canvas.restore()
    }

    private fun iniciarAnimacaoPulso() {
        pulsoAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1400
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener { anim ->
                val frac = anim.animatedFraction
                pulsoRaio = raioBase + (raioMaxPulso - raioBase) * frac
                pulsoAlpha = ((1f - frac) * 200).toInt()
                invalidate()
            }
            start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        pulsoAnimator?.cancel()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleDetector.onTouchEvent(event)

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                lastTouchX = event.x
                lastTouchY = event.y
                activePointerId = event.getPointerId(0)
                isDragging = true
            }
            MotionEvent.ACTION_MOVE -> {
                if (isDragging && !scaleDetector.isInProgress) {
                    val idx = event.findPointerIndex(activePointerId)
                    if (idx >= 0) {
                        val dx = event.getX(idx) - lastTouchX
                        val dy = event.getY(idx) - lastTouchY
                        matrix.postTranslate(dx, dy)
                        limitarPan()
                        lastTouchX = event.getX(idx)
                        lastTouchY = event.getY(idx)
                        invalidate()
                    }
                }
            }
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                isDragging = false
                activePointerId = MotionEvent.INVALID_POINTER_ID
            }
            MotionEvent.ACTION_POINTER_UP -> {
                val idx = event.actionIndex
                val pid = event.getPointerId(idx)
                if (pid == activePointerId) {
                    val newIdx = if (idx == 0) 1 else 0
                    lastTouchX = event.getX(newIdx)
                    lastTouchY = event.getY(newIdx)
                    activePointerId = event.getPointerId(newIdx)
                }
            }
        }
        return true
    }

    private fun limitarPan() {
        val bmp = mapaBitmap ?: return
        matrix.getValues(matrixValues)
        val curScale = matrixValues[Matrix.MSCALE_X]
        val curTx = matrixValues[Matrix.MTRANS_X]
        val curTy = matrixValues[Matrix.MTRANS_Y]

        val bmpW = bmp.width * curScale
        val bmpH = bmp.height * curScale

        var dx = 0f
        var dy = 0f

        if (bmpW <= width) {
            dx = (width - bmpW) / 2f - curTx
        } else {
            if (curTx > 0) dx = -curTx
            if (curTx + bmpW < width) dx = width - (curTx + bmpW)
        }

        if (bmpH <= height) {
            dy = (height - bmpH) / 2f - curTy
        } else {
            if (curTy > 0) dy = -curTy
            if (curTy + bmpH < height) dy = height - (curTy + bmpH)
        }

        if (dx != 0f || dy != 0f) matrix.postTranslate(dx, dy)
    }
}