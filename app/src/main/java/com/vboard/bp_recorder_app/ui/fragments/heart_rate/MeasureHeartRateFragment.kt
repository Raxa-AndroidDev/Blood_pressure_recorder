package com.vboard.bp_recorder_app.ui.fragments.heart_rate

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.hardware.Camera
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PowerManager
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.permissionx.guolindev.PermissionX
import com.vboard.bp_recorder_app.R
import com.vboard.bp_recorder_app.data.database.db_tables.HeartRateTable
import com.vboard.bp_recorder_app.databinding.FragmentMeasureHeartRateBinding
import com.vboard.bp_recorder_app.utils.ImageProcessing
import java.util.concurrent.atomic.AtomicBoolean

class MeasureHeartRateFragment : Fragment() {
    lateinit var binding: FragmentMeasureHeartRateBinding

    var progress = 0
    var timer: CountDownTimer? = null
    var heart_progressbar: ProgressBar? = null

    private var TAG = "HeartRateMonitor"
    private var processing = AtomicBoolean(false)

    private var preview: SurfaceView? = null
    private var previewHolder: SurfaceHolder? = null
    private var camera: Camera? = null
    private var image: View? = null
    private var text: TextView? = null

    private var wakeLock: PowerManager.WakeLock? = null

    private var averageIndex = 0
    private var averageArraySize = 4
    private var averageArray = IntArray(averageArraySize)

    enum class TYPE {
        GREEN, RED
    }


    private var currentType = TYPE.GREEN

    fun getCurrent(): TYPE {
        return currentType
    }


    private var beatsIndex = 0
    private var beatsArraySize = 3
    private var beatsArray = IntArray(beatsArraySize)
    private var beats = 0.0
    private var startTime: Long = 0

    var heartRate: String? = null
    private var calculatingTHR = false


    var heartRateTable: HeartRateTable? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMeasureHeartRateBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        preview = binding.preview
        previewHolder = preview!!.holder
        image = binding.image
        text = binding.text
        heart_progressbar = binding.heartProgressbar


        if ( isCameraPermissionAllowed()){

            initViews()

        }










    }

    private fun initViews() {







        previewHolder!!.addCallback(surfaceCallback)
        previewHolder!!.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)

        // val pm = requireActivity().getSystemService(Context.POWER_SERVICE) as PowerManager?
        // wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen")

        timer = object : CountDownTimer(20000, 20000) {
            override fun onTick(l: Long) {
                progress = progress + 1
                heart_progressbar!!.progress = progress
                Log.e("TAG", "onTick: progress is" + progress)
            }

            override fun onFinish() {}
        }




        camera = Camera.open()
        startTime = System.currentTimeMillis()
    }


   /* override fun onResume() {
        super.onResume()
        // wakeLock!!.acquire()

        if ( isCameraPermissionAllowed()){
            camera = Camera.open()
            startTime = System.currentTimeMillis()
        }


    }*/

    override fun onDestroyView() {

        camera!!.setPreviewCallback(null)
        camera!!.stopPreview()
        camera!!.release()
        camera = null

        super.onDestroyView()
    }


    /*override fun onPause() {
        super.onPause()
        // wakeLock!!.release()
        camera!!.setPreviewCallback(null)
        camera!!.stopPreview()
        camera!!.release()
        camera = null
    }
*/

    private fun isCameraPermissionAllowed():Boolean {

         var permissionStatus = false
        PermissionX.init(requireActivity())
            .permissions(Manifest.permission.CAMERA)
            .onExplainRequestReason { scope, deniedList ->
                scope.showRequestReasonDialog(deniedList, "Core fundamental are based on these permissions", "OK", "Cancel")
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(deniedList, "You need to allow camera permissions in Settings manually", "OK", "Cancel")
            }
            .request { allGranted, grantedList, deniedList ->
               permissionStatus = allGranted

            }

        return permissionStatus
    }


    private val previewCallback =
        Camera.PreviewCallback { data, cam ->
            if (data == null) throw NullPointerException()
            val size = cam.parameters.previewSize ?: throw NullPointerException()
            if (!processing.compareAndSet(false, true)) return@PreviewCallback
            val width = size.width
            val height = size.height
            val imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(data.clone(), height, width)


Toast.makeText(requireContext(),imgAvg.toString(),Toast.LENGTH_SHORT).show()

            if (imgAvg == 0 || imgAvg < 200) {
                progress = 0
                timer!!.cancel()
                processing.set(false)


                binding.heartrateThumb.backgroundTintList = ContextCompat.getColorStateList(requireContext(),
                    R.color.tab_selected_color)

                binding.heartrateMeasureHeartanim.pauseAnimation()
                binding.measureHearrateWaveAnim.pauseAnimation()

                return@PreviewCallback
            }
            timer!!.start()

            binding.heartrateMeasureHeartanim.playAnimation()
            binding.measureHearrateWaveAnim.playAnimation()
            binding.heartrateThumb.backgroundTintList = ContextCompat.getColorStateList(requireContext(),
                R.color.red)


            var averageArrayAvg = 0
            var averageArrayCnt = 0
            for (i in averageArray.indices) {
                Log.e(TAG, "onPreviewFrame: avg array count$i")
                if (averageArray[i] > 0) {
                    averageArrayAvg += averageArray[i]
                    averageArrayCnt++
                }
            }
            val rollingAverage = if (averageArrayCnt > 0) averageArrayAvg / averageArrayCnt else 0
            var newType = currentType
            if (imgAvg < rollingAverage) {
                newType = TYPE.RED
                if (newType != currentType) {
                    beats++
                    Log.e(TAG, "onPreviewFrame: beats" + beats)
                    Log.e(TAG, "onPreviewFrame: beatsarray length" + beatsArray.size)
                }
            } else if (imgAvg > rollingAverage) {
                newType = TYPE.GREEN
            }
            if (averageIndex == averageArraySize) averageIndex =
                0
            averageArray[averageIndex] = imgAvg
            averageIndex++

            // Transitioned from one state to another to the same
            if (newType != currentType) {
                Log.e(
                    TAG,
                    "onPreviewFrame: currenttype is " + currentType
                )
                currentType = newType
                image!!.postInvalidate()
            }
            val endTime = System.currentTimeMillis()
            val totalTimeInSecs = (endTime - startTime) / 1000.0
            if (totalTimeInSecs >= 10) {
                val bps = beats / totalTimeInSecs
                val dpm = (bps * 60.0).toInt()
                if (dpm < 30 || dpm > 180) {
                    startTime = System.currentTimeMillis()
                    beats = 0.0
                    heart_progressbar!!.progress = 0
                    processing.set(false)

                    binding.heartrateThumb.backgroundTintList = ContextCompat.getColorStateList(requireContext(),
                        R.color.tab_selected_color)
                    binding.heartrateMeasureHeartanim.pauseAnimation()
                    binding.measureHearrateWaveAnim.pauseAnimation()
                    return@PreviewCallback
                }
                if (beatsIndex == beatsArraySize) beatsIndex =
                    0
                beatsArray[beatsIndex] = dpm
                beatsIndex++
                Log.e(
                    TAG,
                    "onPreviewFrame: beatsIndex" + beatsIndex
                )
                var beatsArrayAvg = 0
                var beatsArrayCnt = 0
                for (i in beatsArray.indices) {
                    if (beatsArray[i] > 0) {
                        beatsArrayAvg += beatsArray[i]
                        beatsArrayCnt++
                        Log.e(
                            TAG,
                            "onPreviewFrame: beatsarraycount$beatsArrayCnt"
                        )
                    }
                }
                val beatsAvg = beatsArrayAvg / beatsArrayCnt
                heartRate = beatsAvg.toString()
                text!!.text = heartRate
                startTime = System.currentTimeMillis()
                beats = 0.0
                stopCam()

                binding.heartrateThumb.backgroundTintList = ContextCompat.getColorStateList(requireContext(),
                    R.color.tab_selected_color)
                binding.heartrateMeasureHeartanim.pauseAnimation()
                binding.measureHearrateWaveAnim.pauseAnimation()
            }
            processing.set(false)
        }

    fun stopCam() {
        camera!!.stopPreview()
        if (calculatingTHR) {
            setCard()
        }
    }

    private fun setCard() {
        val index = 0
        val workoutHeartRate = heartRate!!.toInt()
        var i = 0.6
        while (i < 1) {
            i = i + 0.1
        }
    }

    fun startCam() {
        camera!!.startPreview()
        startTime = System.currentTimeMillis()
    }

    fun finish() {
        val data = Intent()
        data.putExtra("restingHeartRate", heartRate)
        requireActivity().setResult(Activity.RESULT_OK, data)

    }


    private val surfaceCallback: SurfaceHolder.Callback = object : SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder) {
            try {
                camera!!.setPreviewDisplay(previewHolder)
                camera!!.setPreviewCallback(previewCallback)
            } catch (t: Throwable) {
                Log.e("surfaceCallbackDemo", "Exception in setPreviewDisplay()", t)
            }
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
            val parameters = camera!!.parameters
            parameters.flashMode = Camera.Parameters.FLASH_MODE_TORCH
            val size = getSmallestPreviewSize(width, height, parameters)
            if (size != null) {
                parameters.setPreviewSize(size.width, size.height)
                Log.d(TAG, "Using width=" + size.width + " height=" + size.height)
            }
            camera!!.parameters = parameters
            camera!!.startPreview()
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            // Ignore
        }
    }


    private fun getSmallestPreviewSize(
        width: Int,
        height: Int,
        parameters: Camera.Parameters
    ): Camera.Size? {
        var result: Camera.Size? = null
        for (size in parameters.supportedPreviewSizes) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size
                } else {
                    val resultArea = result.width * result.height
                    val newArea = size.width * size.height
                    if (newArea < resultArea) result = size
                }
            }
        }
        return result
    }


}