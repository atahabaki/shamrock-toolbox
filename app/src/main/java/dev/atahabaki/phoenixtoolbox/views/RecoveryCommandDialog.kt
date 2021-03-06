package dev.atahabaki.phoenixtoolbox.views

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.atahabaki.phoenixtoolbox.R
import dev.atahabaki.phoenixtoolbox.databinding.DialogNewRecoveryCommandBinding
import dev.atahabaki.phoenixtoolbox.models.Command
import dev.atahabaki.phoenixtoolbox.viewmodels.RecoveryCommandViewModel
import java.lang.IllegalStateException

class RecoveryCommandDialog: DialogFragment() {
    private var _binding: DialogNewRecoveryCommandBinding? = null
    private val binding get() = _binding!!

    private val cmdViewModel: RecoveryCommandViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogNewRecoveryCommandBinding.inflate(layoutInflater)
        binding.dialogNewRecCmdType.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        return activity?.let {
            val builder = MaterialAlertDialogBuilder(it)
            builder.setView(binding.root)
            builder.setTitle(R.string.dialog_new_command_title)
            builder.setPositiveButton(R.string.add_cmd, DialogInterface.OnClickListener { dialog, which ->
                val validation = true //TODO validateCmd()
                if (validation) {
                    val command = binding.dialogNewRecCmdType.adapter?.getItem(binding.dialogNewRecCmdType.selectedItemPosition).toString()
                    val parameter = binding.dialogNewRecCmdParameter.editText?.text?.toString()?.split(',')
                    cmdViewModel.addCommand(Command(command, parameter))
                    cmdViewModel.setDataChanged(true)
                }
            })
            builder.setNegativeButton(R.string.cancel, DialogInterface.OnClickListener{ dialog, which ->
                this.dismiss()
            })
            builder.create()
        } ?: throw IllegalStateException("Activity can not be null...")
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        _binding = null
    }
}