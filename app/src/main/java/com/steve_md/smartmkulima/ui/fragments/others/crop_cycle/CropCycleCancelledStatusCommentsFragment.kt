package com.steve_md.smartmkulima.ui.fragments.others.crop_cycle

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentCropCycleCancelledStatusCommentsBinding
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.w3c.dom.Text


@AndroidEntryPoint
class CropCycleCancelledStatusCommentsFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCropCycleCancelledStatusCommentsBinding
    private val viewModel: MainViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  FragmentCropCycleCancelledStatusCommentsBinding.inflate(
            inflater,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cropName = arguments?.getString("cropCycleName")

        // update or add comments why cancel crop cycle
        binding.btnSubmitCommentsForCropCycleCancelling.setOnClickListener {


            if (validateInputs()) {
                if (cropName != null) {
                    viewModel.updateToNewCommentsCropCycleCancelled(
                        binding.inputCommentsForCancelCropCycle.text.toString(),
                        cropName
                    )
                }
                displaySnackBar("Added comments.")

                //view.findViewById<TextView>(R.id.textViewComments).isVisible = true

                dismiss()
            }
        }
    }


    private fun validateInputs(): Boolean {
        return binding.inputCommentsForCancelCropCycle.text.isNullOrEmpty().not().also {
            if (!it) binding.inputCommentsForCancelCropCycle.error = "**required, add comments"
        }
    }
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//
//        dialog?.setOnShowListener { it ->
//            val d = it as BottomSheetDialog
//            val bottomSheet =
//                d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
//            bottomSheet?.let {
//                val behavior = BottomSheetBehavior.from(it)
//                behavior.state = BottomSheetBehavior.STATE_EXPANDED
//            }
//        }
//        return super.onCreateDialog(savedInstanceState)
//    }

    companion object {
        const val TAG = "CropCycleCancelledStatusCommentsBottomSheetDialogFragment"
    }

//    override fun getTheme(): Int {
//        return com.steve_md.smartmkulima.R.style.AppTheme
//    }
}