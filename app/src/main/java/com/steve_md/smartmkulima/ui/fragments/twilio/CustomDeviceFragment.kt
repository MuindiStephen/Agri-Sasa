package com.steve_md.smartmkulima.ui.fragments.twilio

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.common.FileAndMicAudioDevice
import com.steve_md.smartmkulima.common.SoundPoolManager
import com.steve_md.smartmkulima.utils.services.PrintServiceActivity
import com.twilio.voice.Call
import com.twilio.voice.CallException
import com.twilio.voice.ConnectOptions
import com.twilio.voice.Voice
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class CustomDeviceFragment : Fragment() {
    private val accessToken =
        "YOUR_TOKEN"
    private var audioManager: AudioManager? = null
    private var activeCall: Call? = null

    private var savedAudioMode = AudioManager.MODE_NORMAL

    // private var callActionFab: FloatingActionButton? = null
    //  private var hangupActionFab: FloatingActionButton? = null
    private var holdActionFab: FloatingActionButton? = null
    private var muteActionFab: FloatingActionButton? = null
    private var inputSwitchFab: FloatingActionButton? = null
    private var chronometer: Chronometer? = null
    private val alertDialog: AlertDialog? = null
    private val params = HashMap<String, String>()

    var callListener: Call.Listener = callListener()

    var fileAndMicAudioDevice: FileAndMicAudioDevice? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_custom_device, container, false)


        initializeUI(view)
        setupAudioManager()

        if (!checkPermissionForMicrophone()) {
            requestPermissionForMicrophone()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        holdActionFab = view.findViewById(R.id.hold_action_fab)
        muteActionFab = view.findViewById(R.id.mute_action_fab)
        inputSwitchFab = view.findViewById(R.id.input_switch_fab)
    }

    private fun initializeUI(view: View) {

        val holdActionFab: FloatingActionButton = view.findViewById(R.id.hold_action_fab)
        val muteActionFab: FloatingActionButton = view.findViewById(R.id.mute_action_fab)
        val inputSwitchFab: FloatingActionButton = view.findViewById(R.id.input_switch_fab)
        chronometer = view.findViewById(R.id.chronometer)

        val callActionFab: FloatingActionButton = view.findViewById(R.id.call_action_fab)
        val hangupActionFab: FloatingActionButton = view.findViewById(R.id.hangup_action_fab)



        callActionFab.setOnClickListener { placeCall() }
        hangupActionFab.setOnClickListener { hangUpCall() }

        holdActionFab.setOnClickListener(holdActionFabClickListener())
        muteActionFab.setOnClickListener(muteActionFabClickListener())
        inputSwitchFab.setOnClickListener(inputSwitchActionFabClickListener())

        //callActionFab.setOnClickListener { placeCall() }
        //hangupActionFab.setOnClickListener { hangUpCall() }

        val sales: CardView = view.findViewById(R.id.sales)
        val billPay: CardView = view.findViewById(R.id.billPay)
        val bulkPur: CardView = view.findViewById(R.id.bulkPay)

        val vieworder: CardView = view.findViewById(R.id.orders)

        sales.setOnClickListener {
            findNavController().navigate(
                R.id.action_customDeviceFragment_to_marketProduce
            )
        }
        billPay.setOnClickListener {
            findNavController().navigate(
                R.id.action_customDeviceFragment_to_marketProduce
            )
        }
        bulkPur.setOnClickListener {
            findNavController().navigate(
                R.id.action_customDeviceFragment_to_marketProduce
            )
        }

        vieworder.setOnClickListener {
            findNavController().navigate(
                R.id.action_customDeviceFragment_to_voiceBotOrdersFragment
            )
        }

        /**
         * Set up ui
         */
        resetUI()
    }

    private fun holdActionFabClickListener(): View.OnClickListener {
        return View.OnClickListener { v: View? -> hold() }
    }

    private fun hold() {
        if (activeCall != null) {
            val hold = !activeCall!!.isOnHold
            activeCall!!.hold(hold)
            applyFabState(holdActionFab!!, hold)
        }
    }

    private fun muteActionFabClickListener(): View.OnClickListener {
        return View.OnClickListener { v: View? -> mute() }
    }

    private fun mute() {
        if (activeCall != null) {
            val mute = !activeCall!!.isMuted
            activeCall!!.mute(mute)
            applyFabState(muteActionFab!!, mute)
        }
    }

    private fun inputSwitchActionFabClickListener(): View.OnClickListener {
        return View.OnClickListener { v: View? ->
            val enable = fileAndMicAudioDevice!!.isMusicPlaying
            applyFabState(inputSwitchFab!!, !enable)
            fileAndMicAudioDevice!!.switchInput(!enable)
        }
    }


    /*
       * Needed for setting/abandoning audio focus during a call
         */
    private fun setupAudioManager() {
        audioManager = requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager!!.isSpeakerphoneOn = true


        /*
         * Enable changing the volume using the up/down keys during a conversation
         */
        requireActivity().volumeControlStream = AudioManager.STREAM_VOICE_CALL
    }

    private fun placeCall() {
        val contact = EditText(context)
        AlertDialog.Builder(requireActivity())
            .setTitle("Call")
            .setView(contact)
            .setPositiveButton("Call") { dialog: DialogInterface?, which: Int ->
                params["to"] = contact.text.toString()
                val connectOptions = ConnectOptions.Builder(accessToken)
                    .params(params)
                    .build()
                activeCall = Voice.connect(requireContext(), connectOptions, callListener())
                setCallUI()
                alertDialog?.dismiss()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun hangUpCall() {
        SoundPoolManager.getInstance(requireContext()).playDisconnect()
        resetUI()
        if (activeCall != null) {
            activeCall!!.disconnect()
            activeCall = null
        }
    }

    /**
     * Reset ui elements
     */
    private fun resetUI() {
        val callActionFab = view?.findViewById<FloatingActionButton>(R.id.call_action_fab)
        val hangupActionFab =
            view?.findViewById<FloatingActionButton>(R.id.hangup_action_fab)

        callActionFab?.show()
        muteActionFab?.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_mic_white_24dp
            )
        )
        holdActionFab?.hide()
        holdActionFab?.backgroundTintList = ColorStateList
            .valueOf(ContextCompat.getColor(requireContext(), R.color.colorAccent2))
        muteActionFab?.hide()
        hangupActionFab?.hide()
        inputSwitchFab?.let { applyFabState(it, false) }
        inputSwitchFab?.hide()
        chronometer?.visibility = View.INVISIBLE
        chronometer?.stop()
    }

    private fun applyFabState(button: FloatingActionButton, enabled: Boolean) {
        // Set fab as pressed when call is on hold
        val colorStateList = if (enabled) ColorStateList.valueOf(
            ContextCompat.getColor(
                requireContext(),
                R.color.red_text_color_wallet
            )
        ) else ColorStateList.valueOf(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorAccent2
            )
        )
        button.backgroundTintList = colorStateList
    }

    /*
     * The UI state when there is an active call
     */
    private fun setCallUI() {
        val callActionFab = view?.findViewById<FloatingActionButton>(R.id.call_action_fab)
        val hangupActionFab =
            view?.findViewById<FloatingActionButton >(R.id.hangup_action_fab)

        callActionFab?.hide()
        hangupActionFab?.show()
        holdActionFab?.show()
        muteActionFab?.show()
        inputSwitchFab?.show()
        chronometer?.visibility = View.VISIBLE
        chronometer?.base = SystemClock.elapsedRealtime()
        chronometer?.start()
    }


    private fun callListener(): Call.Listener {
        return object : Call.Listener {
            override fun onConnectFailure(call: Call, error: CallException) {

                setAudioFocus(false)

                Log.e(TAG, "Call failed: " + error.message)
                Snackbar.make(requireView(), "Call failed: " + error.message, Snackbar.LENGTH_LONG)
                    .show()
                resetUI()
            }

            override fun onRinging(call: Call) {
                Log.d("CustomDevice", "Ringing")
            }

            override fun onConnected(call: Call) {
                setAudioFocus(true)
                inputSwitchFab?.let { fileAndMicAudioDevice?.let { it1 -> applyFabState(it, it1.isMusicPlaying) } }
                Log.d(TAG, "Call connected")
                activeCall = call
            }

            override fun onReconnecting(call: Call, callException: CallException) {
                Log.d(TAG, "onReconnecting")
            }

            override fun onReconnected(call: Call) {
                Log.d(TAG, "onReconnected")
            }



            override fun onDisconnected(call: Call, error: CallException?) {
                setAudioFocus(false)
                Log.d(TAG, "Call disconnected")

               // startActivity(Intent(requireContext(), PrintServiceActivity::class.java))
                Toast.makeText(requireContext(),"Transaction was Successful...",Toast.LENGTH_LONG)
                    .show()

                if (error != null) {
                    val message = String.format(
                        Locale.US,
                        "Call Error: %d, %s",
                        error.errorCode,
                        error.message
                    )
                    Log.e("CustomDeviceFragment", message)
                    Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
                }

                resetUI()
            }

        }
    }

    private fun setAudioFocus(setFocus: Boolean) {
        if (audioManager != null) {
            if (setFocus) {
                savedAudioMode = audioManager?.mode!!
                // Request audio focus before making any device switch.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val playbackAttributes = AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build()
                    val focusRequest =
                        AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                            .setAudioAttributes(playbackAttributes)
                            .setAcceptsDelayedFocusGain(true)
                            .setOnAudioFocusChangeListener { i: Int -> }
                            .build()
                    audioManager?.requestAudioFocus(focusRequest)
                } else {
                    audioManager?.requestAudioFocus(
                        { focusChange: Int -> },
                        AudioManager.STREAM_VOICE_CALL,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
                    )
                }
                /*
                 * Start by setting MODE_IN_COMMUNICATION as default audio mode. It is
                 * required to be in this mode when playout and/or recording starts for
                 * best possible VoIP performance. Some devices have difficulties with speaker mode
                 * if this is not set.
                 */
                audioManager?.mode = AudioManager.MODE_IN_COMMUNICATION
            } else {
                audioManager?.mode = savedAudioMode
                audioManager?.abandonAudioFocus(null)
            }
        }
    }

    private fun checkPermissionForMicrophone(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissionForMicrophone() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.RECORD_AUDIO),
            MIC_PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray,
    ) {
        /*
         * Check if microphone permissions is granted
         */
        if (requestCode == MIC_PERMISSION_REQUEST_CODE && permissions.size > 0) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(
                    requireView(),
                    "Microphone permissions needed. Please allow in your application settings.",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater){
        // val inflater: MenuInflater = getMenuInflater()
        inflater.inflate(R.menu.menu, menu)
//        return true
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.speaker_menu_item) {
            if (audioManager!!.isSpeakerphoneOn) {
                audioManager!!.isSpeakerphoneOn = false
                item.setIcon(R.drawable.ic_phonelink_ring_white_24dp)
            } else {
                audioManager!!.isSpeakerphoneOn = true
                item.setIcon(R.drawable.ic_volume_up_white_24dp)
            }
        }
        return true
    }

    override fun onDestroy() {
        SoundPoolManager.getInstance(requireContext()).release()
        super.onDestroy()
    }


    companion object {
        private const val TAG = "CustomDeviceFragment"
        private const val MIC_PERMISSION_REQUEST_CODE = 1
    }
}