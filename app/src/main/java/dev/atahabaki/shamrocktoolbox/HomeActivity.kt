package dev.atahabaki.shamrocktoolbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dev.atahabaki.shamrocktoolbox.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initBottomNav()
    }

    private fun initBottomNav() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.mainFramer)
        bottomSheetBehavior.isHideable = true
        bottomSheetBehavior.peekHeight = binding.mainBottomAppbar.height
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        initBottomAppBarNavigationClick()
        dismissWhenClickToRootListener()
    }

    private fun initBottomAppBarNavigationClick() {
        binding.mainBottomAppbar.setNavigationOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        }
    }

    private fun dismissWhenClickToRootListener() {
        binding.root.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun initView() {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}